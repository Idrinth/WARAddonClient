--	Title: AnywhereTrainer v1.3
--
--	Author: DarthVon (thedpui02@sneakemail.com) - Original author
--					Alpha_Male (alpha_male@speakeasy.net) - Current maintainer
--
--	Description: Allows quick and easy access to all the training windows without having to be near the NPC trainers.
--
--							 Simply open the Character Window, there will be 4 new buttons on the right side. Hover over each
--							 button to see what it does.
--
--	Features: * Allows access to the following training interfaces:
--							* Core Training
--							* Mastery Training
--							* Renown Training
--							* Tome Training 
--						* Integrates into the Character Window seamlessly
--						* Zero configuration required 
--
--	Files: \source\AnywhereTrainer.lua
--				 \source\AnywhereTrainer.xml
--				 \AnywhereTrainer.mod
--				 \AnywhereTrainer_Install.txt
--				 \AnywhereTrainer_Readme.txt
--
--	Version History: 1.0 - Initial Release
--									 1.1 - New Features and Fixes
--											 - Added support for 1.3.1 WARInfo Categories and Careers (AnywhereTrainer.mod)
--											 - Added support for version info (AnywhereTrainer.mod)
--											 - Updated mod version information for 1.4.0
--											 - AnywhereTrainer window position fix (1.4.0 Character Window changes)
--											 - Directory structure reorganization
--											 - Readme and install file additions
--									 1.2 - New Features and Maintenance Update
--											 - Updated mod version information for 1.4.1
--											 - Added support for AdvancedRenownTrainer
--											   (http://war.curse.com/downloads/war-addons/details/advancedrenowntrainer.aspx)
--									 1.3 - Maintenance Update
--											 - Updated mod version information for 1.4.5
									 
--  Supported Versions: Warhammer Online v1.4.5
--
--	Dependencies: None
--
--	Addon Compatability: Compatible with:
--											 - Character View Expanded Stats(CaVES) by Alpha_Male (alpha_male@speakeasy.net)
--											 - Advanced Renown Trainer by Varonth
--
--	Future Features: None currently
--
--	Known Issues: The game requires you to be near the specific NPC to actually purchase new abilities, so you 
--								cannot actually use the windows made available by AnywhereTrainer to train anywhere. You can 
--								however view all the details of what you have and what is available.
--
--	Additional Credits: Varonth for his collaboration and changes to make AdvancedRenownTrainer and
--											AnywhereTrainer compatible
--
--	Special Thanks:	EA/Mythic for a great game and for releasing the API specs
--									The War Wiki (www.thewarwiki.com) for being a great source of knowledge for WAR mod development
--									www.curse.com and www.curseforge.com for hosting WAR mod files and projects
--									Ominous Latin Name guild on Gorfang for testing and feedback
--									Trouble guild on Gorfang for support
--
--	License/Disclaimers: This addon and it's relevant parts and files are released under the 
--											 original author's MIT License.
--
--

------------------------------------------------------------------
----  Global Variables
------------------------------------------------------------------

AnywhereTrainer = {}

------------------------------------------------------------------
----  Local Variables
------------------------------------------------------------------

local DefaultCharacterWindow_UpdateMode = CharacterWindow.UpdateMode

-- Support for the mod AdvancedRenownTrainer by Varonth
local bAdvancedRenownTrainer = nil -- boolean that denotes that AdvancedRenownTrainer is installed

------------------------------------------------------------------
----  Core Functions
------------------------------------------------------------------

function AnywhereTrainer.Initialize( )

	-- Get supported mod data
	AnywhereTrainer.GetSupportedModData()

	AnywhereTrainer.TabData = {
		{ Name="AnywhereTrainerTopBookend", Template="AnywhereTrainerTopBookendTemplate", TextureWindow="", Slice="TabTopper", Anchor={ Point="topright", RelativeTo="CharacterWindow", RelativePoint="topleft", X=-73, Y=150 } },
		{ Name="AnywhereTrainerTabCareer", Template="AnywhereTrainerTabTemplate", TextureWindow="Icon", Slice="Tab-Core", Anchor={ Point="bottomleft", RelativeTo="AnywhereTrainerTopBookend", RelativePoint="topleft", X=0, Y=2 }, Tooltip=L"Career Training", OnLeftClick=AnywhereTrainer.OnLeftClickCareer },
		{ Name="AnywhereTrainerTabMastery", Template="AnywhereTrainerTabTemplate", TextureWindow="Icon", Slice="Tab-PassiveSkills", Anchor={ Point="bottomleft", RelativeTo="AnywhereTrainerTabCareer", RelativePoint="topleft", X=0, Y=2 }, Tooltip=L"Mastery Training", OnLeftClick=AnywhereTrainer.OnLeftClickMastery },
		{ Name="AnywhereTrainerTabRenown", Template="AnywhereTrainerTabTemplate", TextureWindow="Icon", Slice="Tab-RvR", Anchor={ Point="bottomleft", RelativeTo="AnywhereTrainerTabMastery", RelativePoint="topleft", X=0, Y=2 }, Tooltip=L"Renown Training", OnLeftClick=AnywhereTrainer.OnLeftClickRenown },
		{ Name="AnywhereTrainerTabTome", Template="AnywhereTrainerTabTemplate", TextureWindow="Icon", Slice="Tab-Tome", Anchor={ Point="bottomleft", RelativeTo="AnywhereTrainerTabRenown", RelativePoint="topleft", X=0, Y=2 }, Tooltip=L"Tome Training", OnLeftClick=AnywhereTrainer.OnLeftClickTome },
		{ Name="AnywhereTrainerBottomBookend", Template="AnywhereTrainerBottomBookendTemplate", TextureWindow="", Slice="TabBottom", Anchor={ Point="bottomleft", RelativeTo="AnywhereTrainerTabTome", RelativePoint="topleft", X=0, Y=2 } }
	}

	local w, h = WindowGetDimensions( "CharacterWindow" )

	WindowSetDimensions( "CharacterWindow", w + 43, h )

	AnywhereTrainer.ReadjustWindowAnchors( "CharacterWindowBackground", "bottomright", -43, 0 )
	AnywhereTrainer.ReadjustWindowAnchors( "DyeMerchantButtons", "bottom", -(43/2), 0 )

	for _, tab in ipairs(AnywhereTrainer.TabData) do
		CreateWindowFromTemplate( tab.Name, tab.Template, "CharacterWindow" )
		WindowAddAnchor( tab.Name, tab.Anchor.Point, tab.Anchor.RelativeTo, tab.Anchor.RelativePoint, tab.Anchor.X, tab.Anchor.Y )
		DynamicImageSetTextureSlice( tab.Name .. tab.TextureWindow, tab.Slice )
	end
	
	CharacterWindow.UpdateMode = AnywhereTrainer.CharacterWindow_UpdateMode

end

function AnywhereTrainer.GetSupportedModData()
		local supportedModData = {}
		supportedModData = ModulesGetData()

    for modIndex, modData in ipairs( supportedModData ) do
        if (modData.name == "AdvancedRenownTrainer") then
        		if (modData.isEnabled == true) then
        				bAdvancedRenownTrainer = true
        		else
        				bAdvancedRenownTrainer = false
        		end
        end
    end
end

function AnywhereTrainer.CharacterWindow_UpdateMode( mode )
	DefaultCharacterWindow_UpdateMode( mode )
	if( (mode == CharacterWindow.MODE_NORMAL) or (mode == CharacterWindow.MODE_DYE_MERCHANT) ) then
		local w, h = WindowGetDimensions( "CharacterWindow" )
		WindowSetDimensions( "CharacterWindow", w + 43, h )
	end
end

function AnywhereTrainer.ReadjustWindowAnchors( windowName, point, xOffset, yOffset )
	local anchors = {}
	local anchorCount = WindowGetAnchorCount( windowName )

	for i=1,anchorCount do
		table.insert( anchors, { WindowGetAnchor( windowName, i ) } )
		if( anchors[i][1] == point ) then
			anchors[i][4] = anchors[i][4] + xOffset
			anchors[i][5] = anchors[i][5] + yOffset
		end
	end

	WindowClearAnchors( windowName )

	for i=1,anchorCount do
		WindowAddAnchor( windowName, anchors[i][1], anchors[i][3], anchors[i][2], anchors[i][4], anchors[i][5] )
	end
end

function AnywhereTrainer.Shutdown( )

end


------------------------------------------------------------------
---- Mouseovers/Tooltips
------------------------------------------------------------------

function AnywhereTrainer.OnMouseOver( )
	for _, tab in ipairs(AnywhereTrainer.TabData) do
		if( tab.Name .. "InactiveImage" == SystemData.MouseOverWindow.name ) then
			if( tab.Tooltip ~= nil ) then
				Tooltips.CreateTextOnlyTooltip( SystemData.MouseOverWindow.name, tab.Tooltip )
				Tooltips.AnchorTooltip( Tooltips.ANCHOR_WINDOW_RIGHT );
			end
			return
		end
	end
end


------------------------------------------------------------------
---- Trainer Window Functions
------------------------------------------------------------------

function AnywhereTrainer.OnLButtonUp( )
	for _, tab in ipairs(AnywhereTrainer.TabData) do
		if( tab.Name .. "InactiveImage" == SystemData.MouseOverWindow.name ) then
			if( tab.OnLeftClick ~= nil ) then
				tab.OnLeftClick( )
			end
			return
		end
	end
end

function AnywhereTrainer.OnLeftClickCareer( )
	if( not WindowGetShowing( "EA_Window_InteractionCoreTraining" ) ) then
		EA_Window_InteractionCoreTraining.Show( )
	else
		EA_Window_InteractionCoreTraining.Hide( )
	end
end

function AnywhereTrainer.OnLeftClickMastery( )
	if( not WindowGetShowing( "EA_Window_InteractionSpecialtyTraining" ) ) then
		EA_Window_InteractionSpecialtyTraining.Show( )
	else
		EA_Window_InteractionSpecialtyTraining.Hide( )
	end
end

function AnywhereTrainer.OnLeftClickRenown( )
	-- if AdvancedRenownTrainer is installed and activated, show this
	-- window instead of the default Renown Training Window
	if (bAdvancedRenownTrainer) then
		if( not WindowGetShowing( "AdvancedRenownTrainingWindow" ) ) then
			AdvancedRenownTraining.AnywhereShow()
		else
			AdvancedRenownTraining.Hide()
		end
	else -- if AdvancedRenownTrainer is not installed and activated display the default Renown Trainer Window
		if( not WindowGetShowing( "EA_Window_InteractionRenownTraining" ) ) then
			EA_Window_InteractionRenownTraining.Show( )
		else
			EA_Window_InteractionRenownTraining.Hide( )
		end
	end
end

function AnywhereTrainer.OnLeftClickTome( )
	if( not WindowGetShowing( "EA_Window_InteractionTomeTraining" ) ) then
		EA_Window_InteractionTomeTraining.Show( )
	else
		EA_Window_InteractionTomeTraining.Hide( )
	end
end