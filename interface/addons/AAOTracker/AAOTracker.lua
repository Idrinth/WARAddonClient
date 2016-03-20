local EnableModCheckToggle = false
local EnableMonitorCheckToggle = false

local VERSION = 1.0

AAOTracker = {}

AAOTracker.Debug = {}
AAOTracker.Debug.Enabled = false
AAOTracker.Debug.Level   = 0
AAOTracker.Enabled       = true

AAOTracker.Buff = {}
AAOTracker.Buff.Active     = false
AAOTracker.Buff.EffectText = ""
AAOTracker.Buff.Id         = 24658
AAOTracker.Buff.Percentage = 0
AAOTracker.Buff.Name       = L"Entgegen jeder Chance"

AAOTracker.Monitor = {}
AAOTracker.Monitor.Window = "AAOTrackerMonitor"

AAOTracker.Options = {}
AAOTracker.Options.Window = "AAOTrackerOptions"
AAOTracker.Options.Labels = {
	   { name = AAOTracker.Options.Window.."TitleBarText"       , value = "AAO Options v"..VERSION}
	 , { name = AAOTracker.Options.Window.."ModLabel"           , value = "Enable the addon"}
	 , { name = AAOTracker.Options.Window.."MonitorLabel"       , value = "Display Monitor window"}
	 , { name = AAOTracker.Options.Window.."AlertsLabel"        , value = "Select an alert type"}
	 , { name = AAOTracker.Options.Window.."ColourSectionLabel" , value = "Select text colour when:"}
	 , { name = AAOTracker.Options.Window.."ColourZeroLabel"    , value = "Percent = 0"}
	 , { name = AAOTracker.Options.Window.."ColourOneLabel"     , value = "Percent = 100+"}
	 , { name = AAOTracker.Options.Window.."ColourTwoLabel"     , value = "Percent = 200+"}
	 , { name = AAOTracker.Options.Window.."ColourThreeLabel"   , value = "Percent = 300+"}
	 , { name = AAOTracker.Options.Window.."ColourFourLabel"    , value = "Percent = 400" }
	                        }

AAOTracker.WarBoard = {}
AAOTracker.WarBoard.Active = false
							
AAOTracker.Colours = {
	  {name = "Green"      , value = DefaultColor.GREEN             } -- 1
	, {name = "Red"        , value = DefaultColor.RED               } -- 2
	, {name = "Magenta"    , value = DefaultColor.MAGENTA           } -- 3
	, {name = "Blue"       , value = DefaultColor.BLUE              } -- 4
	, {name = "Light Blue" , value = DefaultColor.LIGHT_BLUE        } -- 5
	, {name = "White"      , value = DefaultColor.WHITE             } -- 6
	, {name = "Yellow"     , value = DefaultColor.YELLOW            } -- 7
	, {name = "Orange"     , value = DefaultColor.ORANGE            } -- 8
	, {name = "Gold"       , value = DefaultColor.GOLD              } -- 9
	, {name = "Purple"     , value = DefaultColor.PURPLE            } -- 10
	, {name = "Teal"       , value = DefaultColor.TEAL              } -- 11
	, {name = "Grey"       , value = DefaultColor.MEDIUM_LIGHT_GRAY } -- 12
	, {name = "Brown"      , value = DefaultColor.BROWN	            } -- 13
                    }

