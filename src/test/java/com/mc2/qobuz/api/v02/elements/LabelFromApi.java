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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.lists.AlbumListFromApi;
import com.mc2.qobuz.api.v02.query.LabelGet;
import com.mc2.qobuz.api.v02.utils.JsonUtils;
import com.mc2.qobuz.api.v02.API.elements.Label;

/**
 *
 * @author marco
 */
public final class  LabelFromApi extends QobuzObjectFromApi implements Label{  

    
    private Long id;
    private Long supplier_id;
    private Long albums_count;
    private String name;
    private String slug;
    private String description;
    private URL image;
    private AlbumListFromApi albums;
    
    public LabelFromApi() {
        super();
    }

    public LabelFromApi (JSONObject jsonObject)throws QobuzAPIException {
        super(jsonObject);
        
        KeyList.add(ID);
        KeyList.add(SUPPLIER_ID);
        KeyList.add(ALBUM_COUNT);
        KeyList.add(NAME);
        KeyList.add(SLUG);
        KeyList.add(DESCRIPTION);
        KeyList.add(IMAGE);
        KeyList.add(ALBUMS);
        
        checkJSONObject(jsonObject);
        
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }

        try {
                id = jsonObject.getLong(ID);
                name = jsonObject.getString(NAME);
                slug = jsonObject.optString(SLUG);
                
                supplier_id = jsonObject.has(SUPPLIER_ID) ? 
                        jsonObject.isNull(SUPPLIER_ID) ? 
                            null : jsonObject.getLong(SUPPLIER_ID) : null; 
                
                albums_count = jsonObject.has(ALBUM_COUNT) ? 
                        jsonObject.isNull(ALBUM_COUNT) ? 
                            null : jsonObject.getLong(ALBUM_COUNT) : null;
                
                description = jsonObject.has(DESCRIPTION) ? 
                            jsonObject.isNull(DESCRIPTION) ? 
                            null : jsonObject.getString(DESCRIPTION) : null;
                
                image = JsonUtils.getURL(jsonObject, IMAGE);
                                
                albums = jsonObject.has(ALBUMS) ? 
                        jsonObject.isNull(ALBUMS) ? 
                            null : new AlbumListFromApi(jsonObject.getJSONObject(ALBUMS)) : null;

            } catch (JSONException ex) {
                System.out.println(jsonObject.toString());
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
    public AlbumListFromApi loadNextAlbumsPage() throws QobuzAPIException{
    
        if (!isAlbumlistComplete()){
            
            AlbumListFromApi extra = getNexAlbumsPage();
            
            albums.getItems().addAll(extra.getItems());
            return extra;
        }
        return null;
    }
    private AlbumListFromApi getNexAlbumsPage() throws QobuzAPIException{
    
        if (!isAlbumlistComplete()) {
            
            LabelGet q = new LabelGet(id, (long)albums.getItems().size());
            LabelFromApi extra = q.getLabel();
            
            return extra.getAlbums();
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
     * @return the supplier_id
     */
	@Override
    public Long getSupplier_id() {
        return supplier_id;
    }
    /**
     * @return the albums_count
     */
	@Override
    public Long getAlbums_count() {
        return albums_count;
    }
    /**
     * @return the name
     */
	@Override
    public String getName() {
        return name;
    }
    /**
     * @return the slug
     */
	@Override
    public String getSlug() {
        return slug;
    }
        /**
     * @return the description
     */
	@Override
    public String getDescription() {
        return description;
    }

    /**
     * @return the image
     */
	@Override
    public URL getImage() {
        return image;
    }

    /**
     * @return the albums
     */
	@Override
    public AlbumListFromApi getAlbums() {
        return albums;
    }
}
