package com.zmdx.enjoyshow.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ESUser implements Serializable {
    private String age = "";
    private String gender;//性别 0未知 1男 2女
    private String headPortrait;//头像
    private int id;
    private String loginName;
    private String orderId;
    private int orgId;
    private int praise;
    private int report;
    private String userName = "";
    private int votes;

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
            esUser.setLoginName(obj.optString("loginName"));
            esUser.setOrderId(obj.optString("orderId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return esUser;
    }

    public static List<ESUser> convertByJSONArray(JSONArray array) {
        List<ESUser> result = new ArrayList<ESUser>();
        if (array != null) {
            int length = array.length();
            for (int i = 0; i < length; i++) {
                try {
                    ESUser user = ESUser.convertByJSON(array.getString(i));
                    result.add(user);
                } catch (JSONException e) {
                    // ignore
                }
            }
        }
        return result;
    }
}
