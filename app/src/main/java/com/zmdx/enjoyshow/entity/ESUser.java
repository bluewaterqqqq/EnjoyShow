package com.zmdx.enjoyshow.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ESUser {
    private String age = "";
    private String gender;//性别
    private String headPortrait;//头像
    private int id;
    private int orgId;
    private int praise;
    private int report;
    private String userName = "";
    private int votes;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public static ESUser convertByJSON(String user) {
        ESUser esUser = new ESUser();
        try {
            JSONObject obj = new JSONObject(user);
            esUser.setAge(obj.optString("age"));
            esUser.setGender(obj.optString("gender"));
            esUser.setHeadPortrait(obj.optString("headPortrait"));
            esUser.setId(obj.optInt("id"));
            esUser.setOrgId(obj.optInt("orgId"));
            esUser.setPraise(obj.optInt("praise"));
            esUser.setReport(obj.optInt("report"));
            esUser.setUserName(obj.optString("username"));
            esUser.setVotes(obj.optInt("votes"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return esUser;
    }
}
