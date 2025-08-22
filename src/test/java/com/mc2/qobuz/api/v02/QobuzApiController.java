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
package com.mc2.qobuz.api.v02;

import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.API.elements.Album;
import com.mc2.qobuz.api.v02.API.elements.Artist;
import com.mc2.qobuz.api.v02.API.elements.Catalog;
import com.mc2.qobuz.api.v02.API.elements.FeaturedAlbums;
import com.mc2.qobuz.api.v02.API.elements.SimilarArtists;
import com.mc2.qobuz.api.v02.API.elements.Track;
import com.mc2.qobuz.api.v02.API.elements.UserAuth;
import com.mc2.qobuz.api.v02.API.lists.GenreList;
import com.mc2.qobuz.api.v02.elements.GenreListResult;
import com.mc2.qobuz.api.v02.query.AlbumGet;
import com.mc2.qobuz.api.v02.query.AlbumsGetFeatured;
import com.mc2.qobuz.api.v02.query.ArtistGet;
import com.mc2.qobuz.api.v02.query.ArtistGetSimilar;
import com.mc2.qobuz.api.v02.query.CatalogSearch;
import com.mc2.qobuz.api.v02.query.GenreListGet;
import com.mc2.qobuz.api.v02.query.TrackGet;
import com.mc2.qobuz.api.v02.query.UserLogin;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class QobuzApiController implements com.mc2.qobuz.api.v02.API.QobuzApiController {
	
private static Logger logger = Logger.getLogger(QobuzApiController.class.getName());
	
	private com.mc2.qobuz.api.v02.API.QobuzAuth qobuzAuth;
	private UserAuth userAuth;
	
	protected static  com.mc2.qobuz.api.v02.API.QobuzApiController INSTANCE;
	
	public static com.mc2.qobuz.api.v02.API.QobuzApiController findIstance() {
        if (INSTANCE == null) INSTANCE = new QobuzApiController();
		return INSTANCE;
    }
	
    protected QobuzApiController() {}

	@Override
	public com.mc2.qobuz.api.v02.API.QobuzAuth getQobuzAuth() throws QobuzAPIException {
		return qobuzAuth;
	}

	@Override
	public void setQobuzAuth(com.mc2.qobuz.api.v02.API.QobuzAuth qobuzAuth) throws QobuzAPIException {
		this.qobuzAuth = qobuzAuth;
		
		UserLogin login = new UserLogin(qobuzAuth);
		
		userAuth  = login.getUserAuth();
		
	}
	
	@Override
	public UserAuth getUserAuth() throws QobuzAPIException {
		return userAuth;
	}

	
	@Override
	public Catalog getCatalog(String query, long offset, long limit) throws QobuzAPIException {
		
		CatalogSearch q = new CatalogSearch(query, offset, limit);
		return q.getCatalog();
	
	}
	
	@Override
	public Artist getArtist(Long id) throws QobuzAPIException {
		ArtistGet q = new ArtistGet(id);
		Artist artist= q.getArtist();
		return artist;
	}
	
	@Override
	public Artist getArtist(Long id, String extra, Long offset,  Long limit) throws QobuzAPIException {
		ArtistGet q = new ArtistGet(id, extra, offset, limit);
		Artist artist= q.getArtist();
		return artist;
	}
	
	@Override
	public Album getAlbum(String albumId) throws QobuzAPIException {
		AlbumGet q = new AlbumGet(albumId);
		Album album = q.getAlbum();
		return album;
	}
	
	@Override
	public Track getTrack(Long id) throws QobuzAPIException{
		
		TrackGet q = new TrackGet(id);
		Track track = q.getTrack();
		return track;
	}
	
	
	@Override
	public FeaturedAlbums getFeaturedAlbums(String type, Long genre, Long offset, Long limit) throws QobuzAPIException {
		AlbumsGetFeatured q = new AlbumsGetFeatured(type , genre, offset, limit);
		FeaturedAlbums featuredAlbums = q.getFeaturedAlbumResult();
		return featuredAlbums;
	}
	
	@Override
	public SimilarArtists getSimilarArtists(Long id, Long offset, Long limit) throws QobuzAPIException{
	
		ArtistGetSimilar q = new ArtistGetSimilar(id , offset, limit);
		SimilarArtists similarArtists = q.getSimilaArtist();
		return similarArtists;
	}
	
	@Override
	public GenreList getGenreList() throws QobuzAPIException {
		
		GenreListGet q = new GenreListGet();
            
        GenreListResult result= q.getGenreListResult();  
		if (result == null) {return null;}
		
		result.completeGenresList();
		return result.getGenres();
   
	}

}
