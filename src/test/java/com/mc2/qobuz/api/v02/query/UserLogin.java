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
import com.mc2.qobuz.api.v02.API.QobuzAuth;
import com.mc2.qobuz.api.v02.QobuzClient;
import com.mc2.qobuz.api.v02.elements.UserAuthFromApi;

/**
 *
 * @author marco
 */
public class UserLogin extends QobuzObjectQuery {
	
	private static final String LOGIN = "login";
	private static final String APP_ID = "app_id";
	private static final String DEVICE_MANUFACTURER_ID = "device_manufacturer_id";
	public static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	
	public UserLogin(QobuzAuth auth) throws QobuzAPIException{
        
               
        getAnswer(buildUrlStr(auth), auth);

    }
	 private String buildUrlStr(QobuzAuth auth){
        
        String urlStr = QobuzClient.BASEURL + "/"+QobuzObjectQuery.ENDPOINT_USER+"/"+LOGIN+"?"+
						APP_ID+"="+auth.getAppId()+"&"+
						DEVICE_MANUFACTURER_ID+"="+auth.getDeviceManufacturerId()+"&"+
						PASSWORD+"="+auth.getPasswordMd5()+"&"+
						USERNAME+"="+auth.getUsername();
		
        return urlStr;
    }
	 
	 public UserAuthFromApi getUserAuth() throws QobuzAPIException{
         
         return new UserAuthFromApi(super.getObject());
    }
}
