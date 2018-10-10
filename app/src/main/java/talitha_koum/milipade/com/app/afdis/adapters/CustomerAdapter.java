package talitha_koum.milipade.com.app.afdis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.models.Product;

/**
 * Created by TALITHA_KOUM on 19/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.adapters
 */
public class CustomerAdapter extends ArrayAdapter<Product> {
    private final String MY_DEBUG_TAG = "CustomerAdapter";
    private List<Product> items;
    private List<Product> itemsAll;
    private ArrayList<Product> suggestions;
    private int viewResourceId;

    public CustomerAdapter(Context context, int viewResourceId, List<Product> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll =  items;
        this.suggestions = new ArrayList<Product>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        Product customer = items.get(position);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (customer != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.textView);
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
                customerNameLabel.setText(customer.getProduct_name());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Product)(resultValue)).getProduct_name();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Product customer : itemsAll) {
                    if(customer.getProduct_name().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Product> filteredList = (ArrayList<Product>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Product c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}