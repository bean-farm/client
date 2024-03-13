package com.beanfarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.beanfarm.auth.ClientConfiguration;
import com.beanfarm.auth.ServerConfiguration;
import com.beanfarm.client.BeanfarmClient;

@SpringBootApplication
@EnableConfigurationProperties({ClientConfiguration.class, ServerConfiguration.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	BeanfarmClient dadJokeClient() {
		WebClient client = WebClient.builder()
				.baseUrl("http://localhost:5000/")
				.defaultHeader("Accept","application/json")
				.build();

		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
		return factory.createClient(BeanfarmClient.class);
	}
}
