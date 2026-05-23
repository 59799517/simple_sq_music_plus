package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Review{

	@JsonProperty("text")
	private Object text;

	@JsonProperty("source")
	private Object source;

	public Object getText(){
		return text;
	}

	public Object getSource(){
		return source;
	}
}