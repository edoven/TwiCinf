package it.cybion.influence;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 */
public class AppTest {

    @Test
    public void shouldRunASimpleTest() {
        assertTrue(true);
    }

    @Test
    public void shouldDeserializeJsonToObject() {
        //take some json examples from db, save them as files in test/resources;
        //do a test for each file, deserializing the json in an object
        //do asserts.

    }
}
