package com.zmdx.enjoyshow.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zmdx.enjoyshow.utils.LogHelper;

/**
 * Created by zhangyan on 15/11/22.
 */
public class PraiseInfo implements Serializable{
    private String age;
    private String gender; // 性别. 0未知 1男 2女
    private String headPortrait;
    private String id;//点赞人的userId
    private String loginName;
    private String orderId;
    private String orgId;
    private int praise;
    private int report;
    private String username;
    private int votes;

    public static List<PraiseInfo> convertByJSON(JSONArray array) {
        List<PraiseInfo> data = new ArrayList<PraiseInfo>();
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                if (obj != null) {
                    PraiseInfo info = new PraiseInfo();
                    info.setAge(obj.optString("age"));
                    info.setGender(obj.optString("gender"));
                    info.setHeadPortrait(obj.optString("headPortrait"));
                    info.setId(obj.optString("id"));
                    info.setLoginName(obj.optString("loginname"));
                    info.setOrderId(obj.optString("orderId"));
                    info.setOrgId(obj.optString("orgId"));
                    info.setPraise(obj.optInt("praise"));
                    info.setReport(obj.optInt("report"));
                    info.setVotes(obj.optInt("votes"));
                    info.setUsername(obj.optString("username"));
                    data.add(info);
                }
            }
        }
        return data;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
