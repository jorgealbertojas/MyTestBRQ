package com.example.jorge.mytestbrq;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.jorge.mytestbrq.data.source.ShoppingRepository;
import com.example.jorge.mytestbrq.data.source.local.ShoppingLocalDataSource;
import com.example.jorge.mytestbrq.data.source.local.ToDoDatabase;
import com.example.jorge.mytestbrq.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 19/03/2018.
 */

public class Injection {
    public static ShoppingRepository provideShoppingRepository(@NonNull Context context) {
        checkNotNull(context);
        ToDoDatabase database = ToDoDatabase.getInstance(context);

        return ShoppingRepository.getInstance(FakeShoppingRemoteDataSource.getInstance(),
                ShoppingLocalDataSource.getInstance(new AppExecutors(),
                        database.ShoppingDao(),context));
    }
}
