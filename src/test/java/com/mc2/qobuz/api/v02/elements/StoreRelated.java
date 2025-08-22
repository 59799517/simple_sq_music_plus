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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;

/**
 *
 * @author marco
 */
public final class  StoreRelated extends QobuzObjectFromApi{

    public static final String GENRE = "genre";
    public static final String GENRES_LIST = "genres_list";
    public static final String GENRES_SLUGS_LIST = "genres_slugs_list";
    
    
    private String store;
    private GenreFromApi genre;
    private ArrayList<String> genres_list = new ArrayList<>();
    
    public StoreRelated() {
        super();
    }

    public StoreRelated (String store, JSONObject jsonObject)throws QobuzAPIException {
        super(jsonObject);
        this.store = store;
        KeyList.add(GENRE);
        KeyList.add(GENRES_LIST);
        KeyList.add(GENRES_SLUGS_LIST); //Ignore at moment
        
        checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }

        try {
                genre = jsonObject.has(GENRE) ? 
                        jsonObject.isNull(GENRE) ? 
                            null : new GenreFromApi(jsonObject.getJSONObject(GENRE)) : null;
                
                if (jsonObject.has(GENRES_LIST)) {
                    JSONArray jGenres = jsonObject.getJSONArray(GENRES_LIST);
                    for (int i = 0; i < jGenres.length(); i++) {
                        genres_list.add(jGenres.getString(i));
                    }
                }
            } catch (JSONException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                throw new QobuzAPIException(ex.getMessage(), ex);
        }
    }
    /**
     * @return the store
     */
    public String getStore() {
        return store;
    }

    /**
     * @return the genre
     */
    public GenreFromApi getGenre() {
        return genre;
    }

    /**
     * @return the genres_list
     */
    public ArrayList<String> getGenres_list() {
        return genres_list;
    }

}
