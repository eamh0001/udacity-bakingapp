package com.eamh.bakingapp.api;

import com.eamh.bakingapp.models.Recipe;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enmanuel.miron on 24/03/18.
 */

@Rest(rootUrl = "https://go.udacity.com/android-baking-app-json",
        converters = {GsonHttpMessageConverter.class},
        responseErrorHandler = DefaultResponseErrorHandler.class)
public interface BakingApi extends RestClientErrorHandling{

    @Get("")
    @Accept(MediaType.APPLICATION_JSON)
    List<Recipe> getBakingData();
}
