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
package com.mc2.qobuz.api.v02.IMPL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marco
 */
public class QobuzUtils {
	private static final String  SEPARATOR = ";";
	
	public static String workFromTitle(String work, String title){
		
		if (work != null && !"".equals(work)) return work;
		if  (title == null || "".equals(title)) return "";
		
		if (title.split(":").length > 1){
		
			return 	title.split(":")[0].trim();
		
		}
		
		return "";
	}
	
	public static String calcTitleOnly(String work, String title){
		
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
	public static ArrayList<String> calcPerformerList(String performers){
		
		ArrayList<String> out=new ArrayList<>();
        		
        if (performers == null) return out;
        if (performers.isEmpty()) return out;
        
        if (!performers.contains("-")){
            
			out.add(performers);
        
		} else {
        
			String[] parts = performers.split(" - ");

			for (String part : parts){
				out.add(part);
			}
		}

        return out; 
		
	}
	public static Map<String,String> getPerformersRoleNamesMap(ArrayList<String> performerList ){
        
        Map<String,String> performersMap = new HashMap<>();
		
        if (performerList.isEmpty()) return performersMap;
        
		for (String perf : performerList){
		
			String[] parts = perf.split(", ");
			
			if (parts.length > 1){
			
				String value = parts[0].trim();
				
				for (int i = 1; i < parts.length; i++) {
					
					String key = parts[i].trim();
					
					if (performersMap.containsKey(key)){
						performersMap.put(key, performersMap.get(key)+", "+value);
					} else{
						performersMap.put(key, value);
					}
				}
			} else if (parts.length == 1)	{
				
				String value = parts[0].trim();
				performersMap.put("Performer", value);
			}		
		}

        return performersMap; 
    }
	public static String getGenrePathSting(ArrayList<Long> path){
		
		String out="";
		if (path == null || path.isEmpty()){return out;}
		
		for (Long step  : path){
			if (!out.isEmpty()){out=out+SEPARATOR;}
			out = out+step.toString();
		}
		return out;
	}
	public static ArrayList<Long> getGenrePathfromGenrePathString(String pathString){
		
		ArrayList<Long>  out= new ArrayList<>();
		if (pathString == null || pathString.isEmpty()){return out;}
		
		String[] strArray = pathString.split(SEPARATOR);
		for (String step  : strArray){
			out.add(Long.parseLong(step));
		}
		
		return out;
	}
	public static String getGenresFromGenreList(ArrayList<String> genreList){
		
		return getStringFromArrayList(genreList);
	}
	
	public static String getStringFromArrayList(ArrayList<String> list){
		
		String out="";
		if (list == null || list.isEmpty()){return out;}
		
		for (String step  : list){
			if (!out.isEmpty()){out=out+SEPARATOR;}
			out = out+step;
		}
		return out;
	}
	
	public static ArrayList<String> getGenreListFromGenres(String genres){
		
		return getArrayLIstFromString(genres);
	}
	
	public static ArrayList<String> getArrayLIstFromString(String in){
		
		ArrayList<String>  out= new ArrayList<>();
		if (in == null || in.isEmpty()){return out;}
		
		String[] strArray = in.split(SEPARATOR);
		for (String step  : strArray){
			out.add(step);
		}
		
		return out;
	}
}
