package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Ipmap {

    @JsonProperty("attr0")
    private Long attr0;


    public Long getAttr0(){
        return attr0;
    }

    public void setAttr0(Long attr0){
        this.attr0=attr0;
    }



    @Override
    public String toString() {
        return "Ipmap{" +
                "attr0=" + attr0 +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(attr0);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ipmap other = (Ipmap) o;
        return Objects.equals(this.attr0, other.attr0);
    }

}

