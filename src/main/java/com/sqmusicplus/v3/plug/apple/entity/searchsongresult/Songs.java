/**
  * Copyright 2025 bejson.com 
  */
package com.sqmusicplus.v3.plug.apple.entity.searchsongresult;

import java.util.List;

/**
 * Auto-generated: 2025-10-13 14:52:9
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Songs {

    private List<Data> data;
    private String href;
    private String next;
    public void setData(List<Data> data) {
         this.data = data;
     }
     public List<Data> getData() {
         return data;
     }

    public void setHref(String href) {
         this.href = href;
     }
     public String getHref() {
         return href;
     }

    public void setNext(String next) {
         this.next = next;
     }
     public String getNext() {
         return next;
     }

}