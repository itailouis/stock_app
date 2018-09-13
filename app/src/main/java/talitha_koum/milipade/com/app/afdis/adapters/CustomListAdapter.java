package talitha_koum.milipade.com.app.afdis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.models.Product;

/**
 * Created by TALITHA_KOUM on 10/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.adapters
 */
public class CustomListAdapter extends ArrayAdapter<Product> implements Filterable, View.OnClickListener {


    private List<Product> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<Product> dataListAllItems;
    private CustomListAdapterListener listener;
    public interface CustomListAdapterListener {
        void onProductSelected(Product product);
    }


    public CustomListAdapter(Context context, int resource, List<Product> storeDataLst,CustomListAdapterListener listener ) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
        this.listener= listener;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    //@Override
    //public String getItem(int position) {
       // Log.d("CustomListAdapter", dataList.get(position).getProduct_name());
       // return dataList.get(position).getProduct_name();
   // }

    //@Nullable
    //@Override
    //public Object getItem(int position) {
       // return super.getItem(position);
    //}

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

       if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        }
        Product product = getItem(position);;
        TextView strName = (TextView) view.findViewById(R.id.textView);
        strName.setText(product.getProduct_name());
        view.setOnClickListener(this);
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    @Override
    public void onClick(View view) {
        //int position=(Integer) view.getTag();
       // Object object= getItem(position);
       // Product dataModel=(Product)object;
        //listener.onProductSelected(dataModel);
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<Product>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<String> matchValues = new ArrayList<String>();

                for (Product dataItem : dataListAllItems) {
                    if (dataItem.getProduct_name().toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem.getProduct_name());
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<Product>)results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
