package com.example.jorge.mytestbrq.detailCar;

import com.example.jorge.mytestbrq.data.source.cloud.detailCar.model.DetailCar;

/**
 * Created by jorge on 19/03/2018.
 * Contract for set function in Detail car
 */

public interface DetailCarContract {


    interface View {

        void setLoading(boolean isAtivo);

        void showDetailCar(DetailCar detailCarList);

    }

    interface UserActionsListener {

        void savePurchase(String carId, String carName, String carDescription, String carBrand, String quantity, String price, String image, String id);


        void loadingDetailCar();


    }
}
