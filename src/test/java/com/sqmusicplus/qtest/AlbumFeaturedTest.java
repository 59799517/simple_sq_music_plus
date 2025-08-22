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
import com.mc2.qobuz.api.v02.API.elements.Album;
import com.mc2.qobuz.api.v02.API.elements.FeaturedAlbums;
import com.mc2.qobuz.api.v02.API.lists.AlbumList;
import com.mc2.qobuz.api.v02.QobuzApiController;
import com.mc2.qobuz.api.v02.QobuzAuth;
import com.mc2.qobuz.api.v02.QobuzAuthFromEnvVar;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mc2.qobuz.api.v02.query.AlbumsGetFeatured;
import java.util.ArrayList;


/**
 *
 * @author marco
 */
public class AlbumFeaturedTest extends UnitTest {

    //@Test
    public void albumFeaturedTest() throws QobuzAPIException{
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
		
        Long genre= 10L; //Classical
        for(int typeInd=0; typeInd<8; typeInd++){
            albumFeaturedTest(typeInd,genre, false);
        }
    }
    //@Test
    public void newClassicalReleases() throws QobuzAPIException{
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
			
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
		
        albumFeaturedTest(13, 10L, true);
    }
    //@Test
    public void newReleases() throws QobuzAPIException{
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
			
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
       
		albumFeaturedTest(13, 0L, true);
    }
    private void albumFeaturedTest(int typeInd, long genre, Boolean details) throws QobuzAPIException {
        
        //You could use this template to search any single type in the catalog.

        try {
                
                String type = AlbumsGetFeatured.suggestedTypes()[typeInd];
               
                AlbumsGetFeatured q = new AlbumsGetFeatured(type, genre, (long)0, (long)10);
                FeaturedAlbums featuredAlbums = q.getFeaturedAlbumResult();
                
                System.out.println("======================================================================");
                System.out.println("= ALBUM FEATURED: "+type +" - "+genre);
               
            
                AlbumList albums = featuredAlbums.getAlbums();
                if (albums == null){
                    System.out.println("");
                    System.out.println("= NULL =");
                    System.out.println("======================================================================");
                    return;
                }
                
                Long totalAlbum = albums.getTotal();
                long size=0;
                
                if (!details){
                
                    System.out.println("");
                    System.out.println("= "+totalAlbum+" =");
                    
                    System.out.println("======================================================================");
                    return;
                }
                
                System.out.println("");
                
                System.out.println( "- Albums : "+totalAlbum);
                
                 if (albums!= null && albums.getItems().size() > 0) {
                    System.out.println( "- ALBUMS: ");
                    //TestUtils.printAlbumList(albums,"   ", false);
                    long itemSize= albums.getItems().size();
                    size = itemSize > size ? itemSize : size;
                }
                
                int page=1;
                
                boolean stop = ( albums == null);

                while (!stop ) {
                                       
                    System.out.println("= "+page+" =");
                    
                    long offset= size;
                    
                    q = new AlbumsGetFeatured(type, genre , offset);
                    FeaturedAlbums extra = q.getFeaturedAlbumResult();

                    if (albums!= null && albums.getItems().size() > 0) {
						
						ArrayList<Album> a = (ArrayList<Album>)albums.getItems();
						ArrayList<Album> b = (ArrayList<Album>)extra.getAlbums().getItems();
						a.addAll(b);
						
                       System.out.println( "- ALBUMS: ");
                       
                       //TestUtils.printAlbumList(extra.getAlbums(),"   ", false);

                        
                       long itemSize= albums.getItems().size();
                       size = itemSize > size ? itemSize : size;
                    }
                    
                    stop = (offset == size); 
                    
                    page++;
                    
                } 
                //System.out.println("= "+page+" =");
                
                System.out.println("======================================================================");
                
                for  (Album album : albums.getItems()){
                
                        System.out.println(album.getId()+" - "+album.getTitle());
                }

        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}