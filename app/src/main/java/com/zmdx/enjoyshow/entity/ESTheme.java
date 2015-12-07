package com.zmdx.enjoyshow.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhangyan on 15/11/27.
 */
public class ESTheme implements Serializable {

    String mBgUrl;
    /**
     * 主题介绍
     */
    String mDesc;
    /**
     * @deprecated
     */
    String mDetailImageUrl;

    long mEndTime;
    String mId;

    @Deprecated
    String mInsideDetailImageUrl;
    boolean needValidate;
    long mStartTime;
    String mStatus;
    String mTag;
    String mTitle;
    String awardSetting;
    String rule;
    String notice;

    // 详情页内的cover图url
    String insideBgUrl;

    public String getInsideBgUrl() {
        return insideBgUrl;
    }

    public void setInsideBgUrl(String insideBgUrl) {
        this.insideBgUrl = insideBgUrl;
    }

    public String getAwardSetting() {
        return awardSetting;
    }

    public void setAwardSetting(String awardSetting) {
        this.awardSetting = awardSetting;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

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

    @Deprecated
    public String getmDetailImageUrl() {
        return mDetailImageUrl;
    }

    @Deprecated
    public void setmDetailImageUrl(String mDetailImageUrl) {
        this.mDetailImageUrl = mDetailImageUrl;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    @Deprecated
    public String getmInsideDetailImageUrl() {
        return mInsideDetailImageUrl;
    }

    @Deprecated
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
                ESTheme theme = convertByJSON(obj);
                list.add(theme);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ESTheme convertByJSON(JSONObject obj) {
        ESTheme theme = new ESTheme();
        theme.setmBgUrl(obj.optString("bgUrl"));
        theme.setmDesc(obj.optString("descs"));
        theme.setmDetailImageUrl(obj.optString("detailImageUrl"));
        theme.setmEndTime(obj.optLong("endtime"));
        theme.setmId(obj.optString("id"));
        theme.setmInsideDetailImageUrl("insideDetailImageUrl");
        theme.setNeedValidate(obj.optString("isNeedValidate").equals("0")); // 0是需要,1是不需要
        theme.setmStartTime(obj.optLong("starttime"));
        theme.setmStatus(obj.optString("status")); // 0结束,1进行中,2开始
        theme.setmTag(obj.optString("tag"));
        theme.setmTitle(obj.optString("themeTitle"));
        theme.setAwardSetting(obj.optString("awardSetting"));
        theme.setRule(obj.optString("role"));
        theme.setNotice(obj.optString("notice"));
        theme.setInsideBgUrl(obj.optString("insideBgUrl"));
        return theme;
    }
}
