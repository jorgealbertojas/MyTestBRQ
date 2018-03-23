package com.example.jorge.mytestbrq.data.source.cloud.detailCar;


import com.example.jorge.mytestbrq.data.source.cloud.detailCar.model.DetailCar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jorge on 19/03/2018.
 * Service for implement Service Cars
 */

public class DetailCarServiceImpl implements DetailCarServiceApi {
    DetailCarEndpoint mRetrofit;
    String mId;

    /**
     * Constructor
     */
    public DetailCarServiceImpl(String id){
        mId = id;
        mRetrofit = DetailCarClient.getClient().create(DetailCarEndpoint.class);
    }

    /**
     * this function get Detail Car with callback
     * @param callback
     */
    @Override
    public void getDetailCar(final DetailCarServiceCallback<DetailCar> callback) {
        Call<DetailCar> callDetailCar = mRetrofit.getDetailCar(mId);
        callDetailCar.enqueue(new Callback<DetailCar>() {
            @Override
            public void onResponse(Call<DetailCar> call, Response<DetailCar> response) {
                if(response.code()==200){
                    DetailCar resultSearch = response.body();
                    callback.onLoaded(resultSearch);
                }else{
                    DetailCar detailCarErroData = new DetailCar();
                    detailCarErroData.setId(0);
                    detailCarErroData.setQuantidade(0);
                    detailCarErroData.setDescricao("0");
                    detailCarErroData.setImagem("0");
                    detailCarErroData.setMarca("0");
                    detailCarErroData.setNome("0");
                    detailCarErroData.setPreco(0);
                    callback.onLoaded(detailCarErroData);
                }
            }

            @Override
            public void onFailure(Call<DetailCar> call, Throwable t) {

            }
        });
    }

    @Override
    public void getDetailCar(String carId, DetailCarServiceCallback<DetailCar> callback) {

    }



}
