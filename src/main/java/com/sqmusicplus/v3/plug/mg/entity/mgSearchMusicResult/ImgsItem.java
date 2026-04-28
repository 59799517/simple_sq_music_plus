package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImgsItem{

	@JsonProperty("img")
	private String img;

	@JsonProperty("imgSizeType")
	private String imgSizeType;

	public String getImg(){
		return img;
	}

	public String getImgSizeType(){
		return imgSizeType;
	}
}