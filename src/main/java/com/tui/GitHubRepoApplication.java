package com.tui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.tui.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class GitHubRepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitHubRepoApplication.class, args);
	}

}
