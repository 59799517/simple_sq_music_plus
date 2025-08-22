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
package com.mc2.qobuz.api.v02.API.elements;

import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.API.lists.TrackList;
import java.util.ArrayList;

/**
 *
 * @author marco
 */
public interface Album {

	String AREA = "area";
	String ARTICLES = "articles";
	String ARTICLE_IDS = "article_ids"; //no
	String ARTIST = "artist";
	String AWARDS = "awards";
	String CATCHLINE = "catchline";
	String COLLECTION = "collection";
	String COMPOSER = "composer";
	String CONTENT = "content";
	String COPYRIGHT = "copyright";
	String CREATED_AT = "created_at";
	String DESCRIPTION = "description";
	String DISPLAYABLE = "displayable";
	String DOWNLOADABLE = "downloadable";
	String DURATION = "duration";
	String GENRE = "genre";
	String GENRES_LIST = "genres_list";
	String GOODIES = "goodies";
	String HIRES = "hires";
	String HIRES_STREAMABLE = "hires_streamable";
	String ID = "id";
	String IMAGE = "image";
	String INSTRUMENT = "instrument";
	String LABEL = "label";
	String MAXIMUM_BIT_DEPTH = "maximum_bit_depth";
	String MAXIMUM_CHANNEL_COUNT = "maximum_channel_count";
	String MAXIMUM_SAMPLING_RATE = "maximum_sampling_rate";
	String MAXIMUM_TECHNICAL_SPECIFICATIONS = "maximum_technical_specifications";
	String MEDIA_COUNT = "media_count";
	String PARENTAL_WARNING = "parental_warning";
	String PERIOD = "period";
	String POPULARITY = "popularity";
	String PREVIEWABLE = "previewable";
	String PRODUCT_SALES_FACTORS_MONTHLY = "product_sales_factors_monthly"; //no
	String PRODUCT_SALES_FACTORS_WEEKLY = "product_sales_factors_weekly"; //no
	String PRODUCT_SALES_FACTORS_YEARLY = "product_sales_factors_yearly"; //no
	String PRODUCT_TYPE = "product_type";
	String PRODUCT_URL = "product_url";
	String PROGRAM = "program";
	String PURCHASABLE = "purchasable";
	String PURCHASABLE_AT = "purchasable_at";
	String QOBUZ_ID = "qobuz_id";
	String RECORDING_INFORMATION = "recording_information";
	String RELATIVE_URL = "relative_url";
	String RELEASED_AT = "released_at";
	String RELEASE_DATE_DOWNLOAD = "release_date_download";
	String RELEASE_DATE_ORIGINAL = "release_date_original";
	String RELEASE_DATE_STREAM = "release_date_stream";
	String SAMPLEABLE = "sampleable";
	String SLUG = "slug";
	String STORE_RELATED = "store_related";
	String STREAMABLE = "streamable";
	String STREAMABLE_AT = "streamable_at";
	String SUBTITLE = "subtitle";
	String TITLE = "title";
	String TRACKS = "tracks";
	String TRACKSCOUNT = "tracks_count";
	String UPC = "upc";
	String URL = "url";
	
	/*18-08-2020 */
	String VERSION = "version";
	String RELEASE_TYPE ="release_type";
	String RELEASE_TAGS ="release_tags";

	boolean isTracklistComplete();
	
	void completeTrackList() throws QobuzAPIException;

	/**
	 * @return the area
	 */
	Area getArea();

	/**
	 * @return the articles
	 */
	ArrayList<? extends Article> getArticles();

	/**
	 * @return the artist
	 */
	Artist getArtist();

	/**
	 * @return the awards
	 */
	ArrayList<? extends Award> getAwards();

	/**
	 * @return the catchline
	 */
	String getCatchline();

	/**
	 * @return the collection
	 */
	Collection getCollection();

	/**
	 * @return the composer
	 */
	Artist getComposer();

	/**
	 * @return the copyright
	 */
	String getCopyright();

	/**
	 * @return the created_at
	 */
	Long getCreated_at();

	/**
	 * @return the description
	 */
	String getDescription();

