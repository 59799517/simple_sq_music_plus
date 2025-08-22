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
package com.mc2.qobuz.api.v02.API.elements;

import java.util.ArrayList;

/**
 *
 * @author marco
 */
public interface User {
	
	String ID = "id";
	String PUBLIC_ID = "publicId";
	String EMAIL = "email";
	String LOGIN = "login";
	String FIRSTNAME = "firstname";
	String LASTNAME = "lastname";
	String DISPLAY_NAME = "display_name";
	String COUNTRY_CODE = "country_code";
	String LANGUAGE_CODE = "language_code";
	String ZONE = "zone";
	String STORE = "store";
	String COUNTRY = "country";
	String AVATAR_URL = "avatar";
	String GENRE = "genre";
	String AGE = "age";
	String CREATION_DATE = "creation_date";
	String SUBSCRIPTION = "subscription";
	String CREDENTIAL = "credential";
	String EXTERNALS = "externals";
	String DEVICE = "device";

	/**
	 * @return the id
	 */
	Long getId();
	
	/**
	 * @return the pubicId
	 */
	String getPubicId();
	/**
	 * @return the email
	 */
	String getEmail();
	/**
	 * @return the login
	 */
	String getLogin();
	/**
	 * @return the firstName
	 */
	String getFirstName();
	/**
	 * @return the lastName
	 */
	String getLastName();
	/**
	 * @return the displayName
	 */
	String getDisplayName();
	/**
	 * @return the countryCode
	 */
	String getCountryCode();
	/**
	 * @return the languageCode
	 */
	String getLanguageCode();
	/**
	 * @return the zone
	 */
	String getZone();
	/**
	 * @return the store
	 */
	String getStore();
	/**
	 * @return the country
	 */
	String getCountry();
	/**
	 * @return the avatar Url
	 */
	String getAvatarUrl();
	/**
	 * @return the genre
	 */
	String getGenre();
	/**
	 * @return the age
	 */
	Long getAge();
	/**
	 * @return the creation date
	 */
	String getCreationDate();
	/**
	 * @return the Subscription Offer
	 */
	String getSubscriptionOffer();
	/**
	 * @return the Subscription periodicity
	 */
	String getSubscriptionPeriodicity();
	/**
	 * @return the Subscription end date
	 */
	String getSubscriptionEndDate();
	/**
	 * @return the Subscription is cancelled
	 */
	Boolean getSubscriptionIsCanceled();
	/**
	 * @return the Credential id
	 */
	Long getCredentialId();
	/**
	 * @return the Credential label
	 */
	String getCredentialLabel();
	/**
	 * @return the Credential description
	 */
	String getCredentialDescription();
	/**
	 * @return the Credential parameter is lossy Streaming enableb
	 */
	Boolean isLossysStreamingEnabled();
	/**
	 * @return the Credential parameter is lossless Streaming enableb
	 */
	Boolean isLosslessStreamingEnabled();
	/**
	 * @return the Credential parameter is HiRes Streaming enableb
	 */
	Boolean isHiResStreamingEnabled();
	/**
	 * @return the Credential parameter is HiRes Purchases Streaming enableb
	 */
	Boolean isHiResPurchasesStreamingEnabled();
	/**
	 * @return the Credential parameter is Mobile Streaming enableb
	 */
	Boolean isMobileStreamingEnabled();
	/**
	 * @return the Credential parameter is Offline Streaming enableb
	 */
	Boolean isOfflineStreamingEnabled();
	/**
	 * @return the Credential parameter is HFP Purchase enableb
	 */
	Boolean isHfpPurchaseEnabled();
	/**
	 * @return the Included Format Group Ids
	 */
	ArrayList<Integer> getIncludedFormatGroupIds();
	/**
	 * @return the Color Scheme logo
	 */
	String getColorSchemeLogo();
	/**
	 * @return the Label
	 */
	String getLabel();
	/**
	 * @return the Short Label
	 */
	String getShortLabel();
	/**
	 * @return the Source
	 */
	String getSource();
	/**
	 * @return the Device id
	 */
	Long getDeviceId();
	/**
	 * @return the Device Manufacturer id
	 */
	String getDeviceManufacturerId();
	/**
	 * @return the Device Manufacturer model
	 */
	String getDeviceModel();
	/**
	 * @return the Device OS version
	 */
	String getDeviceOsVersion();
	/**
	 * @return the Device platform
	 */
	String getDevicePlatform();
	
	
}
