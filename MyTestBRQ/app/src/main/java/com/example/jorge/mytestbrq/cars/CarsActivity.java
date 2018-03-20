package com.example.jorge.mytestbrq.cars;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jorge.mytestbrq.R;
import com.example.jorge.mytestbrq.util.Common;

// Activity for list Cars
public class CarsActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_PURCHASE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        if (null == savedInstanceState) {
            if (Common.isOnline(this)) {
                initFragment(CarsFragment.newInstance());
            }
        }
    }

    /**
     * Init Fragment for cars
     * @param carFragment
     */
    private void initFragment(Fragment carFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_cars, carFragment);
        transaction.commit();

    }

}
