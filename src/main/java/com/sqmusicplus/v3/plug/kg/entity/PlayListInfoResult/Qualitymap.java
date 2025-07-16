package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Qualitymap {

    @JsonProperty("attr0")
    private Long attr0;
    @JsonProperty("attr1")
    private Long attr1;


    public Long getAttr0(){
        return attr0;
    }

    public void setAttr0(Long attr0){
        this.attr0=attr0;
    }

    public Long getAttr1(){
        return attr1;
    }

    public void setAttr1(Long attr1){
        this.attr1=attr1;
    }



    @Override
    public String toString() {
        return "Qualitymap{" +
                "attr0=" + attr0 +
                ", attr1=" + attr1 +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(attr0, attr1);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qualitymap other = (Qualitymap) o;
        return Objects.equals(this.attr0, other.attr0) && Objects.equals(this.attr1, other.attr1);
    }

}

