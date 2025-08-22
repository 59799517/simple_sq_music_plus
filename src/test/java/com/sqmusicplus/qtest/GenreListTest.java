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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mc2.qobuz.api.v02.elements.GenreFromApi;
import com.mc2.qobuz.api.v02.elements.GenreListResult;
import com.mc2.qobuz.api.v02.lists.GenreListFromApi;
import com.mc2.qobuz.api.v02.query.GenreListGet;
import com.mc2.qobuz.api.v02.IMPL.TestUtils;
import com.mc2.qobuz.api.v02.QobuzApiController;
import com.mc2.qobuz.api.v02.QobuzAuth;
import com.mc2.qobuz.api.v02.QobuzAuthFromEnvVar;
import org.junit.jupiter.api.Test;

/**
 *
 * @author marco
 */
public class GenreListTest {
    @Test
    public void getGenreList() {
    
        try {
			com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
			
			QobuzAuth auth = new QobuzAuthFromEnvVar();
			controller.setQobuzAuth(auth);
			
            GenreListGet q = new GenreListGet();
            GenreListResult result= q.getGenreListResult();
            
            
            System.out.println("======================================================================");
            System.out.println("= GENRES                                                             =");
            System.out.println("======================================================================");
                
            GenreListFromApi genreList = result.getGenres();
            ArrayList<GenreFromApi> genres = genreList.getItems();
            
            for (GenreFromApi genre : genres){
            
                TestUtils.printGenre(genre, "",false);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
