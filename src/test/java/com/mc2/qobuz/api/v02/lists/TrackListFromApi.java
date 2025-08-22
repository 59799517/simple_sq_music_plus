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

import com.mc2.qobuz.api.v02.API.lists.TrackList;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mc2.qobuz.api.v02.elements.TrackFromApi;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import static com.mc2.qobuz.api.v02.lists.QobuzObjectListFromApi.ITEMS;

/**
 *
 * @author marco
 */
public final class TrackListFromApi extends QobuzObjectListFromApi implements TrackList{

    private ArrayList<TrackFromApi> items = new ArrayList<>();
    
    public TrackListFromApi() {
        super();
    }

    public TrackListFromApi (JSONObject jsonObject)throws QobuzAPIException {
        super(jsonObject);
        try {
            if (jsonObject.has(ITEMS)) {
                JSONArray jItems = jsonObject.getJSONArray(ITEMS);
                for (int i = 0; i < jItems.length(); i++) {                       
                    items.add(new TrackFromApi(jItems.getJSONObject(i)));
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            throw new QobuzAPIException(ex.getMessage(), ex);
        }
    }

    @Override
    public ArrayList<TrackFromApi> getItems() {
        return items;
    }

}
