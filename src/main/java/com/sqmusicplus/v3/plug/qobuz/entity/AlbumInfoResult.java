package com.sqmusicplus.v3.plug.qobuz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname AlbumInfoResult
 * @Description Qobuz专辑详情结果
 * @Version 1.0.0
 * @Date 2026/5/7
 * @Created by Lingma
 */
@NoArgsConstructor
@Data
public class AlbumInfoResult {
    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("image")
    private Image image;
    @JsonProperty("artist")
    private Artist artist;
    @JsonProperty("release_date_original")
    private String releaseDateOriginal;
    @JsonProperty("tracks_count")
    private Integer tracksCount;
    @JsonProperty("upc")
    private String upc;
    @JsonProperty("hires")
    private Boolean hires;
    @JsonProperty("hires_streamable")
    private Boolean hiresStreamable;
    @JsonProperty("tracks")
    private List<TrackDTO> tracks;

    @NoArgsConstructor
    @Data
    public static class Image {
        @JsonProperty("small")
        private String small;
        @JsonProperty("thumbnail")
        private String thumbnail;
        @JsonProperty("large")
        private String large;
        @JsonProperty("back")
        private String back;
    }

    @NoArgsConstructor
    @Data
    public static class Artist {
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
    }

    @NoArgsConstructor
    @Data
    public static class TrackDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("version")
        private String version;
        @JsonProperty("duration")
        private Integer duration;
        @JsonProperty("track_number")
        private Integer trackNumber;
        @JsonProperty("isrc")
        private String isrc;
        @JsonProperty("maximum_bit_depth")
        private Integer maximumBitDepth;
        @JsonProperty("maximum_sampling_rate")
        private Double maximumSamplingRate;
        @JsonProperty("hires")
        private Boolean hires;
        @JsonProperty("hires_streamable")
        private Boolean hiresStreamable;
        @JsonProperty("streamable")
        private Boolean streamable;
        @JsonProperty("downloadable")
        private Boolean downloadable;
        @JsonProperty("parental_warning")
        private Boolean parentalWarning;
    }
}
