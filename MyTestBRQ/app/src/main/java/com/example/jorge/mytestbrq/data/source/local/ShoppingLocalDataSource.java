package com.example.jorge.mytestbrq.data.source.local;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.widget.Toast;
import com.example.jorge.mytestbrq.R;
import com.example.jorge.mytestbrq.data.Purchase;
import com.example.jorge.mytestbrq.data.source.ShoppingDataSource;
import com.example.jorge.mytestbrq.util.AppExecutors;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import static com.example.jorge.mytestbrq.util.ConstTagKey.TAG_COMPLETE;
import static com.example.jorge.mytestbrq.util.ConstTagKey.TAG_ERROR;
import static com.example.jorge.mytestbrq.util.ConstTagKey.TAG_NORMAL;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 19/03/2018.
 * ORM for put Purchase
 */

public class ShoppingLocalDataSource implements ShoppingDataSource {

    private static volatile ShoppingLocalDataSource INSTANCE;

    private ShoppingDao mShoppingDao;

    private AppExecutors mAppExecutors;

    private Context mContext;


    // Prevent direct instantiation.
    private ShoppingLocalDataSource(@NonNull AppExecutors appExecutors,
                                    @NonNull ShoppingDao shoppingDao, Context context) {
        mAppExecutors = appExecutors;
        mShoppingDao = shoppingDao;
        mContext = context;
    }

