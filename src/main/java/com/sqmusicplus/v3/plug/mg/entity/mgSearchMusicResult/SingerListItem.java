package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SingerListItem{

	@JsonProperty("img")
	private String img;

	@JsonProperty("name")
	private String name;

	@JsonProperty("id")
	private String id;

	@JsonProperty("nameSpelling")
	private String nameSpelling;

	public String getImg(){
		return img;
	}

	public String getName(){
		return name;
	}

	public String getId(){
		return id;
	}

	public String getNameSpelling(){
		return nameSpelling;
	}
}