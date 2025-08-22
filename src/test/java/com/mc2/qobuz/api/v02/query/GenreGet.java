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
import com.mc2.qobuz.api.v02.elements.GenreFromApi;

public class GenreGet extends QobuzObjectQuery {
    
    public GenreGet(String urlStr) throws QobuzAPIException{
        getAnswer(urlStr);
    }

    public GenreGet(Long id) throws QobuzAPIException{
        
        getAnswer(buildUrlStr(id));
    }

    public GenreGet(Long id, Long offset) throws QobuzAPIException {
        
        getAnswer(buildUrlStr(id, offset));
        
    }
    public GenreGet(Long id, Long offset, Long limit) throws QobuzAPIException {

        getAnswer(buildUrlStr(id, offset, limit));
        
    }
        
    private String buildUrlStr(Long id){
        
        String urlStr = URL_GET_TEMPLATE;
        
        urlStr = urlStr.replace("[ENDPOINT]", QobuzObjectQuery.ENDPOINT_GENRE);
        urlStr = urlStr.replace("[ID]", id.toString());
        
        return urlStr;
    }
    private String buildUrlStr(Long id, Long offset){
        
        String urlStr = buildUrlStr(id);
        
        urlStr = urlStr+"&extra=albums";
        
        urlStr = urlStr+"&offset="+offset;

        return urlStr;
    }
    
    private String buildUrlStr(Long id, Long offset, Long limit){
        
        String urlStr = buildUrlStr(id, offset);
        
        urlStr = urlStr+"&limit="+limit;
        return urlStr;
    }
    
    public GenreFromApi getGenre() throws QobuzAPIException{
         
         return new GenreFromApi(super.getObject());
    }
}