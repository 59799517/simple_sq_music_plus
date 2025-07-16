package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Mvdata {

    @JsonProperty("typ")
    private Long typ;


    public Long getTyp(){
        return typ;
    }

    public void setTyp(Long typ){
        this.typ=typ;
    }



    @Override
    public String toString() {
        return "Mvdata{" +
                "typ=" + typ +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(typ);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mvdata other = (Mvdata) o;
        return Objects.equals(this.typ, other.typ);
    }

}

