package com.beanfarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.beanfarm.client.BeanfarmClient;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	BeanfarmClient dadJokeClient() {
		WebClient client = WebClient.builder()
				.baseUrl("https://icanhazdadjoke.com")
				.defaultHeader("Accept","application/json")
				.build();

		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
		return factory.createClient(BeanfarmClient.class);
	}
}
