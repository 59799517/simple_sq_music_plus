/**
  * Copyright 2025 bejson.com 
  */
package com.sqmusicplus.v3.plug.apple.entity.searchsongresult;

import java.util.List;
import java.util.Date;

/**
 * Auto-generated: 2025-10-13 14:52:9
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Attributes {

    private String albumName;
    private String artistName;
    private Artwork artwork;
    private String audioLocale;
    private List<String> audioTraits;
    private String composerName;
    private int discNumber;
    private long durationInMillis;
    private List<String> genreNames;
    private boolean hasLyrics;
    private boolean hasTimeSyncedLyrics;
    private boolean isAppleDigitalMaster;
    private boolean isMasteredForItunes;
    private boolean isVocalAttenuationAllowed;
    private String isrc;
    private String name;
    private PlayParams playParams;
    private List<Previews> previews;
    private Date releaseDate;
    private int trackNumber;
    private String url;
    public void setAlbumName(String albumName) {
         this.albumName = albumName;
     }
     public String getAlbumName() {
         return albumName;
     }

    public void setArtistName(String artistName) {
         this.artistName = artistName;
     }
     public String getArtistName() {
         return artistName;
     }

    public void setArtwork(Artwork artwork) {
         this.artwork = artwork;
     }
     public Artwork getArtwork() {
         return artwork;
     }

    public void setAudioLocale(String audioLocale) {
         this.audioLocale = audioLocale;
     }
     public String getAudioLocale() {
         return audioLocale;
     }

    public void setAudioTraits(List<String> audioTraits) {
         this.audioTraits = audioTraits;
     }
     public List<String> getAudioTraits() {
         return audioTraits;
     }

    public void setComposerName(String composerName) {
         this.composerName = composerName;
     }
     public String getComposerName() {
         return composerName;
     }

    public void setDiscNumber(int discNumber) {
         this.discNumber = discNumber;
     }
     public int getDiscNumber() {
         return discNumber;
     }

    public void setDurationInMillis(long durationInMillis) {
         this.durationInMillis = durationInMillis;
     }
     public long getDurationInMillis() {
         return durationInMillis;
     }

    public void setGenreNames(List<String> genreNames) {
         this.genreNames = genreNames;
     }
     public List<String> getGenreNames() {
         return genreNames;
     }

    public void setHasLyrics(boolean hasLyrics) {
         this.hasLyrics = hasLyrics;
     }
     public boolean getHasLyrics() {
         return hasLyrics;
     }

    public void setHasTimeSyncedLyrics(boolean hasTimeSyncedLyrics) {
         this.hasTimeSyncedLyrics = hasTimeSyncedLyrics;
     }
     public boolean getHasTimeSyncedLyrics() {
         return hasTimeSyncedLyrics;
     }

    public void setIsAppleDigitalMaster(boolean isAppleDigitalMaster) {
         this.isAppleDigitalMaster = isAppleDigitalMaster;
     }
     public boolean getIsAppleDigitalMaster() {
         return isAppleDigitalMaster;
     }

    public void setIsMasteredForItunes(boolean isMasteredForItunes) {
         this.isMasteredForItunes = isMasteredForItunes;
     }
     public boolean getIsMasteredForItunes() {
         return isMasteredForItunes;
     }

    public void setIsVocalAttenuationAllowed(boolean isVocalAttenuationAllowed) {
         this.isVocalAttenuationAllowed = isVocalAttenuationAllowed;
     }
     public boolean getIsVocalAttenuationAllowed() {
         return isVocalAttenuationAllowed;
     }

    public void setIsrc(String isrc) {
         this.isrc = isrc;
     }
     public String getIsrc() {
         return isrc;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setPlayParams(PlayParams playParams) {
         this.playParams = playParams;
     }
     public PlayParams getPlayParams() {
         return playParams;
     }

    public void setPreviews(List<Previews> previews) {
         this.previews = previews;
     }
     public List<Previews> getPreviews() {
         return previews;
     }

    public void setReleaseDate(Date releaseDate) {
         this.releaseDate = releaseDate;
     }
     public Date getReleaseDate() {
         return releaseDate;
     }

    public void setTrackNumber(int trackNumber) {
         this.trackNumber = trackNumber;
     }
     public int getTrackNumber() {
         return trackNumber;
     }

    public void setUrl(String url) {
         this.url = url;
     }
     public String getUrl() {
         return url;
     }

}