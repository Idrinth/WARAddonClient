# WARAddonClient

A Client to recieve Addon-Data for Warhammer Online and handle the installation, removal and updating of them
This is meant to be a replacement for the Curse-Client, that no longer supports Warhammer Online. The website-version is at http://tools.idrinth.de/addons/ .
If you got ideas or requests, just open a ticket here :)

## Users

This is a java-Application, so you'll need to be able to run .jar files on the system you desire to use this on.
The only file you need is the <a href="https://github.com/Idrinth/WARAddonClient/releases/latest">jar-file in the latest Release</a>.
After downloading it, copy it to your Warhammer Online directory and run it.

## Addon Authors

There is now an option to let data be uploaded automatically - if the user so choses.
This requires an "upload.idrinth" file in the addon's main directory, for example:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Upload>
	<File>demo.xml</File>
	<Url>http://tools.idrinth.de/my-test-upload.php</Url>
	<Reason>This is just a test</Reason>
</Upload>
```

This example would grab all files that are named "demo.xml" in /user/settings and subfolders.
If the user allowed the related addon to automatically upload files, the found file will be attached as a body to a post request to the configured URL.
The Reason-tag wrapps your own text, that is shown to users when they have the option of allowing automatic uploads or not.
Obviously, it would be helpful to provide a short explanation to the readers, so they'll recall why they should opt in.

## Contributions

I'll happily take contributions, code reviews or improvements.
If you want to supply code, please don't just import everything and work with fully qualified names to prevent later collisions.

Other than that everyone is invited to grap the current developement .jar and test it, so issues can be fixed as early as possible.