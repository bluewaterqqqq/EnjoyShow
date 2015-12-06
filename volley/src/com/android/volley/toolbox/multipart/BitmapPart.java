/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.android.volley.toolbox.multipart;

/*
 * Copyright (C) 2015 zhangyan, Inc. All Rights Reserved.
 */

import android.graphics.Bitmap;
import android.text.TextUtils;

import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.android.volley.misc.MultipartUtils.BINARY;
import static com.android.volley.misc.MultipartUtils.COLON_SPACE;
import static com.android.volley.misc.MultipartUtils.CRLF_BYTES;
import static com.android.volley.misc.MultipartUtils.FORM_DATA;
import static com.android.volley.misc.MultipartUtils.HEADER_CONTENT_DISPOSITION;
import static com.android.volley.misc.MultipartUtils.HEADER_CONTENT_TRANSFER_ENCODING;
import static com.android.volley.misc.MultipartUtils.HEADER_CONTENT_TYPE;

/**
 * Created by zhangyan on 15/12/5.
 */
public class BitmapPart extends BasePart {

    private byte[] mValuesBytes;

    public BitmapPart(String name, Bitmap bmp) {
        if (TextUtils.isEmpty(name) || bmp == null) {
            throw new IllegalArgumentException("参数不能为null");
        }

        final String partName = com.android.volley.toolbox.multipart.UrlEncodingHelper.encode(name, HTTP.DEFAULT_PROTOCOL_CHARSET);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        mValuesBytes = baos.toByteArray();

        headersProvider = new BasePart.IHeadersProvider() {
            public String getContentDisposition() {
                return String.format(HEADER_CONTENT_DISPOSITION + COLON_SPACE + FORM_DATA, partName);
            }
            public String getContentType() {
                return HEADER_CONTENT_TYPE + COLON_SPACE + HTTP.DEFAULT_CONTENT_TYPE;
                //"Content-Type: " + HTTP.PLAIN_TEXT_TYPE + HTTP.CHARSET_PARAM + partCharset;  //$NON-NLS-1$
            }
            public String getContentTransferEncoding() {
                return HEADER_CONTENT_TRANSFER_ENCODING + COLON_SPACE + BINARY;
            }
        };
    }

    @Override
    public long getContentLength(Boundary boundary) {
        return getHeader(boundary).length + mValuesBytes.length + CRLF_BYTES.length;
    }

    @Override
    public void writeTo(OutputStream out, Boundary boundary) throws IOException {
        out.write(getHeader(boundary));
        out.write(mValuesBytes);
        out.write(CRLF_BYTES);
    }
}

