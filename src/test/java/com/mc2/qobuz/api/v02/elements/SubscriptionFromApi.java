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

import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.API.elements.Subscription;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author marco
 */
public final class SubscriptionFromApi extends QobuzObjectFromApi implements Subscription {

	private String offer;
	private String periodicity;
	private String endDate;
	private Boolean isCancelled;

	
	public SubscriptionFromApi() {
        super();
    }
	
	public SubscriptionFromApi(JSONObject jsonObject) throws QobuzAPIException {
        super(jsonObject);
        
        // fill the KeyList here;
        
        KeyList.add(OFFER);
		KeyList.add(PERIODICITY);
		KeyList.add(END_DATE);
		KeyList.add(IS_CANCELED);
		
		checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }
		
		try {
                offer = this.getString(OFFER, jsonObject);
				periodicity = this.getString(PERIODICITY, jsonObject);
				endDate = this.getString(END_DATE, jsonObject);
				isCancelled = this.getBoolean(IS_CANCELED, jsonObject);
				
			} catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, jsonObject.toString(), ex);
            throw new QobuzAPIException(ex.getMessage(), ex);
        }
   
    }

	@Override
	public String getOffer() {
		return offer;
	}
	
	@Override
	public String getPeriodicity() {
		return periodicity;
	}

	@Override
	public String getEndDate() {
		return endDate;
	}

	@Override
	public Boolean getIsCanceled() {
		return isCancelled;
	}
}
