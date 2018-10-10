package talitha_koum.milipade.com.app.afdis.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.models.Orders;

/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.adapters
 */

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

        private static String TAG = OrdersAdapter.class.getSimpleName();
        private static String today;

        private Context mContext;
        private ArrayList<Orders> orders;
    public ArrayList<Orders> _data;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        orders.clear();
        if (charText.length() == 0) {
            orders.addAll(_data);
        } else {
            for (Orders wp : _data) {
                if (wp.getProduct_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    orders.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView productName, productQuantity,dateDay,dateMonth;

            public ViewHolder(View view) {
                super(view);
                dateDay = (TextView) itemView.findViewById(R.id.date_day);
                dateMonth = (TextView) itemView.findViewById(R.id.date_month);
                productName = (TextView) itemView.findViewById(R.id.product_name);
                productQuantity = (TextView) itemView.findViewById(R.id.product_quantity);
            }
        }


        public OrdersAdapter(Context mContext, ArrayList<Orders> orders) {
            this.mContext = mContext;
            this.orders = orders;
            this._data = new ArrayList<>();
            this._data.addAll(orders);
            Calendar calendar = Calendar.getInstance();
            today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;

         itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order, parent, false);


            return new OrdersAdapter.ViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            Orders order = orders.get(position);
            ((OrdersAdapter.ViewHolder) holder).productName.setText(order.getProduct_name());
            ((OrdersAdapter.ViewHolder) holder).dateDay.setText(getDay(order.getDate_created()));
            ((OrdersAdapter.ViewHolder) holder).dateMonth.setText(getMonth(order.getDate_created()));
            ((OrdersAdapter.ViewHolder) holder).productQuantity.setText("Quantity ordered :"+order.getQuantity_ordered());
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

    private String getMonth(String date_created) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateToday = "";


        try {
            Date date = format.parse(date_created);
            SimpleDateFormat todayFormat = new SimpleDateFormat("MM-yyyy");
            dateToday = todayFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateToday;
    }

    private String getDay(String date_created) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateToday = "";
        try {
            Date date = format.parse(date_created);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            dateToday = todayFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateToday;
    }
        public interface ClickListener {
            void onClick(View view, int position);

            void onLongClick(View view, int position);
        }
        public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

            private GestureDetector gestureDetector;
            private OrdersAdapter.ClickListener clickListener;

            public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final OrdersAdapter.ClickListener clickListener) {
                this.clickListener = clickListener;
                gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && clickListener != null) {
                            clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                        }
                    }
                });
            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                    clickListener.onClick(child, rv.getChildPosition(child));
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        }
    }


