package com.amani.sdk.ui.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amani.sdk.R;
import com.amani.sdk.ui.fragment.IDScanFragment;
import com.amani.sdk.ui.activity.NFCScanActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DialogFragment extends BottomSheetDialogFragment {

    String mTitle, mDesc, mGoTo;

    public DialogFragment(@Nullable String dialogTitle, @Nullable String dialogDesc, String goTo) {

        setStyle(STYLE_NORMAL, R.style.BottomSheetTheme);
        mTitle = dialogTitle;
        mDesc = dialogDesc;
        mGoTo = goTo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_message, container, false);
        Button button = view.findViewById(R.id.dialogConfirm);
        TextView title = view.findViewById(R.id.dialog_title);
        TextView desc = view.findViewById(R.id.dialog_desc);

        if (mTitle != null && mDesc != null) {
            title.setText(mTitle);
            desc.setText(mDesc);
        }

        button.setOnClickListener(v -> {
            button.setClickable(false);

            if (mGoTo == null) {
                ((NFCScanActivity) getActivity()).startIdScanFragment();
                NFCScanActivity.nfcScan = false;
                dismiss();

            } else if (mGoTo.equals("ID_SCREEN")) {
                requireActivity().getSupportFragmentManager().popBackStackImmediate(IDScanFragment.class.getName(),0);
                dismiss();

            } else if (mGoTo.equals("SE_SCREEN")) {
                dismiss();
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            } else if (mGoTo.equals("DISMISS")) {
                NFCScanActivity.nfcScan = true;
                dismiss();
            } else {
                requireActivity().finish();
            }

        });
        return view;
    }
}