AAOTracker.Alerts = {
	  { id = nil                                                   , name = "Do not alert" }
	, { id = SystemData.AlertText.Types.DEFAULT                    , name = "Default" }
	, { id = SystemData.AlertText.Types.COMBAT                     , name = "Combat" }
	, { id = SystemData.AlertText.Types.QUEST_NAME                 , name = "Quest Name" }
	, { id = SystemData.AlertText.Types.QUEST_CONDITION            , name = "Quest Condition" }
	, { id = SystemData.AlertText.Types.QUEST_END                  , name = "Quest End" }
	, { id = SystemData.AlertText.Types.OBJECTIVE                  , name = "Objective" }
	, { id = SystemData.AlertText.Types.RVR                        , name = "RvR" }
	, { id = SystemData.AlertText.Types.SCENARIO                   , name = "Scenario" }
	, { id = SystemData.AlertText.Types.MOVEMENT_RVR               , name = "Movement RvR" }
	, { id = SystemData.AlertText.Types.ENTERAREA                  , name = "Enter Area" }
	, { id = SystemData.AlertText.Types.STATUS_ERRORS              , name = "Status Errors" }
	, { id = SystemData.AlertText.Types.STATUS_ACHIEVEMENTS_GOLD   , name = "Status Achievements Gold"   }
	, { id = SystemData.AlertText.Types.STATUS_ACHIEVEMENTS_PURPLE , name = "Status Achievements Purple" }
	, { id = SystemData.AlertText.Types.STATUS_ACHIEVEMENTS_RANK   , name = "Status Achievements Rank"   }
	, { id = SystemData.AlertText.Types.STATUS_ACHIEVEMENTS_RENOUN , name = "Status Achievements Renown" }
	, { id = SystemData.AlertText.Types.PQ_ENTER                   , name = "PQ Enter" }
	, { id = SystemData.AlertText.Types.PQ_NAME                    , name = "PQ Name" }
	, { id = SystemData.AlertText.Types.PQ_DESCRIPTION             , name = "PQ Description" }
	, { id = SystemData.AlertText.Types.ENTERZONE                  , name = "Enter Zone" }
	, { id = SystemData.AlertText.Types.ORDER                      , name = "Order" }
	, { id = SystemData.AlertText.Types.DESTRUCTION                , name = "Destruction" }
	, { id = SystemData.AlertText.Types.NEUTRAL                    , name = "Neutral" }
	, { id = SystemData.AlertText.Types.ABILITY                    , name = "Ability" }
	, { id = SystemData.AlertText.Types.BO_ENTER                   , name = "BO Enter" }
	, { id = SystemData.AlertText.Types.BO_NAME                    , name = "BO Name" }
	, { id = SystemData.AlertText.Types.BO_DESCRIPTION             , name = "BO Description" }
	, { id = SystemData.AlertText.Types.ENTER_CITY                 , name = "Enter City" }
	, { id = SystemData.AlertText.Types.CITY_RATING                , name = "City Rating" }
	, { id = SystemData.AlertText.Types.GUILD_RANK                 , name = "Guild Rank" }
	, { id = SystemData.AlertText.Types.RRQ_UNPAUSED               , name = "RRQ Unpaused" }
	, { id = SystemData.AlertText.Types.LARGE_ORDER                , name = "Large Order" }
	, { id = SystemData.AlertText.Types.LARGE_DESTRUCTION          , name = "Large Destruction" }
	, { id = SystemData.AlertText.Types.LARGE_NEUTRAL              , name = "Large Neutral" }
	                         }

function AAOTracker.Initialise()
	AAOTracker.DebugMsg(L"Entering Initialise",3)
	-- Check to see if any options are saved on the users profile. If not set default values.
	if not AAOTracker.Settings then
		AAOTracker.Settings = {}
		AAOTracker.Settings.Enabled = true
		AAOTracker.Settings.AlertChannel = 1
		AAOTracker.Settings.Monitor = {}
		AAOTracker.Settings.Monitor.Show = true
		AAOTracker.Settings.TextColour = {}
		AAOTracker.Settings.TextColour.Zero  = 2
		AAOTracker.Settings.TextColour.One   = 8
		AAOTracker.Settings.TextColour.Two   = 7
		AAOTracker.Settings.TextColour.Three = 1
		AAOTracker.Settings.TextColour.Four  = 4
		AAOTracker.Settings.Version = VERSION
	end
	AAOTracker.Settings.Version = VERSION
	AAOTracker.Enabled = AAOTracker.Settings.Enabled

	--Initialise the Gui
	AAOTracker.Monitor.WindowInit()
	AAOTracker.Options.WindowInit()

	if AAOTracker.Enabled then
		AAOTracker.RegisterEvents()
	end
	-- Register the slash command.
	LibSlash.RegisterSlashCmd("AAOT", AAOTracker.Slash)
	LibSlash.RegisterSlashCmd("AAOTd", function(args) AAOTracker.SlashDebug(args) end)

	local msg = ""
	if AAOTracker.Enabled then
		msg = L"AAOTracker is enabled. Type /AAOT for options"
	else
		msg = L"AAOTracker is disabled. Type /AAOT for options"
	end
	EA_ChatWindow.Print(msg)
	AAOTracker.DebugMsg(L"Leaving Initialise",3)
