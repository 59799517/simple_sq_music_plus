package com.sqmusicplus.v3.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult.BestShowResultData;
import com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult.BestShowResultToneData2;
import com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult.ConcertResultData;
import com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult.SongResultData;

public class MgSearchMusicResult{

	@JsonProperty("dynamicEffect")
	private int dynamicEffect;

	@JsonProperty("resultNum")
	private int resultNum;

	@JsonProperty("concertResultData")
	private ConcertResultData concertResultData;

	@JsonProperty("code")
	private String code;

	@JsonProperty("mod")
	private int mod;

	@JsonProperty("songResultData")
	private SongResultData songResultData;

	@JsonProperty("bestShowResultData")
	private BestShowResultData bestShowResultData;

	@JsonProperty("end")
	private boolean end;

	@JsonProperty("bestShowResultToneData2")
	private BestShowResultToneData2 bestShowResultToneData2;

	public int getDynamicEffect(){
		return dynamicEffect;
	}

	public int getResultNum(){
		return resultNum;
	}

	public ConcertResultData getConcertResultData(){
		return concertResultData;
	}

	public String getCode(){
		return code;
	}

	public int getMod(){
		return mod;
	}

	public SongResultData getSongResultData(){
		return songResultData;
	}

	public BestShowResultData getBestShowResultData(){
		return bestShowResultData;
	}

	public boolean isEnd(){
		return end;
	}

	public BestShowResultToneData2 getBestShowResultToneData2(){
		return bestShowResultToneData2;
	}
}