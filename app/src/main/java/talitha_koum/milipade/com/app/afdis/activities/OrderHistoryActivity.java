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
import talitha_koum.milipade.com.app.afdis.adapters.OrdersAdapter;
import talitha_koum.milipade.com.app.afdis.models.Orders;
import talitha_koum.milipade.com.app.afdis.models.Shop;
import talitha_koum.milipade.com.app.afdis.utils.SimpleDividerItemDecoration;

public class OrderHistoryActivity extends AppCompatActivity {
    public static String shop_id;
    public static String shop_name;
    public static String date_ordered;
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private ArrayList<Shop> shops;
    ArrayList<Orders> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Bundle b = this.getIntent().getExtras();
        shop_id =  App.getPrefManager(this).getShopId();
        shop_name =  App.getPrefManager(this).getShopName();


        orders = b.getParcelableArrayList("orders");
        getSupportActionBar().setTitle(shop_name);
        recyclerView = (RecyclerView) findViewById(R.id.list_shops);

        adapter = new OrdersAdapter(this, orders);
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
