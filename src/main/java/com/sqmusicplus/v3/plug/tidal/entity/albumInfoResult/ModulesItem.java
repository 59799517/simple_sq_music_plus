package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ModulesItem{

	@JsonProperty("preTitle")
	private String preTitle;

	@JsonProperty("playbackControls")
	private List<PlaybackControlsItem> playbackControls;

	@JsonProperty("credits")
	private Credits credits;

	@JsonProperty("album")
	private Album album;

	@JsonProperty("review")
	private Review review;

	@JsonProperty("width")
	private int width;

	@JsonProperty("description")
	private String description;

	@JsonProperty("id")
	private String id;

	@JsonProperty("type")
	private String type;

	@JsonProperty("title")
	private String title;

	@JsonProperty("copyright")
	private String copyright;

	@JsonProperty("shuffleButton")
	private boolean shuffleButton;

	@JsonProperty("releaseDate")
	private String releaseDate;

	@JsonProperty("quickPlay")
	private boolean quickPlay;

	@JsonProperty("supportsPaging")
	private boolean supportsPaging;

	@JsonProperty("playButton")
	private boolean playButton;

	@JsonProperty("pagedList")
	private PagedList pagedList;

	@JsonProperty("showMore")
	private Object showMore;

	@JsonProperty("listFormat")
	private String listFormat;

	@JsonProperty("scroll")
	private String scroll;

	@JsonProperty("layout")
	private Object layout;

	@JsonProperty("header")
	private Object header;

	public String getPreTitle(){
		return preTitle;
	}

	public List<PlaybackControlsItem> getPlaybackControls(){
		return playbackControls;
	}

	public Credits getCredits(){
		return credits;
	}

	public Album getAlbum(){
		return album;
	}

	public Review getReview(){
		return review;
	}

	public int getWidth(){
		return width;
	}

	public String getDescription(){
		return description;
	}

	public String getId(){
		return id;
	}

	public String getType(){
		return type;
	}

	public String getTitle(){
		return title;
	}

	public String getCopyright(){
		return copyright;
	}

	public boolean isShuffleButton(){
		return shuffleButton;
	}

	public String getReleaseDate(){
		return releaseDate;
	}

	public boolean isQuickPlay(){
		return quickPlay;
	}

	public boolean isSupportsPaging(){
		return supportsPaging;
	}

	public boolean isPlayButton(){
		return playButton;
	}

	public PagedList getPagedList(){
		return pagedList;
	}

	public Object getShowMore(){
		return showMore;
	}

	public String getListFormat(){
		return listFormat;
	}

	public String getScroll(){
		return scroll;
	}

	public Object getLayout(){
		return layout;
	}

	public Object getHeader(){
		return header;
	}
}