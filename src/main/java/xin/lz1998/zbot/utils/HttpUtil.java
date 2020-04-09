package xin.lz1998.zbot.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpUtil {
    private static OkHttpClient client = new OkHttpClient();

    /**
     * 发送HTTP GET请求，得到字符串
     *
     * @param url 请求地址
     * @return 返回字符串
     * @throws IOException
     */
    public static String getString(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
