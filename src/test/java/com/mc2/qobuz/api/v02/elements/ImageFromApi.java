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

import com.mc2.qobuz.api.v02.API.elements.Image;
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
public final class  ImageFromApi extends QobuzObjectFromApi implements Image {

    private URL thumbnail;
    private URL small;
    private URL medium;
    private URL large;
    private URL extralarge;
    private URL mega;
    private URL back;
    
    public ImageFromApi() {
        super();
    }

    public ImageFromApi (JSONObject jsonObject)throws QobuzAPIException {
         super(jsonObject);
         
        KeyList.add(THUMBNAIL);
        KeyList.add(SMALL);
        KeyList.add(MEDIUM);
        KeyList.add(LARGE);
        KeyList.add(EXTRALARGE);
        KeyList.add(MEGA);
        KeyList.add(BACK);
        
        checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }
        
        try {
            
            thumbnail = JsonUtils.getURL(jsonObject, THUMBNAIL);
            small = JsonUtils.getURL(jsonObject, SMALL);
            medium = JsonUtils.getURL(jsonObject, MEDIUM);
            large = JsonUtils.getURL(jsonObject, LARGE);
            extralarge = JsonUtils.getURL(jsonObject, EXTRALARGE);
            mega = JsonUtils.getURL(jsonObject, MEGA);
            back = JsonUtils.getURL(jsonObject, BACK);
            
            } catch ( Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                throw new QobuzAPIException(ex.getMessage(), ex);
        }
    }
   
    /**
     * @return the thumbnail (50px)
     */
	@Override
    public URL getThumbnail() {
        return thumbnail;
    }
    /**
     * @return the small (230px)
     */
	@Override
    public URL getSmall() {
        return small;
    }

    /**
     * @return the medium 
     */
	@Override
    public URL getMedium() {
        return medium;
    }

    /**
     * @return the large (600px)
     */
	@Override
    public URL getLarge() {
        return large;
    }

    /**
     * @return the extralarge
     */
	@Override
    public URL getExtralarge() {
        return extralarge;
    }

    /**
     * @return the mega
     */
	@Override
    public URL getMega() {
        return mega;
    }

    /**
     * @return the back
     */
	@Override
    public URL getBack() {
        return back;
    }
}
