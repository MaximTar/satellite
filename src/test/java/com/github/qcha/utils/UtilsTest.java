package com.github.qcha.utils;

import org.apache.commons.lang3.math.*;
import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {
    @Test
    public void isParsableTest() throws Exception {
        Assert.assertEquals(true, org.apache.commons.lang3.math.NumberUtils.isParsable("3"));
        Assert.assertEquals(true, org.apache.commons.lang3.math.NumberUtils.isParsable("3.2"));
        Assert.assertEquals(true, org.apache.commons.lang3.math.NumberUtils.isParsable("0.3"));
        Assert.assertEquals(true, org.apache.commons.lang3.math.NumberUtils.isParsable("-0.3"));
        Assert.assertEquals(true, org.apache.commons.lang3.math.NumberUtils.isParsable("-3"));

        Assert.assertEquals(false, org.apache.commons.lang3.math.NumberUtils.isParsable("Hello"));
        Assert.assertEquals(false, org.apache.commons.lang3.math.NumberUtils.isParsable("-3Parse"));
        Assert.assertEquals(false, org.apache.commons.lang3.math.NumberUtils.isParsable("-3.2Parse"));
        Assert.assertEquals(false, org.apache.commons.lang3.math.NumberUtils.isParsable("3Parse"));
        Assert.assertEquals(false, org.apache.commons.lang3.math.NumberUtils.isParsable("3.2Parse"));
        Assert.assertEquals(false, org.apache.commons.lang3.math.NumberUtils.isParsable(null));
    }
}
