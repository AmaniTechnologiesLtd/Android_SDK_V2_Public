package com.amani.sdk.ui.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.amani.sdk.R;
import com.amani.sdk.base.AppConstants;
import com.amani.sdk.base.cb.CallBack;
import com.amani.sdk.base.cb.CallBackInternal;
import com.amani.sdk.base.cb.CallBackMessages;
import com.amani.sdk.ui.component.DialogFragment;
import com.amani.sdk.ui.fragment.IDScanFragment;
import com.amani.sdk.ui.fragment.PreviewFragment;

import java.io.FileOutputStream;

import ai.amani.base.util.Amani;
import ai.amani.base.util.SessionManager;

public class NFCScanActivity extends BaseActivity {

    Button  buttonCancel;
    TextView titleToolBar;
    ImageView backToolBar;
    ProgressBar progressBar;
    String idNo, birthDate, expireDate, docNo, token, email, password;
    Dialog dialog;
    FrameLayout bottomContainer;
    LinearLayout linearLayout;
    Toolbar toolbar;
    LottieAnimationView lottieAnimationView;
    Tag tag = null;
    ImageView idCard;
    Fragment selfieFragment;
    Boolean isCourierRequested, isVerificationCompleted, isLoginSuccess, isUserExit = null;
    static int maxAttempt;
    public static boolean nfcScan= true;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nfc_scan);
        getBundle();
        init();
        buttonClicks();
        initAmani();
        setInternalCallBack();
        setGlobalException();

    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void initAmani() {

        nfcScan = true;
        maxAttempt = 0;

        if (token != null && !token.isEmpty()) {
            Amani.sharedInstance().initAmani(this,idNo,token,"tr",(isSuccess, errorCode) -> {
                if (isSuccess) {
                    Log.d("TAG", "Login Amani is SUCCESSFUL by token ");
                    if (CallBackInternal.listener != null) CallBackInternal.listener.loginSuccess(true);

                } else {
                    finish();
                    if (CallBackInternal.listener != null) CallBackInternal.listener.loginSuccess(false);
                }
            });
        } else if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {

            Amani.sharedInstance().initAmani(this,idNo,email,password,"tr",(isSuccess, errorCode) -> {
                if (isSuccess) {
                    if (CallBackInternal.listener != null) CallBackInternal.listener.loginSuccess(true);
                    Log.d("TAG", "Login Amani is SUCCESSFUL by credentials ");

                } else {
                    finish();
                    if (CallBackInternal.listener != null) CallBackInternal.listener.loginSuccess(false);
                }
            });

        } else {
            finish();
            if (CallBackInternal.listener != null) CallBackInternal.listener.loginSuccess(false);
        }
    }

    private void init() {
        buttonCancel = findViewById(R.id.button_nfc_cancel);
        titleToolBar = findViewById(R.id.title_toolbar);
        lottieAnimationView = findViewById(R.id.nfc_animation);
        idCard = findViewById(R.id.id_card);
        backToolBar = findViewById(R.id.tool_barback);
        bottomContainer = findViewById(R.id.bottom_container);
        toolbar = findViewById(R.id.toolbar_nfc);
        linearLayout = findViewById(R.id.toolbar_layout);
        dialog = new Dialog(this);
        progressBar = findViewById(R.id.progressBar);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    protected void onResume() {
        super.onResume();
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter != null) {
            Intent intent = new Intent(getApplicationContext(), this.getClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            String[][] filter = new String[][]{new String[]{"android.nfc.tech.IsoDep"}};
            adapter.enableForegroundDispatch(this, pendingIntent, null, filter);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tag = intent.getExtras().getParcelable(NfcAdapter.EXTRA_TAG);

        if (nfcScan && !birthDate.isEmpty() && !expireDate.isEmpty() && !docNo.isEmpty()) {
            if (maxAttempt < 3) {
                idCard.setVisibility(View.INVISIBLE);
                lottieAnimationView.setVisibility(View.VISIBLE);

                Amani.sharedInstance().ScanNFC().start(tag, getApplicationContext(), birthDate, expireDate, docNo, (bitmap, isSuccess,exception) -> {

                    if (isSuccess) {

                        lottieAnimationView.setAnimation(R.raw.nfc_done);
                        Log.d("TAG", "onNewIntent: NFC SCANNED");

                        Amani.sharedInstance().ScanNFC().upload(this, "NF", (uploadNFCSuccess, resultNFC,errors) -> {

                            if (uploadNFCSuccess) {
                                Log.d("TAG", "onNewIntent: NFC UPLOADED");

                                if (resultNFC.equals("OK")) {
                                    CallBack.listener.nfcScan(true);
                                    Log.d("TAG", "onNewIntent: NFC PENDING REVIEW" + "result: " + resultNFC);
                                    startSelfie();
                                    dismissLoader();

                                } else {
                                    CallBack.listener.nfcScan(false);
                                    Log.d("TAG", "onNewIntent: NFC NOT PENDING REVIEW" + "result: " + resultNFC);
                                    dismissLoader();
                                    showBottomSheetDialog(null, null, null);
                                }

                            } else {
                                dismissLoader();
                                Log.d("TAG", "onNewIntent: NFC NOT UPLOADED ");
                            }
                        });
                        showProgressLoader();
                    } else {
                        nfcScan = false;
                        maxAttempt = maxAttempt + 1;
                        if (maxAttempt == 3) showBottomSheetDialog(null,null,null);
                        else showBottomSheetDialog(null, null, "DISMISS");
                        idCard.setVisibility(View.VISIBLE);
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                        Log.d("TAG", "onNewIntent: NFC SCAN FAILED");
                    }
                });
            } else {
                nfcScan = false;
                if (maxAttempt == 3) showBottomSheetDialog(null,null,null);
            }
        }
        if (maxAttempt == 3) showBottomSheetDialog(null,null,null);
    }

    private void buttonClicks () {

        buttonCancel.setOnClickListener(v -> {
                    if (CallBackInternal.listener != null) CallBackInternal.listener.userExit(true);
                    finish();
                }
        );

        backToolBar.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    public void showBottomSheetDialog(@Nullable String dialogTitle, @Nullable String dialogDesc, String goTo) {

        DialogFragment bottomSheetDialog = new DialogFragment(dialogTitle,dialogDesc, goTo);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show(this.getSupportFragmentManager(),"TAG");

    }

    public void startIdScanFragment() {
        lottieAnimationView.setVisibility(View.INVISIBLE);
        IDScanFragment idScanFragment = new IDScanFragment();
        setActionBarTitle(getResources().getString(R.string.title_id_scan));
        CallBack.listener.nfcScan(false);
        (NFCScanActivity.this).replaceFragmentWithBackStack(R.id.bottom_container, idScanFragment);

    }

    public void setActionBarTitle(String title){
        titleToolBar.setText(title);
    }

    private void getBundle() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            idNo = extras.getString(AppConstants.ID_NO);
            birthDate = extras.getString(AppConstants.BIRTH_DATE);
            docNo = extras.getString(AppConstants.DOC_NO);
            expireDate = extras.getString(AppConstants.EXPIRE_DATE);
            token = extras.getString(AppConstants.TOKEN);
            email = extras.getString(AppConstants.EMAIL);
            password = extras.getString(AppConstants.PASSWORD);
        }

        Log.e(this.getClass().getSimpleName(), "getBundle: \n" +
                "idNo : " + idNo + "\n" +
                "birthDate : " + birthDate + "\n" +
                "docNo : " + docNo + "\n" +
                "expireDate : " + expireDate + "\n" +
                "token : " + token + "\n" +
                "email : " + email + "\n" +
                "password : " + password + "\n"
        );
    }

    public void showProgressLoader() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoader() {
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void startSelfie() {
        selfieFragment = Amani.sharedInstance().Selfie().start("XXX_SE_0",(bitmap, isDestroyed) -> {

            if (bitmap != null) {

                try {
                    //Write file
                    String filename = "bitmapSelfie.png";
                    FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    PreviewFragment previewFragment = new PreviewFragment();
                    Bundle arg = new Bundle();
                    arg.putString("image", filename);
                    arg.putBoolean("selfie", true);
                    previewFragment.setArguments(arg);
                    (this).replaceFragmentWithBackStack(R.id.bottom_container, previewFragment);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        (this).replaceFragmentWithBackStack(R.id.bottom_container, selfieFragment);
    }

    @Override
    protected void onDestroy() {
        dismissLoader();
        if (Boolean.TRUE.equals(isCourierRequested))  CallBack.listener.activityFinished(true, CallBackMessages.COURIER_REQUESTED,SessionManager.getCustomerId(), null);
        if (Boolean.FALSE.equals(isLoginSuccess)) CallBack.listener.activityFinished(true, CallBackMessages.CAUSE_LOGIN, null,null);
        if (Boolean.TRUE.equals(isUserExit)) CallBack.listener.activityFinished(true, CallBackMessages.DESTROYED_BY_USER, SessionManager.getCustomerId(),null);
        if (Boolean.TRUE.equals(isVerificationCompleted)) CallBack.listener.activityFinished(true, CallBackMessages.VERIFICATION_COMPLETED, SessionManager.getCustomerId(), null);
        super.onDestroy();
    }

    private void setInternalCallBack() {
        CallBackInternal callBack = new CallBackInternal();
        callBack.setEventListener(new CallBackInternal.EventListener() {

            @Override
            public void courierRequested(boolean courierRequested) {
                if (courierRequested) isCourierRequested = true;
            }

            @Override
            public void lastStepCompleted(boolean isCompleted) {
                if (isCompleted) isVerificationCompleted = true;
            }

            @Override
            public void loginSuccess(boolean isSuccess) {
                if (!isSuccess) isLoginSuccess = false;
            }

            @Override
            public void userExit(boolean isExit) {
                if(isExit) isUserExit = true;
            }
        });
    }

    private void setGlobalException() {
        Thread.setDefaultUncaughtExceptionHandler ((thread, e) ->
                CallBack.listener.activityFinished(true, CallBackMessages.THROW_EXCEPTION, null, e));
    }
}
