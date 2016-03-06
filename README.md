# WARAddonClient
A Client to recieve Addon-Data for Warhammer Online and handle the installation, removal and updating of them

This is meant to be a replacement for the Curse-Client, that no longer supports Warhammer Online. The website-version is at http://tools.idrinth.de/addons/ .

If you want to use this, the only thing needed is the jar-file in the folder bin, copy that to your Warhammer Online directory and run it.

If you got ideas or requests, just open a ticket here :)

If you find an addon without any .mod file, please add a simple "self.idrinth" textfile before uploading. The content of that should look like:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<UiMod>
	<name>zTimeLib</name>
	<version>0.6.0</version>
</UiMod>
```
