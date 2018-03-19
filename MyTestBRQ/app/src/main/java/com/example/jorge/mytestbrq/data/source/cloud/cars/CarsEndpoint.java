package com.example.jorge.mytestbrq.data.source.cloud.cars;

import com.example.jorge.mytestbrq.data.source.cloud.cars.model.Cars;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jorge on 19/03/2018.
 * EndPoint for access data list Cars for Json Retrofit
 */

public interface CarsEndpoint {
    @GET("/v1/carro")
    Call<List<Cars>> getCars() ;
}
