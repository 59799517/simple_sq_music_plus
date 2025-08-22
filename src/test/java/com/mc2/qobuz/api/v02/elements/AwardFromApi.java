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
import com.mc2.qobuz.api.v02.API.elements.Award;
/**
 *
 * @author marco
 */
public final class  AwardFromApi extends QobuzObjectFromApi implements Award{  

    
    private String award_id;
    private String award_slug;
    private String publication_name;
    private String publication_id;
    private String publication_slug;
    private String name;
    private String slug;
    private Long awarded_at;
    
    
    public AwardFromApi() {
        super();
    }

    public AwardFromApi (JSONObject jsonObject)throws QobuzAPIException {
        super(jsonObject);
        
        KeyList.add(AWARD_ID);
        KeyList.add(AWARD_SLUG);
        KeyList.add(PUBLICATION_NAME);
        KeyList.add(PUBLICATION_ID);
        KeyList.add(PUBLICATION_SLUG);
        KeyList.add(NAME);
        KeyList.add(SLUG);
        KeyList.add(AWARDED_AT);
        
        checkJSONObject(jsonObject);
        
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }

        try {
                award_id = jsonObject.getString(AWARD_ID);
                name = jsonObject.getString(NAME);

                slug= jsonObject.has(SLUG) ? 
                            jsonObject.isNull(SLUG) ? 
                            null : jsonObject.getString(SLUG) : null;
                
                award_slug= jsonObject.has(AWARD_SLUG) ? 
                            jsonObject.isNull(AWARD_SLUG) ? 
                            null : jsonObject.getString(AWARD_SLUG) : null;
                
                publication_name = jsonObject.has(PUBLICATION_NAME) ? 
                            jsonObject.isNull(PUBLICATION_NAME) ? 
                            null : jsonObject.getString(PUBLICATION_NAME) : null;
                
                publication_id= jsonObject.has(PUBLICATION_ID) ? 
                            jsonObject.isNull(PUBLICATION_ID) ? 
                            null : jsonObject.getString(PUBLICATION_ID) : null;
                       
                publication_slug= jsonObject.has(PUBLICATION_SLUG) ? 
                            jsonObject.isNull(PUBLICATION_SLUG) ? 
                            null : jsonObject.getString(PUBLICATION_SLUG) : null;
                
                awarded_at = jsonObject.has(AWARDED_AT) ? 
                        jsonObject.isNull(AWARDED_AT) ? 
                            null : jsonObject.getLong(AWARDED_AT) : null;

            } catch (JSONException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                throw new QobuzAPIException(ex.getMessage(), ex);
        }
    }
    /**
     * @return the award_id
     */
	@Override
    public String getAward_id() {
        return award_id;
    }
    /**
     * @return the award_slug
     */
	@Override
    public String getAward_Slug() {
        return award_slug;
    }
    /**
     * @return the publication_name
     */
	@Override
    public String getPublication_name() {
        return publication_name;
    }

    /**
     * @return the publication_id
     */
	@Override
    public String getPublication_id() {
        return publication_id;
    }

    /**
     * @return the publication_slug
     */
	@Override
    public String getPublication_slug() {
        return publication_slug;
    }

    /**
     * @return the name
     */
	@Override
    public String getName() {
        return name;
    }

    /**
     * @return the slug
     */
	@Override
    public String getSlug() {
        return slug;
    }

    /**
     * @return the awarded_at
     */
	@Override
    public Long getAwarded_at() {
        return awarded_at;
    }
}
