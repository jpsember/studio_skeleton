package com.js.basic;

import com.js.testUtils.MyTestCase;
import static com.js.basic.Tools.*;
import com.js.testUtils.IOSnapshot;

public class PointTest extends MyTestCase {

    public void testMagnitude() {
        Point pt = new Point(3,4);
        assertEqualsFloat(5,pt.magnitude());
    }

    public void testWithSnapshot() {
        IOSnapshot.open();
        Point pt = new Point(3,4);
        pr("Point: "+pt);
        IOSnapshot.close();
    }
}
