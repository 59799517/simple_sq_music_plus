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

package com.mc2.qobuz.api.v02.query;

import com.mc2.qobuz.api.v02.QobuzClient;
import com.mc2.qobuz.api.v02.elements.FeaturedAlbumsFromApi;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.API.QobuzApiController;
import com.mc2.qobuz.api.v02.API.elements.FeaturedAlbums;

public class AlbumsGetFeatured extends QobuzObjectQuery {

    public static final String[] TYPES = QobuzApiController.FEATURED_ALBUM_TYPES;

    public AlbumsGetFeatured(String  type) throws QobuzAPIException{
        
        getAnswer(buildUrlStr(type));
    }

    public AlbumsGetFeatured(String  type, Long genre) throws QobuzAPIException {
        
        getAnswer(buildUrlStr(type, genre));
        
    }
    public AlbumsGetFeatured(String  type, Long genre, Long offset) throws QobuzAPIException {
        
        getAnswer(buildUrlStr(type, genre, offset));
        
    }
    public AlbumsGetFeatured(String  type, Long genre, Long offset, Long limit) throws QobuzAPIException {
        
        getAnswer(buildUrlStr(type, genre, offset, limit));
        
    }
   
    private String buildUrlStr(String  type){
        
        String urlStr = QobuzClient.BASEURL + "/"+QobuzObjectQuery.ENDPOINT_ALBUM+
                        "/getFeatured?type="+type;
        
        return urlStr;
    }
    
    private String buildUrlStr(String  type, Long genre){
        
        String urlStr = buildUrlStr(type);
		
        if (genre >0L){ //15/02/2020 - different answert in respect of the Qobz APP and Plugin.
			urlStr = urlStr+"&genre_id="+genre;
		}
        return urlStr;
    }
     private String buildUrlStr(String  type, Long genre, Long offset){
        
        String urlStr = buildUrlStr(type, genre);
        
        urlStr = urlStr+"&offset="+offset;
        
        return urlStr;
    }
    private String buildUrlStr(String  type, Long genre, Long offset, Long limit){
        
        String urlStr = buildUrlStr(type, genre, offset);
        
        urlStr = urlStr+"&limit="+limit;
        
        return urlStr;
    }
    
    public FeaturedAlbums getFeaturedAlbumResult() throws QobuzAPIException{
         
         return new FeaturedAlbumsFromApi(super.getObject());
    }
    
    public static String[] suggestedTypes(){
         
         return TYPES;
    }
}