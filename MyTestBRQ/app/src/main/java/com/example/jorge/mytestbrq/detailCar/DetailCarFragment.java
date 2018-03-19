package com.example.jorge.mytestbrq.detailCar;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.mytestbrq.R;
import com.example.jorge.mytestbrq.data.source.cloud.detailCar.DetailCarServiceImpl;
import com.example.jorge.mytestbrq.data.source.cloud.detailCar.model.DetailCar;
import com.squareup.picasso.Picasso;

/**
 * Created by jorge on 19/03/2018.
 */

public class DetailCarFragment extends Fragment implements DetailCarContract.View {

    private DetailCarContract.UserActionsListener mActionsListener;

    private static String mId;

    private ImageView mCarImage;

    private TextView mCarDescription;

    public DetailCarFragment() {
    }

    public static DetailCarFragment newInstance(String id) {
        mId = id;
        return new DetailCarFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionsListener = new DetailCarPresenter(new DetailCarServiceImpl(mId), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadingDetailCar();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_detail_car, container, false);

        mCarImage = (ImageView) root.findViewById(R.id.im_detail_car_image);
        mCarDescription = (TextView) root.findViewById(R.id.tv_detail_description);


        return root;
    }


    @Override
    public void setLoading(boolean isAtivo) {

    }

    /**
     * Show Detail car in Fragment was select
     * @param detailCarList
     */
    @Override
    public void showDetailCar(DetailCar detailCarList) {
        mCarDescription.setText(detailCarList.getDescricao());

        Picasso.with(mCarDescription.getContext())
                .load(detailCarList.getImagem())
                .fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(mCarImage);

    }


}


