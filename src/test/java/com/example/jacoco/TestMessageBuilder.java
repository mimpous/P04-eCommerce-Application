package com.example.jacoco;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestMessageBuilder {

    @Test
    public void testNameMkyong() {

        MessageBuilder obj = new MessageBuilder();
        assertEquals("Hello mkyong", obj.getMessage("mkyong"));

    }

}