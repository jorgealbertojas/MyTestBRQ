package com.example.jorge.mytestbrq.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.jorge.mytestbrq.data.Purchase;

import java.util.List;



/**
 * Created by jorge on 19/03/2018.
 * Data Access Object for the Purchase table.
 */
@Dao
public interface ShoppingDao {

    /**
     * Select all Purchase from the Purchase table.
     */
    @Query("SELECT * FROM Purchase")
    List<Purchase> getShopping();

    /**
     * Select a Purchase by id.
     */
    @Query("SELECT * FROM Purchase WHERE entryid = :shoppingId ")
    Purchase getPurchaseById(String shoppingId);

    /**
     * Delete a Purchase by id.
     */
    @Query("DELETE FROM Purchase WHERE entryid = :shoppingId")
    int deletePurchaseById(String shoppingId);

    /**
     * Update the complete status of a Purchase
     */
    @Query("UPDATE Purchase SET quantity = :quantity WHERE entryid = :shoppingId ")
    int updateQuantity(String shoppingId, String quantity);

    /**
     * Insert a Purchase in the database. If the Purchase already exists, replace it.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPurchase(Purchase purchase);

    /**
     * Delete all Purchase.
     */
    @Query("DELETE FROM Purchase")
    void deleteShopping();
}
