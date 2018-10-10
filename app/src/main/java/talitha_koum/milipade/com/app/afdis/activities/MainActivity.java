package talitha_koum.milipade.com.app.afdis.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import talitha_koum.milipade.com.app.afdis.App;
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.SplashActivity;
import talitha_koum.milipade.com.app.afdis.adapters.ShopAdapter;
import talitha_koum.milipade.com.app.afdis.data.AfdisController;
import talitha_koum.milipade.com.app.afdis.data.AfidsContract;
import talitha_koum.milipade.com.app.afdis.geofance.Constants;
import talitha_koum.milipade.com.app.afdis.geofance.GeofenceBroadcastReceiver;
import talitha_koum.milipade.com.app.afdis.geofance.GeofenceErrorMessages;
import talitha_koum.milipade.com.app.afdis.models.Shop;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;
import talitha_koum.milipade.com.app.afdis.responses.ShopResponse;
import talitha_koum.milipade.com.app.afdis.utils.SimpleDividerItemDecoration;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnCompleteListener<Void> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ShopAdapter adapter;
    private ArrayList<Shop> shops;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AfdisController DBcontroller;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }

    //Provides access to the Geofencing API.

    private GeofencingClient mGeofencingClient;
    private ArrayList<Geofence> mGeofenceList;
    //Used when requesting to add or remove geofences.
    private PendingIntent mGeofencePendingIntent;

    // Buttons for kicking off the process of adding or removing geofences.
    //private Button mAddGeofencesButton;
    //private Button mRemoveGeofencesButton;

    private PendingGeofenceTask mPendingGeofenceTask = PendingGeofenceTask.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGeofenceList = new ArrayList<>();
        mGeofencePendingIntent = null;
        DBcontroller = new AfdisController(this);
        // Get the geofences used. Geofence data is hard coded in this sample.
        //populateGeofenceList();
        mGeofencingClient = LocationServices.getGeofencingClient(this);


        if (!App.getPrefManager(MainActivity.this).isLogedIn()) {

            // Opening UserProfileActivity .
            /// Intent intent = new Intent(MainActivity.this, UserAuthActivity.class);
            Intent intent = new Intent(MainActivity.this, UserAuthActivity.class);
            startActivity(intent);
            finish();
           ;
        }
        populateGeofenceList();
        recyclerView = (RecyclerView) findViewById(R.id.list_shops);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        shops = new ArrayList<>();
        //shops = ShopMock.getShopList();
        // self user id is to identify the message owner
        //String selfUserId = App.getInstance().getPrefManager().getUser().getId();
        String selfUserId = "modock_id";
        adapter = new ShopAdapter(this, shops, selfUserId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        addGeofencesButtonHandler();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        //getInbox();
                        getShops();
                    }
                }
        );

        recyclerView.addOnItemTouchListener(new ShopAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new ShopAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                Shop shop = shops.get(position);
                Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                App.getPrefManager(MainActivity.this).setShopName(shop.getShop_name());
                App.getPrefManager(MainActivity.this).setShopId(shop.getShop_id());
                //intent.putExtra("shop_id", shop.getShop_id());
                //intent.putExtra("shop_name", shop.getShop_name());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    private void getShops() {
        swipeRefreshLayout.setRefreshing(true);
        DBcontroller.open();
        shops.clear();
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
            shops.add(shop);
        }
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);

       /* // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.product_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //itemArrayAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //itemArrayAdapter.filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);*/
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_plan) {
            startActivity(new Intent(MainActivity.this,PlanActivity.class));
            return true;
        }
        if (id == R.id.action_logout) {
            App.getPrefManager(MainActivity.this).clear();
            DBcontroller.open();
            //DBcontroller.empty();
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void lgetInbox() {
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        ///Call<ShopResponse> call = apiService.getShop(App.getPrefManager(MainActivity.this).getUser().getId());
        Call<ShopResponse> call = apiService.getShop("1");

        call.enqueue(new Callback<ShopResponse>() {
            @Override
            public void onResponse(Call<ShopResponse> call, Response<ShopResponse> response) {
                // clear the inbox
                shops.clear();
                ShopResponse shopResponse = response.body();

                if (shopResponse.getStatus_code() == 201) {

                    for (Shop message : shopResponse.getData()) {
                        // generate a random color
                        //message.setColor(getRandomMaterialColor("400"));
                        shops.add(message);
                    }
                }


                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ShopResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        // swipe refresh is performed, fetch the messages again
        //getInbox();
        getShops();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);
        // Return a GeofencingRequest.
        return builder.build();
    }

    public void addGeofencesButtonHandler() {
        if (!checkPermissions()) {
            mPendingGeofenceTask = PendingGeofenceTask.ADD;
            requestPermissions();
            return;
        }
        addGeofences();
    }

    /**
     * Adds geofences. This method should be called after the user has granted the location
     * permission.
     */
    private void addGeofences() {
        if (!checkPermissions()) {
            showSnackbar(getString(R.string.insufficient_permissions));
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent()).addOnCompleteListener(this);
    }
    public void removeGeofencesButtonHandler(View view) {
        if (!checkPermissions()) {
            mPendingGeofenceTask = PendingGeofenceTask.REMOVE;
            requestPermissions();
            return;
        }
        removeGeofences();
    }

    /**
     * Removes geofences. This method should be called after the user has granted the location
     * permission.
     */
    @SuppressWarnings("MissingPermission")
    private void removeGeofences() {
        if (!checkPermissions()) {
            showSnackbar(getString(R.string.insufficient_permissions));
            return;
        }

        mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(this);
    }

    /**
     * Runs when the result of calling {@link #addGeofences()} and/or {@link #removeGeofences()}
     * is available.
     * @param task the resulting Task, containing either a result or error.
     */
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        mPendingGeofenceTask = PendingGeofenceTask.NONE;
        if (task.isSuccessful()) {
            updateGeofencesAdded(!getGeofencesAdded());
            setButtonsEnabledState();

            int messageId = getGeofencesAdded() ? R.string.geofences_added : R.string.geofences_removed;
            Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this, task.getException());
            Log.w(TAG, errorMessage);
        }
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    /**
     * This sample hard codes geofence data. A real app might dynamically create geofences based on
     * the user's location.
     */
    private void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.BAY_AREA_LANDMARKS.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    // Create the geofence.
                    .build());
        }
    }

    /**
     * Ensures that only one button is enabled at any time. The Add Geofences button is enabled
     * if the user hasn't yet added geofences. The Remove Geofences button is enabled if the
     * user has added geofences.
     */
    private void setButtonsEnabledState() {
        if (getGeofencesAdded()) {
            //mAddGeofencesButton.setEnabled(false);
            //mRemoveGeofencesButton.setEnabled(true);
        } else {
            //mAddGeofencesButton.setEnabled(true);
            ///mRemoveGeofencesButton.setEnabled(false);
        }
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId, View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Returns true if geofences were added, otherwise false.
     */
    private boolean getGeofencesAdded() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                Constants.GEOFENCES_ADDED_KEY, false);
    }

    /**
     * Stores whether geofences were added ore removed in {@link SharedPreferences};
     *
     * @param added Whether geofences were added or removed.
     */
    private void updateGeofencesAdded(boolean added) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(Constants.GEOFENCES_ADDED_KEY, added)
                .apply();
    }

    /**
     * Performs the geofencing task that was pending until location permission was granted.
     */
    private void performPendingGeofenceTask() {
        if (mPendingGeofenceTask == PendingGeofenceTask.ADD) {
            addGeofences();
        } else if (mPendingGeofenceTask == PendingGeofenceTask.REMOVE) {
            removeGeofences();
        }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted.");
                performPendingGeofenceTask();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                mPendingGeofenceTask = PendingGeofenceTask.NONE;
            }
        }
    }
}
