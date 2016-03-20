<?xml version="1.0" encoding="UTF-8"?>
<ModuleFile xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<UiMod name="WarBoard_AAOTracker" version="1.0" date="20/08/2010" >
		<Author name="Medikage" email="medikage@gmail.com" />
		<Description text="The Against All Odds (AAO) Tracker addon for WarBoard" />
		<VersionSettings gameVersion="1.3.6" windowsVersion="1.0" savedVariablesVersion="1.0" />
		<Dependencies>
			<Dependency name="WarBoard"   />
			<Dependency name="AAOTracker" />
		</Dependencies>
		<Files>
			<File name="AAOTWarBoard.xml" />
		</Files>
		<OnInitialize>
			<CallFunction name="AAOTWarBoard.Initialise" />
		</OnInitialize>
	</UiMod>
</ModuleFile>