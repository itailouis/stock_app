package talitha_koum.milipade.com.app.afdis.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.adapters.CustomListAdapter;
import talitha_koum.milipade.com.app.afdis.dialogs.ScanCodeDialog;
import talitha_koum.milipade.com.app.afdis.models.Product;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;
import talitha_koum.milipade.com.app.afdis.responses.ProductSizeResponse;
import talitha_koum.milipade.com.app.afdis.responses.SaveResponse;
import talitha_koum.milipade.com.app.afdis.utils.Const;
import talitha_koum.milipade.com.app.afdis.utils.CustomToast;

public class AddStockActivity extends AppCompatActivity implements ScanCodeDialog.SimpleDialogListener  {
    ImageView ShowSelectedImage;
    private static final int REQUEST_CAMERA = 23;
    private static final int SELECT_FILE = 1;
    Bitmap FixBitmap;
    ProgressDialog progressDialog;
    private String mImageUrl = "";
    public static String shop_id;
    public static String shop_name;

   AutoCompleteTextView productName;
    Spinner productMl;
    EditText productQuantity, productbreakeges, ProductPrice,competitorAName,competitorBName,competitorAPrice,competitorBprice,facingInline, facingProductNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        shop_id = intent.getStringExtra("shop_id");
        shop_name = intent.getStringExtra("shop_name");
        getSupportActionBar().setTitle(shop_name);
        ImageButton scanbtn = findViewById(R.id.scan_btn);
        ShowSelectedImage = (ImageView)findViewById(R.id.facing_image);
        Button fab = (Button) findViewById(R.id.btn_addstock);
        progressDialog =  new ProgressDialog(this);
        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.autocompleteitem, getData(), new CustomListAdapter.CustomListAdapterListener() {
            @Override
            public void onProductSelected(Product product) {
                loadSpinner(product);

                Toast.makeText(AddStockActivity.this, "Clicked item from auto completion list " + product.getProduct_name(), Toast.LENGTH_SHORT).show();

            }
        });
        productName = (AutoCompleteTextView) findViewById(R.id.product_name);

        productName.setAdapter(adapter);
        productName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loadSpinner((Product) adapterView.getItemAtPosition(i));

                Toast.makeText(AddStockActivity.this, "Clicked item from auto completion list " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
            }
        });
        //productName = (EditText) findViewById(R.id.quantity);
        productMl = (Spinner) findViewById(R.id.product_size);
        productMl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //interesting = interested.getItemAtPosition(i).toString();
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


        productQuantity= (EditText) findViewById(R.id.quantity);
        productbreakeges= (EditText)findViewById(R.id.breackege);
        ProductPrice= (EditText)findViewById(R.id.product_price);
        competitorAName= (EditText)findViewById(R.id.product_name_a);
        competitorBName= (EditText)findViewById(R.id.product_price_a);
        competitorAPrice= (EditText)findViewById(R.id.product_name_b);
        competitorBprice= (EditText)findViewById(R.id.product_price_b);
        facingInline= (EditText)findViewById(R.id.facing_quantity);
        facingProductNumber= (EditText)findViewById(R.id.facing_inline);




        ShowSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                // UploadImageOnServerButton.setEnabled(true);
                //Intent intent = new Intent();
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

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
                String productSize = productMl.getSelectedItem().toString();
                String quantity =productQuantity.getText().toString();
                String breakeges =productbreakeges.getText().toString();
                String price =ProductPrice.getText().toString();

                String aName =competitorAName.getText().toString();
                String bName =competitorBName.getText().toString();
                String aPrice =competitorAPrice.getText().toString();
                String bPrice =competitorBprice.getText().toString();

                String inline =facingInline.getText().toString();
                String facingNumber =facingProductNumber.getText().toString();

                if (quantity.equals("") || quantity.length() == 0 || breakeges.equals("") || breakeges.length() == 0) {
                    new CustomToast().Show_Toast(AddStockActivity.this, view, "Please Complete the Form ");

                    return;
                }else{
                    progressDialog.setMessage("Saving Data...");

                    progressDialog.show();

                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    File file = new File(mImageUrl);
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part image = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
                    //RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

                    Call<SaveResponse> call = apiService.saveStockTake(productname,quantity,price,aName,bName,aPrice,bPrice,inline,facingNumber,image);
                    call.enqueue(new Callback<SaveResponse>() {
                        @Override
                        public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                            progressDialog.setMessage("Data has been Saved");

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    //dismiss();
                                    //mListener.onClosed();
                                }
                            }, 2000);
                            clearForm();
                        }

                        @Override
                        public void onFailure(Call<SaveResponse> call, Throwable t) {
                            Toast.makeText(AddStockActivity.this, "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    });

                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        facingProductNumber.setText("");;

    }

    AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    loadSpinner((Product) adapterView.getItemAtPosition(i));
                    Toast.makeText(AddStockActivity.this,
                            "Clicked item from auto completion list "
                                    + adapterView.getItemAtPosition(i)
                            , Toast.LENGTH_SHORT).show();
                }
            };
    private void loadSpinner(Product itemAtPosition) {
        ArrayAdapter<String> adapter;
        Set<String> set = new HashSet<String>();
        String[] data = new String[]{};
        if(itemAtPosition.getSize().getM125()=="1"){
            set.add("125");
        }
        if(itemAtPosition.getSize().getMl50()=="1"){
            set.add("ml50");
        }
        if(itemAtPosition.getSize().getM175()=="1"){
            set.add("m175");
        }
        if(itemAtPosition.getSize().getMl200()=="1"){
            set.add("200");
        }
        if(itemAtPosition.getSize().getMl200_pet()=="1"){
            set.add("200 pet");
        }
        if(itemAtPosition.getSize().getMl275()=="1"){
            set.add("275");
        }
        if(itemAtPosition.getSize().getMl330()=="1"){
            set.add("330");
        }
        if(itemAtPosition.getSize().getMl340()=="1"){
            set.add("340");
        }
        if(itemAtPosition.getSize().getMl346()=="1"){
            set.add("346");
        }
        if(itemAtPosition.getSize().getMl375()=="1"){
            set.add("375");
        }
        if(itemAtPosition.getSize().getMl750_pet()=="1"){
            set.add("750 pet");
        }






        List<String> list = new ArrayList<String>(set);
        adapter = new ArrayAdapter<String>(AddStockActivity.this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productMl.setAdapter(adapter);
        productMl.setWillNotDraw(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri selectedImageUri = I.getData();
            mImageUrl = getPath(selectedImageUri);
            try {

                FixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ShowSelectedImage.setImageBitmap(FixBitmap);
                InputStream is = getContentResolver().openInputStream(I.getData());

                //uploadImage(mImageUrl);
                Log.e("picked",mImageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("pic faied",e.getMessage());
            }
        }
        //UploadImageOnServerButton.setEnabled(false);
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddStockActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Const.checkPermission(AddStockActivity.this);
                if (items[item].equals("Take Photo")) {
                    //userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    //userChoosenTask="Choose from Library";
                    if(result) galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private List<Product> getData(){
        final List<Product> dataList = new ArrayList<Product>();
        //dataList.add("--Choose Product--");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductSizeResponse> call = apiService.getProduct();
        call.enqueue(new Callback<ProductSizeResponse>() {
            @Override
            public void onResponse(Call<ProductSizeResponse> call, Response<ProductSizeResponse> response) {

                ProductSizeResponse sizeResponse = response.body();

                //if(sizeResponse.getStatus_code()==201) {
                for (Product stock : sizeResponse.getData()) {
                    // generate a random color
                    //stock.setColor(getRandomMaterialColor("400"));
                    dataList.add(stock);
                }
                //}
            }

            @Override
            public void onFailure(Call<ProductSizeResponse> call, Throwable t) {
                Toast.makeText(AddStockActivity.this, "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

        return dataList;
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
