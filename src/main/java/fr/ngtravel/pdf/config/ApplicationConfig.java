package fr.ngtravel.pdf.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.ngtravel.util.model.criteria.Meals;

@Configuration
public class ApplicationConfig {
	@Bean
    public Locale defaultLocale() {
        return new Locale("fr", "FR");
    }
	
	@Bean
	public Meals meals() {
		Meals meals = new Meals();
		meals.put("TI", "Tout compris");
		meals.put("TIP", "Tout compris Premium");
		
		return meals;
	}
}
