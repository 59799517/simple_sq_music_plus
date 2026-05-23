package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArtistRolesItem{

	@JsonProperty("category")
	private String category;

	@JsonProperty("categoryId")
	private int categoryId;

	public String getCategory(){
		return category;
	}

	public int getCategoryId(){
		return categoryId;
	}
}