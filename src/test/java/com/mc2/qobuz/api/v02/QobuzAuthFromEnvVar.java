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

/**
 *
 * @author marco
 */
public class QobuzAuthFromEnvVar extends QobuzAuth implements com.mc2.qobuz.api.v02.API.QobuzAuth {
	
	public QobuzAuthFromEnvVar(){
		
		super("","","","","");
		
		userAgent = System.getenv("QOBUZ_USER_AGENT") == null ? "" : System.getenv("QOBUZ_USER_AGENT");
		appId = System.getenv("QOBUZ_API_KEY") == null ? "" : System.getenv("QOBUZ_API_KEY");
		deviceManufacturerId = System.getenv("QOBUZ_DEVICE_MANUFACTURER_ID") == null ? "" : System.getenv("QOBUZ_DEVICE_MANUFACTURER_ID");
		username = System.getenv("QOBUZ_USERNAME") == null ? "" : System.getenv("QOBUZ_USERNAME");
		passwordMd5 = System.getenv("QOBUZ_PASSWORD_MD5") == null ? "" : System.getenv("QOBUZ_PASSWORD_MD5");
	}
	
}
