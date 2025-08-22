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

import com.mc2.qobuz.api.v02.API.lists.AlbumList;
import com.mc2.qobuz.api.v02.API.lists.ArtistList;
import com.mc2.qobuz.api.v02.API.lists.TrackList;

/**
 *
 * @author marco
 */
public interface Catalog {

	String ALBUMS = "albums";
	String ARTICLES = "articles";
	String ARTISTS = "artists";
	String FOCUS = "focus";
	String PLAYLISTS = "playlists";
	String QUERY = "query";
	String TRACKS = "tracks";

	/**
	 * @return the albums
	 */
	AlbumList getAlbums();
	/**
	 * @return the playlists
	 */
	//public PlaylistList getPlaylists() {
	//    return playlists;
	//}
	/**
	 * @return the playlists
	 */
	//public ArticleList getArticles() {
	//    return articles;
	//}
	/**
	 * @return the playlists
	 */
	//public FocusList getFoci() {
	//    return foci;
	//}

	/**
	 * @return the artists
	 */
	ArtistList getArtists();

	/**
	 * @return the query
	 */
	String getQuery();

	/**
	 * @return the tracks
	 */
	TrackList getTracks();
	
}
