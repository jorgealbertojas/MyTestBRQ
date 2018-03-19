package com.example.jorge.mytestbrq.cars;

import android.support.annotation.NonNull;

import com.example.jorge.mytestbrq.data.source.cloud.cars.model.Cars;

import java.util.List;

/**
 * Created by jorge on 19/03/2018.
 * Contract View and Present for Cars
 */

public interface CarsContract {

    interface View {

        void setLoading(boolean isActive);

        void showCars(List<Cars> carsList);

        void showAllShopping();
    }

    interface UserActionsListener {

        void loadingCars();

        void openDetail(@NonNull Cars cars);
    }
}
