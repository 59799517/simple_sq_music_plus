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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import com.mc2.qobuz.api.v02.lists.LabelListFromApi;
import com.mc2.qobuz.api.v02.query.LabelListGet;

/**
 *
 * @author marco
 */
public final class LabelListResult extends QobuzObjectFromApi{

    public static final String LABELS = "labels";
    
    
    private LabelListFromApi labels;
    
    public LabelListResult() {
        super();
    }

    public LabelListResult (JSONObject jsonObject)throws QobuzAPIException {
        super(jsonObject);
        
        KeyList.add(LABELS);
        
        checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }

        try {
                labels = jsonObject.has(LABELS) ? 
                        jsonObject.isNull(LABELS) ? 
                            null : new LabelListFromApi(jsonObject.getJSONObject(LABELS)) : null;
                
                
            } catch (JSONException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                throw new QobuzAPIException(ex.getMessage(), ex);
        }
    }
    public boolean isLabelListComplete(){
        
        if (labels == null) return true; //no labels to get.
        
        // API BUG: sometime reported total is different than the actual number
        // of records, forget it and get all that we have.
        
        //return (labels.getItems().size()>= labels.getTotal());
        return false;
    }
    public void completeLabelsList() throws QobuzAPIException{
        
        long size = 0;
        if (labels != null){
            
            size = labels.getItems().size();
        }
        boolean stop = (labels == null);
        
        while (!stop) {
            
            loadNextLabelsPage();
            stop = (labels.getItems().size() == size); 
            size = labels.getItems().size();
        } 
    }
    public LabelListFromApi loadNextLabelsPage() throws QobuzAPIException{
    
        if (!isLabelListComplete()){
            
            LabelListFromApi extra = getNexLablesPage();
            
            labels.getItems().addAll(extra.getItems());
            return extra;
        }
        return null;
    }
    private LabelListFromApi getNexLablesPage() throws QobuzAPIException{
    
        if (!isLabelListComplete()) {
            
            
            LabelListGet q = new LabelListGet((long)labels.getItems().size());
            LabelListResult extra = q.getLabelListResult();
            
            return extra.getLables();
        }
        return null;
    }
    /**
     * @return the genres
     */
    public LabelListFromApi getLables() {
        return labels;
    }
}
