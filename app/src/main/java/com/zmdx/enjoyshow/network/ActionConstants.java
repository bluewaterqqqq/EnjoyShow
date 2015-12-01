package com.zmdx.enjoyshow.network;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ActionConstants {

    private static final String CONTEXT = "draftServer/";

    // 拉取最新、最热图集，参数：category=0 最新、category=1 最热
    public static final String ACTION_QUERY_PHOTO_WALL = CONTEXT + "photo_queryPhotosWall.action";

    public static final String ACTION_VIEW_PICSET = CONTEXT + "photo_viewPictureSet.action";

    public static final String ACTION_DETECT_LATEST = CONTEXT + "photo_newestPhotoSet.action";

    public static final String ACTION_DETECT_LOOKUP = CONTEXT + "photo_browsePhotoSet.action";

    public static final String ACTION_IMAGE_DETAIL = CONTEXT + "photo_viewPictureSet.action";

    public static final String ACTION_SEND_COMMENT = CONTEXT + "photo_replyComment.action";

    public static final String ACTION_PRESS_PRAISE = CONTEXT + "photo_praisePhoto.action";

    public static final String ACTION_CANCEL_PRAISE = CONTEXT + "photo_cancelPraisePhoto.action";

    public static final String ACTION_QUERY_THEME = CONTEXT + "photo_queryThemes.action";

    public static final String ACTION_VOTE = CONTEXT + "photo_vote.action";

    public static final String ACTION_THEME_DETAIL = CONTEXT + "photo_loadCycleInfo.action";

    public static final String ACTION_THEME_HOSTEST = CONTEXT + "photo_queryCycleRanking.action";

    public static final String ACTION_THEME_NEWEST = CONTEXT + "photo_queryDraftPhotosWall.action";

    public static final String ACTION_THEME_USERRANK = CONTEXT + "photo_queryUserCycleRanking.action";

    public static final String ACTION_UPLOAD_IMAGES = CONTEXT + "photo_uploadPhoto.action";

}
