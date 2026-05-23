package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaMetadata{

	@JsonProperty("tags")
	private List<String> tags;

	public List<String> getTags(){
		return tags;
	}
}