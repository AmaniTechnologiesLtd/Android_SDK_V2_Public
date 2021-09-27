package com.amani.sdk.ui;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.amani.sdk.R;

import java.io.FileOutputStream;

import ai.amani.base.util.Amani;

public class AnimationFragment extends Fragment {

    LottieAnimationView idScanAnimation;
    Fragment fragment;
    FrameLayout bottomContainer;
    boolean frontSide;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundle();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animation, container, false);
        View view1 = inflater.inflate(R.layout.activity_nfc_scan, container,false);
        bottomContainer = view1.findViewById(R.id.bottom_container);
        init(view);
        startAnimation();
        return view;
    }

    private void init(View view) {
        idScanAnimation = view.findViewById(R.id.id_scan_animation);
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        frontSide = bundle.getBoolean("frontSide", false);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void startAnimation() {
        if (frontSide) {
            idScanAnimation.setAnimation(R.raw.trfront);

        }
        else idScanAnimation.setAnimation(R.raw.trback);

        idScanAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (frontSide) {
                    fragment = Amani.sharedInstance().IDCapture().start(getActivity(),bottomContainer,"TUR_ID_1", true, (bitmap, isDestroyed) -> {

                        if (bitmap != null) {

                            try {
                                //Write file
                                String filename = "bitmapID.png";
                                FileOutputStream stream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                                PreviewFragment previewFragment = new PreviewFragment();
                                Bundle arg = new Bundle();
                                arg.putString("image", filename);
                                arg.putBoolean("frontSide", true);
                                previewFragment.setArguments(arg);

                                ((NFCScanActivity) getActivity()).replaceFragmentWithBackStack(R.id.bottom_container, previewFragment);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                    idScanAnimation.setVisibility(View.GONE);
                    ((NFCScanActivity) getContext()).setActionBarTitle(getResources().getString(R.string.title_id_card_scan));
                    ((NFCScanActivity) getActivity()).addFragment(R.id.bottom_container, fragment,"");
                }
                else {

                    fragment = Amani.sharedInstance().IDCapture().start(getActivity(), bottomContainer, "TUR_ID_1", false, (bitmap, isDestroyed) -> {
                        if (bitmap != null) {

                            try {
                                //Write file
                                String filename = "bitmapBack.png";
                                FileOutputStream stream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                                PreviewFragment previewFragment = new PreviewFragment();
                                Bundle arg = new Bundle();
                                arg.putString("image", filename);
                                arg.putBoolean("frontSide", false);
                                previewFragment.setArguments(arg);
                                ((NFCScanActivity) getActivity()).replaceFragmentWithBackStack(R.id.bottom_container, previewFragment);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    idScanAnimation.setVisibility(View.GONE);
                    ((NFCScanActivity) getActivity()).addFragment(R.id.bottom_container, fragment,"O");

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        idScanAnimation.setVisibility(View.GONE);
    }
}
