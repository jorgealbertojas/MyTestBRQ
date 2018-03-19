package com.example.jorge.mytestbrq.data.source.cloud.detailCar;

import com.example.jorge.mytestbrq.data.source.cloud.detailCar.model.DetailCar;

/**
 * Created by jorge on 19/03/2018.
 * Service Api for call data Detail car
 */

public interface DetailCarServiceApi {
    /**
     * Interface for signature Detail Cars Service Callback
     * @param <T>
     */
    interface DetailCarServiceCallback<T> {

        void onLoaded(DetailCar detailCarsList);
    }

    void getDetailCar(DetailCarServiceApi.DetailCarServiceCallback<DetailCar> callback);

    void getDetailCar(String carId, DetailCarServiceApi.DetailCarServiceCallback<DetailCar> callback);
}

