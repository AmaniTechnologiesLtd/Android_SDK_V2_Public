package com.amani.sdk.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.amani.sdk.R;
import com.amani.sdk.base.cb.CallBack;
import com.amani.sdk.base.cb.CallBackInternal;
import com.amani.sdk.ui.activity.NFCScanActivity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import ai.amani.sdk.Amani;
import datamanager.model.customer.Errors;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PreviewFragment extends Fragment {

    ImageView imageID;
    Button buttonConfirm, buttonExit;
    Fragment selfieFragment;
    Boolean frontSide, selfie;
    FrameLayout frameLayout;
    TextView subTitle;
    static int maxAttemptSelfie, maxAttemptID = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        imageID = view.findViewById(R.id.image_id);
        buttonConfirm = view.findViewById(R.id.bttn_id_confirm);
        buttonExit = view.findViewById(R.id.bttn_id_exit);
        frameLayout = view.findViewById(R.id.container_bottom_id_prew);
        subTitle = view.findViewById(R.id.id_sub_title);
        getBundle();
        clickEvents();
        return view;
    }

    private void getBundle() {

        Bitmap bmp = null;
        Bundle bundle = getArguments();

        try {
            String filename = bundle.getString("image");
            frontSide = bundle.getBoolean("frontSide", false);
            selfie = bundle.getBoolean("selfie" , false);
            try {
                FileInputStream is = getActivity().openFileInput(filename);
                bmp = BitmapFactory.decodeStream(is);
                is.close();
                if (!frontSide) {
                    subTitle.setText(getResources().getText(R.string.id_back_confirm_text));
                    ((NFCScanActivity) getContext()).setActionBarTitle(getResources().getString(R.string.front_side_photo));
                }

                if (!selfie) ((NFCScanActivity) getContext()).setActionBarTitle(getResources().getString(R.string.choose_id_type));
                else {
                    ((NFCScanActivity) getContext()).setActionBarTitle(getResources().getString(R.string.biometric_verification));
                    subTitle.setText(getResources().getText(R.string.selfie_confirm_text));
                }
                imageID.setImageBitmap(bmp);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }catch (Exception e){

        }

    }



    private void clickEvents() {

        buttonConfirm.setOnClickListener(v -> {

            if (frontSide) {

                AnimationFragment animationFragmentBack = new AnimationFragment();
                Bundle arg = new Bundle();
                arg.putBoolean("frontSide", false);
                animationFragmentBack.setArguments(arg);

                ((NFCScanActivity) getActivity()).replaceFragmentWithBackStack(R.id.bottom_container, animationFragmentBack);

            }
            else if(selfie) {

                ((NFCScanActivity) getActivity()).showProgressLoader();
                Amani.sharedInstance().Selfie().upload(getActivity(),"XXX_SE_0", (isSuccess, result, errors) -> {

                    if (isSuccess){
                        Log.d("TAG", "selfie: Selfie is uploaded!");
                        ((NFCScanActivity) getActivity()).dismissLoader();

                        if (result.equals("OK")) {
                            CallBack.listener.selfieUpload(true);
                            Log.d("TAG", "Selfie: Pending review: " + "result: " + result);
                            getActivity().finish();
                            if (CallBackInternal.listener != null) CallBackInternal.listener.lastStepCompleted(true);
                        }else{
                            maxAttemptSelfie = maxAttemptSelfie + 1;
                            if (isErrorCodeExist(errors,2004) && maxAttemptSelfie != 3) {
                                CallBack.listener.selfieUpload(false);
                                ((NFCScanActivity) getActivity()).showBottomSheetDialog(getResources().getString(R.string.selfie_try_again), getResources().getString(R.string.error_2004_message), "SE_SCREEN");

                            } else if (maxAttemptSelfie == 3) {
                                CallBack.listener.selfieUpload(false);
                                ((NFCScanActivity) getActivity()).showBottomSheetDialog(getResources().getString(R.string.video_onboarding_failed), getResources().getString(R.string.selfie_failed_desc), "COURIER");
                            } else {
                                CallBack.listener.selfieUpload(false);
                                ((NFCScanActivity) getActivity()).showBottomSheetDialog(getResources().getString(R.string.selfie_try_again), getResources().getString(R.string.selfie_try_again_desc), "SE_SCREEN");
                                Log.d("TAG", "Selfie: Invalid doc " + "result: " + result);
                            }
                        }

                    } else {
                        ((NFCScanActivity) getActivity()).dismissLoader();
                        Log.d("TAG", "SELFIE UPLOAD NOT SUCCESS ");
                    }

                });
            }
            else {

                ((NFCScanActivity) getActivity()).showProgressLoader();
                Amani.sharedInstance().IDCapture().upload(getActivity(), "TUR_ID_1", (uploadIDSuccess, result,errors) -> {

                    selfieFragment = Amani.sharedInstance().Selfie().start("XXX_SE_0",(bitmap, isDestroyed,file) -> {

                        if (isDestroyed) Log.d("TAG", "SelfiFragment: is destroyed");

                        if (bitmap != null) {
                            try {
                                //Write file
                                String filename = "bitmapSelfie.png";
                                FileOutputStream stream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                                PreviewFragment previewFragment = new PreviewFragment();
                                Bundle arg = new Bundle();
                                arg.putString("image", filename);
                                arg.putBoolean("selfie", true);
                                previewFragment.setArguments(arg);
                                ((NFCScanActivity) getActivity()).replaceFragmentWithBackStack(R.id.bottom_container, previewFragment);

                            } catch (Exception e) {
                                e.printStackTrace();
                            } }

                    });

                    if (uploadIDSuccess) {
                        Log.d("TAG", "ID UPLOAD IS SUCCESS ");
                        ((NFCScanActivity) getActivity()).dismissLoader();

                        if (result.equals("OK")) {
                            CallBack.listener.idUpload(true);
                            ((NFCScanActivity) getContext()).setActionBarTitle(getResources().getString(R.string.biometric_verification));
                            ((NFCScanActivity) getActivity()).addFragment(R.id.bottom_container, selfieFragment,"selfie");
                            Log.d("TAG", "ID: Pending review: ");
                        }
                        else{
                            maxAttemptID = maxAttemptID + 1;
                            if (maxAttemptID == 3) {
                                CallBack.listener.idUpload(false);
                                ((NFCScanActivity) getActivity()).showBottomSheetDialog(getResources().getString(R.string.video_onboarding_failed), getResources().getString(R.string.id_failed_desc), "COURIER");
                                Log.d("TAG", "ID: Invalid doc ");
                            }
                            else {
                                CallBack.listener.idUpload(false);
                                ((NFCScanActivity) getActivity()).showBottomSheetDialog(getResources().getString(R.string.id_try_again), getResources().getString(R.string.id_try_again_desc), "ID_SCREEN");
                                Log.d("TAG", "ID: Invalid doc ");
                            }
                        }
                    } else {
                        ((NFCScanActivity) getActivity()).dismissLoader();
                        Log.d("TAG", "ID UPLOAD NOT SUCCESS ");
                    }
                });
            }
        });

        buttonExit.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((NFCScanActivity) getActivity()).dismissLoader();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean isErrorCodeExist(List<Errors> errors, int errorCode) {
        if (errors != null) {

            for(int l=0; l < errors.size(); l++){
                if (errors.get(l).getErrorCode() == errorCode) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

}