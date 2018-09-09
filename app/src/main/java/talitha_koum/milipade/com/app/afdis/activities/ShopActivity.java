package talitha_koum.milipade.com.app.afdis.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.adapters.ShopPagerAdapter;
import talitha_koum.milipade.com.app.afdis.fragments.InventoryFragment;
import talitha_koum.milipade.com.app.afdis.fragments.OrdersFragment;

public class ShopActivity extends AppCompatActivity implements  OrdersFragment.OnFragmentInteractionListener,InventoryFragment.OnFragmentInteractionListener {
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments

    InventoryFragment inventoryFragment;
    OrdersFragment ordersFragment;
    public static String shop_id;
    public static String shop_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if (savedInstanceState == null) {
            shop_id = intent.getStringExtra("shop_id");
            shop_name = intent.getStringExtra("shop_name");

        } else {
            shop_id = savedInstanceState.getString("shop_id");
            shop_name = savedInstanceState.getString("shop_name");
        }


        getSupportActionBar().setTitle(shop_name);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setOffscreenPageLimit(2);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position,false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void setupViewPager(ViewPager viewPager)
    {
        ShopPagerAdapter adapter = new ShopPagerAdapter(getSupportFragmentManager());

        OrdersFragment ordersFragment;
        inventoryFragment = InventoryFragment.newInstance(shop_id,"");
        ordersFragment=OrdersFragment.newInstance(shop_id,"");

        adapter.addFragment(ordersFragment,"ORDERS");
        adapter.addFragment(inventoryFragment,"INVENTORY");

        viewPager.setAdapter(adapter);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putString("shop_id", shop_id);
        savedInstanceState.putString("shop_name", shop_name);
        // etc.
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        shop_id = savedInstanceState.getString("shop_id");
        shop_name = savedInstanceState.getString("shop_name");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
