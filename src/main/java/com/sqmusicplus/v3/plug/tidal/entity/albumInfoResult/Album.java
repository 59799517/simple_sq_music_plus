package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Album{

	@JsonProperty("streamReady")
	private boolean streamReady;

	@JsonProperty("copyright")
	private String copyright;

	@JsonProperty("audioModes")
	private List<String> audioModes;

	@JsonProperty("releaseDate")
	private String releaseDate;

	@JsonProperty("upload")
	private boolean upload;

	@JsonProperty("numberOfVolumes")
	private int numberOfVolumes;

	@JsonProperty("payToStream")
	private boolean payToStream;

	@JsonProperty("allowStreaming")
	private boolean allowStreaming;

	@JsonProperty("audioQuality")
	private String audioQuality;

	@JsonProperty("title")
	private String title;

	@JsonProperty("type")
	private String type;

	@JsonProperty("version")
	private Object version;

	@JsonProperty("url")
	private String url;

	@JsonProperty("duration")
	private int duration;

	@JsonProperty("cover")
	private String cover;

	@JsonProperty("explicit")
	private boolean explicit;

	@JsonProperty("artists")
	private List<ArtistsItem> artists;

	@JsonProperty("popularity")
	private int popularity;

	@JsonProperty("videoCover")
	private Object videoCover;

	@JsonProperty("mediaMetadata")
	private MediaMetadata mediaMetadata;

	@JsonProperty("numberOfVideos")
	private int numberOfVideos;

	@JsonProperty("id")
	private int id;

	@JsonProperty("streamStartDate")
	private String streamStartDate;

	@JsonProperty("numberOfTracks")
	private int numberOfTracks;

	@JsonProperty("vibrantColor")
	private String vibrantColor;

	public boolean isStreamReady(){
		return streamReady;
	}

	public String getCopyright(){
		return copyright;
	}

	public List<String> getAudioModes(){
		return audioModes;
	}

	public String getReleaseDate(){
		return releaseDate;
	}

	public boolean isUpload(){
		return upload;
	}

	public int getNumberOfVolumes(){
		return numberOfVolumes;
	}

	public boolean isPayToStream(){
		return payToStream;
	}

	public boolean isAllowStreaming(){
		return allowStreaming;
	}

	public String getAudioQuality(){
		return audioQuality;
	}

	public String getTitle(){
		return title;
	}

	public String getType(){
		return type;
	}

	public Object getVersion(){
		return version;
	}

	public String getUrl(){
		return url;
	}

	public int getDuration(){
		return duration;
	}

	public String getCover(){
		return cover;
	}

	public boolean isExplicit(){
		return explicit;
	}

	public List<ArtistsItem> getArtists(){
		return artists;
	}

	public int getPopularity(){
		return popularity;
	}

	public Object getVideoCover(){
		return videoCover;
	}

	public MediaMetadata getMediaMetadata(){
		return mediaMetadata;
	}

	public int getNumberOfVideos(){
		return numberOfVideos;
	}

	public int getId(){
		return id;
	}

	public String getStreamStartDate(){
		return streamStartDate;
	}

	public int getNumberOfTracks(){
		return numberOfTracks;
	}

	public String getVibrantColor(){
		return vibrantColor;
	}
}