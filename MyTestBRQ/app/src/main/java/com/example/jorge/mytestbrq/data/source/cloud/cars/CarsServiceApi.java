package com.example.jorge.mytestbrq.data.source.cloud.cars;

import com.example.jorge.mytestbrq.data.source.cloud.cars.model.Cars;

import java.util.List;

/**
 * Created by jorge on 19/03/2018.
 * Service Api for getCars with callback
 */

public interface CarsServiceApi {

    /**
     * Interface for signature Cars Service Callback
     * @param <T>
     */
    interface CarsServiceCallback<T> {

        void onLoaded(List<Cars> carsList);
    }

    void getCars(CarsServiceCallback<List<Cars>> callback);

    void getCar(String carId, CarsServiceCallback<Cars> callback);
}
