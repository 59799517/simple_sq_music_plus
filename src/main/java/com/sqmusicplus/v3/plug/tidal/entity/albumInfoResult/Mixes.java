package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mixes{

	@JsonProperty("TRACK_MIX")
	private String tRACKMIX;

	@JsonProperty("ARTIST_MIX")
	private String aRTISTMIX;

	public String getTRACKMIX(){
		return tRACKMIX;
	}

	public String getARTISTMIX(){
		return aRTISTMIX;
	}
}