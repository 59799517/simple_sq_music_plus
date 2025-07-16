package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Singerinfo {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("publish")
    private Long publish;
    @JsonProperty("name")
    private String name;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("type")
    private Long type;


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

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getAvatar(){
        return avatar;
    }

    public void setAvatar(String avatar){
        this.avatar=avatar;
    }

    public Long getType(){
        return type;
    }

    public void setType(Long type){
        this.type=type;
    }



    @Override
    public String toString() {
        return "Singerinfo{" +
                "id=" + id +
                ", publish=" + publish +
                ", name='" + name + "'" +
                ", avatar='" + avatar + "'" +
                ", type=" + type +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, publish, name, avatar, type);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Singerinfo other = (Singerinfo) o;
        return Objects.equals(this.id, other.id) && Objects.equals(this.publish, other.publish) && Objects.equals(this.name, other.name) && Objects.equals(this.avatar, other.avatar) && Objects.equals(this.type, other.type);
    }

}

