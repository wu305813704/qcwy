package com.qcwy.chaowei.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qcwy.chaowei.myapplication.R;

import org.xutils.x;

/**
 * Created by KouKi on 2016/12/26.
 */

public class FragmentSystemMessage extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_message,null);
        x.view().inject(this,view);
        return view;
    }
}