	/**
	 * @return the displayable
	 */
	Boolean getDisplayable();

	/**
	 * @return the downloadable
	 */
	Boolean getDownloadable();

	/**
	 * @return the duration
	 */
	Long getDuration();

	/**
	 * @return the genre
	 */
	Genre getGenre();

	/**
	 * @return the genres_list
	 */
	ArrayList<String> getGenres_list();

	/**
	 * @return the goodies
	 */
	ArrayList<? extends Goody> getGoodies();

	/**
	 * @return the hires
	 */
	Boolean getHires();

	/**
	 * @return the hires streamable
	 */
	Boolean getHiresStreamable();

	/**
	 * @return the id
	 */
	String getId();

	/**
	 * @return the image
	 */
	Image getImage();

	/**
	 * @return the store_related
	public ArrayList<StoreRelated> getStore_related() {
	return store_related;
	}
	/*
	 *
	 * @return the instrument
	 */
	Instrument getInstrument();

	/**
	 * @return the label
	 */
	Label getLabel();

	/**
	 * @return the maximum_channel_count
	 */
	Long getMaximumChannelCount();

	/**
	 * @return the maximum_bit_depth
	 */
	Long getMaximum_bit_depth();

	/**
	 * @return the maximum_sampling_rate
	 */
	Double getMaximum_sampling_rate();

	/**
	 * @return the maximum_technical_specifications
	 */
	String getMaximum_technical_specifications();

	/**
	 * @return the media_count
	 */
	Long getMedia_count();

	/**
	 * @return the parental warning
	 */
	Boolean getParentalWarning();

	/**
	 * @return the period
	 */
	Period getPeriod();

	/**
	 * @return the popularity
	 */
	Double getPopularity();

	/**
	 * @return the previewable
	 */
	Boolean getPreviewable();

	/**
	 * @return the product_sales_factors_monthly
	 */
	Double getProduct_sales_factors_monthly();

	/**
	 * @return the product_sales_factors_weekly
	 */
	Double getProduct_sales_factors_weekly();

	/**
	 * @return the product_sales_factors_yearly
	 */
	Double getProduct_sales_factors_yearly();

	/**
	 * @return the product_type
	 */
	String getProduct_type();

	/**
	 * @return the product_url
	 */
	String getProduct_url();

	/**
	 * @return the program
	 */
	String getProgram();

	/**
	 * @return the purchasable
	 */
	Boolean getPurchasable();

	/**
	 * @return the purchasable_at
	 */
	Long getPurchasable_at();

	/**
	 * @return the qobuz_id
	 */
	Long getQobuz_id();

	/**
	 * @return the recording_information
	 */
	String getRecording_information();

	/**
	 * @return the relative_url
	 */
	String getRelative_url();

	/**
	 * @return the release_date_download
	 */
	String getReleaseDateDownload();

	/**
	 * @return the release_date_original
	 */
	String getReleaseDateOriginal();
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

	/**
	 * @return the release_date_stream
	 */
	String getReleaseDateStream();

	/**
	 * @return the released_at
	 */
	Long getReleased_at();

	/**
	 * @return the sampleable
	 */
	Boolean getSampleable();

	/**
	 * @return the slug
	 */
	String getSlug();

	/**
	 * @return the streamable
	 */
	Boolean getStreamable();

	/**
	 * @return the streamable_at
	 */
	Long getStreamable_at();

	/**
	 * @return the subtitle
	 */
	String getSubtitle();

	/**
	 * @return the title
	 */
	String getTitle();

	/**
	 * @return the tracks
	 */
	TrackList getTrackList();

	/**
	 * @return the tracks_count
	 */
	Long getTracks_count();

	/**
	 * @return the upc
	 */
	String getUpc();

	/**
	 * @return the url
	 */
	String getUrl();
	
	/**
	 * @return the version
	 */
	String getVersion();

	/**
	 * @return the release type
	 */
	String getReleaseType();
	
	/**
	 * @return the release tags
	 */
	ArrayList<String> getReleaseTags();
	
	
	/**
	 * @return the rawKeyValuePair
	 
	Map<String, String> getRawKeyValuePair();
	*/
}
