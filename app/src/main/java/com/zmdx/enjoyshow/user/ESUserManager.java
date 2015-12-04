package com.zmdx.enjoyshow.user;

import com.zmdx.enjoyshow.common.ESPreferences;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.utils.LogHelper;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ESUserManager {

    public static final int STATUS_UNLOGIN = 1;
    public static final int STATUS_LOGIN_BY_ES = 2;
    public static final int STATUS_LOGIN_BY_THRID = 4;
    public static final int STATUS_EXPERIENCE = 3;
    private static final String TAG = "ESUserManager";
    private static ESUserManager INSTANCE;
    private ESUser mUser;

    private ESUserManager() {

    }

    public synchronized static ESUserManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ESUserManager();
        }
        return INSTANCE;
    }

    public String getCurrentUserId() {
        mUser = getCurrentUser();
        if (mUser != null) {
            return mUser.getId() + "";
        }
        return null;
    }

    public void setLoginStatus(int status) {
        ESPreferences.saveLoginStatus(status);
    }

    public void saveUserInfo(String user) {
        ESPreferences.saveUserInfo(user);
        mUser = ESUser.convertByJSON(user);
        LogHelper.d(TAG, "保存登录用户信息, user:" + user);
    }

    public void saveUserInfo(ESUser user) {
        String userStr = ESUser.convert2JSON(user).toString();
        saveUserInfo(userStr);
    }

    public void logout() {
        mUser = null;
        ESPreferences.saveLoginStatus(ESUserManager.STATUS_UNLOGIN);
    }

    public ESUser getCurrentUser() {
        if (mUser == null) {
            String userInfo = ESPreferences.getUserInfo();
            if (userInfo != null) {
                mUser = ESUser.convertByJSON(userInfo);
            }
        }
        return mUser;
//        ESUser user = new ESUser();
//        user.setHeadPortrait(
//                "http://wx.qlogo"
//                        + ".cn/mmopen/eZbpr3s49Ws8j2FVkJGV3miad5W1hQPBoEZ549WHNZX0htO7zZlbYMlgKFOal7Ue2RcLy1Eu0151sAvXfdTibK5iawY5NK3YVAP/0");
//        user.setUserName("至美");
//        user.setId(0);
//        return user;
    }
}
