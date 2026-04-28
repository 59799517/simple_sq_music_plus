package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlbumShow{

	@JsonProperty("albumName")
	private String albumName;

	@JsonProperty("mod")
	private String mod;

	@JsonProperty("isPay")
	private int isPay;

	@JsonProperty("singer")
	private String singer;

	@JsonProperty("albumPicUrl")
	private String albumPicUrl;

	@JsonProperty("intro")
	private String intro;

	@JsonProperty("id")
	private String id;

	@JsonProperty("type")
	private String type;

	public String getAlbumName(){
		return albumName;
	}

	public String getMod(){
		return mod;
	}

	public int getIsPay(){
		return isPay;
	}

	public String getSinger(){
		return singer;
	}

	public String getAlbumPicUrl(){
		return albumPicUrl;
	}

	public String getIntro(){
		return intro;
	}

	public String getId(){
		return id;
	}

	public String getType(){
		return type;
	}
}