package com.zmdx.enjoyshow.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by baidu on 15/11/27.
 */
public class ESTheme {

    String mBgUrl;
    String mDesc;
    String mDetailImageUrl;

    long mEndTime;
    String mId;
    String mInsideDetailImageUrl;
    boolean needValidate;
    long mStartTime;
    String mStatus;
    String mTag;
    String mTitle;


    public void setmEndTime(long mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void setmStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public long getmEndTime() {
        return mEndTime;
    }

    public long getmStartTime() {
        return mStartTime;
    }

    public String getmBgUrl() {
        return mBgUrl;
    }

    public boolean isNeedValidate() {
        return needValidate;
    }

    public void setNeedValidate(boolean needValidate) {
        this.needValidate = needValidate;
    }

    public void setmBgUrl(String mBgUrl) {
        this.mBgUrl = mBgUrl;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmDetailImageUrl() {
        return mDetailImageUrl;
    }

    public void setmDetailImageUrl(String mDetailImageUrl) {
        this.mDetailImageUrl = mDetailImageUrl;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmInsideDetailImageUrl() {
        return mInsideDetailImageUrl;
    }

    public void setmInsideDetailImageUrl(String mInsideDetailImageUrl) {
        this.mInsideDetailImageUrl = mInsideDetailImageUrl;
    }

    /**
     * 0结束,1机型中,2开始
     *
     * @return
     */
    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmTag() {
        return mTag;
    }

    public void setmTag(String mTag) {
        this.mTag = mTag;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public static Collection<ESTheme> convertListByJSON(JSONArray data) {
        List<ESTheme> list = new ArrayList<ESTheme>();
        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);

                ESTheme theme = new ESTheme();
                theme.setmBgUrl(obj.optString("bgUrl"));
                theme.setmDesc(obj.optString("descs"));
                theme.setmDetailImageUrl(obj.optString("detailImageUrl"));
                theme.setmEndTime(obj.optLong("endtime"));
                theme.setmId("id");
                theme.setmInsideDetailImageUrl("insideDetailImageUrl");
                theme.setNeedValidate(obj.optString("isNeedValidate").equals("0")); // 0是需要,1是不需要
                theme.setmStartTime(obj.optLong("starttime"));
                theme.setmStatus(obj.optString("status")); // 0结束,1机型中,2开始
                theme.setmTag(obj.optString("tag"));
                theme.setmTitle(obj.optString("themeTitle"));
                list.add(theme);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}