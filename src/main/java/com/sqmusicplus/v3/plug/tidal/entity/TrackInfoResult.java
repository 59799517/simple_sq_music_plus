package com.sqmusicplus.v3.plug.tidal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname TrackInfoResult
 * @Description Tidal歌曲详情
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@NoArgsConstructor
@Data
public class TrackInfoResult {

    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("replayGain")
    private Double replayGain;
    @JsonProperty("peak")
    private Double peak;
    @JsonProperty("allowStreaming")
    private Boolean allowStreaming;
    @JsonProperty("streamReady")
    private Boolean streamReady;
    @JsonProperty("payToStream")
    private Boolean payToStream;
    @JsonProperty("adSupportedStreamReady")
    private Boolean adSupportedStreamReady;
    @JsonProperty("djReady")
    private Boolean djReady;
    @JsonProperty("stemReady")
    private Boolean stemReady;
    @JsonProperty("streamStartDate")
    private String streamStartDate;
    @JsonProperty("premiumStreamingOnly")
    private Boolean premiumStreamingOnly;
    @JsonProperty("trackNumber")
    private Integer trackNumber;
    @JsonProperty("volumeNumber")
    private Integer volumeNumber;
    @JsonProperty("version")
    private Object version;
    @JsonProperty("popularity")
    private Integer popularity;
    @JsonProperty("copyright")
    private String copyright;
    @JsonProperty("bpm")
    private Integer bpm;
    @JsonProperty("key")
    private String key;
    @JsonProperty("keyScale")
    private String keyScale;
    @JsonProperty("url")
    private String url;
    @JsonProperty("isrc")
    private String isrc;
    @JsonProperty("editable")
    private Boolean editable;
    @JsonProperty("explicit")
    private Boolean explicit;
    @JsonProperty("audioQuality")
    private String audioQuality;
    @JsonProperty("audioModes")
    private List<String> audioModes;
    @JsonProperty("mediaMetadata")
    private MediaMetadataDTO mediaMetadata;
    @JsonProperty("upload")
    private Boolean upload;
    @JsonProperty("accessType")
    private String accessType;
    @JsonProperty("spotlighted")
    private Boolean spotlighted;
    @JsonProperty("artist")
    private ArtistDTO artist;
    @JsonProperty("artists")
    private List<ArtistsDTO> artists;
    @JsonProperty("album")
    private AlbumDTO album;
    @JsonProperty("mixes")
    private MixesDTO mixes;

    @NoArgsConstructor
    @Data
    public static class MediaMetadataDTO {
        @JsonProperty("tags")
        private List<String> tags;
    }

    @NoArgsConstructor
    @Data
    public static class ArtistDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("handle")
        private Object handle;
        @JsonProperty("type")
        private String type;
        @JsonProperty("picture")
        private String picture;
    }

    @NoArgsConstructor
    @Data
    public static class AlbumDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("cover")
        private String cover;
        @JsonProperty("vibrantColor")
        private String vibrantColor;
        @JsonProperty("videoCover")
        private Object videoCover;
    }

    @NoArgsConstructor
    @Data
    public static class MixesDTO {
        @JsonProperty("TRACK_MIX")
        private String trackMix;
    }

    @NoArgsConstructor
    @Data
    public static class ArtistsDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("handle")
        private Object handle;
        @JsonProperty("type")
        private String type;
        @JsonProperty("picture")
        private String picture;
    }
}
