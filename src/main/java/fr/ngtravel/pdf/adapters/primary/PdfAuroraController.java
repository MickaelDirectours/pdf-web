package fr.ngtravel.pdf.adapters.primary;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.ngtravel.configuration.adapters.secondary.GenericWhiteLabelDAO;
import fr.ngtravel.configuration.adapters.secondary.TangramConfigurationDao;
import fr.ngtravel.configuration.domain.AppConfig;
import fr.ngtravel.configuration.domain.WhitelabelConfig;
import fr.ngtravel.model.product.ProductListItem;
import fr.ngtravel.pdf.adapters.secondary.CdnRepository;
import fr.ngtravel.pdf.controller.InternalRenderer;
import fr.ngtravel.pdf.model.CatalogueIdMapper;
import fr.ngtravel.pdf.model.Cdn;
import fr.ngtravel.pdf.service.ProductService;
import fr.ngtravel.pdf.view.pdf.ProductPdf;
import fr.ngtravel.service_tangram.repository.entity.TangramConfiguration;
import fr.ngtravel.util.thread.WhiteLabelThreadLocal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PdfAuroraController {
	private static final String APPLICATION_PDF = "application/pdf";

    private final ProductService productService;
	private final InternalRenderer internalRenderer;
	private final GenericWhiteLabelDAO whiteLabelDAO;
	private final TangramConfigurationDao tangramConfigurationDao;
	private final CdnRepository cdnRepository;
	
	@Value("${fileManager.basePath}") private String basePath;
	@Value("${fileManager.publicBasePath}") private String publicBasePath;
	
	/*@GetMapping(value = "/products/{id}/catalogue.do", produces = APPLICATION_PDF)
	public @ResponseBody HttpEntity<byte[]> catalogueAgence(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String productId) {
		ProductListItem product = findProduct(request, CatalogueIdMapper.getCatalogueId(productId));
		
		String html = executeJsp(request, response, product, "pdf/catalog");
		
		return createResponse(request, "jettours", html, "Brochure-" + productId +".pdf");
	}*/
	
	@GetMapping(value = "/{tenant}/products/{id}/pdf", produces = APPLICATION_PDF)
	public @ResponseBody HttpEntity<byte[]> pdf(HttpServletRequest request, HttpServletResponse response, @PathVariable String tenant, @PathVariable("id") String productId) {
		WhiteLabelThreadLocal.set(tenant, null);
		ProductListItem product = findProduct(request, productId);
		
		String html = executeJsp(request, response, product, "pdf/product");
		
		return createResponse(request, tenant, html, "Descriptif-" + productId +".pdf");
	}
	
	@GetMapping(value = "/{tenant}/products/{id}/catalogue", produces = APPLICATION_PDF)
	public @ResponseBody HttpEntity<byte[]> catalogueAgence(HttpServletRequest request, HttpServletResponse response, @PathVariable String tenant, @PathVariable("id") String productId) {
		WhiteLabelThreadLocal.set(tenant, null);
		ProductListItem product = findProduct(request, CatalogueIdMapper.getCatalogueId(productId));
		
		String html = executeJsp(request, response, product, "pdf/catalog");
		
		return createResponse(request, tenant, html, "Brochure-" + productId +".pdf");
	}	

	private ProductListItem findProduct(HttpServletRequest request, String productId) {
		TangramConfiguration tangramConfiguration;
		String whiteLabelCode = null;
		
		if(AppConfig.HAS_WHITELABEL) {
			WhitelabelConfig whitelabelConfig = WhitelabelConfig.getOrInitialize(request, whiteLabelDAO);
			Integer tangramConfigId = (Integer) whitelabelConfig.get("tangramConfigurationId");
			tangramConfiguration = tangramConfigurationDao.find(tangramConfigId);
			whiteLabelCode = (String) whitelabelConfig.get("whiteLabelCode");
		} else {
			tangramConfiguration = tangramConfigurationDao.find();
		}
		
		log.info("Find product {} for tangramConfiguration {} and whiteLabelCode {}", productId, tangramConfiguration.getId(), whiteLabelCode);
		
		ProductListItem product = productService.findProduct(productId, tangramConfiguration, whiteLabelCode);
		return product;
	}
	
	

	public String executeJsp(HttpServletRequest request, HttpServletResponse response, Object product, String viewPath){
		try {
			Model model = new ExtendedModelMap();
			model.addAttribute("product", product);
			return internalRenderer.evalView(request, response, model, Locale.FRENCH, viewPath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public HttpEntity<byte[]> createResponse(HttpServletRequest request, String tenant, String html, String fileName) {
		try {
			Optional<Cdn> cdn = cdnRepository.findById(tenant);
			String publicBasePath = cdn
				.map(Cdn::getPublicpath)
				.orElse(this.publicBasePath);
			
			String webResourcePath = publicBasePath +"/"+ basePath;
			ProductPdf productPdf = cdn.isEmpty() ? new ProductPdf(webResourcePath, html, tenant) : new ProductPdf(webResourcePath, html); 
			byte[] document = FileCopyUtils.copyToByteArray(productPdf.file());

			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "pdf"));
			header.set("Content-Disposition", "inline; filename="+ fileName);
			header.set("X-Robots-Tag", "noindex");
			header.setContentLength(document.length);

			return new HttpEntity<byte[]>(document, header);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
