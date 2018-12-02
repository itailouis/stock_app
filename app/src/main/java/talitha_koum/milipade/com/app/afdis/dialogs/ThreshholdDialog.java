package talitha_koum.milipade.com.app.afdis.dialogs;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.adapters.ProductAdapter;
import talitha_koum.milipade.com.app.afdis.models.Product;
import talitha_koum.milipade.com.app.afdis.models.Shop;
import talitha_koum.milipade.com.app.afdis.models.Threshold;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;
import talitha_koum.milipade.com.app.afdis.responses.ProductSizeResponse;
import talitha_koum.milipade.com.app.afdis.responses.SaveResponse;
import talitha_koum.milipade.com.app.afdis.utils.CustomToast;


public class ThreshholdDialog extends DialogFragment {
    private static final String TAG = ThreshholdDialog.class.getSimpleName();
    ProgressDialog progressDialog;
    public ArrayList<Product> products= new ArrayList<Product>();
    AppCompatAutoCompleteTextView productName;
    Spinner productMl;
    ProductAdapter adapter;
    String product_id;
    String spinnervalue;
    ArrayAdapter<String> spinnerAdapter;
    private static Dialog mDialog;
    Shop shop;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    Threshold threshold = new Threshold();
    private ThreshholdDialogInteractionListener mListener;
    public ThreshholdDialog() {

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        //mHelper = new OrderDbHelper(getActivity());
        shop = b.getParcelable("shop");


         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Create the custom layout using the LayoutInflater class
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.confirm_threshold_dialog_layout,null);
        progressDialog =  new ProgressDialog(getActivity());
        if (getActivity() instanceof ThreshholdDialogInteractionListener) {
            mListener = (ThreshholdDialogInteractionListener) getActivity();
        } else {
            throw new RuntimeException( " must implement OnFragmentInteractionListener");
        }
        mDialog = new Dialog(getActivity(),R.style.NewDialog);
        mDialog.addContentView(new ProgressBar(getActivity()), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Button add = (Button) v.findViewById(R.id.btn_confirm);
        Button cancel = (Button) v.findViewById(R.id.btncancel);

        ImageButton scanbtn = v.findViewById(R.id.scan_btn);


        productName = (AppCompatAutoCompleteTextView) v.findViewById(R.id.product_name);
        getDataProducts();
        adapter = new ProductAdapter(getActivity(), android.R.layout.select_dialog_item, products);
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
                product_id = fruit.getProduct_id();

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
                product_id = fruit.getProduct_id();
                //Toast.makeText(AddStockActivity.this, product_id+"", Toast.LENGTH_SHORT).show();
            }
        });

        productMl = (Spinner) v.findViewById(R.id.product_size);
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
        final EditText thresholdQuantity= (EditText) v.findViewById(R.id.threshold_quantity);
        final EditText thresholdPrice= (EditText) v.findViewById(R.id.threshold_price);


        final TextView shopName= (TextView) v.findViewById(R.id.productName);

       shopName.setText(shop.getShop_name());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Orders order = new Orders();
               progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Saving ...");
                progressDialog.show();
                String quantity = thresholdQuantity.getText().toString();
                String price = thresholdPrice.getText().toString();

                if (quantity.equals("") || quantity.length() == 0 || price.equals("") || price.length() == 0) {
                    new CustomToast().Show_Toast(getActivity(), v, "Please Complete the Form ");
                    progressDialog.dismiss();
                    return;
                }else{
                    progressDialog.show();

                    //order.setProposed_delivery_date(date);
                    //order.setDelivery_date(date);
                    //order.setDelivered_quantity(quantity);


                    threshold.setProduct_id(product_id);
                    threshold.setBuffer_level(quantity);
                    threshold.setShop_id(shop.getShop_id());
                    threshold.setProduct_price(price);
                    threshold.setProduct_price(spinnervalue);
                    //threshold.setDate_created( String.valueOf(""));

                    //order.setOrder_status("1");
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<SaveResponse> call = apiService.saveThreshold(threshold);
                    call.enqueue(new Callback<SaveResponse>() {
                        @Override
                        public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                            progressDialog.setMessage("Data has been Saved ");

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    mListener.onClosed();
                                    dismiss();
                                }
                            }, 2000);

                            //mListener.onClosed();
                            //mListener.onClosed();
                            //getActivity().finish();
                        }

                        @Override
                        public void onFailure(Call<SaveResponse> call, Throwable t) {
                            //Toast.makeText(getActivity().getBaseContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            //progressDialog.dismiss();

                        }
                    });
                 mListener.onClosed();
                } /**/

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(v);
        builder.setTitle("Add Threshold");
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface ThreshholdDialogInteractionListener  {
        void onClosed();
    }
    private void loadtheSpinner() {
        // ArrayAdapter<String> adapter;
        Set<String> set = new HashSet<String>();
        String[] data = new String[]{};


        // if(itemAtPosition.getSize().getSize_175()=="1"){
        set.add("75");
        // }
        //  if(itemAtPosition.getSize().getSize_150()=="1"){
        set.add("150");
        // }
        // if(itemAtPosition.getSize().getSize_125()=="1"){
        set.add("125");
        //       // }
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
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productMl.setAdapter(spinnerAdapter);
        productMl.setWillNotDraw(false);
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
               adapter = new  ProductAdapter(getContext(), R.layout.list_item_content, products);
                //adapter = new ArrayAdapter<String>(AddStockActivity.this, android.R.layout.select_dialog_item, productNames);
                productName.setThreshold(2);
                productName.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductSizeResponse> call, Throwable t) {
                //Toast.makeText(AddStockActivity.this, "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

        //return dataList;
    }
}

