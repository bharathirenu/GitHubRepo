package com.tui.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.payload.GithubBranchBean;
import com.tui.payload.GithubCommitBean;
import com.tui.payload.GithubOwnerBean;
import com.tui.payload.GithubRepoBean;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class GithubRepoServiceImplTest {
	
	
	private static WebClient webClient;
	private static  ClientAndServer clientAndServer;

	
	 
	 GithubRepoServiceImpl githubRepoService = new GithubRepoServiceImpl();

	@BeforeAll
	public static void setup() throws IOException {
		
		clientAndServer = new ClientAndServer(4000);
		webClient = WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
		
		}
	
		@BeforeTestMethod
		public void initMocks() {
			MockitoAnnotations.initMocks(this);
		}
	
	@AfterEach
	public void reset() {
		clientAndServer.reset();
	}

	@Test
	public void testgetReposWithBranchesForUser() throws JsonProcessingException {
		ReflectionTestUtils.setField(githubRepoService, "webClient", webClient);

		List<GithubBranchBean> branchList = Arrays.asList(new GithubBranchBean("firstbranch", new GithubCommitBean("commitsha")),
				new GithubBranchBean("main", new GithubCommitBean("commitsha1")));

		GithubRepoBean repo = new GithubRepoBean("SampleRepo", new GithubOwnerBean("bharathirenu"), branchList);

		HttpRequest repoRequest = HttpRequest.request()
                .withMethod(HttpMethod.GET.name())
                .withPath("/users/bharathirenu/repos");

        this.clientAndServer.when(
        		repoRequest
        ).respond(
                HttpResponse.response()
                        .withBody(new ObjectMapper().writeValueAsString(repo))
                        .withContentType(MediaType.APPLICATION_JSON)
        );
        
        HttpRequest branchRequest = HttpRequest.request()
                .withMethod(HttpMethod.GET.name())
                .withPath("/repos/bharathirenu/SampleRepo/branches");

        this.clientAndServer.when(
        		branchRequest
        ).respond(
                HttpResponse.response()
                        .withBody(new ObjectMapper().writeValueAsString(repo))
                        .withContentType(MediaType.APPLICATION_JSON)
        );
       Flux<GithubRepoBean> res= githubRepoService.getReposWithBranchesForUser("bharathirenu");
       

		
		  StepVerifier.create(githubRepoService.getReposWithBranchesForUser(
		  "bharathirenu")) .expectNextMatches(gitRepo ->
		  "SampleRepo".equals(gitRepo.getReponame())).verifyComplete();
		 
		
		/*
		 * this.clientAndServer.verify(repoRequest, VerificationTimes.once());
		 * this.clientAndServer.verify(branchRequest, VerificationTimes.once());
		 */
	}
}
