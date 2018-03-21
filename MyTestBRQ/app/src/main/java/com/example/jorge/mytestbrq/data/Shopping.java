package com.example.jorge.mytestbrq.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Created by jorge on 19/03/2018.
 * Immutable model class for a Purchase.
 */

@Entity(tableName = "Shopping")
public final class Shopping {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private final String mId;


    @NonNull
    @ColumnInfo(name = "cartid")
    private final String mCarId;


    @Nullable
    @ColumnInfo(name = "carname")
    private final String mCarName;

    @Nullable
    @ColumnInfo(name = "cardescription")
    private final String mCarDescription;

    @Nullable
    @ColumnInfo(name = "carbrand")
    private final String mCarBrand;

    @Nullable
    @ColumnInfo(name = "quantity")
    private final String mQuantity;

    @Nullable
    @ColumnInfo(name = "price")
    private final String mPrice;



    @Nullable
    @ColumnInfo(name = "image")
    private final String mImage;

    @Ignore
    public Shopping(@Nullable String carId, @Nullable String carName, @Nullable String carDescription, @Nullable String carBrand, @Nullable String quantity, @Nullable String price, @Nullable String image) {
        this(carId, carName,carDescription,carBrand, quantity,price, image, UUID.randomUUID().toString());
    }

    public Shopping(@Nullable String carId, @Nullable String carName, @Nullable String carDescription, @Nullable String carBrand, @Nullable String quantity, @Nullable String price, @Nullable String image ,
                    @NonNull String id) {
        mId = id;
        mCarId = carId;
        mCarName = carName;
        mCarDescription = carDescription;
        mCarBrand = carBrand;
        mQuantity = quantity;
        mPrice = price;
        mImage = image;
    }



    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getCarId() {
        return mCarId;
    }

    @Nullable
    public String getCarName() {
        return mCarName;
    }

    @Nullable
    public String getCarDescription() {
        return mCarDescription;
    }

    @Nullable
    public String getCarBrand() {
        return mCarBrand;
    }

    @Nullable
    public String getQuantity() {
        return mQuantity;
    }

    @Nullable
    public String getPrice() {
        return mPrice;
    }

    @Nullable
    public String getImage() {
        return mImage;
    }



    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mId);
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mCarName)) {
            return mCarName;
        } else {
            return mId;
        }
    }

    public boolean isCompleted() {
        return (mCarId == null);
    }





}
