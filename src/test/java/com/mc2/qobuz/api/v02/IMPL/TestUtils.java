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


import com.mc2.qobuz.api.v02.API.elements.Album;
import com.mc2.qobuz.api.v02.API.elements.Article;
import com.mc2.qobuz.api.v02.API.elements.Artist;
import com.mc2.qobuz.api.v02.API.elements.Award;
import com.mc2.qobuz.api.v02.API.elements.Genre;
import com.mc2.qobuz.api.v02.API.elements.Goody;
import com.mc2.qobuz.api.v02.API.elements.Image;
import com.mc2.qobuz.api.v02.API.elements.Label;
import com.mc2.qobuz.api.v02.API.elements.Track;
import com.mc2.qobuz.api.v02.API.lists.AlbumList;
import com.mc2.qobuz.api.v02.API.lists.ArtistList;
import com.mc2.qobuz.api.v02.API.lists.GenreList;
import com.mc2.qobuz.api.v02.API.lists.LabelList;
import com.mc2.qobuz.api.v02.API.lists.TrackList;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marco
 */
public class TestUtils {
    

    public static void printGenre(Genre genre, String Indent){
        printGenre(genre,Indent,true);
    }
    public static void printGenre(Genre genre, String Indent, Boolean details){
        
        if (!details){
            System.out.println(Indent+"Genre: "+genre.getId()+" - "+genre.getName());
        } else {
            System.out.println(Indent+"Id: "+genre.getId());
            System.out.println(Indent+"Name: "+genre.getName());
            System.out.println(Indent+"Color: "+genre.getColor());
            System.out.println(Indent+"PATH: ");

            for (int i = 0; i < genre.getPath().size(); i++) {
                System.out.println(Indent+" - "+i+": "+genre.getPath().get(i));
            }
            System.out.println(Indent+"Slug: "+genre.getSlug());

            if (genre.getAlbums() != null){
                 System.out.println(Indent+"ALBUMS: ");
                 printAlbumList(genre.getAlbums(), Indent+"    ");
            } 
        }
    }
    public static void printLabel(Label label,String Indent){
        
        printLabel(label, Indent, true);
        
    }
    
