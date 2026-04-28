package com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AudioFormatsItem{

	@JsonProperty("aformat")
	private String aformat;

	@JsonProperty("isize")
	private String isize;

	@JsonProperty("showTags")
	private List<String> showTags;

	@JsonProperty("formatType")
	private String formatType;

	@JsonProperty("asize")
	private String asize;

	@JsonProperty("iformat")
	private String iformat;

	@JsonProperty("resourceType")
	private String resourceType;

	public String getAformat(){
		return aformat;
	}

	public String getIsize(){
		return isize;
	}

	public List<String> getShowTags(){
		return showTags;
	}

	public String getFormatType(){
		return formatType;
	}

	public String getAsize(){
		return asize;
	}

	public String getIformat(){
		return iformat;
	}

	public String getResourceType(){
		return resourceType;
	}
}