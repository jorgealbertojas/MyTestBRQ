package com.example.jorge.mytestbrq;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.example.jorge.mytestbrq.data.Purchase;
import com.example.jorge.mytestbrq.data.source.ShoppingDataSource;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jorge on 19/03/2018.
 * This function is make for the test with android
 * Have objective create data Fake in this Class for Test All the system
 */

public class FakeShoppingRemoteDataSource implements ShoppingDataSource {

    private static FakeShoppingRemoteDataSource INSTANCE;

    private static final Map<String, Purchase> TASKS_SERVICE_DATA = new LinkedHashMap<>();

    // Prevent direct instantiation.
    private FakeShoppingRemoteDataSource() {}

    public static FakeShoppingRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeShoppingRemoteDataSource();
        }
        return INSTANCE;
    }


    @Override
    public void getShopping(@NonNull LoadShoppingCallback callback) {

    }

    @Override
    public void getPurchase(@NonNull String shoppingId, @NonNull GetPurchaseCallback callback) {

    }

    @Override
    public void savePurchase(@NonNull Purchase purchase) {

    }

    @Override
    public void activatePurchase(@NonNull String productI, String Quantity) {

    }

    @Override
    public void activatePurchase(@NonNull Purchase purchase, String quantity) {

    }


    @Override
    public void refreshShopping(List<Purchase> purchaseList) {

    }


    @Override
    public void deleteAllShopping() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deletePurchase(@NonNull String shoppingId) {

    }

    @Override
    public void completePurchase(@NonNull Purchase purchase, String user) {
     }


    @Override
    public void completePurchase(@NonNull String productId) {

    }

    @Override
    public void showMessageEventLog(String message) {

    }

    @Override
    public void finalizeShopping(String date) {

    }


    @VisibleForTesting
    public void addShopping(Purchase... purchases) {

    }
}
