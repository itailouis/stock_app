package talitha_koum.milipade.com.app.afdis.mergeapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.mergeapp.models.Product;


/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.adapters
 */

public class ProductInputAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<Product> products;
    //private List<Product> movieListFiltered;
    private Context context;
    ProductAdapterTextWatcher listener;

    public void setData(List<Product> products) {
      this.products = products;
      notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productCate,productSize,dateMonth;
        ImageView ShowSelectedImage;
        EditText productQuantity, productbreakeges, ProductPrice,competitorAName,competitorBName,competitorAPrice,competitorBprice,facingInline, facingProductNumber,expiry, near_dated;
        SimpleDateFormat sdf;

        public ViewHolder(View view) {
            super(view);
               productSize = (TextView) itemView.findViewById(R.id.product_list_size);
                productName = (TextView) itemView.findViewById(R.id.product_list_name);
                productCate = (TextView) itemView.findViewById(R.id.product_list_cate);

            productQuantity= (EditText) itemView.findViewById(R.id.quantity);
            productbreakeges= (EditText)itemView.findViewById(R.id.breackege);
            ProductPrice= (EditText)itemView.findViewById(R.id.product_price);
            competitorAName= (EditText)itemView.findViewById(R.id.product_name_a);
            competitorBName= (EditText)itemView.findViewById(R.id.product_name_b);
            competitorAPrice= (EditText)itemView.findViewById(R.id.product_price_a);
            competitorBprice= (EditText)itemView.findViewById(R.id.product_price_b);
            facingInline= (EditText)itemView.findViewById(R.id.facing_quantity);
            facingProductNumber= (EditText)itemView.findViewById(R.id.facing_inline);
            ShowSelectedImage = (ImageView)itemView.findViewById(R.id.facing_image);

        }
    }


    public ProductInputAdapter(Context context, ArrayList<Product> products, ProductAdapterTextWatcher listener ) {
        this.context = context;
        this.products = products;
        this.listener= listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_form, parent, false);


        return new ViewHolder(itemView);
    }
    private void applyTextEvents(ViewHolder holder, final int position) {
        ((ViewHolder) holder).productQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.productQuantity(charSequence,position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).productbreakeges.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.productbreakeges(charSequence,position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).ProductPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.ProductPrice(charSequence,position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).competitorAName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.competitorAName(charSequence,position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).competitorBName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.competitorBName(charSequence,position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).competitorAPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.competitorAPrice(charSequence,position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).competitorBprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.competitorBprice(charSequence,position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).facingInline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.facingInline(charSequence,position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).facingProductNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.facingProductNumber(charSequence,position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).facingInline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.facingInline(charSequence,position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).facingProductNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.facingProductNumber(charSequence,position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
           Product order = products.get(position);
            ((ViewHolder) holder).productName.setText(order.getProduct_name());
            ((ViewHolder) holder).productCate.setText(order.getProduct_category());
            ((ViewHolder) holder).productSize.setText(order.getProduct_size());
        ((ViewHolder) holder).ShowSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ShowSelectedImage.
                listener.facingImagePicker(position);
            }});

                applyTextEvents((ViewHolder) holder , position);







        ((ViewHolder) holder).competitorAPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).competitorBprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).facingInline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((ViewHolder) holder).facingProductNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        })
        ;


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

public interface ProductAdapterTextWatcher{

    void productQuantity(CharSequence charSequence, int position);
    void productbreakeges(CharSequence charSequence, int position);
    void ProductPrice(CharSequence charSequence, int position);
    void competitorAName(CharSequence charSequence, int position);
    void competitorBName(CharSequence charSequence, int position);
    void  competitorAPrice(CharSequence charSequence, int position);
    void  competitorBprice(CharSequence charSequence, int position);
    void  facingInline(CharSequence charSequence, int position);
    void  facingProductNumber(CharSequence charSequence, int position);
    void facingImagePicker(int position);
    void  expiry(CharSequence charSequence, int position);
    void  near_dated(CharSequence charSequence, int position);

}

}


