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

package com.mc2.qobuz.api.v02;

import com.mc2.qobuz.api.v02.exceptions.QobuzConnectionException;
import com.mc2.qobuz.api.v02.API.QobuzAuth;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public final class QobuzClient {

    private static Logger log = Logger.getLogger(QobuzClient.class.getName());
    
    public static final String BASEURL = "http://www.qobuz.com/api.json/0.2";
   
    private static long lastHitTime=0;
    
    public static URL getUrl(String urlString)throws QobuzConnectionException {
        
        URL url;
 
        try {
                url = new URL(urlString);
           
        } catch (MalformedURLException e) {
           String msg = "Failed to create URL, fatal error: " + e.getMessage();
           throw new QobuzConnectionException(msg, e);
        }
        System.out.println( "URL: "+url);
        return  url;
    }
    
    public static String makeApiCall(URL url) throws QobuzConnectionException {
		
		wait(100);
		try(QobuzConnection connection = new QobuzConnection()){
			
			String answer= connection.getDataAsString(url);
			// String answer=QobuzConnection.getInstance().getDataAsString(url);
			lastHitTime =System.currentTimeMillis();
			return answer;
			
		} catch (Exception ex){
			throw new QobuzConnectionException(ex);
		}
    }
	
	public static String makeApiCall(URL url,QobuzAuth auth) throws QobuzConnectionException {
		
		wait(100);
		try(QobuzConnection connection = new QobuzConnection(auth)){
			
			String answer= connection.getDataAsString(url);
			// String answer=QobuzConnection.getInstance().getDataAsString(url);
			lastHitTime =System.currentTimeMillis();
			return answer;
			
		} catch (Exception ex){
			throw new QobuzConnectionException(ex);
		}
    }
    
    public static String makeApiCall(String urlString) throws QobuzConnectionException {

        return makeApiCall(getUrl(urlString));
    }
	 public static String makeApiCall(String urlString,QobuzAuth auth) throws QobuzConnectionException {

        return makeApiCall(getUrl(urlString),auth);
    }
    
    private static void wait (int milliseconds){
        long t1;
        if (lastHitTime >0)
        {
            do{ t1=System.currentTimeMillis();
            } while (t1-lastHitTime<milliseconds);
        }
    }
    
    /*
    public static String makeOAuthorizedApiCall(String urlString, DiscogsOAuthCredentials credentials) throws DiscogsConnectionExeption {

        return makeOAuthorizedApiCall(getUrl(urlString), credentials);
    }
    
    public static String makeOAuthorizedApiCall(URL url,DiscogsOAuthCredentials credentials) throws DiscogsConnectionExeption {
        
        wait(1);
        String answer=DiscogsConnection.getInstance().getDataAsString(url, credentials);
        lastHitTime =System.currentTimeMillis();
        return answer;
    }
    public static BufferedImage getImageFromApi (String urlString,DiscogsOAuthCredentials credentials) throws DiscogsConnectionExeption {
        
        return getImageFromApi(getUrl(urlString), credentials);
    }
    
    public static BufferedImage getImageFromApi (URL url,DiscogsOAuthCredentials credentials) throws DiscogsConnectionExeption {

        wait(1);
        BufferedImage answer=DiscogsConnection.getInstance().getDataAsImage(url,credentials);
        lastHitTime =System.currentTimeMillis();
        return answer;
    }
    */

}