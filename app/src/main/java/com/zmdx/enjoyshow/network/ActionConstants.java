package com.zmdx.enjoyshow.network;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ActionConstants {

    private static final String CONTEXT = "draftServer/";

    // 拉取最新、最热图集，参数：category=0 最新、category=1 最热
    public static final String ACTION_QUERY_PHOTO_WALL = CONTEXT + "photo_queryPhotosWall.action";
    public static final String ACTION_VIEW_PICSET = CONTEXT + "photo_viewPictureSet.action";
}
