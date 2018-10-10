package talitha_koum.milipade.com.app.afdis.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import talitha_koum.milipade.com.app.afdis.adapters.ProductAdapter;
import talitha_koum.milipade.com.app.afdis.dialogs.ScanCodeDialog;
import talitha_koum.milipade.com.app.afdis.models.Product;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;
import talitha_koum.milipade.com.app.afdis.responses.ProductSizeResponse;
import talitha_koum.milipade.com.app.afdis.responses.SaveResponse;
import talitha_koum.milipade.com.app.afdis.responses.StockSaveResponse;
import talitha_koum.milipade.com.app.afdis.utils.CustomToast;

public class AddStockActivity extends AppCompatActivity implements ScanCodeDialog.SimpleDialogListener  {
    ImageView ShowSelectedImage;
    public final static String TAG = AddStockActivity.class.getSimpleName();
    private static final int REQUEST_CAMERA = 23;
    private static final int SELECT_FILE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Bitmap FixBitmap;
    ProgressDialog progressDialog;
    private String mImageUrl = "";
    public static String shop_id;
    public static String shop_name;
    public static String  user_id;
    public ArrayList<Product> products= new ArrayList<Product>();
    AppCompatAutoCompleteTextView productName;
    Spinner productMl;
    private Handler handler;
    String spinnervalue;
    EditText productQuantity, productbreakeges, ProductPrice,competitorAName,competitorBName,competitorAPrice,competitorBprice,facingInline, facingProductNumber;
    SimpleDateFormat sdf;
    Date date ;
    //CustomerAdapter adapter;
    ProductAdapter adapter;
    //ArrayAdapter<String>  adapter;
    ArrayAdapter<String> spinnerAdapter;
    String[] productNames;

    String product_id;
    private Uri file   ;
    //String[] products;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        shop_id =  App.getPrefManager(this).getShopId();
        shop_name =  App.getPrefManager(this).getShopName();
        user_id = App.getPrefManager(this).getUser().getUser_id();
        getSupportActionBar().setTitle(shop_name);
        sdf = new SimpleDateFormat("yyyy-MM-dd");//0000-00-00
        date = new Date();

        getDataProducts();

        productNames =  new String[products.size()];
        ImageButton scanbtn = findViewById(R.id.scan_btn);
        ShowSelectedImage = (ImageView)findViewById(R.id.facing_image);
        Button fab = (Button) findViewById(R.id.btn_addstock);
        progressDialog =  new ProgressDialog(this);
        //CustomListAdapter ArrayAdapter adapter = new  ArrayAdapter(this , android.R.layout.expandable_list_content, getData() );
        /*adapter = new CustomListAdapter(this, R.layout.list_item_content, products, new CustomListAdapter.CustomListAdapterListener() {
            @Override
            public void onProductSelected(Product product) {
                loadSpinner(product);

                Toast.makeText(AddStockActivity.this, "Clicked item from auto completion list " + product.getProduct_name(), Toast.LENGTH_SHORT).show();

            }
        });*/

        //CustomerAdapter adapter = new CustomerAdapter(this,R.layout.autocompleteitem , getData());
        //adapter = new CustomerAdapter(this,R.layout.list_item_content,products);

        productName = (AppCompatAutoCompleteTextView) findViewById(R.id.product_name);

