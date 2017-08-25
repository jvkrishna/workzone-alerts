import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * 
 * @author Vamsi Krishna J
 *
 */
@SpringBootApplication(scanBasePackages = "com.intrans.reactor.*", exclude = { SecurityAutoConfiguration.class })
@EnableMongoRepositories(basePackages = { "com.intrans.reactor.*" })
@EnableScheduling
public class WorkzoneAlertsApp extends SpringBootServletInitializer {

	/**
	 * Override is required for making war applications.
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WorkzoneAlertsApp.class);
	}

	/**
	 * Will be invoked by spring boot.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(WorkzoneAlertsApp.class, args);
	}

	/**
	 * Customized {@link RestTemplate} with XML Binding options.
	 * 
	 * @return {@link RestTemplate}
	 */
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		for (int i = 0; i < restTemplate.getMessageConverters().size(); i++) {
			if (restTemplate.getMessageConverters().get(i) instanceof MappingJackson2XmlHttpMessageConverter) {
				restTemplate.getMessageConverters().set(i, mappingJackson2XmlHttpMessageConverter());
			}
		}
		return restTemplate;
	}

	/**
	 * Modified XML Mapper.
	 * 
	 * @return {@link MappingJackson2XmlHttpMessageConverter}
	 */
	@Bean
	public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter() {
		MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter = new MappingJackson2XmlHttpMessageConverter();
		XmlMapper mapper = new XmlMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mappingJackson2XmlHttpMessageConverter.setObjectMapper(mapper);
		return mappingJackson2XmlHttpMessageConverter;
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(5);
		taskExecutor.setMaxPoolSize(10);
		return taskExecutor;
	}

}
