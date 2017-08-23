package com.indi.ssms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class ShareTextActivity extends AppCompatActivity {


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_text);


        /**

        btnShareRedes = view.findViewById(R.id.btnShare);
        btnShareRedes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: share button
                Log.i(TAG, "btnSharePressed");
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");

                // You can add subject also
                 //sharingIntent.putExtra( android.content.Intent.EXTRA_SUBJECT,
                 //"Subject Here");

                share.putExtra(android.content.Intent.EXTRA_TEXT, "el texto cifrado");
                mainActivity.startActivity(Intent.createChooser(share,"Share via"));
            }
        });
        */

        setToolbar();
        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
