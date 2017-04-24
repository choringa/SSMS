package com.indi.mundo;

/**
 * Created by Choringa on 4/24/17.
 */

public class UserKeysPairsBase {
    public String publicKey, privateKey, cryptMethod, numBytesLong;

    public UserKeysPairsBase() {
    }

    public UserKeysPairsBase(String publicKey, String privateKey, String cryptMethod, String numBytesLong ) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.cryptMethod = cryptMethod;
        this.numBytesLong = numBytesLong;
    }
}
