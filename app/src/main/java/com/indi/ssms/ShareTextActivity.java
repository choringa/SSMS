package com.indi.ssms;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ShareTextActivity extends AppCompatActivity {


    private static final String TAG = "ShareTextActivity";

    private Toolbar toolbar;
    private Button btnShare, btnPreview;
    private TextView etSubject, etPlainText, etCryptText;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_text);

        etPlainText = findViewById(R.id.etSharePlainText);
        etCryptText = findViewById(R.id.etShareCryptText);
        etSubject = findViewById(R.id.etSubject);
        spinner = findViewById(R.id.spinner_share_crypt);

        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btnSharePressed");
                shareButton(view);
            }
        });

        btnPreview = findViewById(R.id.btnPreview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btnPreview");
                previewCrypt(view);
            }
        });


        setToolbar();
        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Metodo que da visibilidad al usuario de su texto ya cifrado antes de que lo comparta.
     */
    private void previewCrypt(View view) {
        String textoPlano = etPlainText.getText().toString();
        if(!textoPlano.trim().equals("") ){
            String encryptedMessage = cryptMessage(textoPlano);
            etCryptText.setVisibility(View.VISIBLE);
            etCryptText.setText(encryptedMessage);
        }
        else {
            Snackbar.make(view, "You must insert a message", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }

    /**
     * Metodo que encripta el mensaje
     * @param textoPlano el texto plano
     * @return el texto cifrado
     */
    private String cryptMessage(String textoPlano) {
        return textoPlano.toCharArray().toString();
    }


    /**
     * Metodo que pemrite al usuario compartir el mensaje encriptado por las diferentes aplicaciones que permiten
     * el ingreso de texto plano y que el usuario tenga instaladas.
     */
    private void shareButton(View view) {


        String textoPlano = etPlainText.getText().toString();
        if(!textoPlano.trim().equals("")){
            String encryptedMessage = cryptMessage(textoPlano);

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");

            // Si tiene subject lo manda si no manda el default
            share.putExtra( android.content.Intent.EXTRA_SUBJECT, etSubject.getText().toString().equals("") ? "Sent from SSMS" : etSubject.getText().toString());

            share.putExtra(android.content.Intent.EXTRA_TEXT, encryptedMessage);
            startActivity(Intent.createChooser(share,"Share via"));

        }
        else {
            Snackbar.make(view, "You must insert a message", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }


    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
