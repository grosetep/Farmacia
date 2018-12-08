package com.estrategiamovilmx.sales.farmacia.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.estrategiamovilmx.sales.farmacia.R;
import com.estrategiamovilmx.sales.farmacia.ui.activities.AddShippingAddressActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferenceFragment extends Fragment {
    private int mPageNumber;
    private EditText text_reference;
    private TextView text_area;

    public String getData(){ return text_reference.getText().toString();}

    public ReferenceFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(AddShippingAddressActivity.ARG_PAGE);
    }

    public static ReferenceFragment create(int pageNumber) {
        ReferenceFragment fragment = new ReferenceFragment();
        Bundle args = new Bundle();
        args.putInt(AddShippingAddressActivity.ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater
                .inflate(R.layout.fragment_reference, container, false);
        text_reference = (EditText) view.findViewById(R.id.text_reference);
        text_area = (TextView) view.findViewById(R.id.text_area);
        return view;
    }

    public void setError(String error){
        text_reference.setError(error);
    }

    @Override
    public void onResume() {
        super.onResume();
        text_area.setText(AddShippingAddressActivity.getShipping_point().getGooglePlace());
    }
}
