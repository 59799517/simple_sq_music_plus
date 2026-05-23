package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PagedList{

	@JsonProperty("offset")
	private int offset;

	@JsonProperty("limit")
	private int limit;

	@JsonProperty("dataApiPath")
	private String dataApiPath;

	@JsonProperty("items")
	private List<ItemsItem> items;

	@JsonProperty("totalNumberOfItems")
	private int totalNumberOfItems;

	public int getOffset(){
		return offset;
	}

	public int getLimit(){
		return limit;
	}

	public String getDataApiPath(){
		return dataApiPath;
	}

	public List<ItemsItem> getItems(){
		return items;
	}

	public int getTotalNumberOfItems(){
		return totalNumberOfItems;
	}
}