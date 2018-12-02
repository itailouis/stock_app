package talitha_koum.milipade.com.app.afdis.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.models.Shop;

/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.adapters
 */
public class ShopAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static String TAG = ShopAdapter.class.getSimpleName();

    private String userId;
    private int SELF = 100;
    private static String today;
    private ShopAdapterListener listener;
    private Context mContext;
    private ArrayList<Shop> shops;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, lastVisited;
        ImageView more;
        RelativeLayout container;


        public ViewHolder(View view) {
            super(view);
            shopName = (TextView) itemView.findViewById(R.id.shopname);
            lastVisited = (TextView) itemView.findViewById(R.id.lastvisted);
            more = itemView.findViewById(R.id.more_btn);
            container = itemView.findViewById(R.id.container);

        }
    }


    public ShopAdapter(Context mContext, ArrayList<Shop> shops, String userId, ShopAdapterListener listener) {
        this.mContext = mContext;
        this.shops = shops;
        this.userId = userId;
        this.listener = listener;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
       //if (viewType == SELF) {
            // self message
            //itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_active, parent, false);
       // } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_active , parent, false);
        //}


        return new ViewHolder(itemView);
    }


    @Override
    public int getItemViewType(int position) {
        Shop shop = shops.get(position);
        //if (shop.getUser().getId().equals(userId)) {
        if (true) {
            return SELF;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Shop shop = shops.get(position);
     ((ViewHolder) holder).shopName.setText(shop.getShop_name());
     //((ViewHolder) holder).lastVisited.setText(shop.getLast_visited());
     ((ViewHolder) holder).lastVisited.setText(getTimeStamp(shop.getLast_visited()));
        applyClickEvents((ViewHolder) holder, position);
    }

    private void applyClickEvents(final ViewHolder holder, final int position) {
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listener.onClick(position);
                showPopupMenu( holder.more, holder, position);
            }
        });
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickContainer(position);
                //showPopupMenu( holder.more, holder, position);
            }
        });
    }

    private void showPopupMenu(View view, ViewHolder holder, int position) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_shop_item, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(holder,position));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        ViewHolder holder;
        View view;
        int position;
        public MyMenuItemClickListener(ViewHolder holder,int position) {
            this.holder=  holder;
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_view_more:
                    listener.onClickViewThreshhold(position);
                    //Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_view_graph:
                    listener.onClick(position);
                    //Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    //holder.full.setVisibility(View.VISIBLE);
                    //holder.mini.setVisibility(View.VISIBLE);
                    //holder.chart.setVisibility(View.VISIBLE);
                    return true;
                default:
            }
            return false;
        }
    }


    @Override
    public int getItemCount() {
        return shops.size();
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
    public interface ShopAdapterListener {
        void onClick(int position);
       void onClickViewThreshhold(int position);
        void onLongClick(View view, int position);

        void onClickContainer(int position);
    }
   /* public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ShopAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ShopAdapter.ClickListener clickListener) {
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
    }*/
}
