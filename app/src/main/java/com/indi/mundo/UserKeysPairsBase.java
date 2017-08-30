package com.indi.mundo;

import java.security.Key;

/**
 * Created by Choringa on 4/24/17.
 */

public class UserKeysPairsBase {
    public String cryptMethod, numBytesLong;
    public Key publicKey;

    public UserKeysPairsBase() {
    }

    public UserKeysPairsBase(Key publicKey, String cryptMethod, String numBytesLong ) {
        this.publicKey = publicKey;
        this.cryptMethod = cryptMethod;
        this.numBytesLong = numBytesLong;
    }
}
