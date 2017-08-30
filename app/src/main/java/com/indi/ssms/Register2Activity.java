package com.indi.ssms;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indi.mundo.SecurityCrypt;
import com.indi.mundo.UserKeysPairsBase;

import java.security.Key;
import java.security.KeyPair;

public class Register2Activity extends AppCompatActivity {

    private static final String TAG = "Register2Activity";

    private Spinner spinnerCryptMethod, spinnerBytesLong;
    private EditText eTpublicKey, eTprivateKey;
    private Button btnFinalizarRegistro;
    private ProgressBar progressBar;
    private ArrayAdapter<CharSequence> adapterBytes, adapterMethod;
    private Switch switchTerminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        spinnerCryptMethod = (Spinner) findViewById(R.id.spinner_crypt_method_register2);
        adapterMethod = ArrayAdapter.createFromResource(this, R.array.crpyt_methods_array, android.R.layout.simple_spinner_dropdown_item);
        adapterMethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCryptMethod.setAdapter(adapterMethod);

        spinnerBytesLong = (Spinner) findViewById(R.id.spinner_crypt_bytes_register2);
        adapterBytes = ArrayAdapter.createFromResource(this, R.array.bytes_long_array, android.R.layout.simple_spinner_dropdown_item);
        adapterBytes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBytesLong.setAdapter(adapterBytes);

        eTpublicKey = (EditText) findViewById(R.id.et_public_key_reg2);

        eTprivateKey = (EditText) findViewById(R.id.et_private_key_reg2);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_register2);

        btnFinalizarRegistro = (Button) findViewById(R.id.btn_finish_reg2);
        btnFinalizarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarCampos();
            }
        });

        switchTerminos = (Switch) findViewById(R.id.switch2_reg2);

    }

    /**
     * Método que verifica que los campos esten bien llenados por el usuario
     * Se verifica si el usuario ingeso una clave privada haya ingresado una publica y viceversa
     * Se verifica que las llaves puestas por el usuario correspondan al algoritmo que se colocó y tengan la cantidad adecuada que haya especificado el usuario.
     * Veriica que no siga seleccionadas las opciones de "Seleccionar una opcion"
     * Si esta en orden guarda las valores en la base.
     * Nota: Si deja los espacios vacios, pasa a generarlos para luego guardarlos con el metodo y la cantidad de bytes que haya elegido el usuario.
     */
    private void verificarCampos() {
        Key publicKey = null;
        Key privateKey = null ;
        int positionSpinnerMethod = spinnerCryptMethod.getSelectedItemPosition();
        int positionSpinnerLongBytes = spinnerBytesLong.getSelectedItemPosition();

        Log.i(TAG, "spin1:" + positionSpinnerMethod +", spin2: " + positionSpinnerLongBytes);

        if(positionSpinnerLongBytes == 0 || positionSpinnerMethod == 0){
            Toast.makeText(this, "Selecciona una opcion valida!", Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.isEmpty(eTpublicKey.getText().toString().trim()) && TextUtils.isEmpty(eTprivateKey.getText().toString().trim())){
            KeyPair kp = SecurityCrypt.generateKeys(adapterMethod.getItem(positionSpinnerMethod).toString(), Integer.parseInt(adapterBytes.getItem(positionSpinnerLongBytes).toString()));
            if (kp == null){
                Toast.makeText(this, "Error generando las llaves, llave.", Toast.LENGTH_LONG);
                return;
            }
            else{
                publicKey = kp.getPublic();
                privateKey = kp.getPrivate();
            }

        }
        else if(!TextUtils.isEmpty(eTpublicKey.getText().toString().trim()) && !TextUtils.isEmpty(eTprivateKey.getText().toString().trim())){
            if(!SecurityCrypt.revisarKeys(eTpublicKey.getText().toString(), eTprivateKey.getText().toString(), positionSpinnerMethod, positionSpinnerLongBytes)){
                Toast.makeText(this, "Las llaves agregadas no corresponden al metodo o no contienen la longitud adecuada de bytes.", Toast.LENGTH_LONG);
                return;
            }
        }
        else{
            Toast.makeText(this, "Las dos campos de llaves deben ser o vacios o estar completos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!switchTerminos.isChecked()){
            Toast.makeText(this, "Debes aceptar terminos y condiciones.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //Si pasa tdo procede a insertar las llaves en la base
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mDatabase.getReference("keys").child(user.getUid()).child(adapterMethod.getItem(positionSpinnerMethod).toString()).child(adapterBytes.getItem(positionSpinnerLongBytes).toString());

        UserKeysPairsBase userKeysBase = new UserKeysPairsBase(publicKey, adapterMethod.getItem(positionSpinnerMethod).toString(), adapterBytes.getItem(positionSpinnerLongBytes).toString());

        final Key finalPrivateKey = privateKey;
        myRef.setValue(userKeysBase).addOnCompleteListener(Register2Activity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if(!task.isSuccessful()){
                    Toast.makeText(Register2Activity.this, "setValue Failed:" + task.getException(), Toast.LENGTH_SHORT).show();
                }
                else{
                    SecurityCrypt.savePrivateKey(finalPrivateKey);
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(Register2Activity.this, MainActivity.class));
                    finish();
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
