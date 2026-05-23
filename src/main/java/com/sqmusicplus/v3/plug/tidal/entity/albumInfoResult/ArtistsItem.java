package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArtistsItem{

	@JsonProperty("contributionLinkUrl")
	private Object contributionLinkUrl;

	@JsonProperty("name")
	private String name;

	@JsonProperty("handle")
	private Object handle;

	@JsonProperty("id")
	private int id;

	@JsonProperty("type")
	private String type;

	@JsonProperty("userId")
	private Object userId;

	@JsonProperty("picture")
	private String picture;

	public Object getContributionLinkUrl(){
		return contributionLinkUrl;
	}

	public String getName(){
		return name;
	}

	public Object getHandle(){
		return handle;
	}

	public int getId(){
		return id;
	}

	public String getType(){
		return type;
	}

	public Object getUserId(){
		return userId;
	}

	public String getPicture(){
		return picture;
	}
}