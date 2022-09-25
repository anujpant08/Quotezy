package com.minimaldev.android.quotezy;

import static com.minimaldev.android.quotezy.constants.Constants.HOST_URL;
import static com.minimaldev.android.quotezy.constants.Constants.HTTPS;
import static com.minimaldev.android.quotezy.constants.Constants.RANDOM;
import static com.minimaldev.android.quotezy.constants.Constants.SUCCESS;
import static com.minimaldev.android.quotezy.constants.Constants.TAGS;

import android.content.Context;
import android.net.Uri;

import androidx.test.platform.app.InstrumentationRegistry;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.minimaldev.android.quotezy.fragment.FirstFragment;
import com.minimaldev.android.quotezy.network.URLRequestCallbackImpl;
import com.minimaldev.android.quotezy.service.QuoteServiceImpl;

import java.util.concurrent.Executors;

@RunWith(MockitoJUnitRunner.class)
public class QuoteServiceInstrumentedTest {

    private Context appContext;

    QuoteServiceImpl quoteService;

    @Mock
    CronetEngine.Builder cronetBuilder;

    @Mock
    CronetEngine cronetEngine;

    @Mock
    URLRequestCallbackImpl urlRequestCallback;

    @Mock
    UrlRequest.Builder urlRequestBuilder;

    @Mock
    UrlRequest urlRequest;

    @Mock
    FirstFragment fragment;

    @Before
    public void setup(){
        openMocks(this);
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.minimaldev.android.quotezy", appContext.getPackageName());
        quoteService = new QuoteServiceImpl(appContext, fragment);
    }

    @Test
    public void quoteServiceTest() {
        //mock
        Uri dummyBuilder = new Uri.Builder()
                .scheme(HTTPS)
                .authority(HOST_URL)
                .appendPath(RANDOM)
                .appendQueryParameter(TAGS, SUCCESS)
                .build();
        when(cronetBuilder.build()).thenReturn(cronetEngine);
        when(cronetEngine.newUrlRequestBuilder(dummyBuilder.toString(),
                urlRequestCallback, Executors.newSingleThreadExecutor()))
                .thenReturn(urlRequestBuilder);
        when(urlRequestBuilder.build()).thenReturn(urlRequest);
        //service call
        quoteService.getQuotesByTag(SUCCESS);
        //verify
        verify(urlRequest, times(1)).start();
    }
}