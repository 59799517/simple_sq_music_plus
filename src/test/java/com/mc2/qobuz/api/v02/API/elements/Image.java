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

import java.net.URL;

/**
 *
 * @author marco
 */
public interface Image {
	
	String THUMBNAIL = "thumbnail"; //50px
	String BACK = "back";
	String EXTRALARGE = "extralarge";
	String LARGE = "large"; //600p
	String MEDIUM = "medium";
	String MEGA = "mega";
	String SMALL = "small"; //230px

	/**
	 * @return the back
	 */
	URL getBack();

	/**
	 * @return the extralarge
	 */
	URL getExtralarge();

	/**
	 * @return the large (600px)
	 */
	URL getLarge();

	/**
	 * @return the medium
	 */
	URL getMedium();

	/**
	 * @return the mega
	 */
	URL getMega();

	/**
	 * @return the small (230px)
	 */
	URL getSmall();

	/**
	 * @return the thumbnail (50px)
	 */
	URL getThumbnail();
	
}
