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
import com.mc2.qobuz.api.v02.API.lists.AlbumList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mc2.qobuz.api.v02.elements.AlbumFromApi;
import com.mc2.qobuz.api.v02.elements.CatalogFromApi;
import com.mc2.qobuz.api.v02.elements.GenreFromApi;
import com.mc2.qobuz.api.v02.elements.GenreListResult;
import com.mc2.qobuz.api.v02.elements.LabelFromApi;
import com.mc2.qobuz.api.v02.elements.LabelListResult;
import com.mc2.qobuz.api.v02.elements.TrackFromApi;
import com.mc2.qobuz.api.v02.lists.ArtistListFromApi;
import com.mc2.qobuz.api.v02.lists.TrackListFromApi;
import com.mc2.qobuz.api.v02.lists.AlbumListFromApi;
import com.mc2.qobuz.api.v02.lists.GenreListFromApi;
import com.mc2.qobuz.api.v02.lists.LabelListFromApi;
import com.mc2.qobuz.api.v02.query.AlbumsGetFeatured;
import com.mc2.qobuz.api.v02.query.GenreListGet;
import com.mc2.qobuz.api.v02.query.LabelListGet;
import com.mc2.qobuz.api.v02.query.AlbumGet;
import com.mc2.qobuz.api.v02.query.GenreGet;
import com.mc2.qobuz.api.v02.query.LabelGet;
import com.mc2.qobuz.api.v02.query.TrackGet;
import com.mc2.qobuz.api.v02.query.CatalogSearch;
import java.util.ArrayList;

import com.mc2.qobuz.api.v02.IMPL.TestUtils;
import com.mc2.qobuz.api.v02.QobuzApiController;
import com.mc2.qobuz.api.v02.QobuzAuth;
import com.mc2.qobuz.api.v02.QobuzAuthFromEnvVar;

/**
 *
 * @author marco
 */
public class Others extends UnitTest {

