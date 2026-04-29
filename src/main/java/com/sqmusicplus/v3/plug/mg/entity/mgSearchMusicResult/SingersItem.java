package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SingersItem{

	@JsonProperty("name")
	private String name;

	@JsonProperty("id")
	private String id;

	public String getName(){
		return name;
	}

	public String getId(){
		return id;
	}
}