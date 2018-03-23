package com.example.jorge.mytestbrq.cars;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.jorge.mytestbrq.data.Purchase;
import com.example.jorge.mytestbrq.data.source.cloud.cars.CarsServiceApi;
import com.example.jorge.mytestbrq.data.source.cloud.cars.model.Cars;
import com.example.jorge.mytestbrq.shopping.ShoppingActivity;

import java.util.List;

/**
 * Created by jorge on 19/03/2018.
 * Presenter for implements Contract Cars
 */

public class CarsPresenter implements CarsContract.UserActionsListener {

    private final CarsServiceApi mCarsServiceApi;
    private final CarsContract.View mCarsContractView;


    public CarsPresenter(CarsServiceApi mCarsServiceApi,CarsContract.View mCarsContract_View) {
        this.mCarsContractView = mCarsContract_View;
        this.mCarsServiceApi = mCarsServiceApi;
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        if (ShoppingActivity.REQUEST_FINALIZE_SHOPPING == requestCode && Activity.RESULT_OK == resultCode) {
            mCarsContractView.showSuccessfullySavedMessage();
        }else if (ShoppingActivity.REQUEST_FINALIZE_SHOPPING == requestCode && ShoppingActivity.REQUEST_EMPTY_SHOPPING == resultCode) {
            mCarsContractView.showShoppingEmpty();

        }
    }

    /**
     * Loading the car call Service Api with data
     */
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

    @Override
    public void start() {
       // loadShopping(false);
    }
}
