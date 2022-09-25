package com.minimaldev.android.quotezy.network;

import android.util.Log;

import com.minimaldev.android.quotezy.fragment.FirstFragment;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

public class URLRequestCallbackImpl extends UrlRequest.Callback {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    WritableByteChannel channels = Channels.newChannel(bos);
    FirstFragment fragment;
    public URLRequestCallbackImpl(FirstFragment fragment) {
        this.fragment = fragment;
    }

    private static final String TAG = URLRequestCallbackImpl.class.getName();
    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) throws Exception {
        Log.i(TAG, "onRedirectReceived method called.");
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) throws Exception {
        Log.i(TAG, "onResponseStarted method called.");
        request.read(ByteBuffer.allocateDirect(10240000));
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) throws Exception {
        Log.i(TAG, "onReadCompleted method called. ");
        byteBuffer.flip();
        channels.write(byteBuffer);
        byteBuffer.clear();
        request.read(byteBuffer);
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        Log.i(TAG, "onSucceeded method called.");
        this.fragment.setQuoteTagResponse(bos.toString());
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
        Log.e(TAG, "Request failed due to: ", error);
    }
}
