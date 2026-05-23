package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AlbumInfoResult{

	@JsonProperty("id")
	private String id;

	@JsonProperty("title")
	private String title;

	@JsonProperty("rows")
	private List<RowsItem> rows;

	@JsonProperty("selfLink")
	private Object selfLink;

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public List<RowsItem> getRows(){
		return rows;
	}

	public Object getSelfLink(){
		return selfLink;
	}
}