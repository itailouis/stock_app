package talitha_koum.milipade.com.app.afdis.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.activities.OrderHistoryActivity;
import talitha_koum.milipade.com.app.afdis.activities.ShopActivity;
import talitha_koum.milipade.com.app.afdis.adapters.OrdersHistoryAdapter;
import talitha_koum.milipade.com.app.afdis.dialogs.ConfirmOrderDialog;
import talitha_koum.milipade.com.app.afdis.models.OrdersHistory;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;
import talitha_koum.milipade.com.app.afdis.responses.OrderHistoryResponse;
import talitha_koum.milipade.com.app.afdis.utils.SimpleDividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryOrdersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryOrdersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,ConfirmOrderDialog.ConfirmOrderDialogInteractionListener, OrdersHistoryAdapter.InventoryHistoryAdapterListener {
    private static final String TAG =HistoryOrdersFragment.class.getSimpleName();
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private OrdersHistoryAdapter adapter;
    private ArrayList<OrdersHistory> orders;
    private View viewmain;
    public SwipeRefreshLayout swipeRefreshLayout;
    private OnFragmentInteractionListener mListener;


    public HistoryOrdersFragment() {

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
    public static HistoryOrdersFragment newInstance(String param1, String param2) {
        final HistoryOrdersFragment fragment = new HistoryOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        //Toast.makeText(getContext(), "shop id " +mParam1, Toast.LENGTH_LONG).show();
        // self user id is to identify the message owner
        //String selfUserId = App.getInstance().getPrefManager().getUser().getId();

        adapter = new OrdersHistoryAdapter(getContext(), orders, this);
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

        recyclerView.addOnItemTouchListener(new OrdersHistoryAdapter.RecyclerTouchListener(getContext(), recyclerView, new OrdersHistoryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                OrdersHistory history = orders.get(position);
                Intent intent = new Intent(getContext(),OrderHistoryActivity.class);

                Bundle b = new Bundle();
                b.putString("shop_id", ShopActivity.shop_id);
                b.putString("shop_name", ShopActivity.shop_name);
                b.putParcelableArrayList("orders", history.getOrders());
              Toast.makeText(getContext(), "size: " + position, Toast.LENGTH_LONG).show();
                intent.putExtras(b);
                //intent.putParcelableArrayListExtra("date_created", history.getOrders());
                //startActivity(intent);
                getActivity().startActivity(intent);

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

        Call<OrderHistoryResponse> call = apiService.getOrderHistory(mParam1);
        call.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                // clear the inbox
                orders.clear();

                // add all the messages
                // messages.addAll(response.body());
                Log.e(TAG, "In loop "+response.body().getStatus_code());

                // the loop was performed to add colors to each message
                for (OrdersHistory message : response.body().getData()) {
                    // generate a random color
                    //message.setColor(getRandomMaterialColor("400"));
                    Log.e(TAG, "element added");
                    orders.add(message);
                }

                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onContactSelected(OrdersHistory contact) {

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
        void onFragmentInteraction(Uri uri);
    }
}
