package com.example.jorge.mytestbrq.data.source.cloud.detailCar;

import com.example.jorge.mytestbrq.data.source.cloud.detailCar.model.DetailCar;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by jorge on 19/03/2018.
 * Endpoint for get detail the Car with parameter ID of the car
 */

public interface DetailCarEndpoint {
    @GET("/v1/carro/{id}")
    Call<DetailCar> getDetailCar(@Path("id") String id) ;
}
