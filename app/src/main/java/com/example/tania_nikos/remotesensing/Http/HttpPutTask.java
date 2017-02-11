package com.example.tania_nikos.remotesensing.Http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.protocol.HTTP;

import static cz.msebera.android.httpclient.HttpHeaders.USER_AGENT;

/**
 * Created by Tania-Nikos on 9/2/2017.
 */

public class HttpPutTask implements HttpObject {

    /**
     * Url to call
     */
    protected String url;

    /**
     * Data to send
     */
    protected List<NameValuePair> data;

    /**
     * AsyncResponse Callback
     */
    protected AsyncResponse asycResponse;

    /**
     * Constructor
     *
     * @param url
     * @param data
     * @param asycResponse
     */
    public HttpPutTask(String url, List<NameValuePair> data, AsyncResponse asycResponse) {
        this.url = url;
        this.data = data;
        this.asycResponse = asycResponse;
    }

    /**
     * Execute Http task
     *
     * @return Response
     */
    @Override
    public Response execute() {
        try {
            System.out.println("before send");

            HttpClient client = HttpClientBuilder.create().build();
            HttpPut put = new HttpPut(this.url);
            // add header
            put.setHeader("User-Agent", USER_AGENT);
            put.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            //add data
            // UTF-8 Encoding...
            put.setEntity(new UrlEncodedFormEntity(this.data, "UTF-8"));

            //Get streamed response
            HttpResponse httpResponse = client.execute(put);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            Response response = new Response();
            response.status_code = httpResponse.getStatusLine().getStatusCode();
            response.error = httpResponse.getStatusLine().getReasonPhrase();
            response.data = result.toString();

            this.asycResponse.processFinish(response);
            return response;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
