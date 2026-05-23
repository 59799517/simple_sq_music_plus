package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item{

	@JsonProperty("streamReady")
	private boolean streamReady;

	@JsonProperty("volumeNumber")
	private int volumeNumber;

	@JsonProperty("upload")
	private boolean upload;

	@JsonProperty("payToStream")
	private boolean payToStream;

	@JsonProperty("audioQuality")
	private String audioQuality;

	@JsonProperty("title")
	private String title;

	@JsonProperty("spotlighted")
	private boolean spotlighted;

	@JsonProperty("duration")
	private int duration;

	@JsonProperty("accessType")
	private String accessType;

	@JsonProperty("stemReady")
	private boolean stemReady;

	@JsonProperty("artists")
	private List<ArtistsItem> artists;

	@JsonProperty("popularity")
	private int popularity;

	@JsonProperty("id")
	private int id;

	@JsonProperty("audioModes")
	private List<String> audioModes;

	@JsonProperty("trackNumber")
	private int trackNumber;

	@JsonProperty("doublePopularity")
	private Object doublePopularity;

	@JsonProperty("adSupportedStreamReady")
	private boolean adSupportedStreamReady;

	@JsonProperty("album")
	private Album album;

	@JsonProperty("editable")
	private boolean editable;

	@JsonProperty("allowStreaming")
	private boolean allowStreaming;

	@JsonProperty("ai")
	private boolean ai;

	@JsonProperty("version")
	private Object version;

	@JsonProperty("url")
	private String url;

	@JsonProperty("replayGain")
	private Object replayGain;

	@JsonProperty("explicit")
	private boolean explicit;

	@JsonProperty("mediaMetadata")
	private MediaMetadata mediaMetadata;

	@JsonProperty("mixes")
	private Mixes mixes;

	@JsonProperty("streamStartDate")
	private String streamStartDate;

	@JsonProperty("djReady")
	private boolean djReady;

	public boolean isStreamReady(){
		return streamReady;
	}

	public int getVolumeNumber(){
		return volumeNumber;
	}

	public boolean isUpload(){
		return upload;
	}

	public boolean isPayToStream(){
		return payToStream;
	}

	public String getAudioQuality(){
		return audioQuality;
	}

	public String getTitle(){
		return title;
	}

	public boolean isSpotlighted(){
		return spotlighted;
	}

	public int getDuration(){
		return duration;
	}

	public String getAccessType(){
		return accessType;
	}

	public boolean isStemReady(){
		return stemReady;
	}

	public List<ArtistsItem> getArtists(){
		return artists;
	}

	public int getPopularity(){
		return popularity;
	}

	public int getId(){
		return id;
	}

	public List<String> getAudioModes(){
		return audioModes;
	}

	public int getTrackNumber(){
		return trackNumber;
	}

	public Object getDoublePopularity(){
		return doublePopularity;
	}

	public boolean isAdSupportedStreamReady(){
		return adSupportedStreamReady;
	}

	public Album getAlbum(){
		return album;
	}

	public boolean isEditable(){
		return editable;
	}

	public boolean isAllowStreaming(){
		return allowStreaming;
	}

	public boolean isAi(){
		return ai;
	}

	public Object getVersion(){
		return version;
	}

	public String getUrl(){
		return url;
	}

	public Object getReplayGain(){
		return replayGain;
	}

	public boolean isExplicit(){
		return explicit;
	}

	public MediaMetadata getMediaMetadata(){
		return mediaMetadata;
	}

	public Mixes getMixes(){
		return mixes;
	}

	public String getStreamStartDate(){
		return streamStartDate;
	}

	public boolean isDjReady(){
		return djReady;
	}
}