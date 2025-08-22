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
import com.mc2.qobuz.api.v02.API.elements.Credential;
import com.mc2.qobuz.api.v02.API.elements.Device;
import com.mc2.qobuz.api.v02.API.elements.Subscription;
import com.mc2.qobuz.api.v02.API.elements.User;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author marco
 */
public final class UserFromApi extends QobuzObjectFromApi implements User {
	
	private Long id;
	private String publicId;
	private String email;
	private String login;
	private String firstName;
	private String lastName;
	private String displayName;
	private String countryCode;
	private String languageCode;
	private String zone;
	private String store;
	private String country;
	private String avatarUrl;
	private String genre;
	private Long age;
	private String creationDate;
	
	private Subscription subscription;
	private Credential credential;
	private EmptyClass externals;
	private Device device;
	
	private String userAuthToken;
	
	public UserFromApi() {
        super();
    }
	
	public UserFromApi(JSONObject jsonObject) throws QobuzAPIException {
        super(jsonObject);
        
        // fill the KeyList here;

		KeyList.add(ID);
		KeyList.add(PUBLIC_ID);
		KeyList.add(EMAIL);
		KeyList.add(LOGIN);
		KeyList.add(FIRSTNAME);
		KeyList.add(LASTNAME);
		KeyList.add(DISPLAY_NAME);
		KeyList.add(COUNTRY_CODE);
		KeyList.add(LANGUAGE_CODE);
		KeyList.add(ZONE);
		KeyList.add(STORE);
		KeyList.add(COUNTRY);
		KeyList.add(AVATAR_URL);
		KeyList.add(GENRE);
		KeyList.add(AGE);
		KeyList.add(CREATION_DATE);
		KeyList.add(SUBSCRIPTION);
		KeyList.add(CREDENTIAL);
		KeyList.add(EXTERNALS);
		KeyList.add(DEVICE);
		
		checkJSONObject(jsonObject);
        /**
         * If there is no parameter there is no need to go further
         */
        if (jsonObject == null) {
            throw new NullPointerException();
        }
		
		try {
                id = this.getLong(ID, jsonObject);
				publicId = this.getString(PUBLIC_ID, jsonObject);
				email = this.getString(EMAIL, jsonObject);
				login = this.getString(LOGIN, jsonObject);
				firstName = this.getString(FIRSTNAME, jsonObject);
				lastName = this.getString(LASTNAME, jsonObject);
				displayName = this.getString(DISPLAY_NAME, jsonObject);
				countryCode = this.getString(COUNTRY_CODE, jsonObject);
				languageCode = this.getString(LANGUAGE_CODE, jsonObject);
				zone = this.getString(ZONE, jsonObject);
				store = this.getString(STORE, jsonObject);
				country = this.getString(COUNTRY, jsonObject);
				avatarUrl = this.getString(AVATAR_URL, jsonObject);
				genre = this.getString(GENRE, jsonObject);
				age = this.getLong(AGE, jsonObject);
				creationDate = this.getString(CREATION_DATE, jsonObject);
				
				subscription = jsonObject.has(SUBSCRIPTION) ? 
                        jsonObject.isNull(SUBSCRIPTION) ? 
                            null : new SubscriptionFromApi(jsonObject.getJSONObject(SUBSCRIPTION)) : null;
				
				credential = jsonObject.has(CREDENTIAL) ? 
                        jsonObject.isNull(CREDENTIAL) ? 
                            null : new CredentialFromApi(jsonObject.getJSONObject(CREDENTIAL)) : null;
				
				device = jsonObject.has(DEVICE) ? 
                        jsonObject.isNull(DEVICE) ? 
                            null : new DeviceFromApi(jsonObject.getJSONObject(DEVICE)) : null;
				
				externals = jsonObject.has(EXTERNALS) ? 
                        jsonObject.isNull(EXTERNALS) ? 
                            null : new EmptyClass(jsonObject.getJSONObject(EXTERNALS)) : null;
				
			
				
			} catch (JSONException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, jsonObject.toString(), ex);
            throw new QobuzAPIException(ex.getMessage(), ex);
        }
   
    }

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String getPubicId() {
		return this.publicId;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public String getLogin() {
		return this.login;
	}

	@Override
	public String getFirstName() {
		return this.firstName;
	}

	@Override
	public String getLastName() {
		return this.lastName;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public String getCountryCode() {
		return this.countryCode;
	}

	@Override
	public String getLanguageCode() {
		return this.languageCode;
	}

	@Override
	public String getZone() {
		return this.zone;
	}

	@Override
	public String getStore() {
		return this.store;
	}

	@Override
	public String getCountry() {
		return this.country;
	}

	@Override
	public String getAvatarUrl() {
		return this.avatarUrl;
	}

	@Override
	public String getGenre() {
		return this.genre;
	}

	@Override
	public Long getAge() {
		return this.age;
	}

	@Override
	public String getCreationDate() {
		return this.creationDate;
	}

	@Override
	public String getSubscriptionOffer() {
		if (this.subscription != null) return this.subscription.getOffer();
		return "";
	}

	@Override
	public String getSubscriptionPeriodicity() {
		if (this.subscription != null) return this.subscription.getPeriodicity();
		return "";
	}

	@Override
	public String getSubscriptionEndDate() {
		if (this.subscription != null) return this.subscription.getEndDate();
		return "";
	}

	@Override
	public Boolean getSubscriptionIsCanceled() {
		if (this.subscription != null) return this.subscription.getIsCanceled();
		return false;
	}

	@Override
	public Long getCredentialId() {
		if (this.credential != null) return this.credential.getCredentialId();
		return 0L;
	}

	@Override
	public String getCredentialLabel() {
		if (this.credential != null) return this.credential.getCredentialLabel();
		return "";
	}

	@Override
	public String getCredentialDescription() {
		if (this.credential != null) return this.credential.getCredentialDescription();
		return "";
	}

	@Override
	public Boolean isLossysStreamingEnabled() {
		if (this.credential != null) return this.credential.isLossysStreamingEnabled();
		return false;
	}

	@Override
	public Boolean isLosslessStreamingEnabled() {
		if (this.credential != null) return this.credential.isLosslessStreamingEnabled();
		return false;
	}

	@Override
	public Boolean isHiResStreamingEnabled() {
		if (this.credential != null) return this.credential.isHiResStreamingEnabled();
		return false;
	}

	@Override
	public Boolean isHiResPurchasesStreamingEnabled() {
		if (this.credential != null) return this.credential.isHiResPurchasesStreamingEnabled();
		return false;
	}

	@Override
	public Boolean isMobileStreamingEnabled() {
		if (this.credential != null) return this.credential.isMobileStreamingEnabled();
		return false;
	}

	@Override
	public Boolean isOfflineStreamingEnabled() {
		if (this.credential != null) return this.credential.isOfflineStreamingEnabled();
		return false;
	}

	@Override
	public Boolean isHfpPurchaseEnabled() {
		if (this.credential != null) return this.credential.isHfpPurchaseEnabled();
		return false;
	}

	@Override
	public ArrayList<Integer> getIncludedFormatGroupIds() {
		if (this.credential != null) return this.credential.getIncludedFormatGroupIds();
		return new ArrayList<>();
	}

	@Override
	public String getColorSchemeLogo() {
		if (this.credential != null) return this.credential.getColorSchemeLogo();
		return "";
	}
	
	@Override
	public String getLabel() {
		if (this.credential != null) return this.credential.getLabel();
		return "";
	}

	@Override
	public String getShortLabel() {
		if (this.credential != null) return this.credential.getShortLabel();
		return "";
	}

	@Override
	public String getSource() {
		if (this.credential != null) return this.credential.getSource();
		return "";
	}

	@Override
	public Long getDeviceId() {
		if (this.device != null) return this.device.getDeviceId();
		return 0L;
	}

	@Override
	public String getDeviceManufacturerId() {
		if (this.device != null) return this.device.getDeviceManufacturerId();
		return "";
	}

	@Override
	public String getDeviceModel() {
		if (this.device != null) return this.device.getDeviceModel();
		return "";
	}

	@Override
	public String getDeviceOsVersion() {
		if (this.device != null) return this.device.getDeviceOsVersion();
		return "";
	}
	@Override
	public String getDevicePlatform() {
		if (this.device != null) return this.device.getDevicePlatform();
		return "";
	}
}
