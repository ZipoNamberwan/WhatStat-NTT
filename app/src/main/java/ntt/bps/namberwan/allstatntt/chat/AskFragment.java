package ntt.bps.namberwan.allstatntt.chat;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ntt.bps.namberwan.allstatntt.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AskFragment extends Fragment {


    public AskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ask, container, false);
    }

}
