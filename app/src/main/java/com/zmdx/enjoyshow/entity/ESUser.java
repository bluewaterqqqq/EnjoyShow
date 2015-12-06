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
    private String gender = "";//性别 0未知 1男 2女
    private String headPortrait;//头像
    private int id;
    private String loginName;
    private String orderId;
    private int orgId;
    private int praise;
    private int report;
    private String userName = "";
    private int votes;
    private String addr;
    private String introdution;
    private String isValidate;
    private String telephone;
    private boolean isAttention;
    private String realName;

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
            esUser.setAddr(obj.optString("address"));
            esUser.setTelephone(obj.optString("telephone"));
            esUser.setIsAttention(obj.optInt("isAttention") == 1);
            esUser.setIsValidate(obj.optString("isvalidate"));
            esUser.setIntrodution(obj.optString("introduction"));
            esUser.setRealName(obj.optString("name"));
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

    public static JSONObject convert2JSON(ESUser user) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("gender", user.getGender());
            obj.put("headPortrait", user.getHeadPortrait());
            obj.put("id", user.getId());
            obj.put("username", user.getUserName());
            obj.put("name", user.getRealName());
            obj.put("age", user.getAge());
            obj.put("address", user.getAddr());
            obj.put("loginame", user.getLoginName());
            obj.put("introduction", user.getIntrodution());
            obj.put("isvalidate", user.getIsValidate());
            obj.put("orgId", user.getOrgId());
            obj.put("praise", user.getPraise());
            obj.put("report", user.getReport());
        } catch (JSONException e) {
            // ignore
        }
        return obj;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIsValidate() {
        return isValidate;
    }

    public void setIsValidate(String isValidate) {
        this.isValidate = isValidate;
    }

    public boolean isAttention() {
        return isAttention;
    }

    public void setIsAttention(boolean isAttention) {
        this.isAttention = isAttention;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getIntrodution() {
        return introdution;
    }

    public void setIntrodution(String introdution) {
        this.introdution = introdution;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    /**
     * 0未知 1男 2女
     *
     * @return
     */
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
}
