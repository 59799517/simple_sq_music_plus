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

import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.API.QobuzApiController;
import com.mc2.qobuz.api.v02.elements.ArtistFromApi;

public class ArtistGet extends QobuzObjectQuery {
    
    public static final String EXTRA_ALBUMS = QobuzApiController.EXTRA_ALBUMS;
    public static final String EXTRA_TRACKS = QobuzApiController.EXTRA_TRACKS;
    
    public ArtistGet(String urlStr) throws QobuzAPIException{
        getAnswer(urlStr);
    }

    public ArtistGet(Long id) throws QobuzAPIException{
        
        getAnswer(buildUrlStr(id));
    }

    public ArtistGet(Long id, String extra, Long offset) throws QobuzAPIException {
        
        getAnswer(buildUrlStr(id, extra, offset));
        
    }
    public ArtistGet(Long id, String extra, Long offset, Long limit) throws QobuzAPIException {

        getAnswer(buildUrlStr(id, extra, offset, limit));
        
    }
        
    private String buildUrlStr(Long id){
        
        String urlStr = URL_GET_TEMPLATE;
        
        urlStr = urlStr.replace("[ENDPOINT]", QobuzObjectQuery.ENDPOINT_ARTIST);
        urlStr = urlStr.replace("[ID]", id.toString());
        
        return urlStr;
    }
    private String buildUrlStr(Long id, String extra, Long offset){
        
        String urlStr = buildUrlStr(id);
        
        if (extra.equals(EXTRA_ALBUMS) || extra.equals(EXTRA_TRACKS)){
            
            urlStr = urlStr+"&"+extra;
            
        } 
        
        //Dont'ask for any extra if invalid.
        
        urlStr = urlStr+"&offset="+offset;

        return urlStr;
    }
    
    private String buildUrlStr(Long id, String extra, Long offset, Long limit){
        
        String urlStr = buildUrlStr(id, extra, offset);
        
        urlStr = urlStr+"&limit="+limit;
        return urlStr;
    }
    
    public ArtistFromApi getArtist() throws QobuzAPIException{
         
         return new ArtistFromApi(super.getObject());
    }
}