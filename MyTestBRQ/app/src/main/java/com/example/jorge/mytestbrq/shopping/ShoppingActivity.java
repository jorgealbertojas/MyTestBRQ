package com.example.jorge.mytestbrq.shopping;


import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.jorge.mytestbrq.Injection;
import com.example.jorge.mytestbrq.R;
import com.example.jorge.mytestbrq.util.ActivityUtils;
import com.example.jorge.mytestbrq.util.EspressoIdlingResource;

public class ShoppingActivity extends AppCompatActivity {

    public static final int REQUEST_FINALIZE_SHOPPING = 1;

    public static final int REQUEST_EMPTY_SHOPPING = 10;

    public static final int REQUEST_EMPTY_DETAIL = 20;

    private ShoppingPresenter mShoppingPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);


        ShoppingFragment shoppingFragment =
                (ShoppingFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (shoppingFragment == null) {
            // Create the fragment
            shoppingFragment = ShoppingFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), shoppingFragment, R.id.contentFrame);
        }

        // Create the presenter
        mShoppingPresenter = new ShoppingPresenter(
                Injection.provideShoppingRepository(getApplicationContext()), shoppingFragment);


    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        // outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());

        super.onSaveInstanceState(outState);
    }



    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}



