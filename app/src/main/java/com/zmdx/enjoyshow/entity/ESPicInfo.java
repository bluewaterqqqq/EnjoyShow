package com.zmdx.enjoyshow.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyan on 15/11/29.
 */
public class ESPicInfo {
    String id;
    String url;
    String pictureSetId;
    String type;
    long date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPictureSetId() {
        return pictureSetId;
    }

    public void setPictureSetId(String pictureSetId) {
        this.pictureSetId = pictureSetId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;

    public static List<ESPicInfo> convertByJSON(JSONArray json) {
        List<ESPicInfo> result = new ArrayList<ESPicInfo>();
        int length = json.length();
        for (int i=0;i<length; i++) {
            JSONObject obj = json.optJSONObject(i);
            if (obj != null) {
                ESPicInfo p = new ESPicInfo();
                p.setDate(obj.optLong("uploadDate"));
                p.setId(obj.optString("id"));
                p.setPictureSetId(obj.optString("pictureSetId"));
                p.setType(obj.optString("type"));
                p.setUrl(obj.optString("photoUrl"));
                p.setUserId(obj.optString("userId"));
                result.add(p);
            }
        }
        return result;
    }
}
