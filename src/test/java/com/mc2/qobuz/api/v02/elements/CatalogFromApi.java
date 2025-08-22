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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.API.elements.Catalog;
import com.mc2.qobuz.api.v02.lists.AlbumListFromApi;
import com.mc2.qobuz.api.v02.lists.ArtistListFromApi;
import com.mc2.qobuz.api.v02.lists.TrackListFromApi;
/**
 *
 * @author marco
 */
public final class CatalogFromApi extends QobuzObjectFromApi implements Catalog{


    private String query;      
    private ArtistListFromApi artists;
    private TrackListFromApi tracks;
    private AlbumListFromApi albums;
    //private PlaylistList playlists;
    //private ArticleList articles;
    //private FocusList foci;
    
    public CatalogFromApi() {
        super();
    }

    public CatalogFromApi (JSONObject jsonObject)throws QobuzAPIException {
        super(jsonObject);
        
        KeyList.add(QUERY);
        KeyList.add(ARTISTS);
        KeyList.add(TRACKS);
        KeyList.add(ALBUMS);
        KeyList.add(PLAYLISTS);
        KeyList.add(ARTICLES);
        KeyList.add(FOCUS);
        
        checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }

        try {
                query = jsonObject.has(QUERY) ? 
                            jsonObject.isNull(QUERY) ? 
                            null : jsonObject.getString(QUERY) : null;
            
                artists = jsonObject.has(ARTISTS) ? 
                        jsonObject.isNull(ARTISTS) ? 
                            null : new ArtistListFromApi(jsonObject.getJSONObject(ARTISTS)) : null;
                
                tracks = jsonObject.has(TRACKS) ? 
                        jsonObject.isNull(TRACKS) ? 
                            null : new TrackListFromApi(jsonObject.getJSONObject(TRACKS)) : null;
                
                albums = jsonObject.has(ALBUMS) ? 
                        jsonObject.isNull(ALBUMS) ? 
                            null : new AlbumListFromApi(jsonObject.getJSONObject(ALBUMS)) : null;
                
                /*
                playlists = jsonObject.has(PLAYLISTS) ? 
                        jsonObject.isNull(PLAYLISTS) ? 
                            null : new PlaylistList(jsonObject.getJSONObject(PLAYLISTS)) : null;
                
                articles = jsonObject.has(ARTICLES) ? 
                        jsonObject.isNull(ARTICLES) ? 
                            null : new ArticleList(jsonObject.getJSONObject(ARTICLES)) : null;
                
                foci = jsonObject.has(FOCUS) ? 
                        jsonObject.isNull(FOCUS) ? 
                            null : new FocusList(jsonObject.getJSONObject(FOCUS)) : null;
                
                */
                
            } catch (JSONException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                throw new QobuzAPIException(ex.getMessage(), ex);
        }
    }
     /**
     * @return the query
     */
	@Override
    public String getQuery() {
        return query;
    }

    /**
     * @return the artists
     */
	@Override
    public ArtistListFromApi getArtists() {
        return artists;
    }

    /**
     * @return the tracks
     */
	@Override
    public TrackListFromApi getTracks() {
        return tracks;
    }

    /**
     * @return the albums
     */
	@Override
    public AlbumListFromApi getAlbums() {
        return albums;
    }
  
    
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

}
