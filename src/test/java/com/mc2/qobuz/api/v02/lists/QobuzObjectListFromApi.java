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

package com.mc2.qobuz.api.v02.lists;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.mc2.qobuz.api.v02.elements.QobuzObjectFromApi;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.API.lists.QobuzObjectList;

/**
 *
 * @author marco
 */
public abstract class QobuzObjectListFromApi extends QobuzObjectFromApi implements QobuzObjectList {
    

    protected Long offset;
    protected Long limit;
    protected Long total;
    
    public QobuzObjectListFromApi() {
        super();
    }

    public QobuzObjectListFromApi(JSONObject jsonObject) throws QobuzAPIException {
        super(jsonObject);
        
        KeyList.add(OFFSET);
        KeyList.add(LIMIT);
        KeyList.add(TOTAL);
        KeyList.add(ITEMS);
        
        checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }

        try {
                total = jsonObject.getLong(TOTAL);
                
                offset = jsonObject.has(OFFSET) ? 
                            jsonObject.isNull(OFFSET) ? 
                            null : jsonObject.getLong(OFFSET) : null;
                limit = jsonObject.has(LIMIT) ? 
                            jsonObject.isNull(LIMIT) ? 
                            null : jsonObject.getLong(LIMIT) : null;
                
            } catch (JSONException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                throw new QobuzAPIException(ex.getMessage(), ex);
        }
        
    }

    /**
     * @return the offset
     */
	@Override
    public Long getOffset() {
        return offset;
    }

    /**
     * @return the limit
     */
	@Override
    public Long getLimit() {
        return limit;
    }

    /**
     * @return the total
     */
	@Override
    public Long getTotal() {
        return total;
    }
    
}
