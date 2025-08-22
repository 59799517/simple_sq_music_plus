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

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author marco
 */
public interface Track {

	String ALBUM = "album";
	String ARTICLES = "articles"; //no
	String ARTICLE_IDS = "article_ids"; //no
	String COMPOSER = "composer";
	String COPYRIGHT = "copyright";
	String DISPLAYABLE = "displayable"; //no
	String DOWNLOADABLE = "downloadable"; //no
	String DURATION = "duration"; //no
	String HIRES = "hires"; //no
	String HIRES_STREAMABLE = "hires_streamable"; //no
	String ID = "id"; //no
	String ISRC = "isrc";
	String MAXIMUM_BIT_DEPTH = "maximum_bit_depth"; //no
	String MAXIMUM_CHANNEL_COUNT = "maximum_channel_count"; //no
	String MAXIMUM_SAMPLING_RATE = "maximum_sampling_rate"; //no
	String MEDIA_NUMBER = "media_number"; //no
	/* 9/9/19 */
	String PARENTAL_WARNING = "parental_warning"; //no
	String PERFORMER = "performer";
	String PERFORMERS = "performers"; //no
	String PREVIEWABLE = "previewable"; //no
	String PURCHASABLE = "purchasable"; //no
	String PURCHASABLE_AT = "purchasable_at"; //no
	String SAMPLEABLE = "sampleable"; //no
	String STREAMABLE = "streamable"; //no
	String STREAMABLE_AT = "streamable_at"; //no
	String TITLE = "title";
	String TITLEONLY = "titleOnly"; //no
	String TRACK_NUMBER = "track_number"; //no TRACK_NO
	String VERSION = "version"; //no
	String WORK = "work";
	String WORKGUESSED = "workGuessed"; //no

	/**
	 * @return the work stored in work field or guessed from the title.
	 */
	String geWorkGuessed();

	/**
	 * @return the album
	 */
	Album getAlbum();

	/**
	 * @return the articles
	 */
	ArrayList<? extends Article> getArticles();

	/**
	 * @return the composer
	 */
	Artist getComposer();

	/**
	 * @return the copyright
	 */
	String getCopyright();

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
	Long getId();

	/**
	 * @return the isrc
	 */
	String getIsrc();

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
	 * @return the media_number
	 */
	Long getMedia_number();

	/**
	 * @return the parental warning
	 */
	Boolean getParentalWarning();

	/**
	 * @return the performer
	 */
	Artist getPerformer();

	/**
	 * @return the performers as a list of strings (name: roles)
	 */
	ArrayList<String> getPerformerList();

	/**
	 * @return the performers as a string
	 */
	String getPerformers();

	/**
	 * @return the performers as a map of string where role is the key
	 * and the performers name the value.
	 */
	Map<String, String> getPerformersRoleNamesMap();

	/**
	 * @return the previewable
	 */
	Boolean getPreviewable();

	/**
	 * @return the purchasable
	 */
	Boolean getPurchasable();

	/**
	 * @return the purchasable_at
	 */
	Long getPurchasable_at();

	/**
	 * @return the sampleable
	 */
	Boolean getSampleable();

	/**
	 * @return the streamable
	 */
	Boolean getStreamable();

	/**
	 * @return the streamable_at
	 */
	Long getStreamable_at();

	/**
	 * @return the title
	 */
	String getTitle();

	/**
	 * @return the title cleaned by the work part as per in workGuessed.
	 */
	String getTitleOnly();

	/**
	 * @return the track_number
	 */
	Long getTrack_number();

	/**
	 * @return the version
	 */
	String getVersion();

	/**
	 * @return the work
	 */
	String getWorkTitle();
	
}
