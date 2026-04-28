package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ext{

	@JsonProperty("lrcUrl")
	private String lrcUrl;

	@JsonProperty("disc")
	private String disc;

	public String getLrcUrl(){
		return lrcUrl;
	}

	public String getDisc(){
		return disc;
	}
}