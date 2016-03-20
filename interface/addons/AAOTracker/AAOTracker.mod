<?xml version="1.0" encoding="UTF-8"?>
<ModuleFile xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<UiMod name="AAOTracker" version="1.0" date="20/08/2010" >
		<Author name="Medikage" email="medikage@gmail.com" />
		<Description text="The Against All Odds (AAO) Tracker simply tracks the AAO buff and reports it's status to you via alerts or a dedicated monitor window." />
		<VersionSettings gameVersion="1.3.6" windowsVersion="1.0" savedVariablesVersion="1.0" />
		<Dependencies>
			<Dependency name="LibSlash" />
			<Dependency name="EATemplate_DefaultWindowSkin" />
		</Dependencies>
		<Files>
			<File name="AAOTracker.xml" />
		</Files>
		<SavedVariables>
			<SavedVariable name="AAOTracker.Settings" />
		</SavedVariables>
		<OnInitialize>
			<CallFunction name="AAOTracker.Initialise" />
		</OnInitialize>
		<OnUpdate/>
		<OnShutdown>
			<CallFunction name="AAOTracker.Shutdown" />
		</OnShutdown>
	</UiMod>
</ModuleFile>