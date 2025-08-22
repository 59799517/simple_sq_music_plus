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
package com.mc2.qobuz.api.v02.API;

/**
 *
 * @author marco
 */

public interface QobuzAuth {
	
	/**
    * API use and application authentification

    * In order to use our API, you need to authenticate your application by 
    * sending an application ID parameter expressed as app_id along with method 
    * specific arguments to the root endpoint for all API calls. 
    * 
    * Please contact api@qobuz.com to request your application credentials 
    * (app_id and app_secret values). 
    * Don't forget to provide us with your Qobuz login or email.

    * Example

    * http://www.qobuz.com/api.json/0.2/status/test?app_id=100000000
    
    * Disclaimer : Please note that your app_id and app_secret are strictly
    * personal and should not be given to any other parties without the explicit
    * consent of Qobuz.

    * You must make sure that the email address associated with your developer 
    * account (ie the account associated with the app_id and app_secret given) 
    * is always valid at any time and that the developer can be reached at this 
    * address. Failure to do so will result in the immediate suspension of the 
    * app_id and app_secret associated.

    * Any use of the API implies your full acceptance of their Terms of Use 
    * (http://www.qobuz.com/apps/api/QobuzAPI-TermsofUse.pdf).

    */    

	String getUserAgent();
	String getAppId();
	String getDeviceManufacturerId();
	String getUsername();
	String getPasswordMd5();

}
