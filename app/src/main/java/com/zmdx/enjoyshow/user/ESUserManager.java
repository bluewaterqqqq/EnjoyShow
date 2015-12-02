package com.zmdx.enjoyshow.user;

import com.zmdx.enjoyshow.common.ESPreferences;
import com.zmdx.enjoyshow.entity.ESUser;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ESUserManager {

    private static ESUserManager INSTANCE;

    private ESUserManager() {

    }

    private ESUser mUser;

    public synchronized static ESUserManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ESUserManager();
        }
        return INSTANCE;
    }

    public String getCurrentUserId() {
        if (mUser != null) {
            return mUser.getId() + "";
        }
        return "0";
    }

    public void setAutoLogin() {
        ESPreferences.saveLoginStatus(ESPreferences.STATUS_LOGIN);
    }

    public void saveUserInfo(String user) {
        ESPreferences.saveLoginStatus(ESPreferences.STATUS_LOGIN);
        ESPreferences.saveUserInfo(user);
        mUser = ESUser.convertByJSON(user);
    }

    public void exitLogin() {
        mUser = null;
        ESPreferences.saveLoginStatus(ESPreferences.STATUS_UNLOGIN);
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
