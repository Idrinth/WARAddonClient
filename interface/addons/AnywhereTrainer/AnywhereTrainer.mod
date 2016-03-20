<?xml version="1.0" encoding="UTF-8"?>
<ModuleFile xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" >

	<UiMod name="AnywhereTrainer" version="1.3" date="01/30/2012">
  	<Author name="DarthVon and Alpha_Male" email="alpha_male@speakeasy.net" />
    <Description text="Allows quick and easy access to all the training windows without having to be near the NPC trainers." />

		<VersionSettings gameVersion="1.4.5" windowsVersion="1.0" savedVariablesVersion="1.0" />

		<WARInfo>
			<Categories>
				<Category name="RVR" />
				<Category name="CAREER_SPECIFIC" />
				<Category name="OTHER" />
			</Categories>
			<Careers>
				<Career name="BLACKGUARD" />
				<Career name="WITCH_ELF" />
				<Career name="DISCIPLE" />
				<Career name="SORCERER" />
				<Career name="IRON_BREAKER" />
				<Career name="SLAYER" />
				<Career name="RUNE_PRIEST" />
				<Career name="ENGINEER" />
				<Career name="BLACK_ORC" />
				<Career name="CHOPPA" />
				<Career name="SHAMAN" />
				<Career name="SQUIG_HERDER" />
				<Career name="WITCH_HUNTER" />
				<Career name="KNIGHT" />
				<Career name="BRIGHT_WIZARD" />
				<Career name="WARRIOR_PRIEST" />
				<Career name="CHOSEN" />
				<Career name="MARAUDER" />
				<Career name="ZEALOT" />
				<Career name="MAGUS" />
				<Career name="SWORDMASTER" />
				<Career name="SHADOW_WARRIOR" />
				<Career name="WHITE_LION" />
				<Career name="ARCHMAGE" />
			</Careers>
		</WARInfo>

		<Dependencies>
			<Dependency name="EA_LegacyTemplates" />
			<Dependency name="EATemplate_DefaultWindowSkin" />
			<Dependency name="EA_AbilitiesWindow" />
			<Dependency name="EA_CharacterWindow" />
		</Dependencies>
		
		<Files>
			<File name="source/AnywhereTrainer.lua" />
			<File name="source/AnywhereTrainer.xml" />
		</Files>

        <OnInitialize>
            <CallFunction name="AnywhereTrainer.Initialize" />
        </OnInitialize>
        <OnShutdown>
            <CallFunction name="AnywhereTrainer.Shutdown" />
        </OnShutdown>
    </UiMod>
    
</ModuleFile>    