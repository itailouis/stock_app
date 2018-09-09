package talitha_koum.milipade.com.app.afdis.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.models.Orders;
import talitha_koum.milipade.com.app.afdis.models.Stock;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;
import talitha_koum.milipade.com.app.afdis.responses.SaveResponse;
import talitha_koum.milipade.com.app.afdis.utils.CustomToast;


public class AddOrderDialog extends DialogFragment {
    private static final String TAG = AddOrderDialog.class.getSimpleName();
    ProgressDialog progressDialog;
   // Orders order;
   int Year;
    String Month;
    Stock stock;
    private AddOrderDialogInteractionListener mListener;
    public AddOrderDialog() {

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        //mHelper = new OrderDbHelper(getActivity());
        stock = b.getParcelable("stock");
        //final String product = b.getParcelable("product_id");

        if (getTargetFragment() instanceof AddOrderDialogInteractionListener) {
           mListener = (AddOrderDialogInteractionListener) getTargetFragment();
            } else {
            throw new RuntimeException( " must implement OnFragmentInteractionListener");
           }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Create the custom layout using the LayoutInflater class
        LayoutInflater inflater = getActivity().getLayoutInflater();
        progressDialog =  new ProgressDialog(getActivity());
        View v = inflater.inflate(R.layout.add_order_dialog_layout,null);
        Button add = (Button) v.findViewById(R.id.btn_addtocar);
        Button cancel = (Button) v.findViewById(R.id.btncancel);
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMMMMMMM");
        Month = month_date.format(c.getTime());
        Year = c.get(Calendar.YEAR);
        final EditText orderingQuantity= (EditText) v.findViewById(R.id.quantity);
        final EditText expectedDelivaryDate= (EditText) v.findViewById(R.id.delivery_date);
        expectedDelivaryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new DatePickerFragment().show(getFragmentManager(), "MyProgressDialog");
            }
        });
        final EditText productId= (EditText) v.findViewById(R.id.quantity);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Orders order = new Orders();
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Saving ...");
                progressDialog.show();
                String quantity = orderingQuantity.getText().toString();
                String date = expectedDelivaryDate.getText().toString();

                if (quantity.equals("") || quantity.length() == 0 || date.equals("") || date.length() == 0) {
                    new CustomToast().Show_Toast(getActivity(), v, "Please Complete the Form ");
                    progressDialog.dismiss();
                    return;
                }else{
                    progressDialog.show();
                    order.setProduct_id(stock.getProduct_id());
                    order.setQuantity_order(quantity);
                    order.setProposed_delivery_date(date);
                    order.setShop_id(stock.getShop_id());
                    order.setDate_created("2018-09-15 09:42:47");
                    order.setOrder_status("1");


                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<SaveResponse> call = apiService.saveStock(order);
                    call.enqueue(new Callback<SaveResponse>() {
                        @Override
                        public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                            progressDialog.setMessage("Data has been Saved");

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    dismiss();
                                    mListener.onClosed();
                                }
                            }, 2000);

                        }

                        @Override
                        public void onFailure(Call<SaveResponse> call, Throwable t) {
                            Toast.makeText(getActivity().getBaseContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    });

                }






            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(v);
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       // if (getTargetFragment() instanceof ConfirmOrderDialog.InteractionListener) {
           // mListener = (InteractionListener) getTargetFragment();
           // } else {
           // throw new RuntimeException( " must implement OnFragmentInteractionListener");
       // }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface AddOrderDialogInteractionListener {
        void onClosed();
    }
}

