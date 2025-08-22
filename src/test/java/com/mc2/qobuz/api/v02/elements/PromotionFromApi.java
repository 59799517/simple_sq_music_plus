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

import com.mc2.qobuz.api.v02.API.elements.Promotion;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;

/**
 *
 * @author marco
 */
public final class  PromotionFromApi extends QobuzObjectFromApi implements Promotion{
    
    private Long id;
    private Double original_price;
    
    public PromotionFromApi() {
        super();
    }

    public PromotionFromApi (JSONObject jsonObject)throws QobuzAPIException {
         super(jsonObject);
        
        KeyList.add(ID);
        KeyList.add(ORIGINAL_PRICE);
        
        checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }

        try {
                id = jsonObject.getLong(ID);
                
                original_price = jsonObject.has(ORIGINAL_PRICE) ? 
                        jsonObject.isNull(ORIGINAL_PRICE) ? 
                            null : jsonObject.getDouble(ORIGINAL_PRICE) : null;
                
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
     * @return the original_price
     */
	@Override
    public Double getOriginal_price() {
        return original_price;
    }

}
