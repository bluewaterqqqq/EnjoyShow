package com.zmdx.enjoyshow.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * Created by zhangyan on 15/11/21.
 */
public class ESComment {
    private String content;
    private String date;
    private String id;
    private String parentUserId;
    private String picSetId;
    private ESUser user;
    private String lastId;

    //    private String userId; // 已废弃
//    private String userName; // 已废弃

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(String parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getPicSetId() {
        return picSetId;
    }

    public void setPicSetId(String picSetId) {
        this.picSetId = picSetId;
    }

    public ESUser getUser() {
        return user;
    }

    public void setUser(ESUser user) {
        this.user = user;
    }


    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }

    public static List<ESComment> convertByJSON(String comments) {
        List<ESComment> list = new ArrayList<ESComment>();
        if (!TextUtils.isEmpty(comments)) {
            try {
                JSONArray array = new JSONArray(comments);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    if (obj != null) {
                        ESComment comment = new ESComment();
                        comment.setContent(obj.optString("content"));
                        comment.setDate(obj.optString("datetime"));
                        comment.setId(obj.optString("id"));
                        comment.setParentUserId(obj.optString("parentUserId"));
                        comment.setPicSetId(obj.optString("pictureSetId"));
                        comment.setUser(ESUser.convertByJSON(obj.optString("user")));
                        comment.setLastId(obj.optString("orderId"));
                        list.add(comment);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // ignore
            }
        }

        return list;
    }
}
