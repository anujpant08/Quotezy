package com.minimaldev.android.quotezy.service;

import static com.minimaldev.android.quotezy.constants.Constants.HOST_URL;
import static com.minimaldev.android.quotezy.constants.Constants.HTTPS;
import static com.minimaldev.android.quotezy.constants.Constants.QUOTE_COUNT;
import static com.minimaldev.android.quotezy.constants.Constants.RANDOM;
import static com.minimaldev.android.quotezy.constants.Constants.SORT_BY;
import static com.minimaldev.android.quotezy.constants.Constants.TAGS;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.minimaldev.android.quotezy.fragment.FirstFragment;
import com.minimaldev.android.quotezy.network.URLRequestCallbackImpl;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuoteServiceImpl {
    private static final String TAG = QuoteServiceImpl.class.getName();
    public static CronetEngine.Builder myBuilder;
    public static CronetEngine cronetEngine;
    private final Context context;
    private final Fragment fragment;
    private static Executor executor;
    static {
         executor = Executors.newSingleThreadExecutor();
    }
    public QuoteServiceImpl(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        myBuilder = new CronetEngine.Builder(this.context);
        cronetEngine = myBuilder.build();
    }
    public void getQuoteTags(){
        try{
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme(HTTPS)
                    .authority(HOST_URL)
                    .appendPath(TAGS)
                    .appendQueryParameter(SORT_BY, QUOTE_COUNT);
            UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                    uriBuilder.toString(), new URLRequestCallbackImpl((FirstFragment) fragment), executor);
            UrlRequest request = requestBuilder.build();
            request.start();
        } catch (Exception e){
            Log.e(TAG, "An exception occurred while getting all tags : ", e);
            throw e;
        }
    }
    public void getQuotesByTag(String tagValue){
        try{
            tagValue = tagValue.toLowerCase();
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme(HTTPS)
                    .authority(HOST_URL)
                    .appendPath(RANDOM)
                    .appendQueryParameter(TAGS, tagValue);
            UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                    uriBuilder.toString(), new URLRequestCallbackImpl((FirstFragment) fragment), executor);
            UrlRequest request = requestBuilder.build();
            request.start();
        } catch (Exception e){
            Log.e(TAG, "An exception occurred while getting quote by tag : " + tagValue, e);
            throw e;
        }
    }
}
