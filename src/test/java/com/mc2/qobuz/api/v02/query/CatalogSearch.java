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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mc2.qobuz.api.v02.QobuzClient;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.elements.CatalogFromApi;

public class CatalogSearch extends QobuzObjectQuery {
    //at 2017/10/03
    public static final String TYPE_ARTISTS = "artists";
    public static final String TYPE_ALBUMS = "albums";
    public static final String TYPE_TRACKS = "tracks";
    
    public static final String[] TYPES = {  TYPE_ARTISTS,
                                            TYPE_ALBUMS, 
                                            TYPE_TRACKS, 
                                            //"playlists", 
                                            //"articles", 
                                            //"playlists", 
                                            //"focus",
    };

    public CatalogSearch(String query) throws QobuzAPIException{
        
        String urlStr = buildUrlStr(query);

        getAnswer(urlStr);
    }    
    
    public CatalogSearch(String query, String type) throws QobuzAPIException{
        
        String urlStr = buildUrlStr(query)+"&type="+type;

        getAnswer(urlStr);

    }  
    public CatalogSearch(String query, Long offset) throws QobuzAPIException{
        
        String urlStr = buildUrlStr(query,offset);

        getAnswer(urlStr);

    }
    
    public CatalogSearch(String query, String type, Long offset) throws QobuzAPIException{
        
        String urlStr = buildUrlStr(query,offset)+"&type="+type;

        getAnswer(urlStr);

    }
    
    public CatalogSearch(String query,Long offset, Long limit) throws QobuzAPIException{
        
        String urlStr = buildUrlStr(query,offset,limit);

        getAnswer(urlStr);

    }
    public CatalogSearch(String query, String type, Long offset, Long limit) throws QobuzAPIException{
        
        String urlStr = buildUrlStr(query,offset,limit)+"&type="+type;;

        getAnswer(urlStr);

    }

    private String buildUrlStr(String  query) throws QobuzAPIException{
         
         
        try {
            String urlStr =  QobuzClient.BASEURL+"/"+
                    QobuzObjectQuery.ENDPOINT_CATALOG+
                    "/search?query="+URLEncoder.encode(query, "UTF-8");
            
            return urlStr;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CatalogSearch.class.getName()).log(Level.SEVERE, null, ex);
            throw new QobuzAPIException(ex);
        }
    }
    
    private String buildUrlStr(String  query, Long offset) throws QobuzAPIException{
        
        String urlStr = buildUrlStr(query);
        
        urlStr = urlStr+"&offset="+offset;
        
        return urlStr;
    }
    private String buildUrlStr(String  query,Long offset, Long limit) throws QobuzAPIException{
        
        String urlStr = buildUrlStr(query,offset);
        
        urlStr = urlStr+"&limit="+limit;
        
        return urlStr;
    }
    
    public CatalogFromApi getCatalog() throws QobuzAPIException{
         
         return new CatalogFromApi(super.getObject());
    }
    public static String[] suggestedTypes(){
         
         return TYPES;
    }
}