package eu.h2020.helios_social.core.profile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User data cache related to {@link HeliosProfileManager}.
 */
public class HeliosUserData {
    private static final String TAG = "HeliosUserData";
    private static final HeliosUserData ourInstance = new HeliosUserData();
    private Map<String, String> config;

    /**
     * Get the singleton of the HeliosUserData
     * @return {@link HeliosUserData}
     */
    public static HeliosUserData getInstance() {
        return ourInstance;
    }

    private HeliosUserData() {
        this.config =  Collections.synchronizedMap(new HashMap<String, String>());
    }

    /**
     * Get value by key.
     * @param key to be used to fetch the value
     * @return value or null.
     */
    public String getValue(String key) {
        return config.get(key);
    }

    /**
     * Set a value to the cache.
     * @param key used key.
     * @param value value to be saved.
     */
    public void setValue(String key, String value) {
        config.put(key, value);
    }

    /**
     * Check whether cache has a value for this key.
     * @param key to check.
     * @return boolean value whether a value for this key exists.
     */
    public boolean hasValue(String key) {
        return config.containsKey(key);
    }

    /**
     * Remove a value from the cache using a key.
     * @param key key to be used.
     * @return was the remove successful.
     */
    public String removeKey(String key) {
        return (String)config.remove(key);
    }

    /**
     * Get all keys in this cache.
     * @return String array of keys.
     */
    public String[] getKeys() {
        Set<String> keyset = config.keySet();
        return keyset.toArray(new String[0]);
    }
}