end

function AAOTracker.RegisterEvents()
	AAOTracker.DebugMsg(L"Entering RegisterEvents",3)
	-- Hook the mod into the appropriate events
	RegisterEventHandler(SystemData.Events.PLAYER_EFFECTS_UPDATED, "AAOTracker.BuffTracker")
	AAOTracker.DebugMsg(L"Leaving RegisterEvents",3)
end

function AAOTracker.UnregisterEvents()
	AAOTracker.DebugMsg(L"Entering UnregisterEvents",3)
	-- Remove the mod hooks
	UnregisterEventHandler(SystemData.Events.PLAYER_EFFECTS_UPDATED, "AAOTracker.BuffTracker")
	AAOTracker.DebugMsg(L"Leaving UnregisterEvents",3)
end

function AAOTracker.DebugMsg(str,lvl)
	-- If debug is enabled and the priority of the message is high enough then send a debug message
	if AAOTracker.Debug.Enabled and lvl <= AAOTracker.Debug.Level then
		d(towstring(str))
	end
end

function AAOTracker.Shutdown()
	AAOTracker.DebugMsg(L"Entering Shutdown",3)
	UnregisterEvents()
	DestroyWindow(AAOTracker.Monitor.Window)
	DestroyWindow(AAOTracker.Options.Window)
	AAOTracker.DebugMsg(L"Leaving Shutdown",3)
end

function AAOTracker.SlashDebug(args)
	AAOTracker.DebugMsg(L"Entering SlashDebug",3)
	local arg, argv = args:match("([a-z0-9]+)[ ]?(.*)")
	AAOTracker.DebugMsg("SlashDebug: arg ["..arg.."] argv ["..argv.."]",2)
	if arg == nil then
		AAOTracker.Debug.Enabled = not AAOTracker.Debug.Enabled
		return
	end
	-- Set to true to display debug messaAAOTracker in the debug window (Note: type /debug in game to see this window and pAAOTrackers the button "Logs(off)")
	-- Set the debug level (4 = Update Ticks, 3 = Position, 2 = Setting values, 1 = Return values, 0 = Illegal flow catches)
	if arg == "on" then
		AAOTracker.Debug.Enabled = true
	elseif arg == "off" then
		AAOTracker.Debug.Enabled = false
	elseif arg == "1" then
		AAOTracker.Debug.Enabled = true
		AAOTracker.Debug.Level = 1
	elseif arg == "2" then
		AAOTracker.Debug.Enabled = true
		AAOTracker.Debug.Level = 2
	elseif arg == "3" then
		AAOTracker.Debug.Enabled = true
		AAOTracker.Debug.Level = 3
	elseif arg == "4" then
		AAOTracker.Debug.Enabled = true
		AAOTracker.Debug.Level = 4
	else
		AAOTracker.DebugMsg("SlashDebug: Invalid Argument ["..arg.."]",0)
	end
	AAOTracker.DebugMsg(L"Leaving SlashDebug",3)
end

