package com.tui.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class GithubRepoBean {

	@JsonProperty("name")
	@Setter
	@Getter
	private String reponame;
	@Setter
	@Getter
	private GithubOwnerBean owner;
	@Setter
	@Getter
	private List<GithubBranchBean> branch;
	

}
