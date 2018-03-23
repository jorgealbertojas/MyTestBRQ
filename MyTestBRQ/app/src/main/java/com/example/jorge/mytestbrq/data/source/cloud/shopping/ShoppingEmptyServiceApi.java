package com.example.jorge.mytestbrq.data.source.cloud.shopping;

import com.example.jorge.mytestbrq.data.Purchase;
import com.example.jorge.mytestbrq.data.source.cloud.cars.CarsServiceApi;
import com.example.jorge.mytestbrq.data.source.cloud.cars.model.Cars;

import java.util.List;

/**
 * Created by jorge on 22/03/2018.
 */

public interface ShoppingEmptyServiceApi {
    /**
     * Interface for signature Cars Service Callback
     * @param <T>
     */
    interface ShoppingEmptyCallback<T> {

        void onEmpty(List<Purchase> purchaseList);
    }

    void getShoppingEmpty(CarsServiceApi.CarsServiceCallback<List<Cars>> callback);


}

