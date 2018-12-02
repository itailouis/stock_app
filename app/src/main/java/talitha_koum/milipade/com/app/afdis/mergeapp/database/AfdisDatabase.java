package talitha_koum.milipade.com.app.afdis.mergeapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import talitha_koum.milipade.com.app.superviewer.dataobject.ProductDao;
import talitha_koum.milipade.com.app.superviewer.dataobject.StockTackDao;
import talitha_koum.milipade.com.app.superviewer.models.Product;
import talitha_koum.milipade.com.app.superviewer.models.StockTake;

/**
 * Created by TALITHA_KOUM on 20/11/2018.
 * file for the  Superviewer. project
 * in talitha_koum.milipade.com.app.superviewer.database
 */



@Database(entities = {Product.class, StockTake.class}, version = 1)
public abstract class AfdisDatabase extends RoomDatabase {

    public abstract ProductDao productDao();
    public abstract StockTackDao stockTackDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile AfdisDatabase INSTANCE;

    public static AfdisDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AfdisDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AfdisDatabase.class, "afdis_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            //.addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
