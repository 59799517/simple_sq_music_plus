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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class QobuzObjectFromApi {

	
    private static Logger log = Logger.getLogger(QobuzObjectFromApi.class.getName());    
    
	protected List<String> KeyList= new ArrayList();
	//protected Map<String,String> rawKeyValuePair = new HashMap<>();
	
    
    public QobuzObjectFromApi() {
       
    }

    public  QobuzObjectFromApi (JSONObject jsonObject) throws QobuzAPIException {
      
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }
    }
	/**
	 * @return the rawKeyValuePair
	 

	public Map<String,String> getRawKeyValuePair() {
		return this.rawKeyValuePair;
	}
	*/
	
	protected final void checkJSONObject(JSONObject jsonObject){
        
       for ( Iterator<String> keys = jsonObject.keys(); keys.hasNext(); ) {
          String key= keys.next();
          
          //System.out.println(key);
          
          if (this.KeyList.contains(key) || key.isEmpty())continue;
          log.log(Level.WARNING, "Unrecognised element in result object: {0}",key);
        }
    }

	@SneakyThrows
    protected String getString(String key, JSONObject jsonObject){
		
		String value="";
		
		if (jsonObject.has(key)&& !jsonObject.isNull(key)){
			value=jsonObject.getString(key);
		}
		//this.rawKeyValuePair.put(key, value);
		return value;

    }
    @SneakyThrows
    protected Long getLong(String key, JSONObject jsonObject){
		
		Long value=0L;
		if (jsonObject.has(key)&& !jsonObject.isNull(key)){
					
			Object obj =jsonObject.get(key);
			if (obj != null) {
				if (obj instanceof Long){
					value = (Long)obj;
				}
				if (obj instanceof Integer){
					value = (Integer)obj*1L;	
				}
				//this.rawKeyValuePair.put(key, value.toString());
			}
		}
		return value;
	}
	@SneakyThrows
    protected Long getDate(String key, JSONObject jsonObject){
		
		Long value=0L;
		Date date;
		if (jsonObject.has(key)&& !jsonObject.isNull(key)){
					
			Object obj =jsonObject.get(key);
			if (obj != null) {
				if (obj instanceof Long){
					value = (Long)obj;
				}
				if (obj instanceof Integer){
					value = (Integer)obj*1L;	
				}
				
				date = new Date(value*1000);
				//this.rawKeyValuePair.put(key, date.toString());
			}
		}
		return value;
	}
	@SneakyThrows
    protected Double getDouble(String key, JSONObject jsonObject){
		
		Double value=0D;
		if (jsonObject.has(key)&& !jsonObject.isNull(key)){
			value =  jsonObject.getDouble(key);
			//this.rawKeyValuePair.put(key, value.toString());
		}
		return value;
	}
	@SneakyThrows
    protected Boolean getBoolean(String key, JSONObject jsonObject){
	
		Boolean value= false;
		if (jsonObject.has(key)&& !jsonObject.isNull(key)){
			value= jsonObject.getBoolean(key);
		}
		//this.rawKeyValuePair.put(key, value.toString());
		return value;
	}
	
    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof QobuzObjectFromApi)) {
            return false;
        }
        return super.equals(obj);
    }
}