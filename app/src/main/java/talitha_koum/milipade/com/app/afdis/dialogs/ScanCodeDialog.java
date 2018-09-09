package talitha_koum.milipade.com.app.afdis.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import talitha_koum.milipade.com.app.afdis.R;


public class ScanCodeDialog extends DialogFragment {

    public ScanCodeDialog() {

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        //mHelper = new OrderDbHelper(getActivity());
        //final Message product = b.getParcelable("message");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Create the custom layout using the LayoutInflater class
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.scan_layout,null);





        builder.setView(v);
        return builder.create();
    }


}

