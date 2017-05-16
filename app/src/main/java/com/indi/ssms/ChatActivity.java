package com.indi.ssms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.indi.mundo.UserBase;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private Button btnAttachment;
    private ImageButton btnSend;
    private EditText etMensaje;

    private FirebaseAuth firebaseAuth;
    private QBChatDialog qbChatDialog;
    private UserBase contact;
    private int dialogID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        contact = (UserBase) getIntent().getSerializableExtra("contact");
        dialogID = getIntent().getIntExtra("dialogID", 0);

        firebaseAuth = FirebaseAuth.getInstance();


        //Inicializa los settings de quickblox con las credenciales de la aplicacion
        QBSettings.getInstance().init(ChatActivity.this, "56981", "OfgrpPbdW98bkgh", "Yzy3weP7GPzqkkg");
        //Sin esta joda manda un null pointer de String :P
        QBSettings.getInstance().setAccountKey("6YuXLSpsvnxKy1gphqWf");


        QBChatService.setDebugEnabled(true); // enable chat logging
        QBChatService.setDefaultPacketReplyTimeout(10000);


        QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
        chatServiceConfigurationBuilder.setSocketTimeout(60); //Sets chat socket's read timeout in seconds
        chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
        chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
        QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);

        //configura el dialogo de chat de qb;
        getQBChatDialog();


    }

    /**
     * Metodo que setea el dialogo entre el contacto y e usuario.
     */
    private void getQBChatDialog() {
        //Primero verifica si existe el dialogo si no lo crea
        if(dialogID != 0) {
            QBRestChatService.getChatDialogById(String.valueOf(dialogID)).performAsync(
                    new QBEntityCallback<QBChatDialog>() {
                        @Override
                        public void onSuccess(QBChatDialog dialog, Bundle params) {
                            setDialog(dialog);
                        }

                        @Override
                        public void onError(QBResponseException responseException) {
                            Log.e(TAG, "getQBChatDialog->Error SACANDO el dialogo: " + responseException.getLocalizedMessage());
                        }
                    });
        }
        else {
            qbChatDialog = DialogUtils.buildPrivateDialog(contact.qbID);
            QBRestChatService.createChatDialog(qbChatDialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                @Override
                public void onSuccess(QBChatDialog result, Bundle params) {
                    setDialog(result);
                }

                @Override
                public void onError(QBResponseException responseException) {
                    Log.e(TAG, "getQBChatDialog->Error CREANDO el dialogo: " + responseException.getLocalizedMessage());
                }
            });
        }
    }

    public void setDialog(QBChatDialog dialog) {
        this.qbChatDialog = dialog;
    }
}