function AAOTracker.BuffTracker(updatedBuffsTable, isFullList)
	AAOTracker.DebugMsg(L"Entering BuffTracker",3)
	if( not updatedBuffsTable ) then
		AAOTracker.DebugMsg(L"Invalid updatedBuffsTable",2)
		AAOTracker.DebugMsg(L"Leaving BuffTracker",3)
		return
	end
	-- Cycle through the list of buffs provided.
	for buffId, buffData in pairs( updatedBuffsTable ) do
		if buffData.name ~= nil then
			-- Check to see if the Buffs name matches the AAO ability name
			if buffData.ID == AAOTracker.Buff.ID then
				AAOTracker.DebugMsg(L"BuffTracker: Name:["..AAOTracker.Buff.Name..L"] has been found",2)
				AAOTracker.Buff.Id = buffId
				local newPercentage = AAOTracker.GetPercentage(buffData.effectText)
				if newPercentage ~= AAOTracker.Buff.Percentage then
					AAOTracker.Buff.Percentage = newPercentage
					AAOTracker.Buff.Active = true
					AAOTracker.Buff.Deactivate = false
					AAOTracker.Buff.EffectText = buffData.effectText
					AAOTracker.SendAlertText()
					AAOTracker.Monitor.UpdateText()
					if AAOTracker.WarBoard.Active then 
						AAOTWarBoard.UpdateText()
					end
				end
				AAOTracker.DebugMsg(L"Leaving BuffTracker",3)
				return
			end
		elseif AAOTracker.Buff.Id == buffId then
			AAOTracker.Buff.Deactivate = true
			AAOTracker.DebugMsg(L"Leaving BuffTracker",3)
			return			
		end
	end
	if AAOTracker.Buff.Id ~= nil and AAOTracker.Buff.Deactivate then
		AAOTracker.Deactivate()
		AAOTracker.SendAlertText()
		AAOTracker.Monitor.UpdateText()
		if AAOTracker.WarBoard.Active then 
			AAOTWarBoard.UpdateText()
		end
	end
	AAOTracker.DebugMsg(L"Leaving BuffTracker",3)
end

function AAOTracker.Deactivate()
	AAOTracker.DebugMsg(L"Entering Deactivate",3)
	AAOTracker.Buff.Id = nil
	AAOTracker.Buff.Active = false
	AAOTracker.Buff.EffectText = ""
	AAOTracker.Buff.Percentage = 0
	AAOTracker.Buff.Deactivate = false
	AAOTracker.DebugMsg(L"Leaving Deactivate",3)
end

function AAOTracker.GetPercentage(Text)
	AAOTracker.DebugMsg(L"Entering GetPercentage",3)
	local percentage = string.match(tostring(Text), '%d%d%d')
	AAOTracker.DebugMsg(L"Leaving GetPercentage",3)
	return tonumber(percentage)
end

function AAOTracker.SendAlertText()
	AAOTracker.DebugMsg(L"Entering SendAlertText",3)
	if AAOTracker.Alerts[AAOTracker.Settings.AlertChannel].id == nil then return end
	local message
	if AAOTracker.Buff.Active then
		message = "Against All Odds is at "..AAOTracker.Buff.Percentage.."%"
	else
		message = "Against All Odds is no longer active"
	end
	AlertTextWindow.AddLine(AAOTracker.Alerts[AAOTracker.Settings.AlertChannel].id, towstring(message))
	AAOTracker.DebugMsg(L"Leaving SendAlertText",3)
end

----------------------------------
---   Gui Specific Functions   ---
----------------------------------

function AAOTracker.Slash()
	AAOTracker.DebugMsg(L"Entering Slash",3)
	AAOTracker.Options.ShowWindow()
	AAOTracker.DebugMsg(L"Leaving Slash",3)
end

---------------------------------------
-- Monitor Window Specific Functions --
---------------------------------------

function AAOTracker.Monitor.WindowInit()
	AAOTracker.DebugMsg(L"Entering Monitor.WindowInit",3)
	-- Create the window and then hide it.
	CreateWindow(AAOTracker.Monitor.Window, true)
	WindowSetShowing(AAOTracker.Monitor.Window, false)
	-- Register window with the layout editor so that it can be moved and resized there.
	LayoutEditor.RegisterWindow ( AAOTracker.Monitor.Window, L"AAO Tracker", L"AAO Tracker", true, true, true, nil)
	AAOTracker.Monitor.UpdateText()
	AAOTracker.Monitor.Display()
	AAOTracker.DebugMsg(L"Leaving Monitor.WindowInit",3)
end

function AAOTracker.Monitor.UpdateText()
	AAOTracker.DebugMsg(L"Entering Monitor.UpdateText",3)
	if not AAOTracker.Settings.Monitor.Show then return end
	local tint
	if AAOTracker.Buff.Percentage == 400 then
		tint = AAOTracker.Colours[AAOTracker.Settings.TextColour.Four].value
	elseif AAOTracker.Buff.Percentage > 299 then
		tint = AAOTracker.Colours[AAOTracker.Settings.TextColour.Three].value
	elseif AAOTracker.Buff.Percentage > 199 then
		tint = AAOTracker.Colours[AAOTracker.Settings.TextColour.Two].value
	elseif AAOTracker.Buff.Percentage > 100 then
		tint = AAOTracker.Colours[AAOTracker.Settings.TextColour.One].value
	else
		tint = AAOTracker.Colours[AAOTracker.Settings.TextColour.Zero].value
	end
	LabelSetTextColor(AAOTracker.Monitor.Window.."Text", tint.r, tint.g, tint.b )
	LabelSetText(AAOTracker.Monitor.Window.."Text", L"AAO: "..towstring(AAOTracker.Buff.Percentage)..L"%")
	AAOTracker.DebugMsg(L"Leaving Monitor.UpdateText",3)
