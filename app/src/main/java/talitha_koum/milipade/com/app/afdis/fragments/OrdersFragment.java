package talitha_koum.milipade.com.app.afdis.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.adapters.OrdersAdapter;
import talitha_koum.milipade.com.app.afdis.dialogs.ConfirmOrderDialog;
import talitha_koum.milipade.com.app.afdis.models.Orders;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;
import talitha_koum.milipade.com.app.afdis.responses.OrderResponse;
import talitha_koum.milipade.com.app.afdis.utils.SimpleDividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrdersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,ConfirmOrderDialog.ConfirmOrderDialogInteractionListener{
    private static final String TAG =OrdersFragment.class.getSimpleName();
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private ArrayList<Orders> orders;
    private View viewmain;
    public SwipeRefreshLayout swipeRefreshLayout;
    private OnFragmentInteractionListener mListener;


    public OrdersFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        final OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewmain =inflater.inflate(R.layout.fragment_orders, container, false);

        recyclerView = (RecyclerView) viewmain.findViewById(R.id.list_orders);
        swipeRefreshLayout = (SwipeRefreshLayout) viewmain.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        orders = new ArrayList<>();
        //orders = ShopMock.getOrderList();
        Toast.makeText(getContext(), "shop id " +mParam1, Toast.LENGTH_LONG).show();
        // self user id is to identify the message owner
        //String selfUserId = App.getInstance().getPrefManager().getUser().getId();
        String selfUserId = "modock_id";
        adapter = new OrdersAdapter(getContext(), orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getInbox();
                    }
                }
        );

        recyclerView.addOnItemTouchListener(new OrdersAdapter.RecyclerTouchListener(getContext(), recyclerView, new OrdersAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Orders order =new Orders();
                Orders order =orders.get(position);
                //order.setShop_id(mParam1);
                //order.setProduct_id(orders.get(position).getProduct_id());

                ConfirmOrderDialog dialog = new ConfirmOrderDialog();
                dialog.setTargetFragment(OrdersFragment.this, 0);
                Bundle b = new Bundle();

                b.putParcelable("order",order);
                dialog.setArguments(b);
                dialog.show(getActivity().getSupportFragmentManager(), "MyDialog");

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return viewmain;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                       getInbox();
                    }
                }
        );
    }
    private void getInbox() {
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<OrderResponse> call = apiService.getOrders(mParam1);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                // clear the inbox
                orders.clear();

                // add all the messages
                // messages.addAll(response.body());
                Log.e(TAG, "In loop "+response.body().getStatus_code());

                // the loop was performed to add colors to each message
                for (Orders message : response.body().getData()) {
                    // generate a random color
                    //message.setColor(getRandomMaterialColor("400"));
                    Log.e(TAG, "element added");
                    orders.add(message);
                }

                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }




    @Override
    public void onClosed() {
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getInbox();
                    }
                }
        );
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the fragment menu items.
        inflater.inflate(R.menu.menu_item_search, menu);

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.product_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

    }

    // Invoked when activity menu item is clicked.
    // It also will be invoked when use click fragment menu item.
    // So you need to write code to check which menu item trigger this click event.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        boolean showMessage = false;


        return super.onOptionsItemSelected(item);
    }
}
