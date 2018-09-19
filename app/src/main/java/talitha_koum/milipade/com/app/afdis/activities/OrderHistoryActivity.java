package talitha_koum.milipade.com.app.afdis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.adapters.ShopAdapter;
import talitha_koum.milipade.com.app.afdis.models.Shop;
import talitha_koum.milipade.com.app.afdis.utils.SimpleDividerItemDecoration;

public class OrderHistoryActivity extends AppCompatActivity {
    public static String shop_id;
    public static String shop_name;
    public static String date_ordered;
    private RecyclerView recyclerView;
    private ShopAdapter adapter;
    private ArrayList<Shop> shops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if ((savedInstanceState != null)&&(savedInstanceState.getString("shop_id")!=null)&&(savedInstanceState.getString("shop_name")!=null)) {
            shop_id = savedInstanceState.getString("shop_id");
            shop_name = savedInstanceState.getString("shop_name");
            date_ordered = savedInstanceState.getString("date_created");

        } else {
            shop_id = intent.getStringExtra("shop_id");
            shop_name = intent.getStringExtra("shop_name");
            date_ordered = intent.getStringExtra("date_created");
        }
        getSupportActionBar().setTitle(shop_name);
        recyclerView = (RecyclerView) findViewById(R.id.list_shops);

        shops = new ArrayList<>();

        adapter = new ShopAdapter(this, shops,"");
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
