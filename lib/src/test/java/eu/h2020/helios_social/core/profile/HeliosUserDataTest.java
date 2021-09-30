package eu.h2020.helios_social.core.profile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class HeliosUserDataTest {

    /**
     * Set up test data. The current implementation is setting up test data using features
     * of the class that is tested. This setup procedure is run before each individual test.
     */
    @Before
    public void setUp() {
        HeliosUserData.getInstance().setValue("get_value_test", "testing");
        HeliosUserData.getInstance().setValue("remove_key_test", "remove");
    }

    /**
     * Check that we will get expected value when using known key value.
     */
    @Test
    public void getValueTest() {
        String str = HeliosUserData.getInstance().getValue("get_value_test");
        assertTrue(str.equals("testing"));
    }

    /**
     * Store new key-value pair and read it back and compare.
     */
    @Test
    public void setValueTest() {
        HeliosUserData.getInstance().setValue("set_value_test", "setting");
        String str = HeliosUserData.getInstance().getValue("set_value_test");
        assertTrue(str.equals("setting"));
    }

    /**
     * Check that checking presence of a key works.
     */
    @Test
    public void hasValueTest() {
        boolean exists = HeliosUserData.getInstance().hasValue("get_value_test");
        assertTrue(exists);
    }

    /**
     * Check that checking presence of a key also works as expected when the key is not found.
     */
    @Test
    public void hasNotValueTest() {
        boolean exists = HeliosUserData.getInstance().hasValue("no_value");
        assertTrue(!exists);
    }

    /**
     * Check that key removal really removes the key.
     */
    @Test
    public void removeKeyTest() {
        boolean exists1 = HeliosUserData.getInstance().hasValue("remove_key_test");
        String str = HeliosUserData.getInstance().removeKey("remove_key_test");
        if (!str.equals("remove")) {
            fail();
        }
        boolean exists2 = HeliosUserData.getInstance().hasValue("remove_key_test");
        assertTrue(exists1 && !exists2);
    }

    /**
     * Check that it is possible to get all keys.
     */
    @Test
    public void getKeysTest() {
        String[] keys = HeliosUserData.getInstance().getKeys();
        assertTrue(keys.length == 2);
    }

    /**
     * This method is run afre each individual test.
     */
    @After
    public void tearDown() {
        String[] keys = HeliosUserData.getInstance().getKeys();
        for (int i = 0; i < keys.length; i++) {
            HeliosUserData.getInstance().removeKey(keys[i]);
        }
    }
}
