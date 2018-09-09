package talitha_koum.milipade.com.app.afdis.dialogs;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.models.Orders;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;
import talitha_koum.milipade.com.app.afdis.responses.SaveResponse;
import talitha_koum.milipade.com.app.afdis.utils.CustomToast;


public class ConfirmOrderDialog extends DialogFragment {
    private static final String TAG = ConfirmOrderDialog.class.getSimpleName();
    ProgressDialog progressDialog;
    Orders order;
    private ConfirmOrderDialogInteractionListener mListener;
    public ConfirmOrderDialog() {

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        //mHelper = new OrderDbHelper(getActivity());
        order = b.getParcelable("order");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Create the custom layout using the LayoutInflater class
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.confirm_order_dialog_layout,null);
        progressDialog =  new ProgressDialog(getActivity());
        if (getTargetFragment() instanceof ConfirmOrderDialogInteractionListener) {
            mListener = (ConfirmOrderDialogInteractionListener) getTargetFragment();
        } else {
            throw new RuntimeException( " must implement OnFragmentInteractionListener");
        }
        Button add = (Button) v.findViewById(R.id.btn_confirm);
        Button cancel = (Button) v.findViewById(R.id.btncancel);


        final TextView productName= (TextView) v.findViewById(R.id.productName);
        final TextView quantityOrdered= (TextView) v.findViewById(R.id.price_text);
        final TextView totalprice= (TextView) v.findViewById(R.id.totalPrice);

        productName.setText(order.getProduct_name());
        quantityOrdered.setText(order.getQuantity_order());
        totalprice.setText(order.getProposed_delivery_date());

        final EditText orderingQuantity= (EditText) v.findViewById(R.id.edit_quantity);
        final EditText expectedDelivaryDate= (EditText) v.findViewById(R.id.delivared_date);





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Orders order = new Orders();
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

                    //order.setProposed_delivery_date(date);
                    order.setDelivery_date(date);
                    order.setDelivered_quantity(quantity);

                    order.setOrder_status("1");
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<SaveResponse> call = apiService.saveOrder(order);
                    call.enqueue(new Callback<SaveResponse>() {
                        @Override
                        public void onResponse(Call<SaveResponse> call, Response<SaveResponse> response) {
                            //progressDialog.setMessage(response.body().getMessage());

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    mListener.onClosed();
                                    dismiss();
                                }
                            }, 2000);

                            //mListener.onClosed();
                            //mListener.onClosed();
                            //getActivity().finish();
                        }

                        @Override
                        public void onFailure(Call<SaveResponse> call, Throwable t) {
                            //Toast.makeText(getActivity().getBaseContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    });
                    //mListener.onClosed();
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface ConfirmOrderDialogInteractionListener  {
        void onClosed();
    }

}

