package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SongResultData{

	@JsonProperty("result")
	private List<ResultItem> result;

	@JsonProperty("correct")
	private List<Object> correct;

	@JsonProperty("trackId")
	private String trackId;

	@JsonProperty("isFromCache")
	private String isFromCache;

	@JsonProperty("tipStatus")
	private String tipStatus;

	@JsonProperty("totalCount")
	private String totalCount;

	@JsonProperty("resultType")
	private String resultType;

	public List<ResultItem> getResult(){
		return result;
	}

	public List<Object> getCorrect(){
		return correct;
	}

	public String getTrackId(){
		return trackId;
	}

	public String getIsFromCache(){
		return isFromCache;
	}

	public String getTipStatus(){
		return tipStatus;
	}

	public String getTotalCount(){
		return totalCount;
	}

	public String getResultType(){
		return resultType;
	}
}