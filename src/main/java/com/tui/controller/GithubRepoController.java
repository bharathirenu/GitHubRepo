package com.tui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.tui.exception.ServiceException;
import com.tui.payload.GithubRepoBean;
import com.tui.service.GithubRepoService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class GithubRepoController {

	private static final Logger logger = LoggerFactory.getLogger(GithubRepoController.class);

	@Autowired
	private GithubRepoService githubRepoService;

	@GetMapping("/repos/{user}")
	@ResponseBody
	public Flux<GithubRepoBean> listGithubRepositoriesForUser(@PathVariable String user) {
		logger.info("Request received to fetching all the repositories for  user {%s}" ,user);
		Flux<GithubRepoBean> repo = githubRepoService.getReposWithBranchesForUser(user);
		return repo;
	}
	
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
		logger.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString(),
				ex);
		return ResponseEntity.status(ex.getRawStatusCode()).body("whyHasItHappened");
	}

}
