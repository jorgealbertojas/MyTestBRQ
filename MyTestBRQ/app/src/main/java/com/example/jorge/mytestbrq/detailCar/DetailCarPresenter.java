package com.example.jorge.mytestbrq.detailCar;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.jorge.mytestbrq.Injection;
import com.example.jorge.mytestbrq.data.Purchase;
import com.example.jorge.mytestbrq.data.source.ShoppingRepository;
import com.example.jorge.mytestbrq.data.source.cloud.detailCar.DetailCarServiceApi;
import com.example.jorge.mytestbrq.data.source.cloud.detailCar.model.DetailCar;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 19/03/2018.
 * Presenter for implement contract the Detail the car, call service API
 */

public class DetailCarPresenter implements DetailCarContract.UserActionsListener {

    private DetailCarServiceApi mDetailCarServiceApi;
    private DetailCarContract.View mDetailCarContractView;
    private Context mContext;

    private String mShoppingId;
    private DetailCar mDetailCar;
    private static ShoppingRepository mShoppingRepository;

    public DetailCarPresenter(DetailCarServiceApi mDetailCarServiceApi, DetailCarContract.View mDetailCarContract_View, Context context) {
        this.mContext = context;
        this.mDetailCarContractView = mDetailCarContract_View;
        this.mDetailCarServiceApi = mDetailCarServiceApi;
    }

    public DetailCarPresenter(@Nullable ShoppingRepository shoppingRepository,
                                   @Nullable DetailCarContract.View DetailCarView ,
                                   @Nullable String shoppingId, @Nullable DetailCar detailCar ) {
        mShoppingId = shoppingId;
        mShoppingRepository = checkNotNull(shoppingRepository,"tasksRepository cannot be null!");
        mDetailCar = detailCar;

    }

    @Override
    public void savePurchase(String carId, String carName, String carDescription, String carBrand, String quantity, String price, String image, String id) {
        createPurchase(carId, carName, carDescription, carBrand, quantity, price, image);
    }

    @Override
    public void loadingDetailCar() {
        mDetailCarContractView.setLoading(true);
        mDetailCarServiceApi.getDetailCar(new DetailCarServiceApi.DetailCarServiceCallback<DetailCar>(){

            @Override
            public void onLoaded(DetailCar detailCar) {
                mDetailCarContractView.setLoading(false);
                mDetailCarContractView.showDetailCar(detailCar);

                // Create the presenter
                new DetailCarPresenter(
                        Injection.provideShoppingRepository(mContext),
                        mDetailCarContractView, Integer.toString(detailCar.getId()), detailCar);
            }
        });

    }

    private void createPurchase(String carId, String carName, String carDescription, String carBrand, String quantity, String price, String image) {
        Purchase newPurchase  = new Purchase(carId, carName, carDescription, carBrand, quantity, price, image, null);

        mShoppingRepository.savePurchase(newPurchase);

    }

}

