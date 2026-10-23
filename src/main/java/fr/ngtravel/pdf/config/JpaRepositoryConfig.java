package fr.ngtravel.pdf.config;

import java.util.Map;

import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.directours.commons.database.model.ImportedCriteria;

import fr.ngtravel.database.model.product.ProductDuplicate;
import fr.ngtravel.model.entity.TripRating;
import fr.ngtravel.service_tangram.repository.entity.TangramConfiguration;

@Configuration
@EnableJpaRepositories(basePackages = { 
	"fr.ngtravel.lastminutes.adapter.secondary.repository",
	"fr.ngtravel.website.commons.database.repository",
	"fr.ngtravel.adapter.secondary.repository",
	"fr.ngtravel.resoneo.repository",
	"com.directours.commons.database.repository", 
	"fr.ngtravel.configuration.adapaters.secondary", 
	"fr.ngtravel.service_tangram.repository",
	"fr.ngtravel.pdf.adapters.secondary"})
@EnableAutoConfiguration
@EntityScan(basePackageClasses= {TripRating.class, TangramConfiguration.class, ImportedCriteria.class, ProductDuplicate.class})
public class JpaRepositoryConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(JpaVendorAdapter vendorAdapter,  DataSource dataSource, @Qualifier("jpaProps") Map<String, Object> jpaProps) {
    	jpaProps.remove(PersistenceUnitProperties.SESSION_CUSTOMIZER);
    	
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan(
            "fr.ngtravel.website.commons.database.model",
            "com.directours.commons.database.model",
            "com.directours.orchestra",
            "fr.ngtravel.database.model.product",
            "fr.ngtravel.tripadvisorcache.model",
            "fr.ngtravel.model.entity",
            "fr.ngtravel.resoneo.repository.entity",
            "fr.ngtravel.service_tangram.repository",
            "fr.ngtravel.pdf.model"
        );
        emf.setPersistenceUnitName("promosejoursUnit");
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaPropertyMap(jpaProps);
        return emf;
    }
}
