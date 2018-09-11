package com.exozet.gitresumeweb;

import com.exozet.gitresumeweb.web.LoggingRequestInterceptor;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties
public class GitResumeWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitResumeWebApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		final CloseableHttpClient httpClient = HttpClients.createDefault();


		final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

		List converters = Lists.newArrayList();
		converters.add(new MappingJackson2HttpMessageConverter());

		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
		restTemplate.setMessageConverters(converters);
		restTemplate.getInterceptors().add(new LoggingRequestInterceptor());

		return restTemplate;
	}



}
