package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MvInfoListItem{

	@JsonProperty("copyrightId")
	private String copyrightId;

	@JsonProperty("price")
	private String price;

	@JsonProperty("expireDate")
	private String expireDate;

	@JsonProperty("id")
	private String id;

	public String getCopyrightId(){
		return copyrightId;
	}

	public String getPrice(){
		return price;
	}

	public String getExpireDate(){
		return expireDate;
	}

	public String getId(){
		return id;
	}
}