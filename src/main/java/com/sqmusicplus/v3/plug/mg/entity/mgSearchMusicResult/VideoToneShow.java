package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoToneShow{

	@JsonProperty("mod")
	private String mod;

	@JsonProperty("singer")
	private String singer;

	@JsonProperty("name")
	private String name;

	@JsonProperty("aspectRatio")
	private String aspectRatio;

	@JsonProperty("id")
	private String id;

	@JsonProperty("libraryType")
	private String libraryType;

	@JsonProperty("mapImg")
	private MapImg mapImg;

	@JsonProperty("imgItems")
	private List<ImgItemsItem> imgItems;

	@JsonProperty("resourceType")
	private String resourceType;

	public String getMod(){
		return mod;
	}

	public String getSinger(){
		return singer;
	}

	public String getName(){
		return name;
	}

	public String getAspectRatio(){
		return aspectRatio;
	}

	public String getId(){
		return id;
	}

	public String getLibraryType(){
		return libraryType;
	}

	public MapImg getMapImg(){
		return mapImg;
	}

	public List<ImgItemsItem> getImgItems(){
		return imgItems;
	}

	public String getResourceType(){
		return resourceType;
	}
}