package com.zmdx.enjoyshow.network;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ActionConstants {

    private static final String CONTEXT = "draftServer/";

    public static final String ACTION_QUERY_PHOTO_WALL = CONTEXT + "photo_queryPhotosWall.action";

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

    public static final String ACTION_REGISTER = CONTEXT + "user_register.action";

    public static final String ACTION_GET_CODE = CONTEXT + "user_createCaptcha.action";

    public static final String ACTION_LOGIN = CONTEXT + "user_login.action";

    public static final String ACTION_THIRD_LOGIN = CONTEXT + "user_thirdPartyLogin.action";

    public static final String ACTION_QUERY_PERSONAL_PHOTOS = CONTEXT + "photo_queryPersonalPhotos.action"; // 用户的图集

    public static final String ACTION_FOLLOWED_USERS = CONTEXT + "user_queryAttentions.action"; // 用户关注的人

    public static final String ACTION_UNFOLLOW = CONTEXT + "user_cancelAttention.action"; // 取消关注

    public static final String ACTION_FOLLOW = CONTEXT + "user_attention.action"; // 关注

    public static final String ACTION_QUERY_FANS = CONTEXT + "user_queryFans.action"; // 我的粉丝

    public static final String ACTION_USER_INFO = CONTEXT + "user_loadUserInfo.action"; // 用户详情

    public static final String ACTION_LOAD_NOTIFY = CONTEXT + "photo_loadNotify.action"; // 通知列表

    public static final String ACTION_UPLOAD_PHOTO = CONTEXT + "user_uploadPhoto.action"; // 通知列表

    public static final String ACTION_UPLOAD_INFO = CONTEXT + "user_perfectInformation.action"; // 通知列表
}
