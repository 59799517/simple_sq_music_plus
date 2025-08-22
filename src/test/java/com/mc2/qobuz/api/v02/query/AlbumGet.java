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
import com.mc2.qobuz.api.v02.elements.AlbumFromApi;

public class AlbumGet extends QobuzObjectQuery {

    public AlbumGet(String id) throws QobuzAPIException{
        
        String urlStr = URL_GET_TEMPLATE;
        
        urlStr = urlStr.replace("[ENDPOINT]", QobuzObjectQuery.ENDPOINT_ALBUM);
        urlStr = urlStr.replace("[ID]", id);
        //urlStr = urlStr+"&limit="+2;
        
        getAnswer(urlStr);

    }
    /*
		get a new set of tracks starting from offset.
	*/
    public AlbumGet(String id, Long offset) throws QobuzAPIException {
        
        String urlStr = URL_GET_TEMPLATE;
        
        urlStr = urlStr.replace("[ENDPOINT]", QobuzObjectQuery.ENDPOINT_ALBUM);
        urlStr = urlStr.replace("[ID]", id);
        
        //urlStr = urlStr+"&limit="+2;
        urlStr = urlStr+"&offset="+offset;
        
        getAnswer(urlStr);
        
    }

    public AlbumFromApi getAlbum() throws QobuzAPIException{
         
         return new AlbumFromApi(super.getObject());
    }
}