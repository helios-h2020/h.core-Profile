# HELIOS Profile API #

## Introduction ##

The Profile Manager handles user account creation and creation of
associated keys. At least one key pair is needed for signing and
verification of electronic signatures and another public key - private
key pair is needed for encryption and decryption. Other keys are
needed for encrypting contents that the user is producing and/or
sending messages to other users. Uses HeliosKeyStoreManager and
HeliosCryptoManager from HELIOS Security and Privacy API.

HELIOS Profile API is one of the HELIOS Core APIs as highlighted in
the picture below:

![HELIOS Profile API](https://raw.githubusercontent.com/helios-h2020/h.core-Profile/master/doc/images/helios-profile.png "Profile API")

## API usage ##

See javadocs in [javadocs.zip](https://raw.githubusercontent.com/helios-h2020/h.core-Profile/master/doc/javadocs.zip).

### HeliosProfileManager ###

Provides the main functionalities, e.g. load and store attributes from
shared preferences.

Apps are expected to use `HeliosProfileManager` singleton class to
access the functionality. Applications should call the
`getInstance();` method in order to get a singleton object.

```
HeliosProfileManager profileMgr = HeliosProfileManager.getInstance();
```

Thereafter, the app can load/store values, e.g., to check and load/store user UUID:

```
	// Get default preferences userId
        String userId = profileMgr.load(this, getString(R.string.setting_user_id), android.content.Context.MODE_PRIVATE);
        if (userId.isEmpty()) {
            userId = UUID.randomUUID().toString();
            profileMgr.store(this, getString(R.string.setting_user_id), userId, android.content.Context.MODE_PRIVATE);
            Log.d(TAG, "Initialization, userId not found, now set as " + userId);
        } else {
            Log.d(TAG, "Initialization, userId was set as " + userId);
        }
```

This uses the key eu.h2020.helios_social.USER_ID as set in:

`<string name="setting_user_id">eu.h2020.helios_social.USER_ID</string>`

The other values used in the current integration can be seen from
TestClient app's resources under strings.xml

Check out TestClient's MainActivity.checkUserAccount method for
further examples. For instance, saved home location is loaded when the
application is started by:

`profileMgr.load(context, "homelat");`

### HeliosUserData ###

Provides a cache file for the profile attributes.

For example, to get the UUID of the user:

`String mUserId = HeliosUserData.getInstance().getValue(getString(R.string.setting_user_id));`

### Testing ###

There is a test application (see app subdirectory) that also gives an
example of the API usage. The application can be used to run a series
of basic profile operations by clicking the floating action button.
There is also a small set of unit tests to test basic HeliosUserData
functionality.

### Future work ###

* The most common used profile attributes could be included in the
  library itself. Although, the profile manager requirement was to be
  general and it should be easy to extend in case new attributes are
  needed, therefore it implements a key-value structure at the moment.

## Multiproject dependencies ##

HELIOS software components are organized into different repositories
so that these components can be developed separately avoiding many
conflicts in code integration. However, the modules also depend on
each other.

`Profile` depends on the projects:

* HELIOS Security and Privacy API - https://github.com/helios-h2020/h.core-SecurityAndPrivacyManager

* HELIOS Messaging API - https://github.com/helios-h2020/h.core-Messaging

### How to configure the dependencies ###

To manage project dependencies developed by the consortium, the
approach proposed is to use a private Maven repository with Nexus.

To avoid clone all dependencies projects in local, to compile the
"father" project. Otherwise, a developer should have all the projects
locally to be able to compile. Using Nexus, the dependencies are
located in a remote repository, available to compile, as described in
the next section.  Also to improve the automation for deploy,
versioning and distribution of the project.

### How to use the HELIOS Nexus ###

Similar to other dependencies available in Maven Central, Google or
others repositories. In this case we specify the Nexus repository
provided by Atos:

```
https://builder.helios-social.eu/repository/helios-repository/
```

This URL makes the project dependencies available.

To access, we simply need credentials, that we will define locally in
the variables `heliosUser` and `heliosPassword`.

The `build.gradle` of the project define the Nexus repository and the
credential variables in this way:

```
repositories {
        ...
        maven {
            url "https://builder.helios-social.eu/repository/helios-repository/"
            credentials {
                username = heliosUser
                password = heliosPassword
            }
        }
    }
```

And the variables of Nexus's credentials are stored locally at
`~/.gradle/gradle.properties`:

```
heliosUser=username
heliosPassword=password
```
To request Nexus username and password, contact Atos (jordi.hernandezv@atos.net).

### How to use the dependencies ###

To use the dependency in `build.gradle` of the "father" project, you
should specify the last version available in Nexus, related to the
last Jenkins's deploy.  For example, to declare the dependency on the
security and messaging module and their respective versions:

```
implementation 'eu.h2020.helios_social.core.security:security:1.0.3'
implementation 'eu.h2020.helios_social.core.messaging:messaging:2.0.15'
```

If the software module wants to use the profile module then the following line should
be put to the module specific build.gradle file:

    implementation 'eu.h2020.helios_social.core.profile:profile:1.0.11

## Android Studio project structure ##

This Android Studio Arctic Fox 2020.3.1 Patch 2 project contains the
following components:

* app - Profile API test application

* doc - Additional documentation files

* lib - Profile API implementation
