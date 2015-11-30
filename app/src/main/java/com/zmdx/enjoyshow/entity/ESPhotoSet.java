package com.zmdx.enjoyshow.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.zmdx.enjoyshow.common.ESConfig;

/**
 * Created by zhangyan on 15/11/21.
 */
public class ESPhotoSet {

    private List<ESPicInfo> pics;


    private int commentNum;
    private String coverUrl;
    private String descStr;
    private String id;
    private boolean isUserPraised;
    private boolean praiseStatusChanged = false;
    private String orderId;
    private int photoCount;
    private String rank;
    private int report;
    private String status;
    private String themeCycledId;
    private int thread;
    private String type;
    private String uploadDate;
    private ESUser user;
    private String userId;
    private int view;
    private int votes;
    private List<ESComment> comments;
    private int praiseNum;
    private List<ESUser> praiseList;

    public static ESPhotoSet convertByJSON(JSONObject result) {
        ESPhotoSet photo = new ESPhotoSet();
        JSONObject obj = result.optJSONObject("photoSet");
        if (obj != null) {
            photo.setCommentNum(obj.optInt("comments"));
            photo.setCoverUrl(obj.optString("coverUrl"));
            photo.setDescStr(obj.optString("descs"));
            photo.setId(obj.optString("id"));
            photo.setIsUserPraised(obj.optInt("isUserPraised") == 1);
            photo.setOrderId(obj.optString("orderId"));
            photo.setRank(obj.optString("rank"));
            photo.setReport(obj.optInt("report"));
            photo.setPics(ESPicInfo.convertByJSON(obj.optJSONArray("photoList")));
            photo.setThemeCycledId(obj.optString("themeCycleId"));
            photo.setThread(obj.optInt("tread"));
            photo.setType(obj.optString("type"));
            photo.setUploadDate(obj.optString("uploadDate"));
            photo.setUser(ESUser.convertByJSON(obj.optString("user")));
            photo.setUserId(obj.optString("userid"));
            photo.setView(obj.optInt("view"));
            photo.setVotes(obj.optInt("votes"));
            photo.setCommentNum(obj.optInt("comments"));
            photo.setPraiseNum(obj.optInt("praise"));
            photo.setPraiseList(ESUser.convertByJSONArray(obj.optJSONArray("praiseUserList")));
        }
        photo.setComments(ESComment.convertByJSON(result.optString("comments")));
        if (ESConfig.DEBUG && (photo.getComments() == null || photo.getComments().size() <= 0)) {
            List<ESComment> list = new ArrayList<ESComment>();
            int i = 10;
            while (true) {
                if (i < 0) {
                    break;
                }
                ESComment com = new ESComment();
                com.setDate("" + System.currentTimeMillis());
                com.setContent("今天天气不错");
                ESUser user = new ESUser();
                user.setHeadPortrait(
                        "http://wx.qlogo"
                                + ".cn/mmopen/eZbpr3s49Ws8j2FVkJGV3miad5W1hQPBoEZ549WHNZX0htO7zZlbYMlgKFOal7Ue2RcLy1Eu0151sAvXfdTibK5iawY5NK3YVAP/0");
                user.setUserName("新用户");
                user.setId(Integer.valueOf(photo.getUserId()));
                com.setUser(user);
                list.add(com);
                i--;
            }
            photo.setComments(list);
        }
        return photo;
    }


    public List<ESPicInfo> getPics() {
        return pics;
    }

    public void setPics(List<ESPicInfo> pics) {
        this.pics = pics;
    }

    public void setUserPraised(boolean userPraised) {
        isUserPraised = userPraised;
    }
    
    public boolean isPraiseStatusChanged() {
        return praiseStatusChanged;
    }

    public void setPraiseStatusChanged(boolean praiseStatusChanged) {
        this.praiseStatusChanged = praiseStatusChanged;
    }

    public List<ESUser> getPraiseList() {
        return praiseList;
    }

    public void setPraiseList(List<ESUser> praiseList) {
        this.praiseList = praiseList;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescStr() {
        return descStr;
    }

    public void setDescStr(String descStr) {
        this.descStr = descStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isUserPraised() {
        return isUserPraised;
    }

    public void setIsUserPraised(boolean isUserPraised) {
        this.isUserPraised = isUserPraised;
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

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThemeCycledId() {
        return themeCycledId;
    }

    public void setThemeCycledId(String themeCycledId) {
        this.themeCycledId = themeCycledId;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public List<ESComment> getComments() {
        return comments;
    }

    public void setComments(List<ESComment> comments) {
        this.comments = comments;
    }
}
