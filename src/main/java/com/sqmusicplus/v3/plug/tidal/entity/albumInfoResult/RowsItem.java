package com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RowsItem{

	@JsonProperty("modules")
	private List<ModulesItem> modules;

	public List<ModulesItem> getModules(){
		return modules;
	}
}