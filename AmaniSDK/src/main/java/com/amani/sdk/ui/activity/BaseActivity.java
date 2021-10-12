package com.amani.sdk.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
    }


    public void addFragment(int fragmentContainer, Fragment fragment, String tag) {
        if (!fragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(fragmentContainer, fragment, tag)
                    .commit();
        }
    }

    public void replaceFragmentWithBackStack(int fragmentContainer, Fragment fragment) {
        if (!fragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(fragment.getClass().getName())
                    .replace(fragmentContainer, fragment, fragment.getClass().getName())
                    .commit();
        }
    }

}
