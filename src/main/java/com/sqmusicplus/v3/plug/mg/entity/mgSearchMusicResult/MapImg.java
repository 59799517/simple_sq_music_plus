package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MapImg{

	@JsonProperty("16:9")
	private List<JsonMember169Item> jsonMember169;

	@JsonProperty("4:3")
	private List<JsonMember43Item> jsonMember43;

	public List<JsonMember169Item> getJsonMember169(){
		return jsonMember169;
	}

	public List<JsonMember43Item> getJsonMember43(){
		return jsonMember43;
	}
}