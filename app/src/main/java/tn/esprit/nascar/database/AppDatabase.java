package tn.esprit.nascar.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import tn.esprit.nascar.dao.DeliveryDao;
import tn.esprit.nascar.models.Delivery;


@Database(entities = {Delivery.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DeliveryDao deliveryDao();


    private static AppDatabase instance;

    // Synchronized method to get the singleton instance
    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "restaurant_database")
                    .build();
        }
        return instance;
    }
    public static void destroyInstance() {
        instance = null; // Clear the instance for testing or when not needed
    }

}
