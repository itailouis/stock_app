package talitha_koum.milipade.com.app.afdis.activities;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import talitha_koum.milipade.com.app.afdis.App;
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.adapters.ShopPlanAdapter;
import talitha_koum.milipade.com.app.afdis.data.AfdisController;
import talitha_koum.milipade.com.app.afdis.data.AfidsContract;
import talitha_koum.milipade.com.app.afdis.models.Shop;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;
import talitha_koum.milipade.com.app.afdis.responses.PlanResponse;

public class PlanActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ShopPlanAdapter.MessageAdapterListener {
    private List<Shop> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private ShopPlanAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private AfdisController DBcontroller;
    private String arrayshopids="";
    boolean done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DBcontroller = new AfdisController(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new ShopPlanAdapter(this, messages, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        actionModeCallback = new ActionModeCallback();

        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getInbox();
                    }
                }
        );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getInbox() {
        swipeRefreshLayout.setRefreshing(true);
        DBcontroller.open();
        Cursor cursor = DBcontroller.readEntry();
        while (cursor.moveToNext()) {
            Shop shop = new Shop();
            int idx = cursor.getColumnIndex(AfidsContract.ShopEntry.COL_SHOP_ID);
            int idy = cursor.getColumnIndex(AfidsContract.ShopEntry.COL_SHOP_NAME);
            int ida = cursor.getColumnIndex(AfidsContract.ShopEntry.COL_SHOP_STATUS);
            int idb = cursor.getColumnIndex(AfidsContract.ShopEntry.COL_SHOP_LAT);
            int idc = cursor.getColumnIndex(AfidsContract.ShopEntry.COL_SHOP_LON);
            int ide = cursor.getColumnIndex(AfidsContract.ShopEntry.COL_SHOP_LAST_VISIT);
            int idr = cursor.getColumnIndex(AfidsContract.ShopEntry.COL_SHOP_LOCATION);
            shop.setShop_id(cursor.getString(idx));
            shop.setShop_name(cursor.getString(idy));
            shop.setLast_visited(cursor.getString(ide));
            messages.add(shop);
        }
        mAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // deleting the messages from recycler view
    private void deleteMessages() {
        mAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            arrayshopids=arrayshopids+","+messages.get(selectedItemPositions.get(i)).getShop_id();
            //mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
        // Toast.makeText(getApplicationContext(), "ids: " + arrayshopids, Toast.LENGTH_SHORT).show();
        saveVisits(arrayshopids, App.getPrefManager(PlanActivity.this).getUser().getUser_id(),App.getPrefManager(PlanActivity.this).getShopId());
        if(done){
            arrayshopids="";
        }

    }
    private void saveVisits(String arrayshopids, String user_id, String shopId) {

        //final boolean saveres;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        ///Call<ShopResponse> call = apiService.getShop(App.getPrefManager(MainActivity.this).getUser().getId());
        Call<PlanResponse> call = apiService.getVisitPlan(arrayshopids,getCurrentTimeStamp() ,user_id);

        call.enqueue(new Callback<PlanResponse>() {
            @Override
            public void onResponse(Call<PlanResponse> call, Response<PlanResponse> response) {
                // clear the inbox
                PlanResponse planResponse = response.body();
                if(planResponse.getData().getStatus()){
                    done= true;
                    Snackbar.make(recyclerView, planResponse.getData().getMessage(), Snackbar.LENGTH_LONG).setAction("OK", null).show();
                }else{
                    Snackbar.make(recyclerView, planResponse.getData().getMessage(), Snackbar.LENGTH_LONG).setAction("OK", null).show();
                }

            }

            @Override
            public void onFailure(Call<PlanResponse> call, Throwable t) {
                Snackbar mSnackBar = Snackbar.make(recyclerView, "Network Error ", Snackbar.LENGTH_LONG);

                TextView mainTextView = (TextView) (mSnackBar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                TextView actionTextView = (TextView) (mSnackBar.getView()).findViewById(android.support.design.R.id.snackbar_action);

                // To Change Snackbar Color
                mSnackBar.getView().setBackgroundColor(getResources().getColor(R.color.colorBlack));
                done= false;
                // To Apply Custom Fonts for Message and Action
                //Typeface font = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
                //mainTextView.setTypeface(font);
                //actionTextView.setTypeface(font);

                // To Change Text Color for Message and Action
                mainTextView.setTextColor(Color.YELLOW);
                actionTextView.setTextColor(Color.YELLOW);

                mSnackBar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteMessages();
                    }
                });
                mSnackBar.show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }


    @Override
    public void onRefresh() {
        // swipe refresh is performed, fetch the messages again
        //getInbox();
    }

    @Override
    public void onIconClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
    }

    @Override
    public void onIconImportantClicked(int position) {
        // Star icon is clicked,
        // mark the message as important
        Shop message = messages.get(position);
        //message.setImportant(!message.isImportant());
        messages.set(position, message);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            // read the message which removes bold from the row
            Shop message = messages.get(position);
            //message.setRead(true);
            messages.set(position, message);
            mAdapter.notifyDataSetChanged();

            Toast.makeText(getApplicationContext(), "Read: " + message.getShop_name(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

}
