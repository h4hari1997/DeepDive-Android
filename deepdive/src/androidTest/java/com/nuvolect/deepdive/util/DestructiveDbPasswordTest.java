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

public class DestructiveDbPasswordTest {

    @Test
    public void dbPasscodeTest(){

        Context ctx = getTargetContext();

        Persist.deleteKey(ctx, CConst.DB_PASSWORD);

        byte[] clearBytes = CConst.STRING32.getBytes();
        Persist.putDbPassword(ctx, clearBytes);
        assertThat( Persist.keyExists( ctx, CConst.DB_PASSWORD), is(true));

        byte[] clearBytes2 = Persist.getDbPassword(ctx);
        assertThat(Arrays.equals(clearBytes, clearBytes2), is( true));

        boolean keyDeleted = Persist.deleteKey(ctx, CConst.DB_PASSWORD);
        assertThat( keyDeleted, is( true ));
    }
}