    //@Test
    public void artistSearch() throws QobuzAPIException {
        
        //You could use this template to search any single type in the catalog.
        //It returns counts for all entites an in some case also items for type <> 
        //the required one.
        
        //In case of Artists it also reports Tracks.
        
		
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
		
        try {
                
                String [] typesToChooseFrom=CatalogSearch.suggestedTypes();
                //You could use anyone in the above array (time they add some new, so is just a suggestion) 
                // but to search artist you want to use 'artist'
                
                String type =typesToChooseFrom[0]; //albums
                String query = "Antonio Vivaldi"; //can't use an empty string here.
                
                CatalogSearch q = new CatalogSearch(query, type);
                CatalogFromApi catalog = q.getCatalog();
                
                //System.setOut(new PrintStream(System.out, true, "utf-8"));

                System.out.println("======================================================================");
                System.out.println("= "+type.toUpperCase()+" SEARCH (with pagination)");
                System.out.println("======================================================================");

                Long totalArtist = (long)0;
                Long totalAlbum = (long)0;
                Long totalTrack = (long)0;

                ArtistListFromApi artists = catalog.getArtists();
                AlbumListFromApi albums = catalog.getAlbums();
                TrackListFromApi tracks = catalog.getTracks();
                
                System.out.println( "Query ("+type+"): "+catalog.getQuery());
                
                if (artists!= null) {
                    totalArtist=artists.getTotal();   
                }
                if (albums!= null) {
                    totalAlbum=albums.getTotal();
                }
                if (tracks!= null) {
                    totalTrack=tracks.getTotal(); 
                }
                
                System.out.println( "- Artists: "+totalArtist);
                System.out.println( "- Albums : "+totalAlbum);
                System.out.println( "- Tracks : "+totalTrack);
                
                System.out.println("");
                long size=0;
                
                if (artists!= null) {
                    System.out.println( "- ARTISTS: ");
                    TestUtils.printArtistList(artists,"   ");
                    long itemSize= artists.getItems().size();                   
                    size = itemSize > size ? itemSize : size;
                }
                if (albums!= null) {
                    System.out.println( "- ALBUMS: ");
                    TestUtils.printAlbumList(albums,"   ");
                    long itemSize= albums.getItems().size();
                    size = itemSize > size ? itemSize : size;
                }
                if (tracks!= null) {
                    System.out.println( "- TRACKS: ");
                    TestUtils.printTrackList(tracks,"   ");
                    long itemSize= tracks.getItems().size();
                    size = itemSize > size ? itemSize : size;
                }
                
                int page=1;
                
                boolean stop = (artists == null && albums == null && tracks == null);

                while (!stop ) {
                                       
                    System.out.println("= "+page+" =");
                    
                    long offset= size;
                    
                    q = new CatalogSearch(query, type, offset);
                    CatalogFromApi extra = q.getCatalog();
                    
                    if (artists!= null) {
                        artists.getItems().addAll(extra.getArtists().getItems());
                        System.out.println( "- ARTISTS: ");
                        TestUtils.printArtistList(extra.getArtists(),"   ");
                        long itemSize= artists.getItems().size();                   
                        size = itemSize > size ? itemSize : size;
                    }
                    if (albums!= null) {
                       albums.getItems().addAll(extra.getAlbums().getItems());
                       System.out.println( "- ALBUMS: ");
                       TestUtils.printAlbumList(extra.getAlbums(),"   ");
                       
                       long itemSize= albums.getItems().size();
                       size = itemSize > size ? itemSize : size;
                    }
                    if (tracks!= null) {
                       tracks.getItems().addAll(extra.getTracks().getItems());
                       System.out.println( "- TRACKS: ");
                       TestUtils.printTrackList(extra.getTracks(),"   ");
                       
                       long itemSize= tracks.getItems().size();
                       size = itemSize > size ? itemSize : size;
                    }
                    
                    // Sometime reported total is different than the actual number of records,  
                    // this way keep asking until no more records are reported.
                    
                    stop = (offset == size); 
                    
                    page++;
                    
                } 
                System.out.println("= "+page+" =");
                
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    //@Test
    public void getLabel() throws QobuzAPIException {
		
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);

        try {
                LabelGet q = new LabelGet((long)151717);
                LabelFromApi label= q.getLabel();
                

                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= LABEL (with no Album)                                             =");
                System.out.println("======================================================================");
                
                TestUtils.printLabel(label,""); 
                
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    //@Test
    public void getLabelWithAlbums() throws QobuzAPIException {
        // Deprecated, but still working.
		
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
		
        try {
                LabelGet q = new LabelGet((long)151717,(long)0);
                LabelFromApi label= q.getLabel();
                
                label.completeAlbumList();

                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= LABEL (with complete Album list)                                  =");
                System.out.println("======================================================================");
                
                TestUtils.printLabel(label,""); 
                
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    //@Test
    public void getLabelWithAlbumsAndPagination() throws QobuzAPIException {
        // Deprecated, but still working.
		
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
		
        try {
                LabelGet q = new LabelGet((long)151717,(long)0);
                LabelFromApi label= q.getLabel();
                

                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= LABEL (with Albums and pagination)                                =");
                System.out.println("======================================================================");
                
                TestUtils.printLabel(label,""); 

                long size=0;
                
                if (label!= null) {         
                    size = label.getAlbums().getItems().size();     
                }

                int page=1;
                boolean stop = (label.getAlbums()== null);
                
                while (!stop ) {
                    
                    System.out.println("= "+page+" =");
                    
                    AlbumListFromApi extra = label.loadNextAlbumsPage();
                    stop = (label.getAlbums().getItems().size() == size); 
                    size = label.getAlbums().getItems().size();
                    
                    TestUtils.printAlbumList(extra, "    ");
                    
                    page++;
                } 
                
                System.out.println("= "+page+" =");
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
   //@Test
    public void getLableList() throws QobuzAPIException {
        //very long taking query, better use pagination here.
		
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
		
        try {
                LabelListGet q = new LabelListGet();
                LabelListResult result = q.getLabelListResult();
                
                result.completeLabelsList();

                LabelListFromApi labels = result.getLables();
                
                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= LABEL LIST (complete)                                              =");
                System.out.println("======================================================================");

                TestUtils.printLabelList(labels,""); 
                
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    //@Test
    public void getLableListWithPagination() throws QobuzAPIException {
		
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
		
        try {
                LabelListGet q = new LabelListGet();
                LabelListResult result = q.getLabelListResult();
                
                LabelListFromApi labels = result.getLables();
                
                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= LABEL LIST (with pagination)                                       =");
                System.out.println("======================================================================");

                TestUtils.printLabelList(labels,""); 
                
                int page=1;
                // set an arbitrary limit here, we have > 100K labels...
                int limit = 1000;
                
                long size=0;
                
                if (labels != null) {         
                    size = labels.getItems().size();
                }
                
                boolean stop = (labels == null);
                
                while (!stop ) {
                    
                    System.out.println("= "+page+" =");
                    
                    LabelListFromApi extra  = result.loadNextLabelsPage();
                    stop = (labels.getItems().size() == size || labels.getItems().size() > limit); 
                    size = labels.getItems().size();
                    
                     TestUtils.printLabelList(extra, "");
                    
                    page++;
                } 
                System.out.println("= "+page+" =");
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    //@Test
    public void getGenre() throws QobuzAPIException {
		
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);

        try {
                GenreGet q = new GenreGet((long)22);
                GenreFromApi genre= q.getGenre();

                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= GENRE (with no Album)                                             =");
                System.out.println("======================================================================");
                
                TestUtils.printGenre(genre,""); 
                
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    //@Test
    public void getGenreWithAlbums() throws QobuzAPIException {
        // Deprecated, not working anymore.
		
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
		
        try {
                GenreGet q = new GenreGet((long)22,(long)0);
                GenreFromApi genre= q.getGenre();
                
                genre.completeAlbumList();

                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= GENRE (with complete Album list)                                  =");
                System.out.println("======================================================================");
                
                TestUtils.printGenre(genre,""); 
                
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    //@Test
    public void getGenreList() throws QobuzAPIException {
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
        try {
                Long genre =  (long)10;
                
                GenreListGet q = new GenreListGet(genre);
                GenreListResult result = q.getGenreListResult();
                
                result.completeGenresList();
                
                GenreFromApi parent = result.getParent();
                GenreListFromApi genres = result.getGenres();
                
                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= GENRE LIST (complete)                                              =");
                System.out.println("======================================================================");
                
                TestUtils.printGenre(parent,"");
                TestUtils.printGenreList(genres,""); 
                
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    //@Test
    public void getGenreListWithPagination() throws QobuzAPIException {
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
        try {
                Long genre =  (long)10;
                
                GenreListGet q = new GenreListGet(genre);
                GenreListResult result = q.getGenreListResult();
                
                GenreFromApi parent = result.getParent();
                GenreListFromApi genres = result.getGenres();
                
                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= GENRE LIST (with pagination)                                       =");
                System.out.println("======================================================================");
                
                TestUtils.printGenre(parent,"");
                
                TestUtils.printGenreList(genres,""); 
                
                int page=1;
                long size=0;
                
                if (genres != null) {         
                    size = genres.getItems().size();
                }
                
                boolean stop = (genres == null);
                
                while (!stop) {
                    
                    System.out.println("= "+page+" =");
                    
                    GenreListFromApi extra = result.loadNextGenresPage();
                    stop = (genres.getItems().size() == size); 
                    size = genres.getItems().size();
                    
                    TestUtils.printGenreList(extra, "");
                    
                    page++;
                    
                } 
                System.out.println("= "+page+" =");
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //@Test
    public void getFeaturedAlbumsWithPagination() throws QobuzAPIException {
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
        try {
                String [] typesToChooseFrom=AlbumsGetFeatured.suggestedTypes();
                //should be one in aabove array, but time to time they add some new, so is not mandatory
                
                String type =typesToChooseFrom[1];
                Long genreId = (long)27;
      
                AlbumsGetFeatured q = new AlbumsGetFeatured(type, genreId);
                AlbumList featuredAlbums = q.getFeaturedAlbumResult().getAlbums();
                
                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= FEATURED ALBUMS (with pagination)                                  =");
                System.out.println("======================================================================");
                
                TestUtils.printAlbumList(featuredAlbums,""); 
                
                int page=1;
                long size=0;
                
                if (featuredAlbums!= null) {         
                    size = featuredAlbums.getItems().size();     
                }
                
                boolean stop = (featuredAlbums == null);
                
                while (!stop) {
                    
                    System.out.println("= "+page+" =");
                    
                    q = new AlbumsGetFeatured(type, genreId,(long)featuredAlbums.getItems().size());
                    AlbumList extra = q.getFeaturedAlbumResult().getAlbums();
					
					ArrayList<Album> a = (ArrayList<Album>)featuredAlbums.getItems();
					ArrayList<Album> b = (ArrayList<Album>)extra.getItems();
					a.addAll(b);

                    stop = (featuredAlbums.getItems().size() == size); 
                    size = featuredAlbums.getItems().size();
                    
                    TestUtils.printAlbumList(extra,""); 
                    
                    page++;
                    
                } 
                System.out.println("= "+page+" =");
                System.out.println("======================================================================");
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    //@Test
    public void getAlbum() throws QobuzAPIException {
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
        try {
                AlbumGet q = new AlbumGet("0822189023645");
                AlbumFromApi album = q.getAlbum();
                album.completeTrackList();
                
                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= ALBUM                                                              =");
                System.out.println("======================================================================");
                
                TestUtils.printAlbum (album, ""); 
 
                System.out.println("======================================================================");                
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    //@Test
    public void getTrack() throws QobuzAPIException {
		com.mc2.qobuz.api.v02.API.QobuzApiController controller = 	QobuzApiController.findIstance();
		QobuzAuth auth = new QobuzAuthFromEnvVar();
		controller.setQobuzAuth(auth);
        try {
                TrackGet q = new TrackGet((long)34520243);
                TrackFromApi track = q.getTrack();
                                
                //System.setOut(new PrintStream(System.out, true, "utf-8"));
                
                System.out.println("======================================================================");
                System.out.println("= TRACK                                                              =");
                System.out.println("======================================================================");
                
                TestUtils.printTrack (track, ""); 
 
                System.out.println("======================================================================");                
                
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

}