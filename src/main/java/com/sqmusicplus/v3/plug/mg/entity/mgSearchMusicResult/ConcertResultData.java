package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConcertResultData{

	@JsonProperty("result")
	private List<Object> result;

	@JsonProperty("totalCount")
	private String totalCount;

	public List<Object> getResult(){
		return result;
	}

	public String getTotalCount(){
		return totalCount;
	}
}