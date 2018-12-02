package talitha_koum.milipade.com.app.afdis.dialogs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
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
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
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

        productName.setText(order.getProduct_name()+" "+order.getProduct_size()+" ML");
        quantityOrdered.setText("Quantity :"+order.getQuantity_ordered());
        totalprice.setText(order.getProposed_delivery_date());

        final EditText orderingQuantity= (EditText) v.findViewById(R.id.threshold_price);
        final EditText expectedDelivaryDate= (EditText) v.findViewById(R.id.threshold_quantity);
        expectedDelivaryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                expectedDelivaryDate.setText(( year + "-" + (month + 1) + "-" +day));
                            }
                        }, year, month, dayOfMonth);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.setTitle("Select Delivered Date:");
                long minTimeMillis = System.currentTimeMillis() + 7 * 3600000;
                final Calendar minDate = Calendar.getInstance();
                minDate.setTimeInMillis(minTimeMillis);
                minDate.set(Calendar.HOUR_OF_DAY, minDate.getMinimum(Calendar.HOUR_OF_DAY));
                minDate.set(Calendar.MINUTE, minDate.getMinimum(Calendar.MINUTE));
                minDate.set(Calendar.SECOND, minDate.getMinimum(Calendar.SECOND));
                minDate.set(Calendar.MILLISECOND, minDate.getMinimum(Calendar.MILLISECOND));
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
                datePickerDialog.show();

            }
        });





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
        builder.setTitle("Confirm Order");
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

