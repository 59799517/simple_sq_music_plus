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

public class CatalogCount extends QobuzObjectQuery {


    public CatalogCount(String query) throws QobuzAPIException{
        
        try {
            // use an empty string to count all items.
            
            String urlStr =  QobuzClient.BASEURL+"/"+
                    QobuzObjectQuery.ENDPOINT_CATALOG+
                    "/count?query="+URLEncoder.encode(query, "UTF-8");;
                    
                    getAnswer(urlStr);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CatalogCount.class.getName()).log(Level.SEVERE, null, ex);
            throw new QobuzAPIException(ex);
        }

    }

    public CatalogFromApi getCatalog() throws QobuzAPIException{
         
         return new CatalogFromApi(super.getObject());
    }
}