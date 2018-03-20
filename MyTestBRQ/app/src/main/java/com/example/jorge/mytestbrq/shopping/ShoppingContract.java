package com.example.jorge.mytestbrq.shopping;

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

        void showPurchaseDetailsUi(String shoppingId);

        void showPurchaseMarkedComplete();

        void showPurchaseMarkedActive();

        void showCompletedShoppingCleared();

        void showLoadingShoppingError();

        void showNoShopping();


        void showNoActiveShopping();

        void showNoCompletedShopping();

        void showSuccessfullySavedMessage();

        boolean isActive();





    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadShopping(boolean forceUpdate);

        void addNewPurchase();

        void openPurchaseDetails(@NonNull Purchase requestedTask);

        void completePurchase(@NonNull Purchase completedTask);

        void activatePurchase(@NonNull Purchase activeTask);

        void clearCompletedShopping();

    }
}

