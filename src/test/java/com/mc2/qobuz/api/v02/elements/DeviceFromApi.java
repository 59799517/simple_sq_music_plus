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
import com.mc2.qobuz.api.v02.API.elements.Device;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author marco
 */
public final class DeviceFromApi extends QobuzObjectFromApi implements Device {

	private Long id;
	private String manufacturerId;
	private String model;
	private String osVersion;
	private String platform;

	
	public DeviceFromApi() {
        super();
    }
	
	public DeviceFromApi(JSONObject jsonObject) throws QobuzAPIException {
        super(jsonObject);
        
        // fill the KeyList here;
        
        KeyList.add(DEVICE_ID);
		KeyList.add(DEVICE_MANUFACTURER_ID);
		KeyList.add(DEVICE_MODEL);
		KeyList.add(DEVICE_OS_VERSION);
		KeyList.add(DEVICE_PLATFORM);
		
		checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }
		
		try {
                id = this.getLong(DEVICE_ID, jsonObject);
				manufacturerId = this.getString(DEVICE_MANUFACTURER_ID, jsonObject);
				model = this.getString(DEVICE_MODEL, jsonObject);
				osVersion = this.getString(DEVICE_OS_VERSION, jsonObject);
				platform = this.getString(DEVICE_PLATFORM, jsonObject);
				
			} catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, jsonObject.toString(), ex);
            throw new QobuzAPIException(ex.getMessage(), ex);
        }
   
    }

	@Override
	public Long getDeviceId() {
		return id;
	}

	@Override
	public String getDeviceManufacturerId() {
		return manufacturerId;
	}

	@Override
	public String getDeviceModel() {
		return model;
	}

	@Override
	public String getDeviceOsVersion() {
		return osVersion;
	}

	@Override
	public String getDevicePlatform() {
		return platform;
	}

}
