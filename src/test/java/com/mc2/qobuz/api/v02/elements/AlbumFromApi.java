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

import com.mc2.qobuz.api.v02.lists.TrackListFromApi;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mc2.qobuz.api.v02.API.elements.Album;
import com.mc2.qobuz.api.v02.API.elements.Article;

import com.mc2.qobuz.api.v02.query.AlbumGet;

public final class AlbumFromApi extends QobuzObjectFromApi implements Album {
    
	
    private String id;
    private String title;
    
    private String slug;
    private Long tracks_count;
    private String recording_information;
    private Double product_sales_factors_weekly;
    private String description;
    private String catchline;
    private Long created_at;
    private Double product_sales_factors_monthly;
    private Double popularity;
    private Long  media_count;
    private String product_type;
    private String copyright;
    private String product_url;
    private String url;
    private Long  duration;
    private Long  released_at;
    private String maximum_technical_specifications;
    private String program;
    private String relative_url;
    private Long  qobuz_id;
    private Double product_sales_factors_yearly;
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
	private String upc;

    private GenreFromApi genre;
    private CollectionFromApi collection;
    private ArtistFromApi composer;
    private AreaFromApi area;
    private ArtistFromApi artist;
    private ImageFromApi image;
    private InstrumentFromApi instrument;
    private LabelFromApi label;
    private PeriodFromApi period;
    private TrackListFromApi tracks;
    
    private ArrayList<GoodyFromApi> goodies = new ArrayList<>();
    private ArrayList<String> genres_list = new ArrayList<>();
    private ArrayList<Article> articles = new ArrayList<>();
   //private ArrayList<StoreRelated> store_related = new ArrayList<>();
    private ArrayList<AwardFromApi> awards = new ArrayList<>();
    
	/* 9/9/19 */
	private Boolean hires_streamable;
	private Boolean parental_warning;
	private Long maximum_channel_count;
	private String release_date_download;
	private String release_date_stream;
	private String release_date_original;
	private String subtitle;
	
	//private ArrayList<String> article_ids;
	
	/* 18/08/2020 */
	
	private String version;
	private String releaseType;
	private ArrayList<String> releaseTags = new ArrayList<>();
	
     public  AlbumFromApi() {
        super();
     }
     
     public  AlbumFromApi(JSONObject jsonObject) throws QobuzAPIException {
        super(jsonObject);
        
        // fill the KeyList here;
        
        KeyList.add(ID);
        KeyList.add(TITLE);
        KeyList.add(SLUG);
        
        KeyList.add(GENRE);
        KeyList.add(TRACKSCOUNT);
        KeyList.add(RECORDING_INFORMATION);
        KeyList.add(COLLECTION);
        KeyList.add(PRODUCT_SALES_FACTORS_WEEKLY);
        KeyList.add(GOODIES);
        KeyList.add(COMPOSER);
        KeyList.add(AREA);
        KeyList.add(DESCRIPTION);
        KeyList.add(CATCHLINE);
        KeyList.add(CREATED_AT);
        KeyList.add(GENRES_LIST);
        KeyList.add(ARTIST);
        KeyList.add(PRODUCT_SALES_FACTORS_MONTHLY);
        KeyList.add(POPULARITY);
        KeyList.add(ARTICLES);
        KeyList.add(STORE_RELATED);
        KeyList.add(IMAGE);
        KeyList.add(MEDIA_COUNT);
        KeyList.add(PRODUCT_TYPE);
        KeyList.add(LABEL);
        KeyList.add(INSTRUMENT);
        KeyList.add(COPYRIGHT);
        KeyList.add(PERIOD);
        KeyList.add(PRODUCT_URL);
        KeyList.add(URL);
		KeyList.add(UPC);
        KeyList.add(DURATION);
        KeyList.add(RELEASED_AT);
        KeyList.add(MAXIMUM_TECHNICAL_SPECIFICATIONS);
        KeyList.add(AWARDS);
        KeyList.add(PROGRAM);
        KeyList.add(RELATIVE_URL);
        KeyList.add(QOBUZ_ID);
        KeyList.add(PRODUCT_SALES_FACTORS_YEARLY);
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
        KeyList.add(TRACKS);
		
		KeyList.add(PARENTAL_WARNING);
        KeyList.add(HIRES_STREAMABLE);
        KeyList.add(MAXIMUM_CHANNEL_COUNT);
        
		KeyList.add(RELEASE_DATE_DOWNLOAD);
		KeyList.add(RELEASE_DATE_STREAM);
		KeyList.add(RELEASE_DATE_ORIGINAL);
		KeyList.add(SUBTITLE);
		KeyList.add(ARTICLE_IDS);
		
		KeyList.add(VERSION);
		KeyList.add(RELEASE_TYPE);
		KeyList.add(RELEASE_TAGS);
        
        checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }

