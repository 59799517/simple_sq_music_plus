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

/**
 *
 * @author marco
 */
public interface Award {

	String AWARDED_AT = "awarded_at";
	String AWARD_ID = "award_id";
	String AWARD_SLUG = "award_slug";
	String NAME = "name";
	String PUBLICATION_ID = "publication_id";
	String PUBLICATION_NAME = "publication_name";
	String PUBLICATION_SLUG = "publication_slug";
	String SLUG = "slug";

	/**
	 * @return the award_slug
	 */
	String getAward_Slug();

	/**
	 * @return the award_id
	 */
	String getAward_id();

	/**
	 * @return the awarded_at
	 */
	Long getAwarded_at();

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @return the publication_id
	 */
	String getPublication_id();

	/**
	 * @return the publication_name
	 */
	String getPublication_name();

	/**
	 * @return the publication_slug
	 */
	String getPublication_slug();

	/**
	 * @return the slug
	 */
	String getSlug();
	
}
