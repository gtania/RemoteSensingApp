package com.example.tania_nikos.remotesensing.Http;

/**
 * Created by Tania-Nikos on 2/2/2017.
 * Call back to call after Http call
 */

public interface AsyncResponse {
    void processFinish(Response response);
}
