package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonMember43Item{

	@JsonProperty("img")
	private String img;

	@JsonProperty("imgSizeType")
	private String imgSizeType;

	@JsonProperty("imgOri")
	private String imgOri;

	@JsonProperty("fileId")
	private String fileId;

	@JsonProperty("imgRatio")
	private String imgRatio;

	@JsonProperty("webpImg")
	private String webpImg;

	public String getImg(){
		return img;
	}

	public String getImgSizeType(){
		return imgSizeType;
	}

	public String getImgOri(){
		return imgOri;
	}

	public String getFileId(){
		return fileId;
	}

	public String getImgRatio(){
		return imgRatio;
	}

	public String getWebpImg(){
		return webpImg;
	}
}