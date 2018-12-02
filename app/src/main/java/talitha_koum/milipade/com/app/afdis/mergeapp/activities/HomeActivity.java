package talitha_koum.milipade.com.app.afdis.mergeapp.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import talitha_koum.milipade.com.app.afdis.App;
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.mergeapp.Viewmodels.ProductViewModel;
import talitha_koum.milipade.com.app.afdis.mergeapp.adapters.MovieAdapter;
import talitha_koum.milipade.com.app.afdis.mergeapp.adapters.ProductAdapter;
import talitha_koum.milipade.com.app.afdis.mergeapp.fragments.AddStockDialog;
import talitha_koum.milipade.com.app.afdis.mergeapp.models.Product;
import talitha_koum.milipade.com.app.afdis.mergeapp.models.ServerProduct;
import talitha_koum.milipade.com.app.afdis.mergeapp.models.Size;
import talitha_koum.milipade.com.app.afdis.mergeapp.models.StockTake;
import talitha_koum.milipade.com.app.afdis.mergeapp.responses.ProductSizeResponse;
import talitha_koum.milipade.com.app.afdis.mergeapp.responses.StockSaveResponse;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;



public class HomeActivity extends AppCompatActivity implements MovieAdapter.ProductAdapterListener ,AddStockDialog.SimpleDialogListener{
    RecyclerView productListview;
    private List<Product> productList = new ArrayList<>();
    private ArrayList<StockTake> stockTakes = new ArrayList<>();
    public ArrayList<Product> products= new ArrayList<Product>();
    //private ProductInputAdapter mAdapter;
    ProductAdapter adapter;
    MovieAdapter movieAdapter ;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    SimpleDateFormat sdf;
    ProgressDialog progressDialog;
    ProductViewModel mWordViewModel;
    AppCompatAutoCompleteTextView productName;
    public static String shop_id;
    public static String shop_name;
    public static String  user_id;
    String product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shop_id =  App.getPrefManager(this).getShopId();
        shop_name =  App.getPrefManager(this).getShopName();
        user_id = App.getPrefManager(this).getUser().getUser_id();
        getSupportActionBar().setTitle(shop_name);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        movieAdapter = new MovieAdapter();
        adapter = new ProductAdapter(this, android.R.layout.select_dialog_item, products);
        //mAdapter=new ProductInputAdapter(this, (ArrayList<Product>) productList,this);
        mWordViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productName = (AppCompatAutoCompleteTextView) findViewById(R.id.product_name);
        productName.setThreshold(2);
        productName.setAdapter(adapter);
        //adapter.setData();
        productName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                productName.showDropDown();
                return false;
            }
        });
        productName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                movieAdapter.getFilter().filter(charSequence+"");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //movieAdapter.getFilter().filter(editable.);
            }
        });
        productName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = (Product) adapterView.getItemAtPosition(i);
                // productName.setText(fruit.getProduct_name());
                //product_id = product.getProduct_id();
                //Toast.makeText(AddStockActivity.this, product_id+"", Toast.LENGTH_SHORT).show();
                //loadSpinner((Product) adapterView.getItemAtPosition(i));
                //Toast.makeText(AddStockActivity.this, "Clicked item from auto completion list " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        productName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product fruit = (Product) adapterView.getItemAtPosition(i);
                // productName.setText(fruit.getProduct_name());
                //product_id = fruit.getProduct_id();
                //Toast.makeText(AddStockActivity.this, product_id+"", Toast.LENGTH_SHORT).show();
            }
        });
        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mWordViewModel.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable final List<Product> words) {
                // Update the cached copy of the words in the adapter.
                productList= words;
                //products= (ArrayList<Product>) words;

                movieAdapter.setMovieList(getApplicationContext(),productList, HomeActivity.this );
                movieAdapter.notifyDataSetChanged();
            }
        });


        //0000-00-00
       productListview = findViewById(R.id.product_list);
       // productListview = findViewById(R.id.restaurants_recyclerview);
        actionModeCallback = new ActionModeCallback();

        progressDialog =  new ProgressDialog(this);
       //getMockData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        productListview.setLayoutManager(layoutManager);
        productListview.setItemAnimator(new DefaultItemAnimator());
        productListview.setAdapter(movieAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getMockData() {
        for(int i=0;i<=100;i++){
            Product product = new Product();
            product.setProduct_id(""+i);

            product.setProduct_name("sample"+i);
            product.setProduct_category("3"+i);
            product.setProduct_size("200");
            product.setProduct_identifier("909"+i);
            product.setProduct_size("8");
            mWordViewModel.insert(product);

        }
        //productListview.setItemViewCacheSize(productList.size());
        //mAdapter.notifyDataSetChanged();
        //movieAdapter.setMovieList(getApplicationContext(),productList, this );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        /*// Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //mAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //mAdapter.filter(newText);
                return true;
            }
        });*/

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mWordViewModel.deleteAll();
            return true;
        }
        if (id == R.id.action_sync_products) {
            //getDataProducts();
            mWordViewModel.deleteAll();
            //getMockData();
            getDataProducts();
            return true;
        }

        if (id == R.id.action_sync_products) {
            saveDataProducts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveDataProducts() {
        //mWordViewModel.getSaveStock();

        for (final StockTake stock : stockTakes) {
            progressDialog.setMessage("Saving Data...");

            progressDialog.show();

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            //File file = new File(mImageUrl);
            File file = new File(stock.getImagefile());

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

            //if(product_id==null||product_id=="")
              //  product_id= "1";

            RequestBody product_id_ = RequestBody.create(MediaType.parse("text/plain"), stock.getProduct_id_());
            RequestBody ShopId = RequestBody.create(MediaType.parse("text/plain"), stock.getShopId());
            RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"), stock.getShopId());
            RequestBody created_date = RequestBody.create(MediaType.parse("text/plain"), sdf.format(stock.getCreated_date()));

            RequestBody product_quantity = RequestBody.create(MediaType.parse("text/plain"), stock.getProduct_quantity());
            RequestBody product_breakages = RequestBody.create(MediaType.parse("text/plain"), stock.getProduct_breakages());
            RequestBody product_size = RequestBody.create(MediaType.parse("text/plain"), stock.getProduct_size());

            RequestBody product_inline  = RequestBody.create(MediaType.parse("text/plain"), stock.getProduct_inline());
            RequestBody product_total_shelf = RequestBody.create(MediaType.parse("text/plain"), stock.getProduct_total_shelf());

            RequestBody Price = RequestBody.create(MediaType.parse("text/plain"), stock.getPrice());

            RequestBody competitor_name_a = RequestBody.create(MediaType.parse("text/plain"), stock.getCompetitor_name_a());
            RequestBody competitor_name_b = RequestBody.create(MediaType.parse("text/plain"), stock.getCompetitor_name_b());

            RequestBody competitor_price_a = RequestBody.create(MediaType.parse("text/plain"), stock.getCompetitor_price_a());
            RequestBody competitor_price_b = RequestBody.create(MediaType.parse("text/plain"), stock.getCompetitor_price_b());

            RequestBody expiry = RequestBody.create(MediaType.parse("text/plain"), stock.getExpiry());
            RequestBody near_date = RequestBody.create(MediaType.parse("text/plain"), stock.getNear_date());

            MultipartBody.Part image = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);


            //RequestBody Inline = RequestBody.create(MediaType.parse("text/plain"), inline);
            //RequestBody Inline = RequestBody.create(MediaType.parse("text/plain"), inline);

            //RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

            //RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
            //RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
            //RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

            Call<StockSaveResponse> call = apiService.saveStockTake(
                    product_id_,
                    created_date,
                    ShopId,
                    UserId,
                    product_quantity,
                    product_breakages,
                    product_size,
                    product_inline,
                    product_total_shelf,
                    Price,
                    competitor_name_a,
                    competitor_name_b,
                    competitor_price_a,
                    competitor_price_b,
                    expiry,
                    near_date,
                    image);
            call.enqueue(new Callback<StockSaveResponse>() {
                @Override
                public void onResponse(Call<StockSaveResponse> call, Response<StockSaveResponse> response) {
                   progressDialog.setMessage(" Saved "+stock.getProduct_id_());
                    //Toast.makeText(AddStockActivity.this, "Unable to fetch json: " +response.body().getData() , Toast.LENGTH_LONG).show();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            progressDialog.setMessage("Data has been Saved ");

                        }
                    }, 1000);
                    //clearForm();
                }

                @Override
                public void onFailure(Call<StockSaveResponse> call, Throwable t) {
                    //Toast.makeText(AddStockActivity.this, "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    //progressDialog.dismiss();

                }
            });

        }
    }

    private void getDataProducts() {
        //mDialog.setCancelable(true);
        // mDialog.show();
        final ArrayList<Product> dataList = new ArrayList<Product>();
        //dataList.add("--Choose Product--");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductSizeResponse> call = apiService.getProductList();
        call.enqueue(new Callback<ProductSizeResponse>() {
            @Override
            public void onResponse(Call<ProductSizeResponse> call, Response<ProductSizeResponse> response) {

                ProductSizeResponse sizeResponse = response.body();


                for (ServerProduct serverProduct : sizeResponse.getData()) {
                    for (Size size : serverProduct.getProducts_size()) {
                    Product product = new Product();
                    product.setProduct_id(serverProduct.getProduct_id());
                    product.setProduct_name(serverProduct.getProduct_name());
                    product.setProduct_category(serverProduct.getProduct_category());
                    product.setProduct_identifier(serverProduct.getProduct_identifier());
                    product.setProduct_size(size.getSize_name());

                    mWordViewModel.insert(product);
                    }
                }

            }

            @Override
            public void onFailure(Call<ProductSizeResponse> call, Throwable t) {
                Snackbar mSnackBar = Snackbar.make(productListview, "Network Error ", Snackbar.LENGTH_LONG);

                TextView mainTextView = (TextView) (mSnackBar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                TextView actionTextView = (TextView) (mSnackBar.getView()).findViewById(android.support.design.R.id.snackbar_action);

                // To Change Snackbar Color
                mSnackBar.getView().setBackgroundColor(getResources().getColor(R.color.colorBlack));
                mainTextView.setTextColor(Color.YELLOW);
                actionTextView.setTextColor(Color.YELLOW);

                mSnackBar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //deleteMessages();
                    }
                });
                mSnackBar.show();

            }
        });

    }



    @Override
    public void OnClickRow(int possition) {
        Product product = productList.get(possition);
        mWordViewModel.getProducts(product.getProduct_name());

        AddStockDialog dl = AddStockDialog.newInstance(product.getProduct_name());
        dl.show(getSupportFragmentManager(),"mydialog");
    }

    @Override
    public void onPositiveResult(String dlg) {

    }

    @Override
    public void onNegativeResult(String dlg) {

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            //swipeRefreshLayout.setEnabled(false);
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
                    //deleteMessages();
                    saveDataProducts();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //mAdapter.clearSelections();
            //swipeRefreshLayout.setEnabled(true);
            actionMode = null;

        }
    }
    private void toggleSelection(int position) {
        //mAdapter.toggleSelection(position);
        //int count = mAdapter.getSelectedItemCount();

        //if (count == 0) {
           // actionMode.finish();
        //} else {
            actionMode.setTitle("Edit Mode");
            actionMode.invalidate();
        //}
    }
    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

}
