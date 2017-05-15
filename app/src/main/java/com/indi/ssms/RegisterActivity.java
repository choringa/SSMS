package com.indi.ssms;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indi.mundo.UserBase;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;


public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputUsername, inputPhoneNumber;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email_register);
        inputPassword = (EditText) findViewById(R.id.password_resgister);
        inputUsername = (EditText) findViewById(R.id.username_register);
        inputPhoneNumber = (EditText) findViewById(R.id.phone_register);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Cuando le da al boton de registrarse.
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Comprueba que todos los campos esten bien
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                final String username = inputUsername.getText().toString().trim();
                final String phoneNumber = inputPhoneNumber.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(username)){
                    Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(getApplicationContext(), "Enter Phone Number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!TextUtils.isDigitsOnly(phoneNumber)){
                    Toast.makeText(getApplicationContext(), "Phone Number only can be digits!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                signUpFirebase(email, password, username, phoneNumber);
            }
        });

    }

    /**
     * Metodo que registra al usuario en firebase y le agrega el username despues de crearlo
     * @param email email con el que se registra
     * @param password password que ingreso el usuario
     * @param username el username que elijió el usuario
     * @param phoneNumber nuemero de celular del contacto.
     */
    private void signUpFirebase(final String email, String password, final String username, final String phoneNumber){
        //TODO: cuando el email ya esta registrado sale el exception.
        Log.i(TAG, "btnSignUp-->Va a guardar el nuevo uaurio en firebase para autenticacion con email:  " + email);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            if(progressBar.getVisibility() == ProgressBar.VISIBLE)
                                progressBar.setVisibility(ProgressBar.GONE);

                            Toast.makeText(RegisterActivity.this, getString(R.string.error_try_again), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "btnSignUp->Error autenticación fallida: " + task.getException());
                        } else {

                            FirebaseUser userFB = auth.getCurrentUser();
                            final String fbID = userFB.getUid();
                            //Si crea la nueva autenticacion en FB va a crear el nuevo usaurio en QB
                            signUpQB(username, fbID, email, phoneNumber,userFB);

                        }
                    }
                });


    }

    /**
     * Registra al usuario en quicklox con su username y de password le pone el UID de firebase. Corrobora identidad?
     * @param username el username del usaurio.
     * @param fbID el uid de firebase.
     */
    private void signUpQB(final String username, final String fbID, final String email,final String phoneNumber,final FirebaseUser userFB) {
        //Inicializa los settings de quickblox con las credenciales de la aplicacion
        QBSettings.getInstance().init(RegisterActivity.this, "56981", "OfgrpPbdW98bkgh", "Yzy3weP7GPzqkkg");
        //Sin esta joda manda un null pointer de String :P
        QBSettings.getInstance().setAccountKey("6YuXLSpsvnxKy1gphqWf");
        //Comico que sirva con el account key de quickblox como tal.
        //QBSettings.getInstance().setAccountKey("rz2sXxBt5xgSxGjALDW6");

        Log.i(TAG, "signUpQB-->Va a crear el nuevo acceso para QUICKBLOX al usuario: " + username + ",pass: "  + fbID);

        final QBUser qbUser = new QBUser();
        qbUser.setLogin(email);
        qbUser.setEmail(email);
        qbUser.setFullName(username);
        qbUser.setPassword(fbID);
        //qbUser.setPhone(phoneNumber);


        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.i(TAG, "signUpQB->QBUser->Creado bien");

                //Cuando termina de ingresar al usuario en QB lo guarda el objeto usuario en la base de firebase
                int qbID = qbUser.getId();

                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = mDatabase.getReference("users");

                UserBase userBase = new UserBase(username, email, phoneNumber, qbID);
                Log.i(TAG, "btnSignUp-->Va a guardar el nuevo usuario en firebase como objeto usuario con username: " + username);
                myRef.child(userFB.getUid()).setValue(userBase).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            if(progressBar.getVisibility() == ProgressBar.VISIBLE)
                                progressBar.setVisibility(ProgressBar.GONE);

                            Toast.makeText(RegisterActivity.this, getString(R.string.error_try_again),Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "btnSignUp->signUpQB->Error ingresando al usuario en la base de firebase setValue Failed: " + task.getException());
                        }
                        else{
                            Log.i(TAG, "btnSignUp-->Setea a la autenticacion con el nombre de usuario: " + username);
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            auth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(progressBar.getVisibility() == ProgressBar.VISIBLE)
                                        progressBar.setVisibility(ProgressBar.GONE);
                                    startActivity(new Intent(RegisterActivity.this, Register2Activity.class));
                                    finish();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {

                if(progressBar.getVisibility() == ProgressBar.VISIBLE)
                    progressBar.setVisibility(ProgressBar.GONE);

                Toast.makeText(RegisterActivity.this, e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "signUpQB->QBUser->Error autenticación fallida: " + e.getLocalizedMessage());
                //signUpQB(username, password, email);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
