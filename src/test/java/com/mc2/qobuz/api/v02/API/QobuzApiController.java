/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mc2.qobuz.api.v02.API;

import com.mc2.qobuz.api.v02.API.elements.Album;
import com.mc2.qobuz.api.v02.API.elements.Artist;
import com.mc2.qobuz.api.v02.API.elements.Catalog;
import com.mc2.qobuz.api.v02.API.elements.FeaturedAlbums;
import com.mc2.qobuz.api.v02.API.elements.SimilarArtists;
import com.mc2.qobuz.api.v02.API.elements.Track;
import com.mc2.qobuz.api.v02.API.elements.UserAuth;
import com.mc2.qobuz.api.v02.API.lists.GenreList;

/**
 *
 * @author marco
 */
public interface QobuzApiController {
	
	public static final String EXTRA_ALBUMS = "extra=albums";
    public static final String EXTRA_TRACKS = "extra=tracks";
	
	//at 2017/10/03
    public static final String[] FEATURED_ALBUM_TYPES = {  
                                            "new-releases", 
                                            "press-awards", 
                                            "editor-picks", 
                                            
                                            "ideal-discography", 
                                            "qobuzissims",
                                            
                                            "most-featured", 
                                            "most-streamed", 
                                            "best-sellers", 

                                            "harmonia-mundi", 
                                            "universal-classic", 
                                            "universal-jazz", 
                                            "universal-jeunesse", 
                                            "universal-chanson", 
                                            
                                            "new-releases-full", 
                                            "recent-releases", 
                                            
                                            
                                            
    };

	void setQobuzAuth(QobuzAuth auth) throws QobuzAPIException;
	
	QobuzAuth getQobuzAuth() throws QobuzAPIException;
	
	UserAuth getUserAuth() throws QobuzAPIException;
	
	Catalog getCatalog(String query, long offset, long limit) throws QobuzAPIException ;
	
	Artist getArtist(Long id) throws QobuzAPIException;
	
	Artist getArtist(Long id, String extra, Long offset, Long limit) throws QobuzAPIException;
		
	Album getAlbum(String albumId) throws QobuzAPIException;
	
	Track getTrack(Long id) throws QobuzAPIException;
	
	FeaturedAlbums getFeaturedAlbums (String  type, Long genre, Long offset, Long limit) throws QobuzAPIException;

	SimilarArtists getSimilarArtists(Long id, Long offset, Long limit) throws QobuzAPIException;
	
	GenreList getGenreList() throws QobuzAPIException;
}
