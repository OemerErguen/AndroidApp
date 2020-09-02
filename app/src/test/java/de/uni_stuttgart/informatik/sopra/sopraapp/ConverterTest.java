package de.uni_stuttgart.informatik.sopra.sopraapp;

import junit.framework.Assert;

import org.junit.Test;

import de.uni_stuttgart.informatik.sopra.sopraapp.util.Converter;

public class ConverterTest {

    @Test
    public void testQuotedString() {
        Assert.assertNull(Converter.convertToQuotedString(null));
        Assert.assertNull(Converter.convertToQuotedString(""));
        Assert.assertEquals("\"a\"", Converter.convertToQuotedString("a"));
        Assert.assertEquals("\"'a'\"", Converter.convertToQuotedString("'a'"));
    }
}
