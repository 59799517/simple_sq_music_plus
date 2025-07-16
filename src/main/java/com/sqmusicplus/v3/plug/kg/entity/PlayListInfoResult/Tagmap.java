package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Tagmap {

    @JsonProperty("genre0")
    private Long genre0;


    public Long getGenre0(){
        return genre0;
    }

    public void setGenre0(Long genre0){
        this.genre0=genre0;
    }



    @Override
    public String toString() {
        return "Tagmap{" +
                "genre0=" + genre0 +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(genre0);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tagmap other = (Tagmap) o;
        return Objects.equals(this.genre0, other.genre0);
    }

}

