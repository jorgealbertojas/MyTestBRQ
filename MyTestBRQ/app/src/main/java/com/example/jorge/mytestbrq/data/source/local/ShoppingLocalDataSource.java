package com.example.jorge.mytestbrq.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.example.jorge.mytestbrq.data.Purchase;
import com.example.jorge.mytestbrq.data.source.ShoppingDataSource;
import com.example.jorge.mytestbrq.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 19/03/2018.
 * ORM for put Purchase
 */

public class ShoppingLocalDataSource implements ShoppingDataSource {

    private static volatile ShoppingLocalDataSource INSTANCE;

    private ShoppingDao mShoppingDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private ShoppingLocalDataSource(@NonNull AppExecutors appExecutors,
                                    @NonNull ShoppingDao shoppingDao) {
        mAppExecutors = appExecutors;
        mShoppingDao = shoppingDao;
    }

    public static ShoppingLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                      @NonNull ShoppingDao shoppingDao) {
        if (INSTANCE == null) {
            synchronized (ShoppingLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ShoppingLocalDataSource(appExecutors, shoppingDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * This function is Fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getShopping(@NonNull final LoadShoppingCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Purchase> shopping = mShoppingDao.getShopping();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (shopping.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onShoppingLoaded(shopping);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }


    @Override
    public void getPurchase(@NonNull final String shoppingId, @NonNull final GetPurchaseCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Purchase purchase = mShoppingDao.getPurchaseById(shoppingId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (purchase != null) {


                            callback.onPurchaseLoaded(purchase);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }


    @Override
    public void savePurchase(@NonNull final Purchase purchase) {
        checkNotNull(purchase);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                Purchase purchaseNew = mShoppingDao.getPurchaseById(purchase.getCarId());
                if (purchaseNew == null) {
                    mShoppingDao.insertPurchase(purchase);
                }else{
                    String quantity1 = purchase.getQuantity();
                    String quantity2 = purchaseNew.getQuantity();

                    if (quantity1.equals("")){
                        quantity1 = "1";
                    }

                    Integer contTotal = Integer.parseInt(quantity1) + Integer.parseInt(quantity2);
                    String valueFinal = Integer.toString(contTotal);

                    mShoppingDao.updateQuantity(purchaseNew.getId(), valueFinal);
                }
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }



    @Override
    public void activatePurchase(@NonNull final String shoppingId,@NonNull final String quantity) {
        Runnable activateRunnable = new Runnable() {
            @Override
            public void run() {
                String newQuantity = "1";
                if (!quantity.equals("")){
                    newQuantity  =   quantity;
                }
                mShoppingDao.updateQuantity(shoppingId, newQuantity);
            }
        };
        mAppExecutors.diskIO().execute(activateRunnable);
    }

    @Override
    public void activatePurchase(@NonNull Purchase purchase, String Quantity) {

    }


    @Override
    public void refreshShopping() {
        // Not required because the {@link ShoppingRepository} handles the logic of refreshing the
        // Purchase from all the available data sources.

    }

    @Override
    public void deleteAllShopping() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mShoppingDao.deleteShopping();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deletePurchase(@NonNull final String shoppingId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mShoppingDao.deletePurchaseById(shoppingId);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }


    @Override
    public void completePurchase(@NonNull final Purchase purchase,@NonNull final String quantity) {
        Runnable completeRunnable = new Runnable() {
            @Override
            public void run() {
                mShoppingDao.updateQuantity(purchase.getId(),purchase.getQuantity());
            }
        };

        mAppExecutors.diskIO().execute(completeRunnable);
    }




    @Override
    public void completePurchase(@NonNull String productId) {

    }


    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }
}

