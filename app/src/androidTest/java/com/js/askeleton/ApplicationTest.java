package com.js.askeleton;

import android.app.Application;
import android.test.ApplicationTestCase;

public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
    }

    // All unit tests are methods starting with prefix 'test'

    public void testExample() {
        final int a = 2;
        final int b = 2;
        assertEquals(a, b);
    }

    public void willNotExecuteTestFailure() {
        final int a = 2;
        final int b = 3;
        assertEquals(a, b);
    }

}