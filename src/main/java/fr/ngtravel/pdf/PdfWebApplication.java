package fr.ngtravel.pdf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import fr.ngtravel.configuration.adapters.primary.ConfigurationController;
import fr.ngtravel.configuration.adapters.secondary.config.SiteEnvNameResolver;
import fr.ngtravel.event.mq.SubscriptionCleaner;
import fr.ngtravel.newsletter.service.NewsletterService;
import fr.ngtravel.pdf.controller.PdfController;
import fr.ngtravel.service.impl.SearchEngineServiceImpl;

@SpringBootApplication(exclude = {SearchEngineServiceImpl.class})
@EnableAutoConfiguration
@ComponentScan(basePackages = { 
		"fr.ngtravel", 
		"fr.ngtravel.adapter.secondary",
		"com.directours.commons"}, excludeFilters = @ComponentScan.Filter(
			    type = FilterType.ASSIGNABLE_TYPE, 
			    value = {ConfigurationController.class,
			    	SiteEnvNameResolver.class, 
			    	NewsletterService.class,
			    	SubscriptionCleaner.class,
			    	PdfController.class}
			))
public class PdfWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfWebApplication.class, args);
	}

}
