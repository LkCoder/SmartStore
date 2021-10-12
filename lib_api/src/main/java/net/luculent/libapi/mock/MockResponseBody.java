package net.luculent.libapi.mock;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;

/**
 *
 * @Description:     mock数据拦截器
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 13:31
 */
public class MockResponseBody extends ResponseBody {

    private static final MediaType DEFAULT_MEDIA_TYPE = MediaType.parse("application/json;charset=UTF-8");

    private final String response;

    public MockResponseBody(String response) {
        this.response = response;
    }

    @Override
    public MediaType contentType() {
        return DEFAULT_MEDIA_TYPE;
    }

    @Override
    public long contentLength() {
        return 0;
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(Okio.source(inputStream()));
    }

    private InputStream inputStream() {
        return new ByteArrayInputStream(response.getBytes());
    }
}
