----------------------------------------
-- WarBoard Window Specific Functions --
----------------------------------------
AAOTWarBoard = {}
AAOTWarBoard.Window = "AAOTrackerWarBoard"

function AAOTWarBoard.Initialise()
	AAOTracker.DebugMsg(L"Entering AAOTWarBoard.Initialise",3)
	-- Create the window and then hide it.
	if WarBoard ~= nil and WarBoard.AddMod(AAOTWarBoard.Window) then	
		AAOTracker.WarBoard.Active = true
		AAOTWarBoard.UpdateText()
	end
	AAOTracker.DebugMsg(L"Leaving AAOTWarBoard.Initialise",3)
end

function AAOTWarBoard.UpdateText()
	AAOTracker.DebugMsg(L"Entering AAOTWarBoard.UpdateText",3)
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
	LabelSetTextColor(AAOTWarBoard.Window.."Text", tint.r, tint.g, tint.b )
	LabelSetText(AAOTWarBoard.Window.."Text", L"AAO: "..towstring(AAOTracker.Buff.Percentage)..L"%")
	AAOTracker.DebugMsg(L"Leaving AAOTWarBoard.UpdateText",3)
end