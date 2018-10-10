package talitha_koum.milipade.com.app.afdis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import talitha_koum.milipade.com.app.afdis.App;
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.adapters.StocksAdapter;
import talitha_koum.milipade.com.app.afdis.models.Stock;
import talitha_koum.milipade.com.app.afdis.utils.SimpleDividerItemDecoration;

public class HistoryInventoryActivity extends AppCompatActivity {
    public static String shop_id;
    public static String shop_name;
    public static String date_ordered;
    private RecyclerView recyclerView;
    private StocksAdapter adapter;
    private ArrayList<Stock> stocks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shop_id =  App.getPrefManager(this).getShopId();
        shop_name =  App.getPrefManager(this).getShopName();
        Intent intent = getIntent();
        Bundle b = this.getIntent().getExtras();
        if (savedInstanceState != null) {

            stocks=  b.getParcelableArrayList("stocks");

        } else {

            stocks=  b.getParcelableArrayList("stocks");
        }
        recyclerView = (RecyclerView) findViewById(R.id.list_shops);



        adapter = new StocksAdapter(this, stocks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
