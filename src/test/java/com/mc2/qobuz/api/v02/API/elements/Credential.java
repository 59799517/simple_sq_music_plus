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
public interface Credential {
	
	String ID = "id";
	String LABEL = "label";
	String DESCRIPTION = "description";
	String PARAMETERS = "parameters";
	
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
	
}
