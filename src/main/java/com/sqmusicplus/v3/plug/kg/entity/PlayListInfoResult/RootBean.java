package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RootBean {

    @JsonProperty("data")
    private Data data;
    @JsonProperty("status")
    private Long status;
    @JsonProperty("error_code")
    private Long errorCode;


    public Data getData(){
        return data;
    }

    public void setData(Data data){
        this.data=data;
    }

    public Long getStatus(){
        return status;
    }

    public void setStatus(Long status){
        this.status=status;
    }

    public Long getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(Long errorCode){
        this.errorCode=errorCode;
    }



    @Override
    public String toString() {
        return "RootBean{" +
                "data=" + data +
                ", status=" + status +
                ", errorCode=" + errorCode +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(data, status, errorCode);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RootBean other = (RootBean) o;
        return Objects.equals(this.data, other.data) && Objects.equals(this.status, other.status) && Objects.equals(this.errorCode, other.errorCode);
    }

}