        try {
                id = this.getString(ID, jsonObject);
				title = this.getString(TITLE, jsonObject);
				slug = this.getString(SLUG, jsonObject);
				tracks_count = this.getLong(TRACKSCOUNT, jsonObject);
				recording_information = this.getString(RECORDING_INFORMATION, jsonObject);
				product_sales_factors_weekly = this.getDouble(PRODUCT_SALES_FACTORS_WEEKLY, jsonObject);
				catchline = this.getString(CATCHLINE, jsonObject);
				created_at = this.getDate(CREATED_AT, jsonObject);
				product_sales_factors_monthly = this.getDouble(PRODUCT_SALES_FACTORS_MONTHLY, jsonObject);
				popularity =this.getDouble(POPULARITY, jsonObject);                
				media_count = this.getLong(MEDIA_COUNT, jsonObject);
				product_type = this.getString(PRODUCT_TYPE, jsonObject);
				product_url = this.getString(PRODUCT_URL, jsonObject);
				url = this.getString(URL, jsonObject);
				upc = this.getString(UPC, jsonObject);
				duration = this.getLong(DURATION, jsonObject);
				released_at = this.getDate(RELEASED_AT, jsonObject);
				maximum_technical_specifications = this.getString(MAXIMUM_TECHNICAL_SPECIFICATIONS, jsonObject);
				program = this.getString(PROGRAM, jsonObject);
				relative_url = this.getString(RELATIVE_URL, jsonObject);
				qobuz_id = this.getLong(QOBUZ_ID, jsonObject);
				product_sales_factors_yearly = this.getDouble(PRODUCT_SALES_FACTORS_YEARLY, jsonObject);
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
				copyright = this.getString(COPYRIGHT, jsonObject);
				
				hires_streamable = this.getBoolean(HIRES_STREAMABLE, jsonObject);
				parental_warning = this.getBoolean(PARENTAL_WARNING, jsonObject);
				maximum_channel_count= this.getLong(MAXIMUM_CHANNEL_COUNT, jsonObject);
				release_date_download = this.getString(RELEASE_DATE_DOWNLOAD, jsonObject);
				release_date_stream = this.getString(RELEASE_DATE_STREAM, jsonObject);
				release_date_original = this.getString(RELEASE_DATE_ORIGINAL, jsonObject);
				subtitle  = this.getString(SUBTITLE, jsonObject);
				
				version  = this.getString(VERSION, jsonObject);
				releaseType  = this.getString(RELEASE_TYPE, jsonObject);
				
				if (jsonObject.has(DESCRIPTION)&& !jsonObject.isNull(DESCRIPTION)){
					
					Object obj = jsonObject.get(DESCRIPTION);
					
					if (obj instanceof String) {    
                
						description = this.getString(DESCRIPTION, jsonObject);

					} else if (obj instanceof JSONObject) {
						
						description = this.getString(CONTENT, (JSONObject) obj);
						
					}
					//rawKeyValuePair.put(DESCRIPTION, description);
				}
				
				image = jsonObject.has(IMAGE) ? 
                        jsonObject.isNull(IMAGE) ? 
                            null : new ImageFromApi(jsonObject.getJSONObject(IMAGE)) : null;
							
				if (jsonObject.has(COMPOSER)&& !jsonObject.isNull(COMPOSER)){
					composer =  new ArtistFromApi(jsonObject.getJSONObject(COMPOSER));
					//rawKeyValuePair.put(COMPOSER, composer.getName());
				}
				
				if (jsonObject.has(ARTIST)&& !jsonObject.isNull(ARTIST)){
					artist =  new ArtistFromApi(jsonObject.getJSONObject(ARTIST));
					//rawKeyValuePair.put(ARTIST, artist.getName());
				}
				
				if (jsonObject.has(GENRE)&& !jsonObject.isNull(GENRE)){
					genre =  new GenreFromApi(jsonObject.getJSONObject(GENRE));
					//rawKeyValuePair.put(GENRE, genre.getName());
				}
	
				if (jsonObject.has(COLLECTION)&& !jsonObject.isNull(COLLECTION)){
					collection =  new CollectionFromApi(jsonObject.getJSONObject(COLLECTION));
					//rawKeyValuePair.put(COLLECTION, collection.getName());
				}
				if (jsonObject.has(AREA)&& !jsonObject.isNull(AREA)){
					area =  new AreaFromApi(jsonObject.getJSONObject(AREA));
					//rawKeyValuePair.put(AREA, area.getName());
				}
				if (jsonObject.has(LABEL)&& !jsonObject.isNull(LABEL)){
					label =  new LabelFromApi(jsonObject.getJSONObject(LABEL));
					//rawKeyValuePair.put(LABEL, label.getName());
				}
				
				if (jsonObject.has(INSTRUMENT)&& !jsonObject.isNull(INSTRUMENT)){
					instrument =  new InstrumentFromApi(jsonObject.getJSONObject(INSTRUMENT));
					//rawKeyValuePair.put(INSTRUMENT, instrument.getName());
				}

				if (jsonObject.has(PERIOD)&& !jsonObject.isNull(PERIOD)){
					period =  new PeriodFromApi(jsonObject.getJSONObject(PERIOD));
					//rawKeyValuePair.put(PERIOD, period.getName());
				}
				
				tracks = jsonObject.has(TRACKS) ? 
                        jsonObject.isNull(TRACKS) ? 
                            null : new TrackListFromApi(jsonObject.getJSONObject(TRACKS)) : null;
				
				if (jsonObject.has(AWARDS)) {
                    JSONArray jAwards = jsonObject.getJSONArray(AWARDS);
                    for (int i = 0; i < jAwards.length(); i++) {
						AwardFromApi award = new AwardFromApi(jAwards.getJSONObject(i));
                        awards.add(award);
						//rawKeyValuePair.put(AWARDS, award.getName());
                    }
                }

				if (jsonObject.has(GOODIES)) {
                    JSONArray jGoodies = jsonObject.getJSONArray(GOODIES);
                    for (int i = 0; i < jGoodies.length(); i++) {
                        goodies.add(new GoodyFromApi(jGoodies.getJSONObject(i)));
                    }
                }
				
				if (jsonObject.has(GENRES_LIST)) {
                    JSONArray jGenres = jsonObject.getJSONArray(GENRES_LIST);
                    for (int i = 0; i < jGenres.length(); i++) {
                        genres_list.add(jGenres.getString(i));
                    }
                }
				
				if (jsonObject.has(RELEASE_TAGS)) {
                    JSONArray jTags = jsonObject.getJSONArray(RELEASE_TAGS);
                    for (int i = 0; i < jTags.length(); i++) {
                        releaseTags.add(jTags.getString(i));
                    }
                }
				/*
				if (jsonObject.has(STORE_RELATED)) {
                    
                    JSONObject store = jsonObject.getJSONObject(STORE_RELATED);
                    for(int i = 0; i<store.names().length(); i++){
                        
                        String key = store.names().getString(i);
                        store_related.add(new StoreRelated(key, store.getJSONObject(key)));
                    }
                }
				*/
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, jsonObject.toString(), ex);
            throw new QobuzAPIException(ex.getMessage(), ex);
        }
   
    }
	
	@Override
    public boolean isTracklistComplete(){
        
        if (tracks == null) return true; // no tracks to get.
        
        // API BUG: sometime reported total is different than the actual number
        // of records.
        
        // forget total if lesser than count (costs at least 1 more api call)
        if (tracks_count > tracks.getTotal()) return false;
        
        //Stop at total.
        return (tracks == null || tracks.getItems().size()>= tracks.getTotal());
    }
	@Override
    public void completeTrackList() throws QobuzAPIException{
        
        long size = 0;
        if (tracks != null){
            
            size = tracks.getItems().size();
        }
        
        // API BUG: sometime reported total is wrong.
        // this avoid a neverending loop if total is greater than 
        // the actual number of records.
        
        boolean stop=false;
        
        while (!stop && !isTracklistComplete()) {
            
            loadNextTracksPage();
            stop = (tracks.getItems().size() == size); 
            size = tracks.getItems().size();
        } 
    }
    
    private void loadNextTracksPage() throws QobuzAPIException{
    
        if (!isTracklistComplete()){
            
            ArrayList<TrackFromApi> extra = getNextTracksPage();
            tracks.getItems().addAll(extra);
        }
        
    }
    private ArrayList<TrackFromApi> getNextTracksPage() throws QobuzAPIException{
    
        if (!isTracklistComplete()) {
            
            AlbumGet q = new AlbumGet(id, (long)tracks.getItems().size());
            AlbumFromApi extra = q.getAlbum();
            
            return extra.getTrackList().getItems();
        }
        return new ArrayList<>();
    }
    
    /**
     * @return the id
     */
	@Override
    public String getId() {
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
     * @return the composer
     */
	@Override
    public ArtistFromApi getComposer() {
        return composer;
    }
    
    /**
     * @return the artist
     */
	@Override
    public ArtistFromApi getArtist() {
        return artist;
    }
    /**
     * @return the description
     */
	@Override
    public String getDescription() {
        return description;
    }
    /**
     * @return the genre
     */
	@Override
    public GenreFromApi getGenre() {
        return genre;
    }
    /**
     * @return the genres_list
     */
	@Override
    public ArrayList<String> getGenres_list() {
        return genres_list;
    }
    /**
     * @return the store_related
     
    public ArrayList<StoreRelated> getStore_related() {
        return store_related;
    }
    /*
	* 
     * @return the instrument
     */
	@Override
    public InstrumentFromApi getInstrument() {
        return instrument;
    }
    /**
     * @return the period
     */
	@Override
    public PeriodFromApi getPeriod() {
        return period;
    }
    /**
     * @return the area
     */
	@Override
    public AreaFromApi getArea() {
        return area;
    }
    /**
     * @return the recording_information
     */
	@Override
    public String getRecording_information() {
        return recording_information;
    }
    /**
     * @return the label
     */
	@Override
    public LabelFromApi getLabel() {
        return label;
    }
    /**
     * @return the released_at
     */
	@Override
    public Long getReleased_at() {
        return released_at;
    }
    /**
     * @return the copyright
     */
	@Override
    public String getCopyright() {
        return copyright;
    }
    /**
     * @return the program
     */
	@Override
    public String getProgram() {
        return program;
    }
    /**
     * @return the collection
     */
	@Override
    public CollectionFromApi getCollection() {
        return collection;
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
     * @return the maximum_technical_specifications
     */
	@Override
    public String getMaximum_technical_specifications() {
        return maximum_technical_specifications;
    }
    
    /**
     * @return the awards
     */
	@Override
    public ArrayList<AwardFromApi> getAwards() {
        return awards;
    }
    
     /**
     * @return the image
     */
	@Override
    public ImageFromApi getImage() {
        return image;
    }

    /**
     * @return the catchline
     */
	@Override
    public String getCatchline() {
        return catchline;
    }

    /**
     * @return the created_at
     */
	@Override
    public Long getCreated_at() {
        return created_at;
    }
    
/**
     * @return the product_type
     */
	@Override
    public String getProduct_type() {
        return product_type;
    }

    /**
     * @return the product_url
     */
	@Override
    public String getProduct_url() {
        return product_url;
    }

    /**
     * @return the url
     */
	@Override
    public String getUrl() {
        return url;
    }
	
    /**
     * @return the upc
     */
	@Override
    public String getUpc() {
        return upc;
    }
    /**
     * @return the relative_url
     */
	@Override
    public String getRelative_url() {
        return relative_url;
    }

    /**
     * @return the qobuz_id
     */
	@Override
    public Long getQobuz_id() {
        return qobuz_id;
    }
    /**
     * @return the slug
     */
	@Override
    public String getSlug() {
        return slug;
    }
    
    /**
     * @return the popularity
     */
	@Override
    public Double getPopularity() {
        return popularity;
    }
    /**
     * @return the product_sales_factors_weekly
     */
	@Override
    public Double getProduct_sales_factors_weekly() {
        return product_sales_factors_weekly;
    }
    /**
     * @return the product_sales_factors_monthly
     */
	@Override
    public Double getProduct_sales_factors_monthly() {
        return product_sales_factors_monthly;
    }
    /**
     * @return the product_sales_factors_yearly
     */
	@Override
    public Double getProduct_sales_factors_yearly() {
        return product_sales_factors_yearly;
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
     * @return the articles
     */
	@Override
    public ArrayList<Article> getArticles() {
        return articles;
    }

    /**
     * @return the goodies
     */
	@Override
    public ArrayList<GoodyFromApi> getGoodies() {
        return goodies;
    }

    /**
     * @return the media_count
     */
	@Override
    public Long getMedia_count() {
        return media_count;
    }
    /**
     * @return the tracks_count
     */
	@Override
    public Long getTracks_count() {
        return tracks_count;
    }
    
    /**
     * @return the duration
     */
	@Override
    public Long getDuration() {
        return duration;
    }
    
    /**
     * @return the tracks
     */
	@Override
    public TrackListFromApi getTrackList() {
        return tracks;
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
     * @return the subtitle
     */
	@Override
    public String getSubtitle() {
        return subtitle;
    }
	
	/**
     * @return the release_date_download
     */
	@Override
    public String getReleaseDateDownload() {
        return release_date_download;
    }
	/**
     * @return the release_date_stream
     */
	@Override
    public String getReleaseDateStream() {
        return release_date_stream;
    }
	/**
     * @return the release_date_original
     */
	@Override
    public String getReleaseDateOriginal() {
        return release_date_original;
    }
	
	/**
     * @return the version
     */
	@Override
    public String getVersion() {
        return version;
    }
	/**
	 * @return the release type
	 */
	@Override
	public String getReleaseType() {
        return releaseType;
    }
	
	 /**
     * @return the release tags
     */
	@Override
    public ArrayList<String> getReleaseTags() {
        return releaseTags;
    }
	
	/**
     * @return the article_Ids (not used)
     
    public List<String> getArticleIds() {
        return article_ids;
    }
	
    * @return the store_related (not used)
    
    public ArrayList<StoreRelated> getStore_related() {
        return store_related;
    }
	*/
}

