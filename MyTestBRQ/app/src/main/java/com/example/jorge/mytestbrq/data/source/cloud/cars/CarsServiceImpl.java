package com.example.jorge.mytestbrq.data.source.cloud.cars;

import com.example.jorge.mytestbrq.data.source.cloud.cars.model.Cars;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jorge on 19/03/2018.
 * Service for implement Service Cars
 */

public class CarsServiceImpl implements CarsServiceApi {
        CarsEndpoint mRetrofit;

    /**
     * Constructor
     */
    public CarsServiceImpl(){
            mRetrofit = CarsClient.getClient().create(CarsEndpoint.class);
        }

    /**
     * this function get Cars with callback
     * @param callback
     */
    @Override
        public void getCars(final CarsServiceCallback<List<Cars>> callback) {
            Call<List<Cars>> callCars = mRetrofit.getCars();
            callCars.enqueue(new Callback<List<Cars>>() {
                @Override
                public void onResponse(Call<List<Cars>> call, Response<List<Cars>> response) {
                    if(response.code()==200){
                        List<Cars> resultSearch = response.body();
                        callback.onLoaded(resultSearch);
                    }
                }

                @Override
                public void onFailure(Call<List<Cars>> call, Throwable t) {

                }
            });
        }

    @Override
    public void getCar(String productId, CarsServiceCallback<Cars> callback) {

    }



}
