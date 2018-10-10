package talitha_koum.milipade.com.app.afdis.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import talitha_koum.milipade.com.app.afdis.activities.AddStockActivity;
import talitha_koum.milipade.com.app.afdis.activities.ShopActivity;
import talitha_koum.milipade.com.app.afdis.adapters.StocksAdapter;
import talitha_koum.milipade.com.app.afdis.dialogs.AddOrderDialog;
import talitha_koum.milipade.com.app.afdis.models.Stock;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;
import talitha_koum.milipade.com.app.afdis.responses.StockResponse;
import talitha_koum.milipade.com.app.afdis.utils.SimpleDividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InventoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,AddOrderDialog.AddOrderDialogInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private StocksAdapter adapter;
    private ArrayList<Stock> stocks;
    private View mainview ;
    FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OnFragmentInteractionListener mListener;

    public InventoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventoryFragment.
     */
    static InventoryFragment fragment;
    // TODO: Rename and change types and number of parameters
    public static InventoryFragment newInstance(String param1, String param2) {
         fragment = new InventoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    private void setOnDialogClickedListener() {

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
        mainview =inflater.inflate(R.layout.fragment_inventory, container, false);
        fab = (FloatingActionButton) mainview.findViewById(R.id.fab);

        recyclerView = (RecyclerView) mainview.findViewById(R.id.list_stocks);
        swipeRefreshLayout = (SwipeRefreshLayout) mainview.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        stocks = new ArrayList<>();
       // Toast.makeText(getContext(), "shop id " +mParam1, Toast.LENGTH_LONG).show();



        adapter = new StocksAdapter(getContext(), stocks);
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

        recyclerView.addOnItemTouchListener(new StocksAdapter.RecyclerTouchListener(getContext(), recyclerView, new StocksAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AddOrderDialog dialog = new AddOrderDialog();
                dialog.setTargetFragment(InventoryFragment.this, 0);
                Bundle b = new Bundle();
                b.putParcelable("stock",stocks.get(position));
                dialog.setArguments(b);
                dialog.show(getActivity().getSupportFragmentManager(), "MyDialog");


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddStockActivity.class);
                intent.putExtra("shop_id", ShopActivity.shop_id);
                intent.putExtra("shop_name", ShopActivity.shop_name);
                startActivity(intent);
                getActivity().startActivity(intent);

               // AddOrderDialog dialog = new AddOrderDialog();
                //Bundle b = new Bundle();
                ///b.putParcelable("message", message);
                // dialog.setArguments(b);
                //dialog.show(getActivity().getFragmentManager(), "MyDialog");
            }
        });

        return mainview;

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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    private void getInbox() {
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<StockResponse> call = apiService.getStocks(ShopActivity.shop_id);
        call.enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                // clear the inbox
                stocks.clear();
                StockResponse stockResponse = response.body();

                if(stockResponse.getStatus_code()==201) {
                    for (Stock stock : stockResponse.getData()) {
                        // generate a random color
                        //stock.setColor(getRandomMaterialColor("400"));
                        stocks.add(stock);
                    }
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClosed() {
        //Toast.makeText(getContext(), "onClosed() ", Toast.LENGTH_LONG).show();
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getInbox();
                    }
                }
        );
    }


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