    public static ShoppingLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                      @NonNull ShoppingDao shoppingDao, Context context) {
        if (INSTANCE == null) {
            synchronized (ShoppingLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ShoppingLocalDataSource(appExecutors, shoppingDao, context);
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
                            Toast.makeText(mContext,mContext.getResources().getString(R.string.shopping_empty),Toast.LENGTH_LONG).show();
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
                Purchase purchaseNew = mShoppingDao.getPurchaseByCarId(purchase.getCarId());
                if (purchaseNew == null) {
                    observeInsertPurchase(purchase).subscribe(subscriberInsert);
                }else{
                    String quantity1 = purchase.getQuantity();
                    String quantity2 = purchaseNew.getQuantity();

                    if (quantity1.equals("")){
                        quantity1 = "1";
                    }

                    Integer contTotal = Integer.parseInt(quantity1) + Integer.parseInt(quantity2);
                    String valueFinal = Integer.toString(contTotal);

                    observeUpdateQuantity(purchaseNew.getCarId(),valueFinal).subscribe(subscriberUpdate);

                }
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);

    }

    @Override
    public void activatePurchase(@NonNull final String shoppingId, @NonNull final String quantity) {
        Runnable activateRunnable = new Runnable() {
            @Override
            public void run() {
                String newQuantity = "1";
                if (!quantity.equals("")){
                    newQuantity  =   quantity;
                }
                observeUpdateQuantity(shoppingId,newQuantity).subscribe(subscriberUpdate);
            }
        };
        mAppExecutors.diskIO().execute(activateRunnable);

    }

    @Override
    public void activatePurchase(@NonNull Purchase purchase, String Quantity) {

    }

    @Override
    public void refreshShopping(List<Purchase> purchaseList) {
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
                observeDeletePurchase(shoppingId).subscribe(subscriberDelete);
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

    @Override
    public void showMessageEventLog(final String message){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
   }

    @Override
    public void finalizeShopping(final String date) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mShoppingDao.updateFinalizeShopping(date);
                observeFinalizeShopping(date).subscribe(subscriberFinalize);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    public Observable<Integer> observeFinalizeShopping(@NonNull String date){
        return Observable.just(callObserveFinalizeShopping(date));
    }

    public Observable<Integer> observeUpdateQuantity(@NonNull String shoppingId, @NonNull String quantity){
        return Observable.just(callObserveUpdateQuantity(shoppingId, quantity));
    }

    public Observable<Long> observeInsertPurchase(@NonNull Purchase purchase){
        return Observable.just(callObserveInsertPurchase(purchase));
    }

    public Observable<Integer> observeDeletePurchase(@NonNull String shoppingId){
        return Observable.just(callObserveDeletePurchase(shoppingId));
    }

    private int callObserveFinalizeShopping(@NonNull String date){
        int i = mShoppingDao.updateFinalizeShopping(date);
        subscriberUpdate.onNext(i);
        subscriberUpdate.onCompleted();
        return i;
    }

    private int callObserveUpdateQuantity(@NonNull String shoppingId, @NonNull String quantity){
        int i = mShoppingDao.updateQuantity(shoppingId, quantity);
        subscriberUpdate.onNext(i);
        subscriberUpdate.onCompleted();
        return i;
    }

    private long callObserveInsertPurchase(@NonNull Purchase purchase){
        long statusCommit = mShoppingDao.insertPurchase(purchase);
        subscriberInsert.onNext(statusCommit);
        subscriberInsert.onCompleted();
        return statusCommit;
    }

    private int callObserveDeletePurchase(@NonNull String shoppingId){
        int statusCommit = mShoppingDao.deletePurchaseById(shoppingId);
        subscriberDelete.onNext(statusCommit);
        subscriberDelete.onCompleted();
        return statusCommit;
    }

    Subscriber<Integer> subscriberUpdate = new Subscriber<Integer>(){
        @Override
        public void onCompleted() {
            Log.e(TAG_COMPLETE, mContext.getResources().getString(R.string.msg_success_return_update));
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG_ERROR, mContext.getResources().getString(R.string.msg_error_return));
            showMessageEventLog(mContext.getResources().getString(R.string.msg_error_return));
        }

        @Override
        public void onNext(Integer names) {
            Log.e(TAG_NORMAL,mContext.getResources().getString(R.string.msg_next_return));
            if (names > 0){
                showMessageEventLog(mContext.getResources().getString(R.string.msg_success_return_update));
            }
        }
    };

    Subscriber<Long> subscriberInsert = new Subscriber<Long>(){
        @Override
        public void onCompleted() {
            Log.e(TAG_COMPLETE, mContext.getResources().getString(R.string.msg_success_return_insert));

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG_ERROR, mContext.getResources().getString(R.string.msg_error_return));
            showMessageEventLog(mContext.getResources().getString(R.string.msg_error_return));
        }

        @Override
        public void onNext(Long names) {
            Log.e(TAG_NORMAL,mContext.getResources().getString(R.string.msg_next_return));
            if (names > 0){
                showMessageEventLog(mContext.getResources().getString(R.string.msg_success_return_insert));
            }
        }
    };

    Subscriber<Integer> subscriberDelete = new Subscriber<Integer>(){
        @Override
        public void onCompleted() {
            Log.e(TAG_COMPLETE, mContext.getResources().getString(R.string.msg_success_return_delete));

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG_ERROR, mContext.getResources().getString(R.string.msg_error_return));
            showMessageEventLog(mContext.getResources().getString(R.string.msg_error_return));
        }

        @Override
        public void onNext(Integer names) {
            Log.e(TAG_NORMAL,mContext.getResources().getString(R.string.msg_next_return));
            if (names == 0){
                showMessageEventLog(mContext.getResources().getString(R.string.msg_success_return_delete));
            }
        }
    };

    Subscriber<Integer> subscriberFinalize = new Subscriber<Integer>(){
        @Override
        public void onCompleted() {
            Log.e(TAG_COMPLETE, mContext.getResources().getString(R.string.msg_success_return_update));
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG_ERROR, mContext.getResources().getString(R.string.msg_error_return));
            showMessageEventLog(mContext.getResources().getString(R.string.msg_error_return));
        }

        @Override
        public void onNext(Integer names) {
            Log.e(TAG_NORMAL,mContext.getResources().getString(R.string.msg_next_return));
            if (names > 0){
                showMessageEventLog(mContext.getResources().getString(R.string.msg_success_return_update));
            }
        }
    };


    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }
}

