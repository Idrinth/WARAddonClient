# WARAddonClient

A Client to recieve Addon-Data for Warhammer Online and handle the installation, removal and updating of them
This is meant to be a replacement for the Curse-Client, that no longer supports Warhammer Online. The website-version is at http://tools.idrinth.de/addons/ .
If you got ideas or requests, just open a ticket here :)

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/14c96ab0b81b46a7921c74b83bfa61ac)](https://www.codacy.com/gh/Idrinth/WARAddonClient/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Idrinth/WARAddonClient&amp;utm_campaign=Badge_Grade)

## Users

### Fully Packaged

#### Windows

The only file you need is the <a href="https://github.com/Idrinth/WARAddonClient/releases/latest">exe-file in the latest Release</a>. Download it and install the WARAddonClient on your computer.
This is an optional way of installing it and BUGs may or may not be fixed.

#### Linux (rpm)

You can grab the <a href="https://github.com/Idrinth/WARAddonClient/releases/latest">rpm-file in the latest Release</a>. Download it and install the WARAddonClient on your computer.
This is an optional way of installing it and BUGs may or may not be fixed.

#### Linux (deb)

You can grab the <a href="https://github.com/Idrinth/WARAddonClient/releases/latest">deb-file in the latest Release</a>. Download it and install the WARAddonClient on your computer.
This is an optional way of installing it and BUGs may or may not be fixed.

#### Linux (Arch)

You can use the <a href="https://aur.archlinux.org/packages/waraddonclient/">AUR package</a> to install the WARAddonClient on your computer.
This is an optional way of installing it and BUGs may or may not be fixed.

### JAR-Release (Prefered)

This is a java-Application, so you'll need to be able to run .jar files on the system you desire to use this on.
The only file you need is the <a href="https://github.com/Idrinth/WARAddonClient/releases/latest">jar-file in the latest Release</a>.
After downloading it, copy it to your Warhammer Online directory and run it.

Java is avaible at <a href="https://www.java.com" target="_blank">java.com</a> and is pretty much able to run on any system.

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

Other than that everyone is invited to grab the current developement .jar and test it, so issues can be fixed as early as possible.
