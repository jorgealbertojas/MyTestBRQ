package com.example.jorge.mytestbrq.detailCar;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private Button mAdd;

    private DetailCar mDetailCar;

    public DetailCarFragment() {
    }

    public static DetailCarFragment newInstance(String id) {
        mId = id;
        return new DetailCarFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionsListener = new DetailCarPresenter(new DetailCarServiceImpl(mId), this,getActivity());
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

        mAdd = (Button) root.findViewById(R.id.b_add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.savePurchase(Integer.toString(mDetailCar.getId()),mDetailCar.getNome(), mDetailCar.getDescricao(),mDetailCar.getMarca(),Integer.toString(mDetailCar.getQuantidade()),Integer.toString(mDetailCar.getPreco()),mDetailCar.getImagem(),Integer.toString(mDetailCar.getId()));

            }
        });

        return root;
    }


    @Override
    public void setLoading(boolean isAtivo) {

    }


    @Override
    public void showDetailCar(DetailCar detailCar) {

        mDetailCar = detailCar;

        mCarDescription.setText(detailCar.getDescricao());

        Picasso.with(mCarDescription.getContext())
                .load(detailCar.getImagem())
                .fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(mCarImage);

    }


}


