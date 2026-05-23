package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShowMore{

	@JsonProperty("apiPath")
	private String apiPath;

	@JsonProperty("title")
	private String title;

	public String getApiPath(){
		return apiPath;
	}

	public String getTitle(){
		return title;
	}
}