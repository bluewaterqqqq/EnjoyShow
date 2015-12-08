/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.protocol;

import com.zmdx.enjoyshow.utils.LogHelper;

/**
 * Created by zhangyan on 15/12/7.
 */
public class VShowUri {

    public static final String PROTOCOL = "vshow://";

    public static final String HOST = "vshow.com";

    public static final String ACTION_THEME = "theme";
    public static final String ACTION_NOTI = "notification";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_PICDETAIL = "pictureSetDetail";
    public static final String ACTION_WEBVIEW = "webview";

    private String mProtocol;

    private String mHost;

    private String mAction;

    private String mParam;

    private String mValue;

    public String getmProtocol() {
        return mProtocol;
    }

    public String getmHost() {
        return mHost;
    }

    public String getmAction() {
        return mAction;
    }

    public String getmParam() {
        return mParam;
    }

    public String getmValue() {
        return mValue;
    }

    public VShowUri() {
    }

    public VShowUri parse(String url) throws VShowParserException {
        if (url.contains("?")) {
            String[] result2 = url.split("[?]");
            if (result2.length == 2) {
                String[] hosts = result2[0].split("://");
                if (hosts.length == 2) {
                    mProtocol = hosts[0];
                    String[] hostAction = hosts[1].split("/");
                    if (hostAction.length == 2) {
                        mHost = hostAction[0];
                        mAction = hostAction[1];
                    } else {
                        throw new VShowParserException();
                    }
                } else {
                    throw new VShowParserException();
                }

                String[] params = result2[1].split("=");
                if (params.length >= 2) {
                    mParam = params[0];
                    mValue = params[1];
                } else {
                    throw new VShowParserException();
                }
            } else {
                throw new VShowParserException();
            }
        } else {
            String[] result = url.split("://");
            if (result.length == 2) {
                mProtocol = result[0];
                String[] hostAction = result[1].split("/");
                if (hostAction.length == 2) {
                    mHost = hostAction[0];
                    mAction = hostAction[1];
                } else {
                    throw new VShowParserException();
                }
            } else {
                throw new VShowParserException();
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "action=" + mAction + ", paramName=" + mParam + ", paramValue=" + mValue;
    }

    public static class VShowParserException extends Exception {
        public VShowParserException() {
            super("非法vshow url");
        }
    }
}
