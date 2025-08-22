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

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.lists.AlbumListFromApi;
import com.mc2.qobuz.api.v02.lists.TrackListFromApi;
import com.mc2.qobuz.api.v02.query.ArtistGet;
import com.mc2.qobuz.api.v02.utils.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import com.mc2.qobuz.api.v02.API.elements.Artist;

/**
 *
 * @author marco
 */
public final class  ArtistFromApi extends QobuzObjectFromApi implements Artist{  


    private Long id;
    private URL picture;
    private Long albums_as_primary_composer_count;
    private Long albums_count;
    private Long albums_as_primary_artist_count;
    private String name;
    private ImageFromApi image;
    private String slug;
    private BiographyFromApi biography;
    private AlbumListFromApi albums;
    private TrackListFromApi tracks;
    private EmptyClass information;
	
	private ArrayList<String> similar_artist_ids;
    
    
    public ArtistFromApi() {
        super();
    }

    public ArtistFromApi (JSONObject jsonObject)throws QobuzAPIException {
        super(jsonObject);
        
        KeyList.add(ID);
        KeyList.add(PICTURE);
        KeyList.add(ALBUM_AS_PRIMARY_COMPOSER_COUNT);
        KeyList.add(ALBUM_COUNT);
        KeyList.add(ALBUM_AS_PRIMARY_ARTIST_COUNT);
        KeyList.add(NAME);
        KeyList.add(IMAGE);
        KeyList.add(SLUG);
        KeyList.add(BIOGRAPHY);
        KeyList.add(ALBUMS);
        KeyList.add(TRACKS);
        KeyList.add(INFORMATION);
		KeyList.add(SIMILAR_ARTIST_IDS);
        
        checkJSONObject(jsonObject);
        
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }
        
