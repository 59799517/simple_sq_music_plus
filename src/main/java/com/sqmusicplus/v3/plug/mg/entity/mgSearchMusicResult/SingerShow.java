package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SingerShow{

	@JsonProperty("singerName")
	private String singerName;

	@JsonProperty("mvCount")
	private String mvCount;

	@JsonProperty("mod")
	private String mod;

	@JsonProperty("albumCount")
	private String albumCount;

	@JsonProperty("singerArea")
	private String singerArea;

	@JsonProperty("singerPicUrl")
	private List<SingerPicUrlItem> singerPicUrl;

	@JsonProperty("id")
	private String id;

	@JsonProperty("vrbtCount")
	private String vrbtCount;

	@JsonProperty("singerAliasNames")
	private List<String> singerAliasNames;

	@JsonProperty("songCount")
	private String songCount;

	public String getSingerName(){
		return singerName;
	}

	public String getMvCount(){
		return mvCount;
	}

	public String getMod(){
		return mod;
	}

	public String getAlbumCount(){
		return albumCount;
	}

	public String getSingerArea(){
		return singerArea;
	}

	public List<SingerPicUrlItem> getSingerPicUrl(){
		return singerPicUrl;
	}

	public String getId(){
		return id;
	}

	public String getVrbtCount(){
		return vrbtCount;
	}

	public List<String> getSingerAliasNames(){
		return singerAliasNames;
	}

	public String getSongCount(){
		return songCount;
	}
}