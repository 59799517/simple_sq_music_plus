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
package com.sqmusicplus.qtest;


import org.junit.jupiter.api.Test;

/**
 *
 * @author marco
 */
public class WorkAndTitleTest {
	
	
	String title= "Symphony No. 6 in A Major, WAB 106: I. Majestoso (Benjamin-Gunnar Cohrs Urtext Edition)";
	String workTitle= "Symphony No. 6 in A Major, WAB 106";
	
	/* tracks returns:
	getWork(): 
	getTitle(): Symphony No. 6 in A Major, WAB 106: I. Majestoso (Benjamin-Gunnar Cohrs Urtext Edition)
    getWorkGuessed: Symphony No. 6 in A Major, WAB 106
    getTitleOnly: Symphony No. 6 in A Major, WAB 106: I. Majestoso (Benjamin-Gunnar Cohrs Urtext Edition)
	*/
	
	
	@Test
	public void calcTitles(){
	
		String workGuessed = workFromTitle(workTitle,title);
		String titleOnly =calcTitleOnly(workGuessed,title);
		
		System.out.println("STORED WORK TITLE  : "+workTitle);
		System.out.println("STORED TITLE       : "+title);
		
		System.out.println("WORK GUESSED       : "+workGuessed);
		System.out.println("TITLE ONLY         : "+titleOnly);
		
		System.out.println("RETURNED WORK TITLE: "+workGuessed);
		System.out.println("RETURNED TITLE ONLY: "+titleOnly);
		
		System.out.println("RETURNED TITLE     : "+workGuessed+" : "+titleOnly);
		
		
	}
	
	private String workFromTitle(String workTitle, String title){
		
		if (workTitle != null && !"".equals(workTitle)) return workTitle;
		if  (title == null || "".equals(title)) return "";
		
		if (title.split(":").length > 1){
		
			return 	title.split(":")[0].trim();
		
		}
		
		return "";
	}
	
	private String calcTitleOnly(String work, String title){
		
		/*
		Work: Cello Sonata in B-Flat Major, RV 46
		Title: Cello Sonata in B-Flat Major, RV 46: I. Preludio (Largo)
		Title only: I. Preludio (Largo)
		
		Work: Cello Sonata in B-Flat Major, RV 46
		Title: Cello Sonata in B-Flat Major, RV 46 : Cello Sonata in B-Flat Major, RV 46: I. Preludio (Largo)
		Title only: I. Preludio (Largo)
		*/
		
		if  (title == null || "".equals(title)) return "";
		if (work == null || "".equals(work)) return title;
		
		String out="";
	
		for (String el :  title.split(":")){
		
			if (!el.trim().toUpperCase().equals(work.trim().toUpperCase())){
				
				if (! out.isEmpty()) out= out.concat(" : ");
				out= out.concat(el.trim());
			}
		}
		if (out.isEmpty()) return title;
		return out;
	}
	
}
