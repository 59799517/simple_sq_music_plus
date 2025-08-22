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
import com.mc2.qobuz.api.v02.API.lists.AlbumList;
import com.mc2.qobuz.api.v02.API.lists.TrackList;

import java.net.URL;
import java.util.List;

/**
 *
 * @author marco
 */
public interface Artist {

	String ALBUMS = "albums";
	String ALBUM_AS_PRIMARY_ARTIST_COUNT = "albums_as_primary_artist_count";
	String ALBUM_AS_PRIMARY_COMPOSER_COUNT = "albums_as_primary_composer_count";
	String ALBUM_COUNT = "albums_count";
	String BIOGRAPHY = "biography";
	String ID = "id";
	String IMAGE = "image";
	String INFORMATION = "information";
	String NAME = "name";
	String PICTURE = "picture";
	/* 9/9/19 */
	String SIMILAR_ARTIST_IDS = "similar_artist_ids";
	String SLUG = "slug";
	String TRACKS = "tracks";

	void completeAlbumList() throws QobuzAPIException;

	void completeTrackList() throws QobuzAPIException;

	/**
	 * @return the albums
	 */
	AlbumList getAlbums();

	/**
	 * @return the albums_as_primary_artist_count
	 */
	Long getAlbums_as_primary_artist_count();

	/**
	 * @return the albums_as_primary_composer_count
	 */
	Long getAlbums_as_primary_composer_count();

	/**
	 * @return the albums_count
	 */
	Long getAlbums_count();

	/**
	 * @return the biography
	 */
	Biography getBiography();

	/**
	 * @return the id
	 */
	Long getId();

	/**
	 * @return the image
	 */
	Image getImage();

	/**
	 * @return the information
	 */
	//EmptyClass getInformation();

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @return the picture
	 */
	URL getPicture();

	/**
	 * @return the similar_artist_ids (not used)
	 */
	List<String> getSimilarArtistIds();

	/**
	 * @return the slug
	 */
	String getSlug();

	/**
	 * @return the tracks
	 */
	TrackList getTracks();

	boolean isAlbumlistComplete();

	boolean isTracklistComplete();

	AlbumList loadNextAlbumsPage();

	TrackList loadNextTracksPage();
	
}
