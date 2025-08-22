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
public class QobuzAuth implements com.mc2.qobuz.api.v02.API.QobuzAuth {
	
	protected  String userAgent;
	protected  String appId;
	protected  String deviceManufacturerId;
	protected  String username;
	protected  String passwordMd5;

	
	protected QobuzAuth(String userAgent, 
					 String appId,
					 String deviceManufacturerId,
					 String username,
					 String passwordMd5){
		
		this.userAgent = userAgent;
		this.appId = appId;
		this.deviceManufacturerId = deviceManufacturerId;
		this.username = username;
		this.passwordMd5 = passwordMd5;
	}
	

	@Override
	public String getUserAgent() {
		return userAgent;
	}

	@Override
	public String getAppId() {
		return appId;
	}

	@Override
	public String getDeviceManufacturerId() {
		return deviceManufacturerId;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPasswordMd5() {
		return passwordMd5;
	}
	
}
