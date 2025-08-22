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

import java.util.ArrayList;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.API.elements.Article;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mc2.qobuz.api.v02.API.elements.Track;
import com.mc2.qobuz.api.v02.IMPL.QobuzUtils;


public final class TrackFromApi extends QobuzObjectFromApi implements Track {

	
    private Long id;
    private String title;
    private String work;
    private ArtistFromApi composer;
    private String performers;
    private Long  duration;
    private AlbumFromApi album;
    private ArtistFromApi performer;
    private String copyright;
    private Long  media_number;
    private Long  track_number;
    private String version;
    private Boolean purchasable;
    private Boolean streamable;
    private Boolean previewable;
    private Boolean sampleable;
    private Boolean downloadable;
    private Boolean displayable;
    private Long  purchasable_at;
    private Long  streamable_at;
    private Double maximum_sampling_rate;
    private Long  maximum_bit_depth;
    private Boolean hires;
	private String isrc;
    
    private ArrayList<Article> articles = new ArrayList<>();
	
	/* 9/9/19 */
	private Boolean hires_streamable;
	private Boolean parental_warning;
	private Long  maximum_channel_count;
	//private ArrayList<String> article_ids;
    
     public  TrackFromApi() {
        super();
     }
     
     public  TrackFromApi(JSONObject jsonObject) throws QobuzAPIException {
        super(jsonObject);
        
        // fill the KeyList here;
        
        KeyList.add(ID);
        KeyList.add(TITLE);
        KeyList.add(WORK);
        KeyList.add(COMPOSER);
        KeyList.add(PERFORMERS);
        KeyList.add(DURATION);
        KeyList.add(ALBUM);
        KeyList.add(ARTICLES);
        KeyList.add(PERFORMER);
        KeyList.add(COPYRIGHT);
        KeyList.add(MEDIA_NUMBER);
        KeyList.add(TRACK_NUMBER);
        KeyList.add(VERSION);
        KeyList.add(PURCHASABLE);
        KeyList.add(STREAMABLE);
        KeyList.add(PREVIEWABLE);
        KeyList.add(SAMPLEABLE);
        KeyList.add(DOWNLOADABLE);
        KeyList.add(DISPLAYABLE);
        KeyList.add(PURCHASABLE_AT);
        KeyList.add(STREAMABLE_AT);
        KeyList.add(MAXIMUM_SAMPLING_RATE);
        KeyList.add(MAXIMUM_BIT_DEPTH);
        KeyList.add(HIRES);
		KeyList.add(ISRC);

		KeyList.add(PARENTAL_WARNING);
        KeyList.add(HIRES_STREAMABLE);
        KeyList.add(MAXIMUM_CHANNEL_COUNT);
		KeyList.add(ARTICLE_IDS);
		
        checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }

