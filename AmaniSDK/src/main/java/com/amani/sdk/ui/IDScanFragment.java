package com.amani.sdk.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.amani.sdk.R;
import com.amani.sdk.base.cb.CallBack;
import com.amani.sdk.base.cb.CallBackInternal;


public class IDScanFragment extends Fragment {

    Button idScanButton;
    Button exitButton;
    FrameLayout frameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_i_d_scan, container, false);
        idScanButton = view.findViewById(R.id.button_id);
        exitButton = view.findViewById(R.id.bttn_exit);
        frameLayout = view.findViewById(R.id.bottom_container);
        clickEvents();
        return view;
    }

    private void clickEvents() {
        idScanButton.setOnClickListener(v -> {

            AnimationFragment animationFragmentFront = new AnimationFragment();
            Bundle arg = new Bundle();
            arg.putBoolean("frontSide", true);
            animationFragmentFront.setArguments(arg);
            ((NFCScanActivity) getActivity()).replaceFragmentWithBackStack(R.id.bottom_container, animationFragmentFront);

        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                if (CallBackInternal.listener != null) CallBackInternal.listener.courierRequested(true);
            }
        });
    }

    @Override
    public void onDestroy() {
        ((NFCScanActivity) getContext()).setActionBarTitle(getResources().getString(R.string.title_id_card_scan));
        super.onDestroy();
    }
}