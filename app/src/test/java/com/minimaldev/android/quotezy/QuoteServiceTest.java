package com.minimaldev.android.quotezy;

import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.openMocks;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuoteServiceTest {

    @Mock
    Context context;

    @Before
    public void setup(){
        openMocks(this);
    }

    @Test
    public void contextTest(){
        assertFalse(context == null);
    }
}