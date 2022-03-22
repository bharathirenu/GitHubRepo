package com.tui.service;

import com.tui.payload.GithubRepoBean;

import reactor.core.publisher.Flux;

public interface GithubRepoService {
	 
	public Flux<GithubRepoBean> getReposWithBranchesForUser(String user);
	
	

}
