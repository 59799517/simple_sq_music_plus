package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemsItem{

	@JsonProperty("item")
	private Item item;

	@JsonProperty("type")
	private String type;

	@JsonProperty("streamReady")
	private boolean streamReady;

	@JsonProperty("vibrantColor")
	private String vibrantColor;

	@JsonProperty("audioModes")
	private List<String> audioModes;

	@JsonProperty("releaseDate")
	private String releaseDate;

	@JsonProperty("upload")
	private boolean upload;

	@JsonProperty("allowStreaming")
	private boolean allowStreaming;

	@JsonProperty("payToStream")
	private boolean payToStream;

	@JsonProperty("audioQuality")
	private String audioQuality;

	@JsonProperty("title")
	private String title;

	@JsonProperty("url")
	private String url;

	@JsonProperty("cover")
	private String cover;

	@JsonProperty("explicit")
	private boolean explicit;

	@JsonProperty("duration")
	private int duration;

	@JsonProperty("artists")
	private List<ArtistsItem> artists;

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

	@JsonProperty("artistTypes")
	private Object artistTypes;

	@JsonProperty("selectedAlbumCoverFallback")
	private String selectedAlbumCoverFallback;

	@JsonProperty("artistRoles")
	private List<ArtistRolesItem> artistRoles;

	@JsonProperty("name")
	private String name;

	@JsonProperty("mixes")
	private Mixes mixes;

	@JsonProperty("picture")
	private Object picture;

	public Item getItem(){
		return item;
	}

	public String getType(){
		return type;
	}

	public boolean isStreamReady(){
		return streamReady;
	}

	public String getVibrantColor(){
		return vibrantColor;
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

	public boolean isAllowStreaming(){
		return allowStreaming;
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

	public String getUrl(){
		return url;
	}

	public String getCover(){
		return cover;
	}

	public boolean isExplicit(){
		return explicit;
	}

	public int getDuration(){
		return duration;
	}

	public List<ArtistsItem> getArtists(){
		return artists;
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

	public Object getArtistTypes(){
		return artistTypes;
	}

	public String getSelectedAlbumCoverFallback(){
		return selectedAlbumCoverFallback;
	}

	public List<ArtistRolesItem> getArtistRoles(){
		return artistRoles;
	}

	public String getName(){
		return name;
	}

	public Mixes getMixes(){
		return mixes;
	}

	public Object getPicture(){
		return picture;
	}
}