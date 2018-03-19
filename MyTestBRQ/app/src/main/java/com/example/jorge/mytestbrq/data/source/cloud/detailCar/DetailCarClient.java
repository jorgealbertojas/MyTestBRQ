package com.example.jorge.mytestbrq.data.source.cloud.detailCar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jorge on 19/03/2018.
 * Client for call data with retrofit
 */

public class DetailCarClient {
    public static final String BASE_URL = "http://desafiobrq.herokuapp.com/";

    private static Retrofit retrofit = null;

    private static Gson gson = new GsonBuilder()
            .create();

    /**
     * This function get Retrofit for get Json
     * @return
     */
    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
