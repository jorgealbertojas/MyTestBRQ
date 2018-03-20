package com.example.jorge.mytestbrq.detailCar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jorge.mytestbrq.R;
import com.example.jorge.mytestbrq.util.ActivityUtils;
import com.example.jorge.mytestbrq.util.Common;

import static com.example.jorge.mytestbrq.cars.CarsFragment.EXTRA_CAR_ID;



public class DetailCarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_car);

        String id = getIntent().getExtras().getString(EXTRA_CAR_ID);

        if (null == savedInstanceState) {
            if (Common.isOnline(this)) {
                initFragment(id);
            }




        }



    }

    /**
     * Init Fragment with parameter ID of the car
     * @param id
     */
    private void initFragment(String id) {
      DetailCarFragment shoppingFragment =
                (DetailCarFragment) getSupportFragmentManager().findFragmentById(R.id.fl_detail_car);

        if (shoppingFragment == null) {
            // Create the fragment
            shoppingFragment = DetailCarFragment.newInstance(id);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), shoppingFragment, R.id.fl_detail_car);
        }
    }

}
