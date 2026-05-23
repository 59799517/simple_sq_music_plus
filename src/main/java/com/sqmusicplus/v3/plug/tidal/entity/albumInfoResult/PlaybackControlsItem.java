package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaybackControlsItem{

	@JsonProperty("icon")
	private String icon;

	@JsonProperty("playbackMode")
	private String playbackMode;

	@JsonProperty("shuffle")
	private boolean shuffle;

	@JsonProperty("title")
	private String title;

	@JsonProperty("targetModuleId")
	private String targetModuleId;

	public String getIcon(){
		return icon;
	}

	public String getPlaybackMode(){
		return playbackMode;
	}

	public boolean isShuffle(){
		return shuffle;
	}

	public String getTitle(){
		return title;
	}

	public String getTargetModuleId(){
		return targetModuleId;
	}
}