package fr.ngtravel.pdf.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfigCaffeine {
	 @Value(value="${cache.duration.products:1}")
	    private Integer cacheDurationProducts; //hours
	 
	@Bean
	public CacheManager cacheManagerInMemory() {
		Caffeine caffeine = Caffeine.newBuilder()
				.initialCapacity(20000)
		    	.maximumSize(20000)
		    	.expireAfterWrite(cacheDurationProducts, TimeUnit.HOURS)
		    	.recordStats();
				
	    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
	    caffeineCacheManager.setCaffeine(caffeine);
	    return caffeineCacheManager;
	}
}
