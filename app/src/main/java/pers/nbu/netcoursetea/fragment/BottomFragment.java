package pers.nbu.netcoursetea.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pers.nbu.netcoursetea.R;

/**
 * Created by gracechan on 2015/10/16.
 */
public class BottomFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_fragment,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
