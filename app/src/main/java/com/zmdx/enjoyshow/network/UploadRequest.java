/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.ParseError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhangyan on 15/12/2.
 */
public class UploadRequest extends MultiPartRequest<JSONObject> {

    private static final String PROTOCOL_CHARSET = "utf-8";

    public UploadRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorlistener) {
        super(Method.POST, url, listener, errorlistener);

    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
