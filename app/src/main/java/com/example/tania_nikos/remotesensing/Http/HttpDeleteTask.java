package com.example.tania_nikos.remotesensing.Http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpDelete;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

/**
 * Created by Tania-Nikos on 9/2/2017.
 */

public class HttpDeleteTask implements HttpObject {

    /**
     * Url to call
     */
    protected String url;

    /**
     * AsyncResponse Callback
     */
    protected AsyncResponse asycResponse;

    /**
     * Constructor
     *
     * @param url
     * @param asycResponse
     */
    public HttpDeleteTask(String url, AsyncResponse asycResponse) {
        this.url = url;
        this.asycResponse = asycResponse;
    }

    /**
     * Execute Http task
     *
     * @return Response
     */
    @Override
    public Response execute() {

        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete delete = new HttpDelete(this.url);

        try {

            //Get streamed response
            HttpResponse httpResponse = client.execute(delete);
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

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
