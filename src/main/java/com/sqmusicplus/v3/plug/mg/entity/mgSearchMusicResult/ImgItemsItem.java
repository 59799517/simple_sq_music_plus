package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImgItemsItem{

	@JsonProperty("img")
	private String img;

	@JsonProperty("imgSizeType")
	private String imgSizeType;

	@JsonProperty("fileId")
	private String fileId;

	public String getImg(){
		return img;
	}

	public String getImgSizeType(){
		return imgSizeType;
	}

	public String getFileId(){
		return fileId;
	}
}