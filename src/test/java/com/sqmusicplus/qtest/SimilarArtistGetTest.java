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

import com.mc2.qobuz.api.v02.API.QobuzAPIException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mc2.qobuz.api.v02.elements.SimilarArtistsFromApi;
import com.mc2.qobuz.api.v02.lists.ArtistListFromApi;
import com.mc2.qobuz.api.v02.query.ArtistGetSimilar;
import com.mc2.qobuz.api.v02.IMPL.TestUtils;
import com.mc2.qobuz.api.v02.QobuzApiController;
import com.mc2.qobuz.api.v02.QobuzAuth;
import com.mc2.qobuz.api.v02.QobuzAuthFromEnvVar;

/**
 *
 * @author marco
 */
public class SimilarArtistGetTest {
    
    //@Test
    public void getSimilarArtistWithPagination() throws QobuzAPIException {
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
        try {
                Long artist =  (long)720;
                ArtistGetSimilar q = new ArtistGetSimilar(artist,(long)0);
                SimilarArtistsFromApi similarArtists = q.getSimilaArtist();
                
                ArtistListFromApi artists = similarArtists.getArtists();
                
                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= SIMILAR ARTIST (with pagination)                                   =");
                System.out.println("======================================================================");
                
                TestUtils.printArtistList(artists,""); 
                
                int page=1;
                long size=0;
                
                if (artists!= null) {         
                    size = artists.getItems().size();     
                }
                
                boolean stop = (artists == null);
                
                // API BUG: sometime reported total is different than the actual number
                // of records, forget it and get all that we have.
                
                while (!stop) {
                    
                    System.out.println("= "+page+" =");
                    
                    q = new ArtistGetSimilar(artist,(long)artists.getItems().size());
                    ArtistListFromApi extra = q.getSimilaArtist().getArtists();
                    artists.getItems().addAll(extra.getItems());
                    stop = (artists.getItems().size() == size); 
                    size = artists.getItems().size();

                    TestUtils.printArtistList(extra, "");
                    
                    page++;
                    
                } 
                System.out.println("= "+page+" =");
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
