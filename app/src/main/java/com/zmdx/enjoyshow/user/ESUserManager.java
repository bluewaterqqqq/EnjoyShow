package com.zmdx.enjoyshow.user;

import com.zmdx.enjoyshow.entity.ESUser;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ESUserManager {

    private static ESUserManager INSTANCE;

    private ESUserManager() {

    }

    public synchronized static ESUserManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ESUserManager();
        }
        return INSTANCE;
    }

    public String getCurrentUserId() {
        return getCurrentUser().getId() + "";
    }

    public void login() {

    }

    public void register() {

    }

    public void modifyPwd() {

    }

    public void setCurrentUser() {

    }

    public ESUser getCurrentUser() {
        ESUser user = new ESUser();
        user.setHeadPortrait(
                "http://wx.qlogo"
                        + ".cn/mmopen/eZbpr3s49Ws8j2FVkJGV3miad5W1hQPBoEZ549WHNZX0htO7zZlbYMlgKFOal7Ue2RcLy1Eu0151sAvXfdTibK5iawY5NK3YVAP/0");
        user.setUserName("至美");
        user.setId(1);
        return user;
    }
}
