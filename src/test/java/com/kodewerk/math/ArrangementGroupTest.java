package com.kodewerk.math;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrangementGroupTest {

    @Test
    public void testGetMember() {
        Arrangement expected = new Arrangement(new int[]{0,65,20130});
            ArrangementGroup group = new ArrangementGroup(3, 100000);
            Index index = new Index("6420000");
            Arrangement arrangement = group.getMember(index);
            assertEquals(expected, arrangement);
    }
}
