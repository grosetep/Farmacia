package com.estrategiamovilmx.sales.farmacia.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.method.CharacterPickerDialog;
import android.view.View;

import com.estrategiamovilmx.sales.farmacia.R;

public class SubscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        AppCompatButton button_exit = (AppCompatButton) findViewById(R.id.button_exit);
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
            }
        });
    }
    @Override
    public void onBackPressed() {
            moveTaskToBack(true);
    }
}