        try {
                id = jsonObject.has(ID) ? 
                        jsonObject.isNull(ID) ? 
                            null : jsonObject.getLong(ID) : null;

                name = jsonObject.has(NAME) ? 
                            jsonObject.isNull(NAME) ? 
                            null : jsonObject.getString(NAME) : null;
                
                slug = jsonObject.has(SLUG) ? 
                            jsonObject.isNull(SLUG) ? 
                            null : jsonObject.getString(SLUG) : null;
                
                picture = JsonUtils.getURL(jsonObject, PICTURE);
                
                albums_as_primary_composer_count = jsonObject.has(ALBUM_AS_PRIMARY_COMPOSER_COUNT) ? 
                        jsonObject.isNull(ALBUM_AS_PRIMARY_COMPOSER_COUNT) ? 
                            null : jsonObject.getLong(ALBUM_AS_PRIMARY_COMPOSER_COUNT) : null;
                
                albums_count = jsonObject.has(ALBUM_COUNT) ? 
                        jsonObject.isNull(ALBUM_COUNT) ? 
                            null : jsonObject.getLong(ALBUM_COUNT) : null;
                
                albums_as_primary_artist_count = jsonObject.has(ALBUM_AS_PRIMARY_ARTIST_COUNT) ? 
                        jsonObject.isNull(ALBUM_AS_PRIMARY_ARTIST_COUNT) ? 
                            null : jsonObject.getLong(ALBUM_AS_PRIMARY_ARTIST_COUNT) : null;
                
                image = jsonObject.has(IMAGE) ? 
                        jsonObject.isNull(IMAGE) ? 
                            null : new ImageFromApi(jsonObject.getJSONObject(IMAGE)) : null;
                
                biography = jsonObject.has(BIOGRAPHY) ? 
                        jsonObject.isNull(BIOGRAPHY) ? 
                            null : new BiographyFromApi(jsonObject.getJSONObject(BIOGRAPHY)) : null;
                
                albums = jsonObject.has(ALBUMS) ? 
                        jsonObject.isNull(ALBUMS) ? 
                            null : new AlbumListFromApi(jsonObject.getJSONObject(ALBUMS)) : null;
                
                tracks = jsonObject.has(TRACKS) ? 
                        jsonObject.isNull(TRACKS) ? 
                            null : new TrackListFromApi(jsonObject.getJSONObject(TRACKS)) : null;

                information = jsonObject.has(INFORMATION) ? 
                            jsonObject.isNull(INFORMATION) ? 
                            null : new EmptyClass(jsonObject.getJSONObject(INFORMATION)) : null;
				/*
				if (jsonObject.has(SIMILAR_ARTIST_IDS)) {
                    JSONArray jIds = jsonObject.getJSONArray(SIMILAR_ARTIST_IDS);
                    for (int i = 0; i < jIds.length(); i++) {
                        similar_artist_ids.add(jIds.getString(i));
                    }
                }
				*/
				
				
            } catch (JSONException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                throw new QobuzAPIException(ex.getMessage(), ex);
        }
    }
	@Override
    public boolean isAlbumlistComplete(){
        
        if (albums == null) return true; //no album to get.
        
        // API BUG: sometime reported total is different than the actual number
        // of records, forget it and get all that we have.
        
        //return (albums.getItems().size()>= albums.getTotal());
        return false;
    }
	@Override
    public boolean isTracklistComplete(){
        
        if (tracks == null) return true; //no album to get.
        
        // API BUG: sometime reported total is different than the actual number
        // of records, forget it and get all that we have.
        
        //return (albums.getItems().size()>= albums.getTotal());
        return false;
    }
	@Override
    public void completeAlbumList() throws QobuzAPIException{
        long size = 0;
        if (albums != null){
            
            size = albums.getItems().size();
        }
        boolean stop = (albums == null); //no albums.
        
        while (!stop) {

            loadNextAlbumsPage();
            stop = (albums.getItems().size() == size); 
            size = albums.getItems().size();
            
        } 
    }
	@Override
    public void completeTrackList() throws QobuzAPIException{
        long size = 0;
        if (tracks != null){
            
            size = tracks.getItems().size();
        }
        boolean stop = (tracks == null); //no tracks.
        
        while (!stop) {

            loadNextTracksPage();
            stop = (tracks.getItems().size() == size); 
            size = tracks.getItems().size();
            
        } 
    }
	@Override
    public AlbumListFromApi loadNextAlbumsPage(){
        
        if (!isAlbumlistComplete()){
            
            AlbumListFromApi extra;
            extra = getNexAlbumsPage();
            albums.getItems().addAll(extra.getItems());
            return extra;
        }
        return null;
        
    }
	@Override
    public TrackListFromApi loadNextTracksPage(){
    
        if (!isTracklistComplete()){
            
            TrackListFromApi extra;
            extra = getNextTracksPage();
            tracks.getItems().addAll(extra.getItems());
            return extra;
        }
        return null;
        
        
    }
    private AlbumListFromApi getNexAlbumsPage() {
    
        if (!isAlbumlistComplete()) {
            
            ArtistGet q;
            try {
                q = new ArtistGet(id, ArtistGet.EXTRA_ALBUMS, (long)albums.getItems().size());
                ArtistFromApi extra = q.getArtist();
                return extra.getAlbums();
            } catch (QobuzAPIException ex) {
                Logger.getLogger(ArtistFromApi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    private TrackListFromApi getNextTracksPage() {
    
        if (!isTracklistComplete()) {
            
            ArtistGet q;
            try {
                q = new ArtistGet(id, ArtistGet.EXTRA_TRACKS, (long)tracks.getItems().size());
                ArtistFromApi extra = q.getArtist();
                return extra.getTracks();
            } catch (QobuzAPIException ex) {
                Logger.getLogger(ArtistFromApi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    /**
     * @return the id
     */
	@Override
    public Long getId() {
        return id;
    }
    /**
     * @return the picture
     */
	@Override
    public URL getPicture() {
        return picture;
    }

    /**
     * @return the albums_as_primary_composer_count
     */
	@Override
    public Long getAlbums_as_primary_composer_count() {
        return albums_as_primary_composer_count;
    }

    /**
     * @return the albums_count
     */
	@Override
    public Long getAlbums_count() {
        return albums_count;
    }

    /**
     * @return the albums_as_primary_artist_count
     */
	@Override
    public Long getAlbums_as_primary_artist_count() {
        return albums_as_primary_artist_count;
    }

    /**
     * @return the name
     */
	@Override
    public String getName() {
        return name;
    }

    /**
     * @return the image
     */
	@Override
    public ImageFromApi getImage() {
        return image;
    }

    /**
     * @return the slug
     */
	@Override
    public String getSlug() {
        return slug;
    }

    /**
     * @return the biography
     */
	@Override
    public BiographyFromApi getBiography() {
        return biography;
    }

    /**
     * @return the albums
     */
	@Override
    public AlbumListFromApi getAlbums() {
        return albums;
    }
     /**
     * @return the tracks
     */
    
	@Override
    public TrackListFromApi getTracks() {
        return tracks;
    }
     /**
     * @return the information
     */
	//@Override
    public EmptyClass getInformation() {
        return information;
    }
	/**
     * @return the similar_artist_ids (not used)
     */
	@Override
    public List<String> getSimilarArtistIds() {
        return similar_artist_ids;
    }
}
