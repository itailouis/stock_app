package talitha_koum.milipade.com.app.afdis.mergeapp.dataobject;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import talitha_koum.milipade.com.app.afdis.mergeapp.models.StockTake;


/**
 * Created by TALITHA_KOUM on 24/11/2018.
 * file for the  Superviewer. project
 * in talitha_koum.milipade.com.app.superviewer.dataobject
 */


@Dao
public interface StockTackDao {

    @Insert
    void insertStock(StockTake stockTake);
}
