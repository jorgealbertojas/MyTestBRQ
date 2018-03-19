package com.example.jorge.mytestbrq.cars;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.mytestbrq.R;
import com.example.jorge.mytestbrq.data.source.cloud.cars.CarsServiceImpl;
import com.example.jorge.mytestbrq.data.source.cloud.cars.model.Cars;
import com.example.jorge.mytestbrq.detailCar.DetailCarActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorge on 19/03/2018.
 * Fragment for Show Cars with Adapter
 */

public class CarsFragment extends Fragment implements CarsContract.View {

    public static String EXTRA_CAR_ID = "CAR_ID";

    private CarsContract.UserActionsListener mActionsListener;

    private CarsAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "RECYCLER_VIEW_STATE";
    private Parcelable mListState;

    /**
     * Constructor
     */
    public CarsFragment() {
    }

    /**
     * Constructor with new Instance
     * @return
     */
    public static CarsFragment newInstance() {
        return new CarsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new CarsAdapter(new ArrayList<Cars>(0), mItemListener);
        mActionsListener = new CarsPresenter(new CarsServiceImpl(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadingCars();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cars, container, false);

        ImageView shopping  = (ImageView) root.findViewById(R.id.iv_shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAllShopping();
            }
        });

        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mActionsListener.loadingCars();
            }
        });


        if (savedInstanceState== null){
            initRecyclerView(root);
            mBundleRecyclerViewState = new Bundle();
            Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        }else{
            initRecyclerView(root);
            mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }

        return root;
    }

    /**
     * Listener which car click
     */
    ItemListener mItemListener = new ItemListener() {
        @Override
        public void onCarsClick(Cars cars) {
            mActionsListener.openDetail(cars);
        }
    };

    @Override
    public void setLoading(final boolean isActive) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(isActive);
            }
        });
    }

    @Override
    public void showCars(List<Cars> carsList) {
        mListAdapter.replaceData(carsList);
    }

    @Override
    public void showAllShopping() {

   /*     Intent intent = new Intent(getActivity(), ShoppingActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PRODUCT, null);

        intent.putExtra(EXTRA_BUNDLE_PRODUCT, bundle);
        startActivity(intent);*/
    }


    /**
     * Init RecyclerView fro show list car
     * @param root
     */
    private void initRecyclerView(View root){
        mRecyclerView= (RecyclerView) root.findViewById(R.id.rv_car_list);
        mRecyclerView.setAdapter(mListAdapter);

        int numColumns = 1;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
    }

    /**
     * Adapter for fill the list of the car
     */
    private static class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {

        private List<Cars> mCars;
        private ItemListener mItemListener;

        public CarsAdapter(List<Cars> cars, ItemListener itemListener) {
            setList(cars);
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.item_car, parent, false);

            return new ViewHolder(noteView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Cars cars = mCars.get(position);

            Picasso.with(viewHolder.carImage.getContext())
                    .load(cars.getImagem())
                    .fit().centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(viewHolder.carImage);

            viewHolder.Description.setText(cars.getNome());
        }

        public void replaceData(List<Cars> notes) {
            setList(notes);
            notifyDataSetChanged();
        }

        private void setList(List<Cars> notes) {
            mCars = notes;
        }

        @Override
        public int getItemCount() {
            return mCars.size();
        }

        public Cars getItem(int position) {
            return mCars.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView carImage;
            public TextView Description;
            private ItemListener mItemListener;

            public ViewHolder(View itemView, ItemListener listener) {
                super(itemView);
                mItemListener = listener;
                Description= (TextView) itemView.findViewById(R.id.tv_description);
                carImage = (ImageView) itemView.findViewById(R.id.im_car_image);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Cars cars = getItem(position);
                mItemListener.onCarsClick(cars);

                Intent intent = new Intent(v.getContext(), DetailCarActivity.class);


                intent.putExtra(EXTRA_CAR_ID, Integer.toString(cars.getId()));

                v.getContext().startActivity(intent);
            }
        }
    }

    public interface ItemListener {

        void onCarsClick(Cars clickedNote);
    }
}