        adapter = new ProductAdapter(this, android.R.layout.select_dialog_item, products);
        productName.setThreshold(2);
        productName.setAdapter(adapter);
        productName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                productName.showDropDown();
                return false;
            }
        });




        productName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //loadSpinner((Product) adapterView.getItemAtPosition(i));
               //Toast.makeText(AddStockActivity.this, "Clicked item from auto completion list " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
          }
       });
        productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(AddStockActivity.this, "Clicked item from auto completion list ", Toast.LENGTH_SHORT).show();
            }
        });
       productName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Product fruit = (Product) adapterView.getItemAtPosition(i);
                // productName.setText(fruit.getProduct_name());
                product_id = fruit.getProduct_id();
                Toast.makeText(AddStockActivity.this, product_id+"", Toast.LENGTH_SHORT).show();
                //loadSpinner((Product) adapterView.getItemAtPosition(i));
                //Toast.makeText(AddStockActivity.this, "Clicked item from auto completion list " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); /**/
        //productName = (EditText) findViewById(R.id.quantity);
        productName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Product fruit = (Product) adapterView.getItemAtPosition(i);
               // productName.setText(fruit.getProduct_name());
                product_id = fruit.getProduct_id();
                Toast.makeText(AddStockActivity.this, product_id+"", Toast.LENGTH_SHORT).show();
            }
        });
        productName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkValue(charSequence+"")){

                }
                //Toast.makeText(AddStockActivity.this, charSequence+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(AddStockActivity.this, editable.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        productMl = (Spinner) findViewById(R.id.product_size);
        loadtheSpinner();
        productMl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //interesting = interested.getItemAtPosition(i).toString();
                spinnervalue= productMl.getItemAtPosition(i).toString();
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {
                }
        });

        productQuantity= (EditText) findViewById(R.id.quantity);
        productbreakeges= (EditText)findViewById(R.id.breackege);
        ProductPrice= (EditText)findViewById(R.id.product_price);
        competitorAName= (EditText)findViewById(R.id.product_name_a);
        competitorBName= (EditText)findViewById(R.id.product_name_b);
        competitorAPrice= (EditText)findViewById(R.id.product_price_a);
        competitorBprice= (EditText)findViewById(R.id.product_price_b);
        facingInline= (EditText)findViewById(R.id.facing_quantity);
        facingProductNumber= (EditText)findViewById(R.id.facing_inline);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
           // takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }


        ShowSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = Uri.fromFile(getOutputMediaFile());
                mImageUrl = file.getPath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                startActivityForResult(intent, 100);

            }
        });
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanCodeDialog dialog = new ScanCodeDialog();
                //Bundle b = new Bundle();
                ///b.putParcelable("message", message);
                // dialog.setArguments(b);
                dialog.show(getSupportFragmentManager(), "MyDialog");
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                String productname =productName.getText().toString();
                //product_id= getProductID(productname);
                String productSize = productMl.getSelectedItem().toString();
                //String productSize="";
                String quantity =productQuantity.getText().toString();
                String breakages =productbreakeges.getText().toString();
                String price =ProductPrice.getText().toString();

                String aName =competitorAName.getText().toString();
                String bName =competitorBName.getText().toString();
                String aPrice =competitorAPrice.getText().toString();
                String bPrice =competitorBprice.getText().toString();

                String inline =facingInline.getText().toString();

                String facingNumber =facingProductNumber.getText().toString();

                if (quantity.equals("") || quantity.length() == 0 || breakages.equals("") || breakages.length() == 0) {
                    new CustomToast().Show_Toast(AddStockActivity.this, view, "Please Complete the Form ");

                    return;
                }else{
                    progressDialog.setMessage("Saving Data...");

                    progressDialog.show();

                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    File file = new File(mImageUrl);
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);


                    RequestBody product_id_ = RequestBody.create(MediaType.parse("text/plain"), product_id);
                    RequestBody ShopId = RequestBody.create(MediaType.parse("text/plain"), shop_id);
                    RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"), user_id);
                    RequestBody created_date = RequestBody.create(MediaType.parse("text/plain"), sdf.format(date));

                    RequestBody product_quantity = RequestBody.create(MediaType.parse("text/plain"), quantity);
                    RequestBody product_breakages = RequestBody.create(MediaType.parse("text/plain"), breakages);
                    RequestBody product_size = RequestBody.create(MediaType.parse("text/plain"), productSize);

                    RequestBody product_inline  = RequestBody.create(MediaType.parse("text/plain"), inline);
                    RequestBody product_total_shelf = RequestBody.create(MediaType.parse("text/plain"), facingNumber);

                    RequestBody Price = RequestBody.create(MediaType.parse("text/plain"), price);

                    RequestBody competitor_name_a = RequestBody.create(MediaType.parse("text/plain"), aName);
                    RequestBody competitor_name_b = RequestBody.create(MediaType.parse("text/plain"), bName);

                    RequestBody competitor_price_a = RequestBody.create(MediaType.parse("text/plain"), aPrice);
                    RequestBody competitor_price_b = RequestBody.create(MediaType.parse("text/plain"), bPrice);

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
                            image);
                    call.enqueue(new Callback<StockSaveResponse>() {
                        @Override
                        public void onResponse(Call<StockSaveResponse> call, Response<StockSaveResponse> response) {
                            progressDialog.setMessage("Data has been Saved");
                            //Toast.makeText(AddStockActivity.this, "Unable to fetch json: " +response.body().getData() , Toast.LENGTH_LONG).show();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();

                                }
                            }, 2000);
                            clearForm();
                        }

                        @Override
                        public void onFailure(Call<StockSaveResponse> call, Throwable t) {
                            Toast.makeText(AddStockActivity.this, "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    });

                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
               // takePictureButton.setEnabled(true);
            }
        }
    }
    //private String getProductID(String productname) {
       // return productname;
   // }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ShowSelectedImage.setImageURI(file);
            }
        }
    }
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    private void getProducts() {

         //productName = new String[products.size()];
        for(int i=0;i<productNames.length;i++){
            //Toast.makeText(AddStockActivity.this, products.get(i).getProduct_name()+"", Toast.LENGTH_SHORT).show();
            productNames[i] = products.get(i).getProduct_name();
        }
        adapter.notifyDataSetChanged();
        //return productName;
    }
    public  boolean checkValue( String targetValue) {
        for (String s: productNames) {
            if (s.equals(targetValue))
                return true;
        }
        return false;
    }
    private void clearForm() {
        productName.setText("");
        //productMl.getAdapter() = null;
        productQuantity.setText("");
        productbreakeges.setText("");
        ProductPrice.setText("");
        competitorAName.setText("");
        competitorBName.setText("");
        competitorAPrice.setText("");
        competitorBprice.setText("");
        facingInline.setText("");
        facingProductNumber.setText("");
        ShowSelectedImage.setImageResource(R.drawable.image_placeholder);


    }

    AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    loadSpinner((Product) adapterView.getItemAtPosition(i));
                    Toast.makeText(AddStockActivity.this, "Clicked item from auto completion list " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                }
            };
    private void loadSpinner(Product itemAtPosition) {
        ArrayAdapter<String> adapter;
        Set<String> set = new HashSet<String>();
        String[] data = new String[]{};
        if(itemAtPosition.getSize().getSize_125()=="1"){
            set.add("125");
        }
        if(itemAtPosition.getSize().getSize_150()=="1"){
            set.add("l50");
        }
        if(itemAtPosition.getSize().getSize_175()=="1"){
            set.add("75 ml");
        }
        if(itemAtPosition.getSize().getSize_200()=="1"){
            set.add("200 ml");
        }
        if(itemAtPosition.getSize().getSize_200_pet()=="1"){
            set.add("200 ml Pet");
        }
        if(itemAtPosition.getSize().getSize_275()=="1"){
            set.add("275 ml");
        }
        if(itemAtPosition.getSize().getSize_330()=="1"){
            set.add("330 ml");
        }
        if(itemAtPosition.getSize().getSize_340()=="1"){
            set.add("340 ml");
        }
        if(itemAtPosition.getSize().getSize_346()=="1"){
            set.add("346 ml");
        }
        if(itemAtPosition.getSize().getSize_375()=="1"){
            set.add("375 ml");
        }
        if(itemAtPosition.getSize().getSize_750()=="1"){
            set.add("750 ml");
        }
        if(itemAtPosition.getSize().getSize_750_pet()=="1"){
            set.add("750 ml Pet");
        }






        List<String> list = new ArrayList<String>(set);
        spinnerAdapter = new ArrayAdapter<String>(AddStockActivity.this, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productMl.setAdapter(spinnerAdapter);
        productMl.setWillNotDraw(false);
    }
    private void loadtheSpinner() {
       // ArrayAdapter<String> adapter;
        Set<String> set = new HashSet<String>();
        String[] data = new String[]{};
       // if(itemAtPosition.getSize().getSize_125()=="1"){
            set.add("125");
       // }
      //  if(itemAtPosition.getSize().getSize_150()=="1"){
            set.add("l50");
       // }
       // if(itemAtPosition.getSize().getSize_175()=="1"){
            set.add("75");
       // }
       // if(itemAtPosition.getSize().getSize_200()=="1"){
            set.add("200");
       // }
       // if(itemAtPosition.getSize().getSize_200_pet()=="1"){
            set.add("200 Pet");
       // }
       // if(itemAtPosition.getSize().getSize_275()=="1"){
            set.add("275");
      //  }
      //  if(itemAtPosition.getSize().getSize_330()=="1"){
            set.add("330");
       // }
       // if(itemAtPosition.getSize().getSize_340()=="1"){
            set.add("340");
       // }
       /// if(itemAtPosition.getSize().getSize_346()=="1"){
            set.add("346");
       /// }
        ///if(itemAtPosition.getSize().getSize_375()=="1"){
            set.add("375");
       /// }
       /// if(itemAtPosition.getSize().getSize_750()=="1"){
            set.add("750");
       // }
       // if(itemAtPosition.getSize().getSize_750_pet()=="1"){
            set.add("750 Pet");
       // }

        List<String> list = new ArrayList<String>(set);
        spinnerAdapter = new ArrayAdapter<String>(AddStockActivity.this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productMl.setAdapter(spinnerAdapter);
        productMl.setWillNotDraw(false);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_stock, menu);
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

        return super.onOptionsItemSelected(item);
    }





    private void getDataProducts(){
        final ArrayList<Product> dataList = new ArrayList<Product>();
        //dataList.add("--Choose Product--");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductSizeResponse> call = apiService.getProduct();
        call.enqueue(new Callback<ProductSizeResponse>() {
            @Override
            public void onResponse(Call<ProductSizeResponse> call, Response<ProductSizeResponse> response) {

                ProductSizeResponse sizeResponse = response.body();


                for (Product stock : sizeResponse.getData()) {
                    // generate a random color
                    //stock.setColor(getRandomMaterialColor("400"));
                    Log.e(TAG,stock.getProduct_name());
                    products.add(stock);
                }
                productNames =  new String[products.size()];
                for(int i=0;i<productNames.length;i++){
                    //Toast.makeText(AddStockActivity.this, products.get(i).getProduct_name()+"", Toast.LENGTH_SHORT).show();
                    productNames[i] = products.get(i).getProduct_name();
                }
                Toast.makeText(AddStockActivity.this, productNames.length+"", Toast.LENGTH_SHORT).show();
               adapter = new  ProductAdapter(AddStockActivity.this, R.layout.list_item_content, products);
                //adapter = new ArrayAdapter<String>(AddStockActivity.this, android.R.layout.select_dialog_item, productNames);
                productName.setThreshold(2);
                productName.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductSizeResponse> call, Throwable t) {
                Toast.makeText(AddStockActivity.this, "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

        //return dataList;
    }

    @Override
    public void onPositiveResult(String dlg) {
        progressDialog.setMessage("Processing Barcode ...");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SaveResponse> call = apiService.getBarcode(dlg);
        call.enqueue(new Callback<SaveResponse>() {
            @Override
            public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        //productName.setText("barcode product");
                        //ToDo update text view and spinner

                    }
                }, 1000);


            }

            @Override
            public void onFailure(Call<SaveResponse> call, Throwable t) {
                Toast.makeText(AddStockActivity.this, "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });


    }

    @Override
    public void onNegativeResult(String dlg) {

    }



}
