package com.ZuWay.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ZuWay.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuntityAddFragment extends BottomSheetDialogFragment {


    public QuntityAddFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_quntity_add, container, false);

        EditText qyutt = view.findViewById(R.id.et_qut);
        TextView typee = view.findViewById(R.id.type);

        final Button btn = view.findViewById(R.id.sub_btn);
        final ImageView close = view.findViewById(R.id.close);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                btn.startAnimation(animFadein);


            }
        });

        return view;
    }

}
