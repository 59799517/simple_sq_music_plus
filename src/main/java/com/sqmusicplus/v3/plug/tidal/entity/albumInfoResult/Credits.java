package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Credits{

	@JsonProperty("items")
	private List<Object> items;

	public List<Object> getItems(){
		return items;
	}
}