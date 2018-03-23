package com.example.jorge.mytestbrq.cars;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.mytestbrq.R;
import com.example.jorge.mytestbrq.data.source.cloud.cars.CarsServiceImpl;
import com.example.jorge.mytestbrq.data.source.cloud.cars.model.Cars;
import com.example.jorge.mytestbrq.detailCar.DetailCarActivity;
import com.example.jorge.mytestbrq.shopping.ShoppingActivity;

import com.example.jorge.mytestbrq.util.EndlessRecyclerViewScrollListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 19/03/2018.
 * Fragment for Show Cars with Adapter
 */

public class CarsFragment extends Fragment implements CarsContract.View {

    public static String EXTRA_CAR_ID = "CAR_ID";

    private CarsContract.UserActionsListener mActionsListener;
    LinearLayoutManager mLinearLayoutManager;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private CarsAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "RECYCLER_VIEW_STATE";
    private final String KEY_ADAPTER_STATE = "ADAPTER_STATE";
    private Parcelable mListState;

    private ArrayList<Cars> mListCars;

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

        if (savedInstanceState == null) {
            mListAdapter = new CarsAdapter(new ArrayList<Cars>(0), mItemListener);
            mActionsListener = new CarsPresenter(new CarsServiceImpl(), this);

            mActionsListener.loadingCars();
            mActionsListener.start();
        }

    }

    @Override
    public void onResume() {
        super.onResume();


        if (mBundleRecyclerViewState != null) {
            mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mListCars = (ArrayList<Cars>) mBundleRecyclerViewState.getSerializable(KEY_ADAPTER_STATE);

        }

       // mActionsListener.loadingCars();
       // mActionsListener.start();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mActionsListener.result(requestCode, resultCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cars, container, false);

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
            mListCars = (ArrayList<Cars>) mBundleRecyclerViewState.getSerializable(KEY_ADAPTER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
            mListAdapter = new CarsAdapter(mListCars,mItemListener);
            mRecyclerView.setAdapter(mListAdapter);

        }

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

        // save RecyclerView state
/*        mBundleRecyclerViewState = new Bundle();
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);*/

        mBundleRecyclerViewState = new Bundle();
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mListCars = (ArrayList<Cars>) mListAdapter.mCars;
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
        mBundleRecyclerViewState.putSerializable(KEY_ADAPTER_STATE, (Serializable) mListCars);

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
        if (mRecyclerView.getAdapter().getItemCount() > 0) {
            carsList.addAll((List<Cars>) mListAdapter.mCars);
        }
        mListAdapter.replaceData(carsList);
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
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mScrollListener = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mActionsListener.loadingCars();
                mActionsListener.start();

            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

    }

    @Override
    public void setPresenter(CarsContract.UserActionsListener presenter) {
        mActionsListener = checkNotNull(presenter);
    }

    /**
     * Adapter for fill the list of the car
     */
    private class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {

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

            int imageDimension =
                    (int) viewHolder.carImage.getContext().getResources().getDimension(R.dimen.image_size);

            Picasso.with(viewHolder.carImage.getContext())
                    .load(cars.getImagem())
                    .resize(imageDimension,imageDimension)
                    .onlyScaleDown()
                    .centerInside()
                    .error(R.drawable.ic_error_black_24dp)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(viewHolder.carImage);

            viewHolder.description.setText(cars.getNome());
            viewHolder.price.setText(getResources().getString(R.string.format_value) + Integer.toString(cars.getPreco()));
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
            public TextView description;
            public TextView price;
            private ItemListener mItemListener;

            public Button carShopping;
            public Button carDetail;

            public ViewHolder(View itemView, ItemListener listener) {
                super(itemView);
                mItemListener = listener;
                description= (TextView) itemView.findViewById(R.id.tv_description);
                price= (TextView) itemView.findViewById(R.id.tv_price);
                carImage = (ImageView) itemView.findViewById(R.id.im_car_image);
                carShopping = (Button) itemView.findViewById(R.id.iv_shopping_car);
                carDetail = (Button) itemView.findViewById(R.id.iv_detail_car);

                carShopping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showShopping();
                    }
                });

                carDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = getAdapterPosition();
                        Cars cars = getItem(position);
                        mItemListener.onCarsClick(cars);
                        showDetail(cars.getId());


                    }
                });

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {



            }
        }
    }

    @Override
    public void showShopping() {
        Intent intent = new Intent(getContext(), ShoppingActivity.class);
        startActivityForResult(intent, ShoppingActivity.REQUEST_FINALIZE_SHOPPING);
    }


    @Override
    public void showDetail(int carId) {
        Intent intent = new Intent(getContext(), DetailCarActivity.class);
        intent.putExtra(EXTRA_CAR_ID, Integer.toString(carId));
        getContext().startActivity(intent);
    }

    public interface ItemListener {

        void onCarsClick(Cars clickedNote);
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    @Override
    public void showShoppingEmpty() {
        showMessage(getString(R.string.shopping_empty));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}