end

function AAOTracker.Monitor.Display()
	AAOTracker.DebugMsg(L"Entering Monitor.Display",3)
	WindowSetShowing(AAOTracker.Monitor.Window, AAOTracker.Settings.Monitor.Show and AAOTracker.Settings.Enabled)
	AAOTracker.DebugMsg(L"Leaving Monitor.Display",3)
end

--------------------------------------
-- Option Window Specific Functions --
--------------------------------------

function AAOTracker.Options.WindowInit()
	AAOTracker.DebugMsg(L"Entering Options.WindowInit",3)
	-- Create the window and then hide it.
	CreateWindow(AAOTracker.Options.Window, true)
	WindowSetShowing(AAOTracker.Options.Window, false)
	-- Apply text to the buttons so they are not blank.
	ButtonSetText(AAOTracker.Options.Window.."SaveButton" , L"Save")
	ButtonSetText(AAOTracker.Options.Window.."CloseButton", L"Close")
	-- Apply text to the labels so they are not blank.
	for i,v in ipairs(AAOTracker.Options.Labels) do
		LabelSetText(v.name,towstring(v.value))
	end
	--Pre-populate the combo boxes with the available colours
	for i, v in ipairs(AAOTracker.Colours) do
		ComboBoxAddMenuItem(AAOTracker.Options.Window.."ColourZeroComboBox" , towstring(v.name))
		ComboBoxAddMenuItem(AAOTracker.Options.Window.."ColourOneComboBox"  , towstring(v.name))
		ComboBoxAddMenuItem(AAOTracker.Options.Window.."ColourTwoComboBox"  , towstring(v.name))
		ComboBoxAddMenuItem(AAOTracker.Options.Window.."ColourThreeComboBox", towstring(v.name))
		ComboBoxAddMenuItem(AAOTracker.Options.Window.."ColourFourComboBox" , towstring(v.name))
	end
	--Pre-populate the combo boxes with the available channels
	for i, v in ipairs(AAOTracker.Alerts) do
		ComboBoxAddMenuItem(AAOTracker.Options.Window.."AlertsComboBox", towstring(v.name))
	end
	AAOTracker.DebugMsg(L"Leaving Options.WindowInit",3)
end

function AAOTracker.Options.ShowWindow()
	AAOTracker.DebugMsg(L"Entering Options.ShowWindow",3)
	-- Set the gui elements to the latest values.
	EnableModCheckToggle = AAOTracker.Settings.Enabled
	ButtonSetPressedFlag(AAOTracker.Options.Window.."ModCheckBox", EnableModCheckToggle)
	EnableMonitorCheckToggle = AAOTracker.Settings.Monitor.Show
	ButtonSetPressedFlag(AAOTracker.Options.Window.."MonitorCheckBox", EnableMonitorCheckToggle)
	-- Populate the Colour settings to the saved settings
	ComboBoxSetSelectedMenuItem(AAOTracker.Options.Window.."ColourZeroComboBox" , AAOTracker.Settings.TextColour.Zero)
	ComboBoxSetSelectedMenuItem(AAOTracker.Options.Window.."ColourOneComboBox"  , AAOTracker.Settings.TextColour.One)
	ComboBoxSetSelectedMenuItem(AAOTracker.Options.Window.."ColourTwoComboBox"  , AAOTracker.Settings.TextColour.Two)
	ComboBoxSetSelectedMenuItem(AAOTracker.Options.Window.."ColourThreeComboBox", AAOTracker.Settings.TextColour.Three)
	ComboBoxSetSelectedMenuItem(AAOTracker.Options.Window.."ColourFourComboBox" , AAOTracker.Settings.TextColour.Four)
	-- Populate the Alert settings to the saved settings
	ComboBoxSetSelectedMenuItem(AAOTracker.Options.Window.."AlertsComboBox", AAOTracker.Settings.AlertChannel)
	-- Show the window
	WindowSetShowing(AAOTracker.Options.Window,true)
	AAOTracker.DebugMsg(L"Leaving Options.ShowWindow",3)
