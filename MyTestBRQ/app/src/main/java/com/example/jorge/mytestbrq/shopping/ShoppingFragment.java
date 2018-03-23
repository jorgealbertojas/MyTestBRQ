package com.example.jorge.mytestbrq.shopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jorge.mytestbrq.R;
import com.example.jorge.mytestbrq.data.Purchase;
import com.example.jorge.mytestbrq.util.ScrollChildSwipeRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 20/03/2018.
 * Fragment Show shopping of the client
 */

public class ShoppingFragment  extends Fragment implements ShoppingContract.View {

    private ShoppingContract.Presenter mPresenter;

    private ShoppingAdapter mListAdapter;

    private View mNoShoppingView;

    private LinearLayout mShoppingView;
    private TextView mNoShoppingMainView;
    private TextView mNoShoppingAddView;
    private static ListView mListView;
    private Button mFinalize;

    public static ShoppingFragment newInstance() {
        return new ShoppingFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new ShoppingAdapter(new ArrayList<Purchase>(0), mItemListener);
    }

    /**
     * Listener for clicks on shopping in the ListView.
     */
    ShoppingItemListener mItemListener = new ShoppingItemListener() {
        @Override
        public void onPurchaseClick(Purchase clickedPurchase) {
            mPresenter.openPurchaseDetails(clickedPurchase);

        }

        @Override
        public void onCompletePurchaseClick(Purchase completedPurchase) {
            mPresenter.completePurchase(completedPurchase);
        }

        @Override
        public void onActivatePurchaseClick(Purchase activatedPurchase) {
            mPresenter.activatePurchase(activatedPurchase);
        }

        @Override
        public void onRemovePurchaseClick(String activatedPurchase, String quantity) {
            mPresenter.removeItemShopping(activatedPurchase, quantity);
            if (mListAdapter.getCount() == 1 && quantity.equals("0")){
                showCarsListEmpty();
            }

        }

        @Override
        public void onFinalizeShoppingClick(final String date) {

            LayoutInflater factory = LayoutInflater.from(getContext());
            final View deleteDialogView = factory.inflate(
                    R.layout.custom_dialog, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(getContext()).create();
            deleteDialog.setView(deleteDialogView);

            TextView nTextView = (TextView) deleteDialogView.findViewById(R.id.txt_dia);
            nTextView.setText(getContext().getResources().getString(R.string.conformation));

            deleteDialogView.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mPresenter.finalizeShopping(date);
                    deleteDialog.dismiss();
                }
            });
            deleteDialogView.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();

                }
            });

            deleteDialog.show();

        }
    };



    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shopping, container, false);

        // Set up Shopping view
        mListView = (ListView) root.findViewById(R.id.lv_shopping);
        mListView.setAdapter(mListAdapter);

        mShoppingView = (LinearLayout) root.findViewById(R.id.ll_shopping);
        mFinalize = (Button) root.findViewById(R.id.btn_finalize);

        // Set up  no Shopping view
        mNoShoppingView = root.findViewById(R.id.noShopping);
        // mNoShoppingIcon = (ImageView) root.findViewById(R.id.im_noShoppingIcon);
        mNoShoppingMainView = (TextView) root.findViewById(R.id.tv_noShoppingMain);
        mNoShoppingAddView = (TextView) root.findViewById(R.id.tv_noShoppingAdd);
        mNoShoppingAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPurchase();
            }
        });



        mFinalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mShoppingView.getVisibility() == View.INVISIBLE) {
                    showMessage(getContext().getResources().getString(R.string.shopping_empty));
                }else{

                    Date currentTime = Calendar.getInstance().getTime();

                    mItemListener.onFinalizeShoppingClick((currentTime.toString()));
                }

            }
        });

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );


        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(mListView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadShopping(false);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void showAddPurchase() {
/*        Intent intent = new Intent(getContext(), AddPurchaseActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PRODUCT_SHOPPING, mProduct );
        intent.putExtra(EXTRA_BUNDLE_PRODUCT_SHOPPING, bundle);

        startActivityForResult(intent, AddPurchaseActivity.REQUEST_ADD_PURCHASE);*/
    }

    @Override
    public void setPresenter(ShoppingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }



    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showShopping(List<Purchase> listPurchase) {
        mListAdapter.replaceData(listPurchase);
        mShoppingView.setVisibility(View.VISIBLE);
        mNoShoppingView.setVisibility(View.GONE);
    }

    @Override
    public void showPurchaseDetailsUi(String shoppingId) {

/*        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PRODUCT_SHOPPING, mProduct );

        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), PurchaseDetailActivity.class);
        intent.putExtra(EXTRA_SHOPPING_ID, shoppingId);
        intent.putExtra(EXTRA_BUNDLE_SHOPPING_SHOPPING, bundle);
        startActivity(intent);*/
    }

    @Override
    public void showPurchaseMarkedComplete() {
        showMessage("purchase_marked_complete");
    }

    @Override
    public void showPurchaseMarkedActive() {
        showMessage("purchase_marked_active");

    }

    @Override
    public void showCompletedShoppingCleared() {
        showMessage("completed_purchase_cleared");

    }

    @Override
    public void showLoadingShoppingError() {
        showMessage("loading_purchase_error");
    }


    @Override
    public void showNoShopping() {
        showNoShoppingViews("no_shopping_all",
                R.drawable.ic_launcher_background,
                false
        );
    }


    @Override
    public void showNoActiveShopping() {
        showNoShoppingViews(
                "no_shopping_active",
                R.drawable.ic_launcher_background,
                false
        );
    }

    @Override
    public void showNoCompletedShopping() {
        showNoShoppingViews(
                "no_shopping_completed",
                R.drawable.ic_launcher_background,
                false
        );
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void showNoShoppingViews(String mainText, int iconRes, boolean showAddView) {
        mShoppingView.setVisibility(View.GONE);
        mNoShoppingView.setVisibility(View.VISIBLE);

        mNoShoppingMainView.setText(mainText);
        // mNoShoppingIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoShoppingAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }


    private class ShoppingAdapter extends BaseAdapter {

        private List<Purchase> mPurchaseList;
        private ShoppingItemListener mItemListener;

        public ShoppingAdapter(List<Purchase> purchaseList, ShoppingItemListener itemListener) {
            setList(purchaseList);
            mItemListener = itemListener;
        }

        public void replaceData(List<Purchase> purchaseList) {
            setList(purchaseList);
            notifyDataSetChanged();
        }

        private void setList(List<Purchase> purchaseList) {

            mPurchaseList = checkNotNull(purchaseList);
        }

        @Override
        public int getCount() {
            return mPurchaseList.size();
        }

        @Override
        public Purchase getItem(int i) {
            return mPurchaseList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.item_shopping, viewGroup, false);
            }

            final Purchase purchase = getItem(i);

            TextView tvName = (TextView) rowView.findViewById(R.id.tv_shopping_name);
            final TextView tvQuantity = (TextView) rowView.findViewById(R.id.tv_shopping_quantity);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.iv_shopping_image);
            Button ShoppingRemove = (Button) rowView.findViewById(R.id.iv_shopping_remove);

            tvName.setText(purchase.getTitleForList());
            tvQuantity.setText(purchase.getQuantity());



            // Active/completed task UI
            Picasso.with(imageView.getContext())
                    .load(purchase.getImage())
                    .fit().centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);

            rowView.setBackgroundDrawable(viewGroup.getContext()
                    .getResources().getDrawable(R.drawable.ic_launcher_background));


            ShoppingRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String quantity = getValueQuantity(tvQuantity);
                    mItemListener.onRemovePurchaseClick(purchase.getId(),quantity);


                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!purchase.isCompleted()) {
                        mItemListener.onCompletePurchaseClick(purchase);
                    } else {
                        mItemListener.onActivatePurchaseClick(purchase);
                    }
                }
            });

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onPurchaseClick(purchase);
                }
            });

            return rowView;
        }


    }


    public static String getValueQuantity(TextView tvQuantity){

        if (tvQuantity.getText().toString() != null) {
            return Integer.toString(Integer.parseInt(tvQuantity.getText().toString()) - 1);
        }else {
            return "0";
        }
    }

    @Override
    public void showCarsList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showCarsListEmpty() {
        mShoppingView.setVisibility(View.INVISIBLE);
        mNoShoppingView.setVisibility(View.VISIBLE);
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    public interface ShoppingItemListener {

        void onPurchaseClick(Purchase clickedPurchase);

        void onCompletePurchaseClick(Purchase completedPurchase);

        void onActivatePurchaseClick(Purchase activatedPurchase);

        void onRemovePurchaseClick(String activatedPurchaseId, String quantity);

        void onFinalizeShoppingClick(String date);
    }


}
