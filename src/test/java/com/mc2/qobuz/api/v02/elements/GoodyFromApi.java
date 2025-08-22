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

import com.mc2.qobuz.api.v02.API.elements.Goody;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.utils.JsonUtils;

/**
 *
 * @author marco
 */
public final class  GoodyFromApi extends QobuzObjectFromApi implements Goody  {

    
    
    private Long id;
    private URL original_url;
    private String description;
    private String name;
    private Long file_format_id;
    private URL url;
    
     public GoodyFromApi() {
        super();
    }

    public GoodyFromApi (JSONObject jsonObject)throws QobuzAPIException {
        super(jsonObject);
         
        KeyList.add(ID);
        KeyList.add(ORIGINAL_URL);
        KeyList.add(DESCRIPTION);
        KeyList.add(NAME);
        KeyList.add(FILE_FORMAT_ID);
        KeyList.add(URL);
        
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
                
                original_url = JsonUtils.getURL(jsonObject, ORIGINAL_URL);
                
                description = jsonObject.has(DESCRIPTION) ? 
                            jsonObject.isNull(DESCRIPTION) ? 
                            null : jsonObject.getString(DESCRIPTION) : null;
                
                file_format_id = jsonObject.has(FILE_FORMAT_ID) ? 
                        jsonObject.isNull(FILE_FORMAT_ID) ? 
                            null : jsonObject.getLong(FILE_FORMAT_ID) : null;
                
                url = JsonUtils.getURL(jsonObject, URL);

            } catch (JSONException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                throw new QobuzAPIException(ex.getMessage(), ex);
        }
    }
    /**
     * @return the id
     */
	@Override
    public Long getId() {
        return id;
    }

    /**
     * @return the original_url
     */
	@Override
    public URL getOriginal_url() {
        return original_url;
    }

    /**
     * @return the description
     */
	@Override
    public String getDescription() {
        return description;
    }

    /**
     * @return the name
     */
	@Override
    public String getName() {
        return name;
    }

    /**
     * @return the file_format_id
     */
	@Override
    public Long getFile_format_id() {
        return file_format_id;
    }

    /**
     * @return the url
     */
	@Override
    public URL getUrl() {
        return url;
    }
}
