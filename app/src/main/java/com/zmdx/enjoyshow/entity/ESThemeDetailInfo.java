package com.zmdx.enjoyshow.entity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangyan on 15/11/29.
 */
public class ESThemeDetailInfo implements Serializable {
    private List<ESPhoto> hotestPhotoList;
    private List<ESPhoto> newestPhotoList;
    private List<ESUser> userRankList;
    private boolean isUserAttented;
    private int leftVotes;
    private ESTheme mTheme;

    public List<ESPhoto> getHotestPhotoList() {
        return hotestPhotoList;
    }

    public ESTheme getmTheme() {
        return mTheme;
    }

    public void setmTheme(ESTheme mTheme) {
        this.mTheme = mTheme;
    }

    public void setHotestPhotoList(List<ESPhoto> hotestPhotoList) {
        this.hotestPhotoList = hotestPhotoList;
    }

    public List<ESPhoto> getNewestPhotoList() {
        return newestPhotoList;
    }

    public void setNewestPhotoList(List<ESPhoto> newestPhotoList) {
        this.newestPhotoList = newestPhotoList;
    }

    public List<ESUser> getUserRankList() {
        return userRankList;
    }

    public void setUserRankList(List<ESUser> userRankList) {
        this.userRankList = userRankList;
    }

    public boolean isUserAttented() {
        return isUserAttented;
    }

    public void setUserAttented(boolean userAttented) {
        isUserAttented = userAttented;
    }

    public int getLeftVotes() {
        return leftVotes;
    }

    public void setLeftVotes(int leftVotes) {
        this.leftVotes = leftVotes;
    }

    public static ESThemeDetailInfo convertByJSON(JSONObject json) {
        ESThemeDetailInfo result = new ESThemeDetailInfo();
        result.setHotestPhotoList(ESPhoto.convertListByJSON(json.optJSONArray("psList")));
        result.setNewestPhotoList(ESPhoto.convertListByJSON(json.optJSONArray("psRank")));
        result.setUserRankList(ESUser.convertByJSONArray(json.optJSONArray("userRank")));
        result.setUserAttented(json.optInt("isUserAttented") == 1); // 0未参加, 1已参加
        result.setLeftVotes(json.optInt("votes"));
        result.setmTheme(ESTheme.convertByJSON(json.optJSONObject("theme")));
        return result;
    }
}