    public static void printLabel(Label label,String Indent, boolean detailed){
        
        if (!detailed){
            
           System.out.println(Indent+"Label: "+label.getName()); 
        
        }else {
            
            System.out.println(Indent+"    Id: "+label.getId());
            System.out.println(Indent+"    supplier id: "+label.getSupplier_id());
            System.out.println(Indent+"    albums_count: "+label.getAlbums_count());
            System.out.println(Indent+"    name: "+label.getName());
            System.out.println(Indent+"    slug: "+label.getSlug()); 

            if (label.getImage()!= null){
                 System.out.println(Indent+"    label: "+label.getImage().toString()); 
            }
            System.out.println(Indent+"    description: "+label.getDescription()); 

            if (label.getAlbums() != null){
                 System.out.println(Indent+"ALBUMS: ");
                 printAlbumList(label.getAlbums(), Indent+"    ");
            }
        }
        
       
    }
    public static void printAlbum(Album a, String Indent){
        
        printAlbum(a, Indent, true);
    }
    public static void printAlbum(Album a, String Indent, Boolean detailed){
  
        System.out.println(Indent+"Id: "+a.getId());
        System.out.println(Indent+"Title: "+a.getTitle());
		System.out.println(Indent+"Subtitle: "+a.getSubtitle());

        if (!detailed){
        
            if (a.getComposer() != null){
                System.out.println(Indent+"Composer: "+a.getComposer().getName());
            } 
            if (a.getArtist() != null){
                System.out.println(Indent+"Artist: "+a.getArtist().getName());
            }
            if (a.getDescription() != null){
                System.out.println(Indent+"Description: "+a.getDescription());
            }
            /*
            printSelectedStoreGenre(a.getStore_related(), "GB-en","      ", false);
            printSelectedStoreGenreList(a.getStore_related(), "GB-en","  ");
            */
            if (a.getLabel() != null){
                System.out.println(Indent+"LABEL: ");
                printLabel(a.getLabel(), Indent+" ");
            }
            System.out.println(Indent+"Program: "+a.getProgram());
            System.out.println(Indent+"Copyright: "+a.getCopyright());
            
            if (a.getReleased_at() != null){
                System.out.println(Indent+"Released at: "+new Date(a.getReleased_at()*1000));
            }else{
                System.out.println(Indent+"Released at: "+ a.getReleased_at());       
            }
            
            System.out.println(Indent+"Recording_information: "+a.getRecording_information());

            if (a.getAwards() != null){
                System.out.println(Indent+"AWARDS: ");
                printAwards(a.getAwards(), Indent+" ");
            }
            
            if (a.getImage() != null){
                System.out.println(Indent+"IMAGE: ");
                printImage(a.getImage(), Indent+" ");
            }
            
            
        }
        else {
        
            if (a.getGenre() != null){
                System.out.println(Indent+"GENRE: ");
                printGenre(a.getGenre(), Indent+"   ");
            }

            System.out.println(Indent+"Tracks count: "+a.getTracks_count());

            if (a.getCollection() != null){
                System.out.println(Indent+"Collection: "+a.getCollection().getId()+" - "+a.getCollection().getName());
            } else {
                System.out.println(Indent+"Collection: "+a.getCollection());
            }

            System.out.println(Indent+"Product sales factors weekly: "+a.getProduct_sales_factors_weekly());

            if (a.getGoodies() != null){
                System.out.println(Indent+"GOODIES: ");
                printGoodies(a.getGoodies(), Indent+" ");
            }

            if (a.getComposer() != null){
                System.out.println(Indent+"COMPOSER: ");
                printAlbumArtist(a.getComposer(), Indent+" ");
            } 

            if (a.getArea() != null){
                System.out.println(Indent+"Area: "+a.getArea().getId()+" - "+a.getArea().getName());
            } else {
                System.out.println(Indent+"Area: "+a.getArea());
            }

            System.out.println(Indent+"Description: "+a.getDescription());
            System.out.println(Indent+"Catchline: "+a.getCatchline());

            if (a.getCreated_at() != null){
                System.out.println(Indent+"Created at: "+new Date(a.getCreated_at()*1000));
            } else {
                System.out.println(Indent+"Created at: "+a.getCreated_at());
            } 

            if (a.getGenres_list() != null){
                System.out.println(Indent+"GENRE_LIST: ");
                printAlbumGenreList(a.getGenres_list(), Indent+" ");
            }

            if (a.getArtist() != null){
                System.out.println(Indent+"ARTIST: ");
                printAlbumArtist(a.getArtist(), Indent+" ");
            } 

            System.out.println(Indent+"Product sales factors monthly: "+a.getProduct_sales_factors_monthly());
            System.out.println(Indent+"Popularity: "+a.getPopularity());

            if (a.getArticles() != null){
                System.out.println(Indent+"ARTICLES: ");
                printArticles(a.getArticles(), Indent+" ");
            }
			/*
            if (a.getStore_related() != null){
                System.out.println(Indent+"STORE RELATED: ");
                printStoreRelated(a.getStore_related(), Indent+" ");
            }
			*/
            if (a.getImage() != null){
                System.out.println(Indent+"IMAGE: ");
                printImage(a.getImage(), Indent+" ");
            }

            System.out.println(Indent+"Media count: "+a.getMedia_count());
            System.out.println(Indent+"Product type: "+a.getProduct_type());

            if (a.getLabel() != null){
                System.out.println(Indent+"LABEL: ");
                printLabel(a.getLabel(), Indent+" ");
            }
            if (a.getReleased_at() != null){
                System.out.println(Indent+"Released at: "+new Date(a.getReleased_at()*1000));
            }else{
                System.out.println(Indent+"Released at: "+ a.getReleased_at());       
            }
            
            System.out.println(Indent+"Recording_information: "+a.getRecording_information());
            
            System.out.println(Indent+"Program: "+a.getProgram());
            System.out.println(Indent+"Copyright: "+a.getCopyright());

            if (a.getPeriod() != null){
                System.out.println(Indent+"Period: "+a.getPeriod().getId()+" - "+a.getPeriod().getName());
            } else {
                System.out.println(Indent+"Period at: "+a.getPeriod());
            } 

            System.out.println(Indent+"Product url: "+a.getProduct_url());

            if (a.getUrl() != null){
                System.out.println(Indent+"Url: "+a.getUrl().toString());
            } else {
                System.out.println(Indent+"Url: "+a.getUrl());
            } 

            System.out.println(Indent+"Duration: "+a.getDuration());

            System.out.println(Indent+"Maximum technical specifications: "+a.getMaximum_technical_specifications());

            if (a.getAwards() != null){
                System.out.println(Indent+"AWARDS: ");
                printAwards(a.getAwards(), Indent+" ");
            }

            System.out.println(Indent+"Program: "+a.getProgram());
            System.out.println(Indent+"Slug: "+a.getSlug());
            System.out.println(Indent+"Relative url: "+a.getRelative_url());
            System.out.println(Indent+"Qobuz id: "+a.getQobuz_id());
            System.out.println(Indent+"Product sales factors yearly: "+a.getProduct_sales_factors_yearly());
            System.out.println(Indent+"Purchasable: "+a.getPurchasable());
            System.out.println(Indent+"Streamable: "+a.getStreamable());
			System.out.println(Indent+"Hi Res Streamable: "+a.getHiresStreamable());
            System.out.println(Indent+"Previewable: "+a.getPreviewable());
            System.out.println(Indent+"Sampleable: "+a.getSampleable());
            System.out.println(Indent+"Downloadable: "+a.getDownloadable());
            System.out.println(Indent+"Displayable: "+a.getDisplayable());
			System.out.println(Indent+"Parental warning: "+a.getParentalWarning());

			if (a.getReleased_at() != null){
                System.out.println(Indent+"Released at: "+new Date(a.getReleased_at()*1000));
            }else{
                System.out.println(Indent+"Released at: "+ a.getReleased_at());       
            }
			
            if (a.getPurchasable_at() != null){
                System.out.println(Indent+"Purchasable at: "+new Date(a.getPurchasable_at()*1000));
            } else {
                System.out.println(Indent+"Purchasable at: "+ a.getPurchasable_at());
            }

            if (a.getStreamable_at() != null){
                System.out.println(Indent+"Streamable at: "+new Date(a.getStreamable_at()*1000));
            } else {
                System.out.println(Indent+"Streamable at: "+ a.getStreamable_at());
            }
			
			System.out.println(Indent+"Released for download at: "+a.getReleaseDateDownload());
			System.out.println(Indent+"Released for streaming at: "+a.getReleaseDateStream());
			System.out.println(Indent+"Originally released at: "+a.getReleaseDateOriginal());
			
			
			System.out.println(Indent+"Maximum channel count: "+a.getMaximumChannelCount());
            System.out.println(Indent+"Maximum sampling rate: "+a.getMaximum_sampling_rate());
            System.out.println(Indent+"Maximum bit depth: "+a.getMaximum_bit_depth());
            System.out.println(Indent+"Hires: "+a.getHires());
			System.out.println(Indent+"UPC: "+a.getUpc());
						
            if (a.getTrackList() != null){
                System.out.println(Indent+"TRACKS: ");
                printTrackList(a.getTrackList(), Indent+" ");
            }
        }
    }
    public static void printArtist(Artist artist, String Indent){
        
        System.out.println(Indent+"Id: "+artist.getId());
        
        if (artist.getPicture() !=  null){
            System.out.println(Indent+"Picture: "+artist.getPicture().toString());
        } else{
            System.out.println(Indent+"Picture: "+artist.getPicture());
        }
        System.out.println(Indent+"Albums_as_primary_composer_count: "+artist.getAlbums_as_primary_composer_count());
        System.out.println(Indent+"Albums_count: "+artist.getAlbums_count());
        System.out.println(Indent+"Albums_as_primary_artist_count: "+artist.getAlbums_as_primary_artist_count());
        System.out.println(Indent+"Name: "+artist.getName());

        if (artist.getImage() !=  null){
           System.out.println(Indent+"IMAGE: ");
           printImage(artist.getImage(), Indent+"");
        } else { 
            System.out.println(Indent+"Image: "+artist.getImage());
        }

        System.out.println(Indent+"Slug: "+artist.getSlug());

        if (artist.getBiography()!=  null){
             System.out.println(Indent+"BIOGRAPHY: ");
             System.out.println(Indent+"    Summary: "+artist.getBiography().getSummary());
             System.out.println(Indent+"    Content: "+artist.getBiography().getContent());
        } else { 
            System.out.println(Indent+"biography: "+artist.getBiography());
        }
        /*
        if (artist.getInformation()!=  null){
            System.out.println(Indent+"Information: "+artist.getInformation().toString());
        } else{
            System.out.println(Indent+"Information: "+artist.getInformation());
        }
        */
        if (artist.getAlbums() != null){
             System.out.println(Indent+"ALBUMS: ");
             printAlbumList(artist.getAlbums(), Indent+"    ");
        } 
        
        
    }
    public static void printGenreList(GenreList genreList, String Indent){
        
        System.out.println(Indent+"Offset: "+genreList.getOffset());
        System.out.println(Indent+"Limit: "+genreList.getLimit());
        System.out.println(Indent+"Total: "+genreList.getTotal());
        System.out.println(Indent+"ITEMS: ");
        
        for (int i = 0; i < genreList.getItems().size(); i++) {
            System.out.println(Indent+" - "+i);
            printGenre(genreList.getItems().get(i),"    ");
        } 
    }
    
