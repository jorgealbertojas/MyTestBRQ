package com.example.jorge.mytestbrq.shopping;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.jorge.mytestbrq.Injection;
import com.example.jorge.mytestbrq.R;
import com.example.jorge.mytestbrq.util.ActivityUtils;
import com.example.jorge.mytestbrq.util.EspressoIdlingResource;

public class ShoppingActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Bundle mBundle;

    private ShoppingPresenter mShoppingPresenter;

    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;

    private Toolbar mToolbar;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }


        return super.onOptionsItemSelected(item);
    }



    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }





}