        try {
                
				id = this.getLong(ID, jsonObject);
				title = this.getString(TITLE, jsonObject);
				work = this.getString(WORK, jsonObject);
				performers = this.getString(PERFORMERS, jsonObject);
                duration = this.getLong(DURATION, jsonObject);
				copyright = this.getString(COPYRIGHT, jsonObject);
				media_number = this.getLong(MEDIA_NUMBER, jsonObject);
				track_number = this.getLong(TRACK_NUMBER, jsonObject);
				purchasable= this.getBoolean(PURCHASABLE, jsonObject);
				streamable= this.getBoolean(STREAMABLE, jsonObject);
				previewable= this.getBoolean(PREVIEWABLE, jsonObject);
				sampleable= this.getBoolean(SAMPLEABLE, jsonObject);
				downloadable= this.getBoolean(DOWNLOADABLE, jsonObject);
				displayable= this.getBoolean(DISPLAYABLE, jsonObject);
				purchasable_at = this.getDate(PURCHASABLE_AT, jsonObject);
				streamable_at = this.getDate(STREAMABLE_AT, jsonObject);
				maximum_sampling_rate = this.getDouble(MAXIMUM_SAMPLING_RATE, jsonObject);
                maximum_bit_depth = this.getLong(MAXIMUM_BIT_DEPTH, jsonObject);
                hires = this.getBoolean(HIRES, jsonObject);
                isrc = this.getString(ISRC, jsonObject);
                hires_streamable = this.getBoolean(HIRES_STREAMABLE, jsonObject);
				parental_warning = this.getBoolean(PARENTAL_WARNING, jsonObject);
				maximum_channel_count= this.getLong(MAXIMUM_CHANNEL_COUNT, jsonObject);
				version = this.getString(VERSION, jsonObject);
				
				if (jsonObject.has(ALBUM)&& !jsonObject.isNull(ALBUM)){
					album =  new AlbumFromApi(jsonObject.getJSONObject(ALBUM));
					//rawKeyValuePair.put(ALBUM, album.getTitle());
				}
				if (jsonObject.has(COMPOSER)&& !jsonObject.isNull(COMPOSER)){
					composer =  new ArtistFromApi(jsonObject.getJSONObject(COMPOSER));
					//rawKeyValuePair.put(COMPOSER, composer.getName());
				}
				
				if (jsonObject.has(PERFORMER)&& !jsonObject.isNull(PERFORMER)){
					performer =  new ArtistFromApi(jsonObject.getJSONObject(PERFORMER));
					//rawKeyValuePair.put(PERFORMER, performer.getName());
				}
				if (jsonObject.has(ARTICLES)) {
                    JSONArray jArticles = jsonObject.getJSONArray(ARTICLES);
                    for (int i = 0; i < jArticles.length(); i++) {
                        articles.add(new ArticleFromApi(jArticles.getJSONObject(i)));
                    }
                }
				/*
				if (jsonObject.has(ARTICLE_IDS)) {
                    JSONArray jIds = jsonObject.getJSONArray(ARTICLE_IDS);
                    for (int i = 0; i < jIds.length(); i++) {
                        article_ids.add(jIds.getString(i));
                    }
                }
				*/
				
        } catch (JSONException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            throw new QobuzAPIException(ex.getMessage(), ex);
        }
    }

    /**
     * @return the id
     */
	@Override
    public Long getId() {
        return id;
    }

    /**
     * @return the title
     */
	@Override
    public String getTitle() {
        return title;
    }

    /**
     * @return the work
     */
	@Override
    public String getWorkTitle() {
        return work;
    }
	
	/**
     * @return the work stored in work field or guessed from the title.
     */
	@Override
    public String geWorkGuessed() {
       return QobuzUtils.workFromTitle(this.getWorkTitle(),this.getTitle());
	}
	/**
     * @return the title cleaned by the work part as per in workGuessed.
     */
	@Override
    public String getTitleOnly() {
       return QobuzUtils.calcTitleOnly(this.geWorkGuessed(),this.getTitle());
	}
    /**
     * @return the composer
     */
	@Override
    public ArtistFromApi getComposer() {
        return composer;
    }

    /**
     * @return the performers as a string
     */
	@Override
    public String getPerformers() {
        return performers;
    }
	/**
     * @return the performers as a list of strings (name: roles)
     */
	@Override
	public ArrayList<String> getPerformerList(){
        
       return QobuzUtils.calcPerformerList(this.getPerformers());
    }
	/**
     * @return the performers as a map of string where role is the key 
	 * and the performers name the value.
     */
	@Override
	public Map<String,String> getPerformersRoleNamesMap(){

        return QobuzUtils.getPerformersRoleNamesMap(this.getPerformerList()); 
    }
    /**
     * @return the duration
     */
	@Override
    public Long getDuration() {
        return duration;
    }
    /**
     * @return the album
     */
	@Override
    public AlbumFromApi getAlbum() {
        return album;
    }
    /**
     * @return the performer
     */
	@Override
    public ArtistFromApi getPerformer() {
        return performer;
    }

    /**
     * @return the copyright
     */
	@Override
    public String getCopyright() {
        return copyright;
    }

    /**
     * @return the media_number
     */
	@Override
    public Long getMedia_number() {
        return media_number;
    }

    /**
     * @return the track_number
     */
	@Override
    public Long getTrack_number() {
        return track_number;
    }

    /**
     * @return the version
     */
	@Override
    public String getVersion() {
        return version;
    }

    /**
     * @return the purchasable
     */
	@Override
    public Boolean getPurchasable() {
        return purchasable;
    }

    /**
     * @return the streamable
     */
	@Override
    public Boolean getStreamable() {
        return streamable;
    }

    /**
     * @return the previewable
     */
	@Override
    public Boolean getPreviewable() {
        return previewable;
    }

    /**
     * @return the sampleable
     */
	@Override
    public Boolean getSampleable() {
        return sampleable;
    }

    /**
     * @return the downloadable
     */
	@Override
    public Boolean getDownloadable() {
        return downloadable;
    }

    /**
     * @return the displayable
     */
	@Override
    public Boolean getDisplayable() {
        return displayable;
    }

    /**
     * @return the purchasable_at
     */
	@Override
    public Long getPurchasable_at() {
        return purchasable_at;
    }

    /**
     * @return the streamable_at
     */
	@Override
    public Long getStreamable_at() {
        return streamable_at;
    }

    /**
     * @return the maximum_sampling_rate
     */
	@Override
    public Double getMaximum_sampling_rate() {
        return maximum_sampling_rate;
    }

    /**
     * @return the maximum_bit_depth
     */
	@Override
    public Long getMaximum_bit_depth() {
        return maximum_bit_depth;
    }

    /**
     * @return the hires
     */
	@Override
    public Boolean getHires() {
        return hires;
    }
	
	 /**
     * @return the isrc
     */
	@Override
    public String getIsrc() {
        return isrc;
    }
	
    /**
     * @return the articles
     */
	@Override
    public ArrayList<Article> getArticles() {
        return articles;
    }
	
	/**
     * @return the hires streamable
     */
	@Override
    public Boolean getHiresStreamable() {
        return hires_streamable;
    }
	
	 /**
     * @return the parental warning
     */
	@Override
    public Boolean getParentalWarning() {
        return parental_warning;
    }
	
	/**
     * @return the maximum_channel_count
     */
	@Override
    public Long getMaximumChannelCount() {
        return maximum_channel_count;
    }
	
	
	
	/**
     * @return the article_Ids (not used)
     
    public List<String> getArticleIds() {
        return article_ids;
    }
	**/
}