end

function AAOTracker.Options.CloseWindow(flags, mouseX, mouseY)
	AAOTracker.DebugMsg(L"Entering Options.CloseWindow",3)
	-- When the button is clicked, hide the AAOTracker window.
	WindowSetShowing(AAOTracker.Options.Window,false)
	AAOTracker.DebugMsg(L"Leaving Options.CloseWindow",3)
end

function AAOTracker.Options.SaveOptions(flags, mouseX, mouseY)
	AAOTracker.DebugMsg(L"Entering Options.SaveOptions",3)
	-- Store the enable settings
	AAOTracker.Settings.Enabled      = ButtonGetPressedFlag(AAOTracker.Options.Window.."ModCheckBox")
	AAOTracker.Settings.Monitor.Show = ButtonGetPressedFlag(AAOTracker.Options.Window.."MonitorCheckBox")
	-- Store the Colour settings
	AAOTracker.Settings.TextColour.Zero  = ComboBoxGetSelectedMenuItem(AAOTracker.Options.Window.."ColourZeroComboBox" )
	AAOTracker.Settings.TextColour.One   = ComboBoxGetSelectedMenuItem(AAOTracker.Options.Window.."ColourOneComboBox"  )
	AAOTracker.Settings.TextColour.Two   = ComboBoxGetSelectedMenuItem(AAOTracker.Options.Window.."ColourTwoComboBox"  )
	AAOTracker.Settings.TextColour.Three = ComboBoxGetSelectedMenuItem(AAOTracker.Options.Window.."ColourThreeComboBox")
	AAOTracker.Settings.TextColour.Four  = ComboBoxGetSelectedMenuItem(AAOTracker.Options.Window.."ColourFourComboBox" )
	-- Re-display the text to reflect the latest selection
	AAOTracker.Monitor.UpdateText()
	if AAOTracker.WarBoard.Active then 
		AAOTWarBoard.UpdateText()
	end
	-- Store the Alert settings
	AAOTracker.Settings.AlertChannel = ComboBoxGetSelectedMenuItem(AAOTracker.Options.Window.."AlertsComboBox")
	-- If enable option has changed, apply the changes.
	if AAOTracker.Enabled ~= AAOTracker.Settings.Enabled then
		AAOTracker.Enabled = AAOTracker.Settings.Enabled
		if AAOTracker.Settings.Enabled then
			AAOTracker.RegisterEvents()
		else
			AAOTracker.UnregisterEvents()
		end
		AAOTracker.Monitor.Display()
	end
	if AAOTracker.Settings.Monitor.Show ~= WindowGetShowing(AAOTracker.Monitor.Window) then
		AAOTracker.Monitor.Display()
	end
	AAOTracker.DebugMsg(L"Leaving SaveOptions",3)
end

function AAOTracker.Options.EnableModCheckToggle(flags, mouseX, mouseY)
	AAOTracker.DebugMsg(L"Entering Options.EnableModCheckToggle",3)
	EnableModCheckToggle = not EnableModCheckToggle
	ButtonSetPressedFlag(AAOTracker.Options.Window.."ModCheckBox",EnableModCheckToggle)
	AAOTracker.DebugMsg(L"Leaving Options.EnableModCheckToggle",3)
end

function AAOTracker.Options.EnableMonitorCheckToggle(flags, mouseX, mouseY)
	AAOTracker.DebugMsg(L"Entering Options.EnableMonitorCheckToggle",3)
	EnableMonitorCheckToggle = not EnableMonitorCheckToggle
	ButtonSetPressedFlag(AAOTracker.Options.Window.."MonitorCheckBox",EnableMonitorCheckToggle)
	AAOTracker.DebugMsg(L"Leaving Options.EnableMonitorCheckToggle",3)
end
