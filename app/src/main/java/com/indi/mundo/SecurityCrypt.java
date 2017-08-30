package com.indi.mundo;

import android.util.Log;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * Created by david.arteaga on 30/8/2017.
 */

public class SecurityCrypt {

    private static final String TAG = "SecurutyCrypt";
    public static final String PRIVATE_PATH = "";

    public static KeyPair generateKeys(String method, int bytesLong) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(method);
            kpg.initialize(bytesLong);
            KeyPair kp = kpg.generateKeyPair();
            return kp;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        }

    }


    /**
     * Metodo que revisa las llaves esten en los parametros especificados por el usuario
     * @param publicKey
     * @param privateKey
     * @param positionSpinnerMethod
     * @param positionSpinnerLongBytes
     * @return
     */
    public static boolean revisarKeys(String publicKey, String privateKey, int positionSpinnerMethod, int positionSpinnerLongBytes) {
        //TODO
        return true;
    }

    public static void savePrivateKey(Key privateKey) {
        //TODO
    }
}
