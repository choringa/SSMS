package com.indi.mundo;

import android.content.Context;

import com.indi.ssms.RegisterActivity;
import com.quickblox.auth.session.QBSettings;

/**
 * Created by Choringa on 5/16/17.
 */

public class InitQBSettings {
    public InitQBSettings(Context context) {
        //Inicializa los settings de quickblox con las credenciales de la aplicacion
        QBSettings.getInstance().init(context, "56981", "OfgrpPbdW98bkgh", "Yzy3weP7GPzqkkg");
        //Sin esta joda manda un null pointer de String :P
        QBSettings.getInstance().setAccountKey("6YuXLSpsvnxKy1gphqWf");
        //Comico que sirva con el account key de quickblox como tal.
        //QBSettings.getInstance().setAccountKey("rz2sXxBt5xgSxGjALDW6");
    }
}
