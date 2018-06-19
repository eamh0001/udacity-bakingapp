package com.eamh.bakingapp.api;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by enmanuel.miron on 25/03/18.
 */

@EBean(scope = EBean.Scope.Singleton)
public class RestRequester {

    @RestService
    BakingApi bakingApi;

    @Background
    public void getBakingData(){
        bakingApi.getBakingData();
    }
}
