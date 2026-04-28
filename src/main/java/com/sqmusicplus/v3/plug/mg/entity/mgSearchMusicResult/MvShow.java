package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MvShow{

	@JsonProperty("singers")
	private List<SingersItem> singers;

	@JsonProperty("imgs")
	private List<ImgsItem> imgs;

	@JsonProperty("mvDuration")
	private String mvDuration;

	@JsonProperty("mod")
	private String mod;

	@JsonProperty("mvType")
	private String mvType;

	@JsonProperty("mvname")
	private String mvname;

	@JsonProperty("playNum")
	private String playNum;

	@JsonProperty("mvInfoList")
	private List<MvInfoListItem> mvInfoList;

	public List<SingersItem> getSingers(){
		return singers;
	}

	public List<ImgsItem> getImgs(){
		return imgs;
	}

	public String getMvDuration(){
		return mvDuration;
	}

	public String getMod(){
		return mod;
	}

	public String getMvType(){
		return mvType;
	}

	public String getMvname(){
		return mvname;
	}

	public String getPlayNum(){
		return playNum;
	}

	public List<MvInfoListItem> getMvInfoList(){
		return mvInfoList;
	}
}