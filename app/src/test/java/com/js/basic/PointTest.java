package com.js.basic;

import junit.framework.TestCase;

public class PointTest extends TestCase {

    public void testMagnitude() {
        Point pt = new Point(3,4);
        assertEquals(pt.magnitude(),5.0f);
    }

    public void testMagnitudeFails() {
        Point pt = new Point(3,4);
        assertEquals(pt.magnitude(),7.0f);
    }


}