package com.tui.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class GithubBranchBean {

	@JsonProperty("name")
	@Setter
	@Getter
	private String branchName;
	@Setter
	@Getter
	private GithubCommitBean commit;

}
