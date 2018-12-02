package talitha_koum.milipade.com.app.afdis.mergeapp.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.mergeapp.Viewmodels.ProductViewModel;
import talitha_koum.milipade.com.app.afdis.mergeapp.adapters.ProductInputAdapter;
import talitha_koum.milipade.com.app.afdis.mergeapp.models.Product;
import talitha_koum.milipade.com.app.afdis.mergeapp.models.StockTake;


public class AddStockDialog extends DialogFragment implements ProductInputAdapter.ProductAdapterTextWatcher  {
    String  productQuantity, productbreakeges, ProductPrice,competitorAName,competitorBName,competitorAPrice,competitorBprice,facingInline, facingProductNumber,expiry, near_dated;



    public AddStockDialog() {

    }
    private final String TAG = AddStockDialog.class.getSimpleName();
    private SimpleDialogListener mHost;
    String msg;
    ProductViewModel mWordViewModel;
    RecyclerView productListview;
    private List<Product> productList = new ArrayList<>();
    private ArrayList<StockTake> stockTakes =new ArrayList<StockTake>() ;
    private ProductInputAdapter mAdapter;


    private Toast toast;

    public static AddStockDialog newInstance(String products_name) {
        AddStockDialog fragment = new AddStockDialog();
        Bundle bundle = new Bundle();
        bundle.putString("products_name", products_name);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void productQuantity(CharSequence charSequence, int position) {
        StockTake  st = stockTakes.get(position);
        st.setProduct_quantity(charSequence.toString());
    }

    @Override
    public void productbreakeges(CharSequence charSequence, int position) {
        StockTake  st = stockTakes.get(position);
        st.setProduct_breakages( charSequence.toString());
    }

    @Override
    public void ProductPrice(CharSequence charSequence, int position) {
        StockTake  st = stockTakes.get(position);
        st.setPrice(charSequence.toString());
    }

    @Override
    public void competitorAName(CharSequence charSequence, int position) {
        StockTake  st = stockTakes.get(position);
        st.setCompetitor_name_a(charSequence.toString());
    }

    @Override
    public void competitorBName(CharSequence charSequence, int position) {
        StockTake  st = stockTakes.get(position);
        st.setCompetitor_name_b(charSequence.toString());
    }

    @Override
    public void competitorAPrice(CharSequence charSequence, int position) {
        StockTake  st = stockTakes.get(position);
        st.setCompetitor_price_a(charSequence.toString());
    }

    @Override
    public void competitorBprice(CharSequence charSequence, int position) {
        StockTake  st = stockTakes.get(position);
        st.setCompetitor_name_b(charSequence.toString());
    }

    @Override
    public void facingInline(CharSequence charSequence, int position) {
        StockTake  st = stockTakes.get(position);
        st.setProduct_inline(charSequence.toString());
    }

    @Override
    public void facingProductNumber(CharSequence charSequence, int position) {
        StockTake  st = stockTakes.get(position);
        st.setProduct_total_shelf(charSequence.toString());
    }

    @Override
    public void expiry(CharSequence charSequence, int position) {
        StockTake  st = stockTakes.get(position);
        //st.setProduct_total_shelf((String) charSequence);
    }

    @Override
    public void near_dated(CharSequence charSequence, int position) {
        StockTake  st = stockTakes.get(position);
        //st.setProduct_total_shelf((String) charSequence);
    }


    public interface SimpleDialogListener {
        void onPositiveResult(String dlg);
        void onNegativeResult(String dlg);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        msg= b.getString("products_name");
        mWordViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        //final Message product = b.getParcelable("message");
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Create the custom layout using the LayoutInflater class
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.add_stock_layout,null);
        productListview = v.findViewById(R.id.product_list);
        Button add = (Button) v.findViewById(R.id.btn_confirm);
        Button cancel = (Button) v.findViewById(R.id.btncancel);
        mWordViewModel.getProducts(msg).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable final List<Product> words) {
                // Update the cached copy of the words in the adapter.
                productList=words;
                //Product p = words.get(0);
                //Snackbar.make(productListview, "Size :"+words.size(), Snackbar.LENGTH_LONG).show();
                //List<Integer> arr = Arrays.asList(new Integer[10]);
                //stockTakes = new ArrayList<StockTake>();
                for (int i = 0; i < productList.size(); i++) {
                    stockTakes.add(new StockTake());
                }

                //stockTakes = Arrays.asList(new Integer[10]);
                //toast = Toast.makeText(getContext(), "Size "+stockTakes.size(), Toast.LENGTH_LONG);
                // toast.show();
                mAdapter=new ProductInputAdapter(getContext(), (ArrayList<Product>) productList,AddStockDialog.this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                productListview.setLayoutManager(layoutManager);
                productListview.setItemAnimator(new DefaultItemAnimator());
                productListview.setAdapter(mAdapter);
                productListview.setItemViewCacheSize(productList.size());
                //mAdapter.notifyDataSetChanged();

                //mAdapter.setData(productList);
                //movieAdapter.setMovieList(getApplicationContext(),productList, HomeActivity.this );
                //movieAdapter.notifyDataSetChanged();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (stockTakes.size()>0){

                    //toast = Toast.makeText(getContext(), "Empty  "+stockTakes.size(), Toast.LENGTH_LONG);
                   // toast.show();
                //}else{
                for (StockTake stock: stockTakes) {
                    if(stock.getProduct_quantity().isEmpty() || stock.getPrice().isEmpty()){
                        toast = Toast.makeText(getContext(), "Empty ", Toast.LENGTH_LONG);
                        toast.show();
                    }else{
                        mWordViewModel.insertStock(stock);
                    }

                }

            //}

            }

        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing Dialog")
                        .setMessage("Are you sure you want to close this Dialog?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //builder.;
                                dismiss();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        //getMockData();
        builder.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing Dialog")
                        .setMessage("Are you sure you want to close this Dialog?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //builder.;
                                dismiss();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        builder.setView(v);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing Dialog")
                        .setMessage("Are you sure you want to close this Dialog?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //builder.;
                                dismiss();

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        builder.setTitle("Add Stocks For "+msg);
        return builder.create();
    }
    @Override
    public void onCancel(DialogInterface dlg) {
        super.onCancel(dlg);
        Log.i(TAG, "Dialog cancelled");
        mHost.onNegativeResult("scanning canceled");
    }
    private void getMockData() {
        //for(int i=0;i<=100;i++){
        int i=0;
        Product product = new Product();
        product.setProduct_id(""+i);
        product.setProduct_name("sample"+i);
        product.setProduct_category("3"+i);
        product.setProduct_size("200");
        product.setProduct_identifier("909"+i);
        productList.add(product);

        //}
        productListview.setItemViewCacheSize(productList.size());
        mAdapter.notifyDataSetChanged();
        //movieAdapter.setMovieList(getApplicationContext(),productList, this );

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHost = (SimpleDialogListener)activity;
    }


}

