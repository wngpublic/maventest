package org.wayne.external;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.pool.BasicNIOConnPool;
import org.wayne.main.MyBasic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyHttpClient implements MyBasic {

    String vurl = "http:/127.0.0.1:5000/statusjson/200/jsonwait";
    String vjson = "{'k1':'v1','k2':'v2','wait':10}";
    HttpClient httpClient = null;

    @Override
    public void shutdown() {

    }

    @Override
    public void init() {
        httpClient = HttpClientBuilder.create().build();
    }

    public void setURL(String vurl) {
        this.vurl = vurl;
    }

    public void setJSON(String vjson) {
        this.vjson = vjson;
    }

    public void testPost1() {
        HttpPost httpPost = new HttpPost(vurl);
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
