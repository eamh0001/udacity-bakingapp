package com.eamh.bakingapp.api;

import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.api.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;

/**
 * Created by enmanuel.miron on 25/03/18.
 */

public class CustomRestErrorHandler implements RestErrorHandler {

    public interface Listener {
        void onRestError();
    }

    private Listener listener;

    public CustomRestErrorHandler(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onRestClientExceptionThrown(NestedRuntimeException e) {
        listener.onRestError();
    }
}
