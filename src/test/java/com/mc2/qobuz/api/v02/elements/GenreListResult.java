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
import com.mc2.qobuz.api.v02.lists.GenreListFromApi;
import com.mc2.qobuz.api.v02.query.GenreListGet;

/**
 *
 * @author marco
 */
public final class GenreListResult extends QobuzObjectFromApi{

    public static final String GENRES = "genres";
    public static final String PARENT = "parent";
    
    
    private GenreListFromApi genres;
    private GenreFromApi parent;
    
    public GenreListResult() {
        super();
    }

    public GenreListResult (JSONObject jsonObject)throws QobuzAPIException {
        super(jsonObject);
        
        KeyList.add(GENRES);
        KeyList.add(PARENT);
        
        checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }

        try {
                genres = jsonObject.has(GENRES) ? 
                        jsonObject.isNull(GENRES) ? 
                            null : new GenreListFromApi(jsonObject.getJSONObject(GENRES)) : null;
                
                parent = jsonObject.has(PARENT) ? 
                        jsonObject.isNull(PARENT) ? 
                            null : new GenreFromApi(jsonObject.getJSONObject(PARENT)) : null;
                
            } catch (JSONException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                throw new QobuzAPIException(ex.getMessage(), ex);
        }
    }
    public boolean isGenreListComplete(){
        
        if (genres == null) return true; //no genres to get.
        
        // API BUG: sometime reported total is different than the actual number
        // of records, forget it and get all that we have.
        
        //return (genres.getItems().size()>= genres.getTotal());
        return false;
    }
    public void completeGenresList() throws QobuzAPIException{
        
        long size = 0;
        if (genres != null){
            
            size = genres.getItems().size();
        }
        boolean stop = (genres == null); //no albums.
        
        while (!stop) {
            
            loadNextGenresPage();
            stop = (genres.getItems().size() == size); 
            size = genres.getItems().size();
        } 
    }
    public GenreListFromApi loadNextGenresPage() throws QobuzAPIException{
    
        if (!isGenreListComplete()){
            
            GenreListFromApi extra = getNexGenresPage();
            
            genres.getItems().addAll(extra.getItems());
            return extra;
        }
        return null;
    }
    private GenreListFromApi getNexGenresPage() throws QobuzAPIException{
    
        if (!isGenreListComplete()) {
            
            long id=0;
            if (parent != null){id = parent.getId();}
            
            GenreListGet q = new GenreListGet(id, (long)genres.getItems().size());
            GenreListResult extra = q.getGenreListResult();
            
            return extra.getGenres();
        }
        return null;
    }
    /**
     * @return the genres
     */
    public GenreListFromApi getGenres() {
        return genres;
    }
    /**
     * @return the parent
     */
    public GenreFromApi getParent() {
        return parent;
    }

}
