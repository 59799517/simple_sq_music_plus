/*
 * JAVA client for QOBUZ.API (http://www.qobuz.com/fr-fr/page/labs).
 *
 * Copyright (C) 2017 Marco Curti (marcoc1712 at gmail dot com).
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.mc2.qobuz.api.v02.elements;

import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.API.elements.Credential;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author marco
 */
public final class CredentialFromApi extends QobuzObjectFromApi implements Credential {

	private Long id;
	private String label;
	private String description;
	private ParametersFromApi parameters;

	
	public CredentialFromApi() {
        super();
    }
	
	public CredentialFromApi(JSONObject jsonObject) throws QobuzAPIException {
        super(jsonObject);
        
        // fill the KeyList here;
        
        KeyList.add(ID);
		KeyList.add(LABEL);
		KeyList.add(DESCRIPTION);
		KeyList.add(PARAMETERS);
		
		checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }
		
		try {
                id = this.getLong(ID, jsonObject);
				label = this.getString(LABEL, jsonObject);
				description = this.getString(DESCRIPTION, jsonObject);
				
				parameters = jsonObject.has(PARAMETERS) ? 
                        jsonObject.isNull(PARAMETERS) ? 
                            null : new ParametersFromApi(jsonObject.getJSONObject(PARAMETERS)) : null;
				
				
			} catch (JSONException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, jsonObject.toString(), ex);
            throw new QobuzAPIException(ex.getMessage(), ex);
        }
   
    }

	@Override
	public Long getCredentialId() {
		return this.id;
	}

	@Override
	public String getCredentialLabel() {
		return this.label;
	}

	@Override
	public String getCredentialDescription() {
		return this.description;
	}

	@Override
	public Boolean isLossysStreamingEnabled() {
		if (parameters != null) return parameters.isLossysStreamingEnabled();
		return false;
	}

	@Override
	public Boolean isLosslessStreamingEnabled() {
		if (parameters != null) return parameters.isLosslessStreamingEnabled();
		return false;
	}

	@Override
	public Boolean isHiResStreamingEnabled() {
		if (parameters != null) return parameters.isHiResStreamingEnabled();
		return false;
	}
	
	@Override
	public Boolean isHiResPurchasesStreamingEnabled() {
		if (parameters != null) return parameters.isHiResPurchasesStreamingEnabled();
		return false;
	}

	@Override
	public Boolean isMobileStreamingEnabled() {
		if (parameters != null) return parameters.isMobileStreamingEnabled();
		return false;
	}

	@Override
	public Boolean isOfflineStreamingEnabled() {
		if (parameters != null) return parameters.isOfflineStreamingEnabled();
		return false;
	}

	@Override
	public Boolean isHfpPurchaseEnabled() {
		if (parameters != null) return parameters.isHfpPurchaseEnabled();
		return false;
	}

	@Override
	public ArrayList<Integer> getIncludedFormatGroupIds() {
		if (parameters != null) return parameters.getIncludedFormatGroupIds();
		return new ArrayList<>();
	}

	@Override
	public String getColorSchemeLogo() {
		if (parameters != null) return parameters.getColorSchemeLogo();
		return "";
	}

	@Override
	public String getLabel() {
		if (parameters != null) return parameters.getLabel();
		return "";
	}

	@Override
	public String getShortLabel() {
		if (parameters != null) return parameters.getShortLabel();
		return "";
	}

	@Override
	public String getSource() {
		if (parameters != null) return parameters.getSource();
		return "";
	}
}

final class ParametersFromApi extends QobuzObjectFromApi  {
	
	String LOSSY_STREAMING = "lossy_streaming";
	String LOSSLESS_STREAMING = "lossless_streaming";
	String HIRES_STREAMING = "hires_streaming";
	String HIRES_PURCHASE_STREAMING = "hires_purchases_streaming";
	String MOBILE_STREAMING = "mobile_streaming";
	String OFFLINE_STREAMING = "offline_streaming";
	String HTF_PURCHASE = "hfp_purchase";
	String INCLUDED_FORMAT_GROUP_IDS = "included_format_group_ids";
	String COLOR_SCHEME = "color_scheme";
	String LABEL = "label";
	String SHORT_LABEL = "short_label";
	String SOURCE = "source";
	
	private Boolean isLossyStreamingEnabled;
	private Boolean isLosslessStreamingEnabled;
	private Boolean isHiresStreamingEnabled;
	private Boolean isHiresPurchasesStreamingEnabled;
	private Boolean isMobileStreamingEnabled;
	private Boolean isOfflineStreamingEnabled;
	private Boolean isHfpPurchaseEnabled;
	
	private ArrayList<Integer> includedFormatGroupIds = new ArrayList<>();
    
	private ColorSchemeFromApi colorScheme;
	
	private String label;
	private String shortLabel;
	private String source;
	

	public ParametersFromApi() {
        super();
    }
	
