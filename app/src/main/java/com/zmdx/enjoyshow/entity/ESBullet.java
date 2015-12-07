/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baidu on 15/12/7.
 */
public class ESBullet implements Serializable {

    private String id;

    private boolean display;

    private String imageUrl;

    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static ArrayList<ESBullet> convertByJson(JSONArray data) {
        ArrayList<ESBullet> result = new ArrayList<ESBullet>(data.length());
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.optJSONObject(i);
            if (obj != null) {
                ESBullet item = new ESBullet();
                item.setUrl(obj.optString("url"));
                item.setDisplay(obj.optInt("display") == 0); // 0显示 1不显示, 暂客户端无用,都是显示
                item.setImageUrl(obj.optString("imageUrl"));
                result.add(item);
            }
        }

        return result;
    }
}
