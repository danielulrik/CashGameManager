package com.example.rank;

import android.app.Application;
import com.example.rank.db.Persistence;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 06/07/2015
 * Time: 12:22
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Persistence.getInstance().setContexDatabase(this);
        Persistence.getInstance().createTables();
    }
}
