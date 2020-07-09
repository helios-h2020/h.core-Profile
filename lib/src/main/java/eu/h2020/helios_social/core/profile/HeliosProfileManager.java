package eu.h2020.helios_social.core.profile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.security.KeyPair;
import java.security.PrivateKey;

import javax.crypto.SecretKey;

import eu.h2020.helios_social.core.security.HeliosCryptoManager;
import eu.h2020.helios_social.core.security.HeliosKeyStoreException;
import eu.h2020.helios_social.core.security.HeliosKeyStoreManager;

/**
 * The HeliosProfileManager class handles user account creation and creation of
 * associated keys. Uses {@link SharedPreferences} with default and private mode operations to
 * store and retrieve values.
 *
 * Requirement was to be general and it should be easy to extend in case new attributes are needed,
 * therefore  this implements a key-value structure at the moment.
 */
public class HeliosProfileManager {
    private static final HeliosProfileManager ourInstance = new HeliosProfileManager();
    private static final String TAG = "HeliosProfileManager";

    /**
     * Get the singleton instance of the class.
     * @return singleton {@link HeliosProfileManager}
     */
    public static HeliosProfileManager getInstance() {
        return ourInstance;
    }

    private HeliosProfileManager() {
    }

    /**
     * Init required cryptographic keys. Uses {@link HeliosCryptoManager} and {@link HeliosKeyStoreManager}
     * to load the private keys used. If they are not present, they will be generated and stored with
     * {@link HeliosKeyStoreManager}
     *
     * @param appContext ({@link Context})
     */
    public void keyInit(Context appContext) {
        try {
            HeliosCryptoManager cryptoManager;
            HeliosKeyStoreManager keyManager;

            Log.d(TAG, "Check security operations");
            cryptoManager = HeliosCryptoManager.getInstance();
            SecretKey aesKey = cryptoManager.generateAESKey();
            if (cryptoManager == null) {
                Log.e(TAG, "Failed to get HeliosCryptoManager!!!");
                return;
            }
            keyManager = new HeliosKeyStoreManager(appContext);
            if (keyManager == null) {
                Log.e(TAG, "Failed to get HeliosKeyStoreManager!!!");
                return;
            }

            Log.d(TAG, "Check PRIV_RSA_MEK");
            PrivateKey rsakey;
            rsakey = keyManager.retrievePrivateKey("PRIV_RSA_MEK", "passphrase");
            if (rsakey == null) {
                Log.d(TAG, "Generating PRIV_RSA_MEK");
                KeyPair rsaKeyPair = cryptoManager.generateRSAKeyPair();
                keyManager.storePrivateKey(rsaKeyPair.getPrivate(), "PRIV_RSA_MEK", "passphrase");
            }
            Log.d(TAG, "Check PRIV_RSA_MSK");
            rsakey = keyManager.retrievePrivateKey("PRIV_RSA_MSK", "passphrase");
            if (rsakey == null) {
                Log.d(TAG, "Generating PRIV_RSA_MSK");
                KeyPair rsaKeyPair = cryptoManager.generateRSAKeyPair();
                keyManager.storePrivateKey(rsaKeyPair.getPrivate(), "PRIV_RSA_MSK", "passphrase");
            }

            Log.d(TAG, "Key aliases");
            String[] aliases = keyManager.getAliases();
            if (aliases != null && aliases.length > 0) {
                for (int i = 0; i < aliases.length; i++) {
                    Log.d(TAG, "Key alias: "  + aliases[i]);
                }
            } else {
                Log.d(TAG, "No keys found");
            }
        } catch (HeliosKeyStoreException e) {
            Log.d(TAG, "Caught HELIOS key store exception " + e.getMessage());
        } catch (Exception e) {
            Log.d(TAG, "Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load a value from the profile using {@link PreferenceManager#getDefaultSharedPreferences(Context)}.
     * Saves the loaded value to {@link HeliosUserData}.
     *
     * @param context ({@link Context})
     * @param key key to load the value
     * @return String value of the property.
     */
    public String load(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPrefs.getString(key, "");
        Log.d(TAG, key + " is " + value);
        HeliosUserData.getInstance().setValue(key, value);
        return value;
    }

    /**
     * Store a value to the profile using {@link PreferenceManager#getDefaultSharedPreferences(Context)}.

     * @param context ({@link Context})
     * @param key used to store the value.
     * @param value Value to be stored.
     */
    public void store(Context context, String key, String value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Load a value from the profile preferences using a privacy mode. Private preferences for
     * this activity. Saves the value to {@link HeliosUserData}.
     *
     * @param activity {@link Activity}
     * @param key key to load value from
     * @param mode Operating mode, see {@link android.content.Context#MODE_PRIVATE}
     * @return String value
     */
    public String load(Activity activity, String key, int mode) {
        SharedPreferences sharedPrefs = activity.getPreferences(mode);
        String value = sharedPrefs.getString(key, "");
        Log.d(TAG, key + " is " + value);
        HeliosUserData.getInstance().setValue(key, value);
        return value;
    }

    /**
     * Store a value to profile preferences using a privacy mode. Private preferences for
     * this activity. Saves the loaded value to {@link HeliosUserData}.
     *
     * @param activity {@link Activity}
     * @param key key to load value from
     * @param value value to store
     * @param mode Operating mode, see {@link android.content.Context#MODE_PRIVATE}
     */
    public void store(Activity activity, String key, String value, int mode) {
        HeliosUserData.getInstance().setValue(key, value);
        SharedPreferences sharedPrefs = activity.getPreferences(mode);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
