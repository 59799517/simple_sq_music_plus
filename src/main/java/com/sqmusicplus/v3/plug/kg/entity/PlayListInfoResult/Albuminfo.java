package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Albuminfo {

    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("publish")
    private Long publish;


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public Long getPublish(){
        return publish;
    }

    public void setPublish(Long publish){
        this.publish=publish;
    }



    @Override
    public String toString() {
        return "Albuminfo{" +
                "name='" + name + "'" +
                ", id=" + id +
                ", publish=" + publish +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, id, publish);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Albuminfo other = (Albuminfo) o;
        return Objects.equals(this.name, other.name) && Objects.equals(this.id, other.id) && Objects.equals(this.publish, other.publish);
    }

}

