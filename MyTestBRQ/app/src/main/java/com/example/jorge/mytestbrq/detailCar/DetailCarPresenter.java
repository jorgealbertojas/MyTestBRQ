package com.example.jorge.mytestbrq.detailCar;

import com.example.jorge.mytestbrq.data.source.cloud.detailCar.DetailCarServiceApi;
import com.example.jorge.mytestbrq.data.source.cloud.detailCar.model.DetailCar;

/**
 * Created by jorge on 19/03/2018.
 * Presenter for implement contract the Detail the car, call service API
 */

public class DetailCarPresenter implements DetailCarContract.UserActionsListener {

    private final DetailCarServiceApi mDetailCarServiceApi;
    private final DetailCarContract.View mDetailCarContractView;

    public DetailCarPresenter(DetailCarServiceApi mDetailCarServiceApi, DetailCarContract.View mDetailCarContract_View) {
        this.mDetailCarContractView = mDetailCarContract_View;
        this.mDetailCarServiceApi = mDetailCarServiceApi;
    }

    /**
     * Call service Api for get data Detail car
      */

    @Override
    public void loadingDetailCar() {
        mDetailCarContractView.setLoading(true);
        mDetailCarServiceApi.getDetailCar(new DetailCarServiceApi.DetailCarServiceCallback<DetailCar>(){

            @Override
            public void onLoaded(DetailCar detailCar) {
                mDetailCarContractView.setLoading(false);
                mDetailCarContractView.showDetailCar(detailCar);
            }
        });

    }

}

