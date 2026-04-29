package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultItem{

	@JsonProperty("songName")
	private String songName;

	@JsonProperty("copyright")
	private String copyright;

	@JsonProperty("isCprWhite")
	private String isCprWhite;

	@JsonProperty("contentId")
	private String contentId;

	@JsonProperty("albumId")
	private String albumId;

	@JsonProperty("haveShockRing")
	private int haveShockRing;

	@JsonProperty("singerList")
	private List<SingerListItem> singerList;

	@JsonProperty("mvCopyrightType")
	private int mvCopyrightType;

	@JsonProperty("duration")
	private int duration;

	@JsonProperty("copyrightType")
	private int copyrightType;

	@JsonProperty("lrcUrl")
	private String lrcUrl;

	@JsonProperty("songPinyin")
	private String songPinyin;

	@JsonProperty("restrictType")
	private int restrictType;

	@JsonProperty("mvId")
	private String mvId;

	@JsonProperty("id")
	private String id;

	@JsonProperty("downloadTags")
	private List<String> downloadTags;

	@JsonProperty("songId")
	private String songId;

	@JsonProperty("lyricUrl")
	private String lyricUrl;

	@JsonProperty("ext")
	private Ext ext;

	@JsonProperty("img3")
	private String img3;

	@JsonProperty("album")
	private String album;

	@JsonProperty("ringCopyrightId")
	private String ringCopyrightId;

	@JsonProperty("highlightStr")
	private List<String> highlightStr;

	@JsonProperty("ringToneId")
	private String ringToneId;

	@JsonProperty("copyrightId")
	private String copyrightId;

	@JsonProperty("audioFormats")
	private List<AudioFormatsItem> audioFormats;

	@JsonProperty("name")
	private String name;

	@JsonProperty("showTags")
	private List<String> showTags;

	@JsonProperty("collect")
	private int collect;

	@JsonProperty("albumPinyin")
	private String albumPinyin;

	@JsonProperty("img2")
	private String img2;

	@JsonProperty("resourceType")
	private String resourceType;

	@JsonProperty("img1")
	private String img1;

	@JsonProperty("songDescs")
	private String songDescs;

	@JsonProperty("originalSing")
	private String originalSing;

	@JsonProperty("clickRatioString")
	private String clickRatioString;

	@JsonProperty("mvShow")
	private MvShow mvShow;

	@JsonProperty("mod")
	private String mod;

	@JsonProperty("videoToneShow")
	private VideoToneShow videoToneShow;

	@JsonProperty("albumShow")
	private AlbumShow albumShow;

	@JsonProperty("singerShow")
	private SingerShow singerShow;

	public String getSongName(){
		return songName;
	}

	public String getCopyright(){
		return copyright;
	}

	public String getIsCprWhite(){
		return isCprWhite;
	}

	public String getContentId(){
		return contentId;
	}

	public String getAlbumId(){
		return albumId;
	}

	public int getHaveShockRing(){
		return haveShockRing;
	}

	public List<SingerListItem> getSingerList(){
		return singerList;
	}

	public int getMvCopyrightType(){
		return mvCopyrightType;
	}

	public int getDuration(){
		return duration;
	}

	public int getCopyrightType(){
		return copyrightType;
	}

	public String getLrcUrl(){
		return lrcUrl;
	}

	public String getSongPinyin(){
		return songPinyin;
	}

	public int getRestrictType(){
		return restrictType;
	}

	public String getMvId(){
		return mvId;
	}

	public String getId(){
		return id;
	}

	public List<String> getDownloadTags(){
		return downloadTags;
	}

	public String getSongId(){
		return songId;
	}

	public String getLyricUrl(){
		return lyricUrl;
	}

	public Ext getExt(){
		return ext;
	}

	public String getImg3(){
		return img3;
	}

	public String getAlbum(){
		return album;
	}

	public String getRingCopyrightId(){
		return ringCopyrightId;
	}

	public List<String> getHighlightStr(){
		return highlightStr;
	}

	public String getRingToneId(){
		return ringToneId;
	}

	public String getCopyrightId(){
		return copyrightId;
	}

	public List<AudioFormatsItem> getAudioFormats(){
		return audioFormats;
	}

	public String getName(){
		return name;
	}

	public List<String> getShowTags(){
		return showTags;
	}

	public int getCollect(){
		return collect;
	}

	public String getAlbumPinyin(){
		return albumPinyin;
	}

	public String getImg2(){
		return img2;
	}

	public String getResourceType(){
		return resourceType;
	}

	public String getImg1(){
		return img1;
	}

	public String getSongDescs(){
		return songDescs;
	}

	public String getOriginalSing(){
		return originalSing;
	}

	public String getClickRatioString(){
		return clickRatioString;
	}

	public MvShow getMvShow(){
		return mvShow;
	}

	public String getMod(){
		return mod;
	}

	public VideoToneShow getVideoToneShow(){
		return videoToneShow;
	}

	public AlbumShow getAlbumShow(){
		return albumShow;
	}

	public SingerShow getSingerShow(){
		return singerShow;
	}
}