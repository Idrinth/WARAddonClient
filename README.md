# WARAddonClient
A Client to recieve Addon-Data for Warhammer Online and handle the installation, removal and updating of them

This is meant to be a replacement for the Curse-Client, that no longer supports Warhammer Online. The website-version is at http://tools.idrinth.de/addons/ .

If you want to use this, the only thing needed is the jar-file in the folder bin, copy that to your Warhammer Online directory and run it.

If you got ideas or requests, just open a ticket here :)

For Addon-Authors there is now an option to let data be uploaded automatically - if the user so choses. this requires an upload.idrinth file in the addon's main dire, that looks somewhat like the following:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Upload>
	<File>demo.xml</File>
	<Url>http://tools.idrinth.de/my-test-upload.php</Url>
	<Reason>This is just a test</Reason>
</Upload>
```

This example would grab all files that turn up newly or are changed within the subtree of user/settings and upload them to the given Url - if the user opted in. The Reason is displayed above the OptIn and should contain information about what and why you want to upload the given file.