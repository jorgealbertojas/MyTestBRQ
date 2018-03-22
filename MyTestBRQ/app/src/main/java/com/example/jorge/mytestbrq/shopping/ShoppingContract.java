package com.example.jorge.mytestbrq.shopping;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.jorge.mytestbrq.BasePresenter;
import com.example.jorge.mytestbrq.BaseView;
import com.example.jorge.mytestbrq.data.Purchase;

import java.util.List;

/**
 * Created by jorge on 20/03/2018.
 */

public interface ShoppingContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showShopping(List<Purchase> listPurchase);

        void showAddPurchase();

        void showCarsList();

        void showPurchaseDetailsUi(String shoppingId);

        void showPurchaseMarkedComplete();

        void showPurchaseMarkedActive();

        void showCompletedShoppingCleared();

        void showLoadingShoppingError();

        void showNoShopping();

        void showNoActiveShopping();

        void showNoCompletedShopping();

        boolean isActive();





    }

    interface Presenter extends BasePresenter {

        void removeItemShopping(@NonNull String removeShoppingId, String quantity);

        void loadShopping(boolean forceUpdate);

        void openPurchaseDetails(@NonNull Purchase requestedShopping);

        void completePurchase(@NonNull Purchase completedShopping);

        void activatePurchase(@NonNull Purchase activeShopping);

        void finalizeShopping(@NonNull String date);

        void clearCompletedShopping();

    }
}


