package com.example.jorge.mytestbrq.data.source;

import android.support.annotation.NonNull;

import com.example.jorge.mytestbrq.data.Purchase;

import java.util.List;

/**
 * Created by jorge on 19/03/2018.
 *
 *  * Main entry point for accessing Purchase data.
 * For simplicity, only getShopping() and getPurchase() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new Purchase is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */

public interface ShoppingDataSource {

    interface LoadShoppingCallback {

        void onShoppingLoaded(List<Purchase> Purchase);

        void onDataNotAvailable();

    }



    interface GetPurchaseCallback {

        void onPurchaseLoaded(Purchase purchase);

        void onDataNotAvailable();
    }

    void getShopping(@NonNull LoadShoppingCallback callback);

    void getPurchase(@NonNull String shoppingId , @NonNull GetPurchaseCallback callback);

    void savePurchase(@NonNull Purchase purchase);

    void activatePurchase(@NonNull String productI, String Quantity);

    void activatePurchase(@NonNull Purchase purchase, String Quantity);

    void refreshShopping(List<Purchase> purchaseList);

    void deleteAllShopping();

    void deletePurchase(@NonNull String shoppingId);

    void completePurchase(@NonNull Purchase purchase, @NonNull String quantity);

    void completePurchase(@NonNull String productId);

    void showMessageEventLog(String message);

    void finalizeShopping(String date);



}

