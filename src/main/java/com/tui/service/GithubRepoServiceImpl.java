package com.tui.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.config.AppProperties;
import com.tui.exception.ServiceException;
import com.tui.payload.GithubBranchBean;
import com.tui.payload.GithubRepoBean;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GithubRepoServiceImpl implements GithubRepoService {

	private static final Logger logger = LoggerFactory.getLogger(GithubRepoServiceImpl.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	WebClient webClient;

	@Autowired
	private AppProperties appProperties;

	
	private Mono<List<GithubBranchBean>> getBranchesForRepo(String reponame, String user) {

		logger.info("Fetching all the branches for a repository  {}", reponame);
		ParameterizedTypeReference<List<GithubBranchBean>> listParameterizedTypeReference = new ParameterizedTypeReference<List<GithubBranchBean>>() {
		};
		return webClient.get()
				.uri("/repos/{owner}/{repo}/branches", user, reponame)
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, clientResponse ->{
					logger.error("status:"+HttpStatus.METHOD_NOT_ALLOWED.value());
					return Mono.error(new ServiceException("whyHasItHappened", clientResponse.rawStatusCode()));
				})
				.onStatus(HttpStatus::is5xxServerError, clientResponse ->{
					return Mono.error(new ServiceException("whyHasItHappened", clientResponse.rawStatusCode()));
				})
				.bodyToMono(listParameterizedTypeReference).log();

	}

	private Flux<GithubRepoBean> getAllReposByUser(String user) {
		logger.info("Fetching all the repositories for  user {}", user);
		return webClient.get()
				.uri("/users/{username}/repos", user)
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, clientResponse -> {
					logger.error("status:" + HttpStatus.METHOD_NOT_ALLOWED.value());
					return Mono.error(new ServiceException("whyHasItHappened", clientResponse.rawStatusCode()));
				})
				.onStatus(HttpStatus::is5xxServerError, clientResponse -> {
					return Mono.error(new ServiceException("whyHasItHappened", clientResponse.rawStatusCode()));
				})
				.bodyToFlux(GithubRepoBean.class);
	}

	public Flux<GithubRepoBean> getReposWithBranchesForUser(String user) {

		return getAllReposByUser(user)
				.flatMap(repo -> 
				Mono.zip(Mono.just(repo), getBranchesForRepo(repo.getReponame(), user)))
				.map(tuple -> {
					logger.info("data:" + tuple.getT2().size());
					return GithubRepoBean.builder().reponame(tuple.getT1().getReponame())
							.owner(tuple.getT1().getOwner()).branch(tuple.getT2()).build();
				});

	}

}
