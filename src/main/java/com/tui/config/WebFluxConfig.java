package com.tui.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer{
	
	private static final Logger logger = LoggerFactory.getLogger(WebFluxConfig.class);
	
	@Value("${api.githup.url}")
	private String gitBaseURL;
	
	@Bean
	public WebClient getWebClient(){
		logger.info("Base url {}:",gitBaseURL);
	
		return WebClient.builder()
		        .baseUrl(gitBaseURL)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		        .filter(logRequest())
		        .build();
	}
	
	  private ExchangeFilterFunction logRequest() {
	        return (clientRequest, next) -> {
	            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
	            clientRequest.headers()
	                    .forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
	            return next.exchange(clientRequest);
	        };
	    }

}
