package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BestShowResultToneData2{

	@JsonProperty("result")
	private List<ResultItem> result;

	@JsonProperty("totalCount")
	private String totalCount;

	public List<ResultItem> getResult(){
		return result;
	}

	public String getTotalCount(){
		return totalCount;
	}
}