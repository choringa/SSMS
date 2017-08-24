package com.indi.mundo;

/**
 * Created by Choringa on 4/24/17.
 */

public class UserKeysPairsBase {
    public String publicKey, cryptMethod, numBytesLong;

    public UserKeysPairsBase() {
    }

    public UserKeysPairsBase(String publicKey, String cryptMethod, String numBytesLong ) {
        this.publicKey = publicKey;
        this.cryptMethod = cryptMethod;
        this.numBytesLong = numBytesLong;
    }
}
