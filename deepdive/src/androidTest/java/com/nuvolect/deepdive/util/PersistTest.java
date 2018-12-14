/*
 * Copyright (c) 2018 Nuvolect LLC.
 * This software is offered for free under conditions of the GPLv3 open source software license.
 * Contact Nuvolect LLC for a less restrictive commercial license if you would like to use the software
 * without the GPLv3 restrictions.
 */

package com.nuvolect.deepdive.util;

import android.content.Context;

import com.nuvolect.deepdive.main.CConst;

import org.junit.Test;

import java.util.Arrays;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PersistTest {

    String testKey = "not_collision_key_dksasoiduu";
    String testValue = "we value many things in life";

    @Test
    public void persistKeyCreateDelete(){

        Context ctx = getTargetContext();

        // Clean up any failed test
        if( Persist.keyExists(ctx, testKey))
            Persist.deleteKey(ctx, testKey);

        Persist.put(ctx, testKey, testValue);
        assertThat( Persist.keyExists(ctx, testKey), is(true));

        boolean deleted = Persist.deleteKey(ctx, testKey);
        assertThat( deleted, is(true));
        assertThat( Persist.keyExists(ctx, testKey), is(false));
    }


    @Test
    public void encryptTest(){

        try {
            // Create test data and convert it to bytes, no encoding required yet
            String testString = "the Quick Brown fox jumped over the lazy dog 0123456789";
            byte[] clearBytes = CrypUtil.getBytes( testString);

            // Encrypt the byte array, creating a new byte array
            byte[] encryptedBytes = CrypUtil.encrypt(clearBytes);

            // Prepare for storage by converting the byte array to a Base64 encoded string
            String encryptedEncodedString = CrypUtil.encodeToB64( encryptedBytes );

            // Persist the string, and get it back again
            Context ctx = getTargetContext();
            Persist.put(ctx, testKey, encryptedEncodedString);

            String encryptedEncodedString2 = Persist.get(ctx, testKey);

            // Decode the string back into a byte array using Base64 decode
            byte[] encryptedBytes2 = CrypUtil.decodeFromB64( encryptedEncodedString2);
            assertThat( java.util.Arrays.equals( encryptedBytes, encryptedBytes2),
                    is( true));

            // Decrypt the byte array, creating a new byte array
            byte[] clearBytes2 = CrypUtil.decrypt( encryptedBytes2);
            assertThat( java.util.Arrays.equals( clearBytes, clearBytes2),
                    is( true));

            // Decode the byte array creating a new String using UTF-8 encoding
            String testString2 = CrypUtil.toStringUTF8( clearBytes2);
            assertThat( testString.contentEquals( testString2), is( true));

            // Cleanup
            boolean deleted = Persist.deleteKey(ctx, testKey);
            assertThat( deleted, is(true));
            assertThat( Persist.keyExists(ctx, testKey), is(false));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void selfSignedKeystoreTest(){

        Context ctx = getTargetContext();

        Persist.deleteKey(ctx, CConst.SELFSIGNED_KS_KEY);

        char[] clearChars= CConst.STRING32.toCharArray();
        Persist.putSelfsignedKsKey(ctx, clearChars);
        assertThat( Persist.keyExists( ctx, CConst.SELFSIGNED_KS_KEY), is(true));

        char[] clearChars2 = Persist.getSelfsignedKsKey(ctx);
        assertThat(Arrays.equals( clearChars, clearChars2), is( true));

        boolean keyDeleted = Persist.deleteKey(ctx, CConst.SELFSIGNED_KS_KEY);
        assertThat( keyDeleted, is( true ));
    }
}
