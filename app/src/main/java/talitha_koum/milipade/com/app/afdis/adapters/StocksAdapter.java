package talitha_koum.milipade.com.app.afdis.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import talitha_koum.milipade.com.app.afdis.models.Stock;


/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.adapters
 */

public class StocksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

        private static String TAG = StocksAdapter.class.getSimpleName();



        private static String today;

        private Context mContext;
        private ArrayList<Stock> stocks;
    public ArrayList<Stock> _data;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        stocks.clear();
        if (charText.length() == 0) {
            Log.d(TAG,"charText  "+stocks.size());

            stocks.addAll(_data);
        } else {
            for (Stock wp : _data) {
                if (wp.getProduct_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    Log.d(TAG,"charText 2 "+stocks.size());
                    stocks.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView productName, productQuantity,productBreakages, productFlag,dateDay,dateMonth;

            public ViewHolder(View view) {
                super(view);
                dateDay = (TextView) itemView.findViewById(R.id.date_day);
                dateMonth = (TextView) itemView.findViewById(R.id.date_month);
                productName = (TextView) itemView.findViewById(R.id.product_name);
                productQuantity = (TextView) itemView.findViewById(R.id.product_quantity);
                productBreakages = (TextView) itemView.findViewById(R.id.product_breakages);
                productFlag = (TextView) itemView.findViewById(R.id.product_flag);
            }
        }


        public StocksAdapter(Context mContext, ArrayList<Stock> stocks) {
            this.mContext = mContext;
            this.stocks = stocks;
            this._data = new ArrayList<>();
            this._data.addAll(stocks);
            Log.d(TAG,"StocksAdapter  "+stocks.size());
            Calendar calendar = Calendar.getInstance();
            today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;

         itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_stock, parent, false);


            return new StocksAdapter.ViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            Stock stock = stocks.get(position);
            ((StocksAdapter.ViewHolder) holder).productName.setText(stock.getProduct_name());

            String timestamp = getTimeStamp("");

            //if (shop.getUser().getName() != null)
            //timestamp = shop.getUser().getName() + ", " + timestamp;
            if(stock.getInstock_diff()!=null){
                if(Integer.parseInt(stock.getInstock_diff())<=0){
                    ((ViewHolder) holder).productFlag.setBackgroundResource(R.drawable.bg_circle_red);
                }else{
                    ((ViewHolder) holder).productFlag.setBackgroundResource(R.drawable.bg_circle);
                }
            }else{
                ((ViewHolder) holder).productFlag.setVisibility(View.INVISIBLE);
            }


            ((StocksAdapter.ViewHolder) holder).dateDay.setText(getDay(stock.getDate_created()));
            ((StocksAdapter.ViewHolder) holder).dateMonth.setText(getMonth(stock.getDate_created()));
            ((StocksAdapter.ViewHolder) holder).productFlag.setText("Buffer level:"+stock.getInstock_diff());
            ((StocksAdapter.ViewHolder) holder).productQuantity.setText("Quantity :"+stock.getProduct_quantity());
            ((StocksAdapter.ViewHolder) holder).productBreakages.setText("Breakages :"+stock.getBreakages());
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

    @Override
        public int getItemCount() {
            return stocks.size();
        }

        public static String getTimeStamp(String dateStr) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = "";

            today = today.length() < 2 ? "0" + today : today;

            try {
                Date date = format.parse(dateStr);
                SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
                String dateToday = todayFormat.format(date);
                format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
                String date1 = format.format(date);
                timestamp = date1.toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return timestamp;
        }
        public interface ClickListener {
            void onClick(View view, int position);
            void onLongClick(View view, int position);
        }
        public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

            private GestureDetector gestureDetector;
            private StocksAdapter.ClickListener clickListener;

            public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final StocksAdapter.ClickListener clickListener) {
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


