package com.example.jorge.mytestbrq.cars;

import android.support.annotation.NonNull;

import com.example.jorge.mytestbrq.BasePresenter;
import com.example.jorge.mytestbrq.BaseView;
import com.example.jorge.mytestbrq.data.source.cloud.cars.model.Cars;
import com.example.jorge.mytestbrq.shopping.ShoppingContract;

import java.util.List;

/**
 * Created by jorge on 19/03/2018.
 * Contract View and Present for Cars
 */

public interface CarsContract {

    interface View extends BaseView<UserActionsListener> {

        void setLoading(boolean isActive);

        void showCars(List<Cars> carsList);

        void showAllShopping();

        void showSuccessfullySavedMessage();

        void showShopping();
    }

    interface UserActionsListener extends BasePresenter {

        void loadingCars();

        void openDetail(@NonNull Cars cars);

        void result(int requestCode, int resultCode);
    }

}
