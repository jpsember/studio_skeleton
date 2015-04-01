package com.js.basic;

import com.js.testUtils.MyTestCase;

public class PointTest extends MyTestCase {

    public void testMagnitude() {
        Point pt = new Point(3,4);
        assertEqualsFloat(5,pt.magnitude());
    }

//    public void testSampleFailure() {
//        Point pt = new Point(3,4);
//        assertEqualsFloat(5.8f,pt.magnitude());
//    }

}
