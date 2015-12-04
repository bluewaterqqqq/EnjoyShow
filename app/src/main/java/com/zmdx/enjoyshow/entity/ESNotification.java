/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangyan on 15/12/5.
 */
public class ESNotification implements Serializable {

    private String coverUrl;
    private String dateTime;
    private String gender;
    private String headPotrait;
    private String id;
    private String orderId;
    private boolean isRead;
    private String picSetId;
    private int type; // 操作类型  4：评论，5：回复，6：@，7：赞，8：关注
    private String userId;
    private String username;

    public static ArrayList<ESNotification> convertByJSONArray(JSONArray data) {
        int length = data.length();
        ArrayList<ESNotification> result = new ArrayList<ESNotification>(length);
        for (int i = 0; i < length; i++) {
            JSONObject obj = data.optJSONObject(i);
            ESNotification n = new ESNotification();
            n.setCoverUrl(obj.optString("coverUrl"));
            n.setDateTime(obj.optString("dateTime"));
            n.setGender(obj.optString("gender"));
            n.setHeadPotrait(obj.optString("headPortrait"));
            n.setId(obj.optString("id"));
            n.setOrderId(obj.optString("orderId"));
            n.setIsRead(obj.optInt("isRead") == 1); // FIXME 还不确定1是否为已读
            n.setPicSetId(obj.optString("pictureSetId"));
            n.setType(obj.optInt("type"));
            n.setUserId(obj.optString("userId"));
            n.setUsername(obj.optString("userName"));
            result.add(n);
        }
        return result;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeadPotrait() {
        return headPotrait;
    }

    public void setHeadPotrait(String headPotrait) {
        this.headPotrait = headPotrait;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getPicSetId() {
        return picSetId;
    }

    public void setPicSetId(String picSetId) {
        this.picSetId = picSetId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
