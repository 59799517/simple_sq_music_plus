package com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Download {

    @JsonProperty("status")
    private Long status;
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("fail_process")
    private Long failProcess;
    @JsonProperty("pay_type")
    private Long payType;


    public Long getStatus(){
        return status;
    }

    public void setStatus(Long status){
        this.status=status;
    }

    public String getHash(){
        return hash;
    }

    public void setHash(String hash){
        this.hash=hash;
    }

    public Long getFailProcess(){
        return failProcess;
    }

    public void setFailProcess(Long failProcess){
        this.failProcess=failProcess;
    }

    public Long getPayType(){
        return payType;
    }

    public void setPayType(Long payType){
        this.payType=payType;
    }



    @Override
    public String toString() {
        return "Download{" +
                "status=" + status +
                ", hash='" + hash + "'" +
                ", failProcess=" + failProcess +
                ", payType=" + payType +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hash(status, hash, failProcess, payType);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Download other = (Download) o;
        return Objects.equals(this.status, other.status) && Objects.equals(this.hash, other.hash) && Objects.equals(this.failProcess, other.failProcess) && Objects.equals(this.payType, other.payType);
    }

}

