package talitha_koum.milipade.com.app.afdis.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import talitha_koum.milipade.com.app.afdis.App;
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.adapters.ShopPagerAdapter;
import talitha_koum.milipade.com.app.afdis.dialogs.ThreshholdDialog;
import talitha_koum.milipade.com.app.afdis.fragments.InventoryFragment;
import talitha_koum.milipade.com.app.afdis.fragments.OrdersFragment;
import talitha_koum.milipade.com.app.afdis.models.Shop;

public class ShopActivity extends AppCompatActivity implements  OrdersFragment.OnFragmentInteractionListener,InventoryFragment.OnFragmentInteractionListener,ThreshholdDialog.ThreshholdDialogInteractionListener {
    private TabLayout tabLayout;

    //This is our viewPagert
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
        shop_id =  App.getPrefManager(this).getShopId();
        shop_name =  App.getPrefManager(this).getShopName();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_threshold) {
            Shop shop = new Shop();
            shop.setShop_id(shop_id );
            shop.setShop_name(shop_name);
            ThreshholdDialog dialog = new ThreshholdDialog();
            Bundle b = new Bundle();
            b.putParcelable("shop", shop);
            dialog.setArguments(b);
            dialog.show(getSupportFragmentManager(),"");
        }

        if (id == R.id.action_history) {
            Intent intent = new Intent(ShopActivity.this, HistoryShopActivity.class);
            intent.putExtra("shop_id", shop_id);
            intent.putExtra("shop_name", shop_name);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ShopPagerAdapter adapter = new ShopPagerAdapter(getSupportFragmentManager());

        //OrdersFragment ordersFragment;
        inventoryFragment = InventoryFragment.newInstance(shop_id,"");
        ordersFragment    = OrdersFragment.newInstance(shop_id,"");

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

    @Override
    public void onClosed() {

    }
}