    public static void printLabelList(LabelList labelList, String Indent){
        
        System.out.println(Indent+"Offset: "+labelList.getOffset());
        System.out.println(Indent+"Limit: "+labelList.getLimit());
        System.out.println(Indent+"Total: "+labelList.getTotal());
        System.out.println(Indent+"ITEMS: ");
        
        for (int i = 0; i < labelList.getItems().size(); i++) {
            System.out.println(Indent+" - "+i);
            printLabel(labelList.getItems().get(i),"    ");
        } 
    }
    public static void printArtistList(ArtistList artistList, String Indent){
        
        System.out.println(Indent+"Offset: "+artistList.getOffset());
        System.out.println(Indent+"Limit: "+artistList.getLimit());
        System.out.println(Indent+"Total: "+artistList.getTotal());
        System.out.println(Indent+"ITEMS: ");
        
        for (int i = 0; i < artistList.getItems().size(); i++) {
            System.out.println(Indent+" - "+i);
            printArtist(artistList.getItems().get(i),Indent+"    ");
        } 
    }
    
    public static void printAlbumList(AlbumList albumList, String Indent){
        printAlbumList(albumList, Indent, true);
    }
     
    public static void printAlbumList(AlbumList albumList, String Indent, Boolean detailed){
        
        System.out.println(Indent+"Offset: "+albumList.getOffset());
        System.out.println(Indent+"Limit: "+albumList.getLimit());
        System.out.println(Indent+"Total: "+albumList.getTotal());
        System.out.println(Indent+"ITEMS: ");
        
        Indent = Indent+"";
        for (int i = 0; i < albumList.getItems().size(); i++) {
            System.out.println(Indent+" - "+i);
            printAlbum(albumList.getItems().get(i), Indent+"   ",detailed);
        } 
    }
    
    
    public static void printGoodies(ArrayList<? extends Goody> goodies, String Indent){
        for (int i = 0; i < goodies.size(); i++) {
            System.out.println(Indent+" - "+i);
            printGoody(goodies.get(i), Indent);
        } 
    }
    public static void printGoody(Goody goody, String Indent){
        System.out.println(Indent+"    Id: "+goody.getId());
        System.out.println(Indent+"    original_url: "+goody.getOriginal_url().toString());
        System.out.println(Indent+"    description: "+goody.getDescription());
        System.out.println(Indent+"    name: "+goody.getName());
        System.out.println(Indent+"    file format id: "+goody.getFile_format_id());
        System.out.println(Indent+"    url: "+goody.getUrl().toString());
       
    }
    public static void printAlbumArtist(Artist artist,String Indent ){
        System.out.println(Indent+"    Id: "+artist.getId());
        
        if (artist.getPicture() !=  null){
            System.out.println(Indent+"    picture: "+artist.getPicture().toString());
        } else{
            System.out.println(Indent+"    picture: "+artist.getPicture());
        }

        System.out.println(Indent+"    albums_count: "+artist.getAlbums_count());
        System.out.println(Indent+"    name: "+artist.getName());
        
        if (artist.getImage() !=  null){
            System.out.println(Indent+"    IMAGE: ");
            printImage(artist.getImage(), Indent);
        } else { 
            System.out.println(Indent+"    image: "+artist.getImage());
        }
        
        System.out.println(Indent+"    slug: "+artist.getSlug());
        
    }
    public static void printImage(Image image, String Indent){
        System.out.println(Indent+"    thumbnail: "+image.getThumbnail());
        System.out.println(Indent+"    small: "+image.getSmall());
        System.out.println(Indent+"    medium: "+image.getLarge());
        System.out.println(Indent+"    large: "+image.getLarge());
        System.out.println(Indent+"    extralarge: "+image.getExtralarge());
        System.out.println(Indent+"    mega: "+image.getMega());
        System.out.println(Indent+"    back: "+image.getBack());
    }
    public static void printAlbumGenreList(ArrayList<String> genrelist, String Indent){
        
        for (int i = 0; i < genrelist.size(); i++) {
            System.out.println(Indent+" - "+i+": "+genrelist.get(i));
        }
         
    }
    public static void printArticles(ArrayList<? extends Article> articles, String Indent){
        for (int i = 0; i < articles.size(); i++) {
            System.out.println(Indent+" - "+i);
            printArticle(articles.get(i), Indent);
        } 
    }
    public static void printArticle(Article article, String Indent){
        System.out.println(Indent+"    Id: "+article.getId());
        System.out.println(Indent+"    price: "+article.getPrice());
        System.out.println(Indent+"    description: "+article.getDescription());
        System.out.println(Indent+"    label: "+article.getLabel());
        System.out.println(Indent+"    type: "+article.getType());
        System.out.println(Indent+"    url: "+article.getUrl());
        System.out.println(Indent+"    currency: "+article.getCurrency());
        if (article.getPromotion()!=  null){
            System.out.println(Indent+"    Promotion: " +article.getPromotion().getId()+" - Original price: "+article.getPromotion().getOriginal_price());
        } else { 
            System.out.println(Indent+"    image: "+article.getPromotion());
        }
       
    }
    /*
    public static void printStoreRelated(ArrayList<StoreRelated> stores, String Indent){
        for (int i = 0; i < stores.size(); i++) {
            printOneStoreRelated(stores.get(i), Indent);
        } 
    }
   
    public static void printOneStoreRelated(StoreRelated store, String Indent){
        System.out.println(Indent+" - "+store.getStore());
        System.out.println(Indent+"    GENRE: ");
        printGenre(store.getGenre(), Indent+"       ");
        System.out.println(Indent+"    GENRE_LIST: ");
        printAlbumGenreList(store.getGenres_list(), Indent+"     ");

    }
	
    public static void printSelectedStoreRelated(ArrayList<StoreRelated> stores, String store, String Indent){
        for (int i = 0; i < stores.size(); i++) {  
            StoreRelated storeRelated = stores.get(i);
            if (storeRelated.getStore().equals(store)){
                
                printOneStoreRelated(stores.get(i), Indent);
            }
        }
    } 
    public static void printSelectedStoreGenre (ArrayList<StoreRelated> stores, String store, String Indent){
        printSelectedStoreGenre(stores, store, Indent, true);
    }
    
    public static void printSelectedStoreGenre (ArrayList<StoreRelated> stores, String store, String Indent, Boolean detailed){
        for (int i = 0; i < stores.size(); i++) {  
            StoreRelated storeRelated = stores.get(i);
            if (storeRelated.getStore().equals(store)){
                printGenre(stores.get(i).getGenre(),Indent,detailed);
            }
        }
    }
    
    public static void printSelectedStoreGenreList(ArrayList<StoreRelated> stores, String store, String Indent){
        for (int i = 0; i < stores.size(); i++) {  
            StoreRelated storeRelated = stores.get(i);
            if (storeRelated.getStore().equals(store)){
                System.out.println(Indent+"    GENRE_LIST: ");
                printAlbumGenreList(stores.get(i).getGenres_list(), Indent+"     ");
            }
        }
    }
    */
    public static void printAwards(ArrayList<? extends Award> awards, String Indent){
        for (int i = 0; i < awards.size(); i++) {
            System.out.println(Indent+" - "+i);
            printAward(awards.get(i), Indent);
        } 
    }
    public static void printAward(Award awards, String Indent){
        System.out.println(Indent+"    Publication name: "+awards.getPublication_name());
        System.out.println(Indent+"    Award Slug: "+awards.getAward_Slug());
        System.out.println(Indent+"    Publication id: "+awards.getPublication_id());
        System.out.println(Indent+"    Publication slug: "+awards.getPublication_slug());
        System.out.println(Indent+"    Name: "+awards.getName());
        System.out.println(Indent+"    Slug: "+awards.getSlug());
        System.out.println(Indent+"    Award id: "+awards.getAward_id());
        System.out.println(Indent+"    Awarded at: "+new Date(awards.getAwarded_at()*1000));
       
    }
    public static void printTrackList(TrackList tracklist, String Indent){
        
        System.out.println(Indent+"Offset: "+tracklist.getOffset());
        System.out.println(Indent+"Limit: "+tracklist.getLimit());
        System.out.println(Indent+"Total: "+tracklist.getTotal());
        System.out.println(Indent+"ITEMS: ");
        
        Indent = Indent+"";
        for (int i = 0; i < tracklist.getItems().size(); i++) {
            System.out.println(Indent+" - "+i);
            printTrack(tracklist.getItems().get(i), Indent+"   ");
        } 
    }
    public static void printTrack (Track track, String Indent){

        System.out.println(Indent+"Id: "+track.getId());

        if (track.getComposer() !=  null){
            System.out.println(Indent+"Composer: "+track.getComposer().getId()+" - "+track.getComposer().getName());
        } else { 
            System.out.println(Indent+"Composer: "+track.getComposer());
        }
		
		System.out.println(Indent+"Work Title: "+track.getWorkTitle());
        System.out.println(Indent+"Title: "+track.getTitle());
		
		System.out.println(Indent+"Work guessed: "+track.geWorkGuessed());
		System.out.println(Indent+"Title only: "+track.getTitleOnly());
		
        System.out.println(Indent+"Duration: "+track.getDuration());
        System.out.println(Indent+"Performers: "+track.getPerformers());
        
		for ( Entry<String,String> entry : track.getPerformersRoleNamesMap().entrySet()){
		
			System.out.println(Indent+"CREDITS: "+entry.getKey()+" - "+entry.getValue());
		}
		
        if (track.getArticles() != null){
            System.out.println(Indent+"ARTICLES: ");
            printArticles(track.getArticles(), Indent+" ");
        }
        
        if (track.getAlbum() != null){
            System.out.println(Indent+"ALBUM: ");
            printAlbum(track.getAlbum(), Indent+"  ");
        }
        
        if (track.getPerformer() !=  null){
            System.out.println(Indent+"Performer: "+track.getPerformer().getId()+" - "+track.getPerformer().getName());
        } else { 
            System.out.println(Indent+"Performer: "+track.getPerformer());
        }
        
        System.out.println(Indent+"Media number: "+track.getMedia_number());
        System.out.println(Indent+"Copyright: "+track.getCopyright());
        System.out.println(Indent+"Track number: "+track.getTrack_number());
        System.out.println(Indent+"Version: "+track.getVersion());
        System.out.println(Indent+"Purchasable: "+track.getPurchasable());
        System.out.println(Indent+"Streamable: "+track.getStreamable());
        System.out.println(Indent+"Previewable: "+track.getPreviewable());
        System.out.println(Indent+"Sampleable: "+track.getSampleable());
        System.out.println(Indent+"Downloadable: "+track.getDownloadable());
        System.out.println(Indent+"Displayable: "+track.getDisplayable());
        
        if (track.getPurchasable_at() !=  null){
            System.out.println(Indent+"Purchasable at: "+new Date(track.getPurchasable_at()*1000));
        } else {
            System.out.println(Indent+"Purchasable at: "+ track.getPurchasable_at());
        }
        
        if (track.getStreamable_at() !=  null){
            System.out.println(Indent+"Streamable at: "+new Date(track.getStreamable_at()*1000));
        } else{
            System.out.println(Indent+"Streamable at: "+ track.getStreamable_at());
        }
        
        System.out.println(Indent+"Maximum sampling rate: "+track.getMaximum_sampling_rate());
        System.out.println(Indent+"Maximum bit depth: "+track.getMaximum_bit_depth());
        System.out.println(Indent+"Hires: "+track.getHires());
		System.out.println(Indent+"ISRC: "+track.getIsrc());
        
    }
}
