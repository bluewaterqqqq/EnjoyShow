package com.zmdx.enjoyshow.user;

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
        return "1";
    }

    public void login() {

    }

    public void register() {

    }

    public void modifyPwd() {

    }

}
