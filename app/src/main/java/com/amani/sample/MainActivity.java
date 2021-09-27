package com.amani.sample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amani.sdk.base.AppConstants;
import com.amani.sdk.base.cb.CallBack;
import com.amani.sdk.base.cb.CallBackMessages;
import com.amani.sdk.ui.NFCScanActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;


public class MainActivity extends AppCompatActivity {

    TextView expireDate, birthDate, docNo, idNo;
    Button buttonStart;
    String expireDateS, birthDateS, docNoS, idNoS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expireDate = findViewById(R.id.expire_Date);
        birthDate = findViewById(R.id.birth_date);
        docNo = findViewById(R.id.doc_no);
        idNo = findViewById(R.id.id_no);
        buttonStart = findViewById(R.id.button_start);


        CallBack callBack = new CallBack();
        callBack.setEventListener(new CallBack.EventListener() {

            @Override
            public void activityFinished(boolean activityFinished, CallBackMessages reason, @Nullable Integer customerID, @Nullable Throwable exception) {
                Log.d("TAG", "reason: " + reason + reason.name() + customerID + exception);

            }

            @Override
            public void nfcScan(boolean isSuccess) {
                if(isSuccess) Log.d("reason", ": "  );

            }

            @Override
            public void idUpload(boolean isSuccess) {
                if(isSuccess) Log.d("reason", ": "  );

            }

            @Override
            public void selfieUpload(boolean isSuccess) {
                if(isSuccess) Log.d("reason", ": "  );

            }

        });

        buttonStart.setOnClickListener(v -> {

            expireDateS = expireDate.getText().toString();
            docNoS = docNo.getText().toString();
            birthDateS = birthDate.getText().toString();
            idNoS = idNo.getText().toString();

            Intent intent = new Intent(MainActivity.this, NFCScanActivity.class);

            intent.putExtra(AppConstants.EXPIRE_DATE, "yyMMdd");
            intent.putExtra(AppConstants.BIRTH_DATE, "yyMMdd");
            intent.putExtra(AppConstants.DOC_NO, "yyMMdd");
            intent.putExtra(AppConstants.ID_NO, idNoS);
            //If you use TOKEN, you do not need to enter EMAIL and PASSWORD.
            intent.putExtra(AppConstants.TOKEN, "");

            intent.putExtra(AppConstants.EMAIL, "EMAIL" );
            intent.putExtra(AppConstants.PASSWORD, "vUcxPvfvP9rYUJ");

            startActivity(intent);
        });
    }
}