	public ParametersFromApi(JSONObject jsonObject) throws QobuzAPIException {
        super(jsonObject);
		
		// fill the KeyList here;
        
        KeyList.add(LOSSY_STREAMING);
		KeyList.add(LOSSLESS_STREAMING);
		KeyList.add(HIRES_STREAMING);
		KeyList.add(HIRES_PURCHASE_STREAMING);
		KeyList.add(MOBILE_STREAMING);
		KeyList.add(OFFLINE_STREAMING);
		KeyList.add(HTF_PURCHASE);
		KeyList.add(INCLUDED_FORMAT_GROUP_IDS);
		KeyList.add(COLOR_SCHEME);
		KeyList.add(LABEL);
		KeyList.add(SHORT_LABEL);
		KeyList.add(SOURCE);
		
		checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }
		
		try {
                isLossyStreamingEnabled = this.getBoolean(LOSSY_STREAMING, jsonObject);
				isLosslessStreamingEnabled = this.getBoolean(LOSSLESS_STREAMING, jsonObject);
				isHiresStreamingEnabled = this.getBoolean(HIRES_STREAMING, jsonObject);
				isHiresPurchasesStreamingEnabled = this.getBoolean(HIRES_PURCHASE_STREAMING, jsonObject);
				isMobileStreamingEnabled = this.getBoolean(MOBILE_STREAMING, jsonObject);
				isOfflineStreamingEnabled = this.getBoolean(OFFLINE_STREAMING, jsonObject);
				isHfpPurchaseEnabled = this.getBoolean(HTF_PURCHASE, jsonObject);
				
				if (jsonObject.has(INCLUDED_FORMAT_GROUP_IDS)) {
                    JSONArray jFormats = jsonObject.getJSONArray(INCLUDED_FORMAT_GROUP_IDS);
                    for (int i = 0; i < jFormats.length(); i++) {
                        includedFormatGroupIds.add(jFormats.getInt(i));
                    }
                }
				
				colorScheme = jsonObject.has(COLOR_SCHEME) ? 
                        jsonObject.isNull(COLOR_SCHEME) ? 
                            null : new ColorSchemeFromApi(jsonObject.getJSONObject(COLOR_SCHEME)) : null;
				
				
				label = this.getString(LABEL, jsonObject);
				shortLabel = this.getString(SHORT_LABEL, jsonObject);
				source = this.getString(SOURCE, jsonObject);
				
				
			} catch (JSONException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, jsonObject.toString(), ex);
            throw new QobuzAPIException(ex.getMessage(), ex);
        }

	}
	
	/**
	 * @return the Credential parameter is lossy Streaming enableb
	 */
	Boolean isLossysStreamingEnabled(){
		return isLossyStreamingEnabled;
	}
	/**
	 * @return the Credential parameter is lossless Streaming enableb
	 */
	Boolean isLosslessStreamingEnabled(){
		return isLosslessStreamingEnabled;
	}
	/**
	 * @return the Credential parameter is HiRes Streaming enableb
	 */
	Boolean isHiResStreamingEnabled(){
		return isHiresStreamingEnabled;
	}
	/**
	 * @return the Credential parameter is HiRes Purchases Streaming enableb
	 */
	Boolean isHiResPurchasesStreamingEnabled(){
		return isHiresPurchasesStreamingEnabled;
	}
	/**
	 * @return the Credential parameter is Mobile Streaming enableb
	 */
	Boolean isMobileStreamingEnabled(){
		return isMobileStreamingEnabled;
	}
	/**
	 * @return the Credential parameter is Offline Streaming enableb
	 */
	Boolean isOfflineStreamingEnabled(){
		return isOfflineStreamingEnabled;
	}
	/**
	 * @return the Credential parameter is HFP Purchase enableb
	 */
	Boolean isHfpPurchaseEnabled(){
		return isHfpPurchaseEnabled;
	}
	/**
	 * @return the Included Format Group Ids
	 */
	ArrayList<Integer> getIncludedFormatGroupIds(){
		return includedFormatGroupIds;
	}
	/**
	 * @return the Color Scheme logo
	 */
	String getColorSchemeLogo(){
		
		if (colorScheme != null) {
			return colorScheme.getLogo();
		}
		return "";
	}
	/**
	 * @return the Label
	 */
	String getLabel(){
		return label;
	}
	/**
	 * @return the Short Label
	 */
	String getShortLabel(){
		return shortLabel;
	}
	/**
	 * @return the Source
	 */
	String getSource(){
		return source;
	}
}

final class ColorSchemeFromApi extends QobuzObjectFromApi  {
	
	String LOGO = "logo";
	
	private String logo;
	
	public ColorSchemeFromApi() {
        super();
    }
	
	public ColorSchemeFromApi(JSONObject jsonObject) throws QobuzAPIException {
        super(jsonObject);
		
		// fill the KeyList here;
        
        KeyList.add(LOGO);
		
		checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }
		
		try {
                logo = this.getString(LOGO, jsonObject);
				
			} catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, jsonObject.toString(), ex);
            throw new QobuzAPIException(ex.getMessage(), ex);
        }
	}
	/**
	 * @return the Color Scheme logo
	 */
	String getLogo(){
		
		return logo;
	}
}