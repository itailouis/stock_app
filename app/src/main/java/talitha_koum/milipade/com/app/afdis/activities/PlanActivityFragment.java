package talitha_koum.milipade.com.app.afdis.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import talitha_koum.milipade.com.app.afdis.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlanActivityFragment extends Fragment {

    public PlanActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }
}
