package com.zmdx.enjoyshow.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ESPhoto {

    private String comments;
    private String coverUrl;
    private String descs;
    private String id; // 即 pictureSetId
    private String orderId;
    private int photoCount;//图集包含的图片个数
//    private int picSetId;
    private int praise;//点赞数
    private int rank;//图集热度（在照片墙的 最热 栏使用）
    private int report;//图集被举报次数（超过10次不显示）
    private int themeCycleId;
    private int tread;
    private int type;
    private String uploadDate;
    private ESUser user;
    private String userId;
    private int views;
    private int votes;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
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

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

//    public int getPicSetId() {
//        return picSetId;
//    }
//
//    public void setPicSetId(int picSetId) {
//        this.picSetId = picSetId;
//    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public int getThemeCycleId() {
        return themeCycleId;
    }

    public void setThemeCycleId(int themeCycleId) {
        this.themeCycleId = themeCycleId;
    }

    public int getTread() {
        return tread;
    }

    public void setTread(int tread) {
        this.tread = tread;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public ESUser getUser() {
        return user;
    }

    public void setUser(ESUser user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public static ESPhoto convertByJSON(JSONObject obj) {
        ESPhoto photo = new ESPhoto();
            photo.setComments(obj.optString("comments"));
            photo.setCoverUrl(obj.optString("coverUrl"));
            photo.setDescs(obj.optString("descs"));
            photo.setId(obj.optString("id"));
            photo.setOrderId(obj.optString("orderId"));
//            photo.setPicSetId(obj.optInt("pictureSetId"));
            photo.setPraise(obj.optInt("praise"));
            photo.setRank(obj.optInt("rank"));
            photo.setPhotoCount(obj.optInt("photoCount"));
            photo.setReport(obj.optInt("report"));
            photo.setThemeCycleId(obj.optInt("themeCycleId"));
            photo.setTread(obj.optInt("tread"));
            photo.setType(obj.optInt("type"));
            photo.setUploadDate(obj.optString("uploadDate"));
            photo.setUser(ESUser.convertByJSON(obj.optString("user")));
            photo.setUserId(obj.optString("userid"));
            photo.setViews(obj.optInt("view"));
            photo.setVotes(obj.optInt("votes"));


        return photo;
    }

    public static List<ESPhoto> convertListByJSON(JSONArray array) {
        List<ESPhoto> list = new ArrayList<ESPhoto>();
            int size = array.length();
            for (int i=0;i<size;i++) {
                JSONObject obj = array.optJSONObject(i);
                ESPhoto photo = convertByJSON(obj);
                list.add(photo);
            }
        return list;
    }
}
