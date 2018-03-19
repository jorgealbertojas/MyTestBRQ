package com.example.jorge.mytestbrq.cars;

import android.support.annotation.NonNull;

import com.example.jorge.mytestbrq.data.source.cloud.cars.CarsServiceApi;
import com.example.jorge.mytestbrq.data.source.cloud.cars.model.Cars;

import java.util.List;

/**
 * Created by jorge on 19/03/2018.
 */

public class CarsPresenter implements CarsContract.UserActionsListener {

    private final CarsServiceApi mCarsServiceApi;
    private final CarsContract.View mCarsContractView;

    public CarsPresenter(CarsServiceApi mCarsServiceApi,CarsContract.View mCarsContract_View) {
        this.mCarsContractView = mCarsContract_View;
        this.mCarsServiceApi = mCarsServiceApi;
    }

    @Override
    public void loadingCars() {
        mCarsContractView.setLoading(true);
        mCarsServiceApi.getCars(new CarsServiceApi.CarsServiceCallback<List<Cars>>(){
            @Override
            public void onLoaded(List listCars) {
                mCarsContractView.setLoading(false);
                mCarsContractView.showCars(listCars);
            }
        });

    }

    @Override
    public void openDetail(@NonNull Cars cars) {

    }
}
