package fr.ngtravel.pdf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.ngtravel.configuration.domain.AppConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:messages.properties")
public class WebConfig implements WebMvcConfigurer, WebApplicationInitializer {

	@Bean
	  public ViewResolver viewResolver() {
	    final InternalResourceViewResolver bean = new InternalResourceViewResolver();
	    bean.setViewClass(JstlView.class);
	    bean.setPrefix("/WEB-INF/jsp/");
	    bean.setSuffix(".jsp");
	    return bean;
	  }
	
    @Override
	public void onStartup(ServletContext servletContext) {

		// Load Spring web application configuration
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(AppConfig.class);

		// Create and register the DispatcherServlet
		DispatcherServlet servlet = new DispatcherServlet(context);
		ServletRegistration.Dynamic registration = servletContext.addServlet("app", servlet);
		registration.setLoadOnStartup(1);
		registration.addMapping("/");
		registration.addMapping("*.do");
		registration.addMapping("/admin");
	}
}
