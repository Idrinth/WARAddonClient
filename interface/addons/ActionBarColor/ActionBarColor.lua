ActionBarColor = {}

local oldMoraleButtonUpdate = nil
local oldActionButtonUpdateEnabledState = nil

function ActionBarColor.Initialize()

    -- Posthook the action buttons' UpdateEnabledState() function

    oldActionButtonUpdateEnabledState = ActionButton.UpdateEnabledState
    ActionButton.UpdateEnabledState = function(self, ...)
        oldActionButtonUpdateEnabledState(self, ...)
        
        local BASE_ICON         = 0
        local USE_ENABLED_ICON  = 42
        
        if (self.m_IsEnabled == true) and (self.m_IsTargetValid == true) then                  
            self.m_Windows[BASE_ICON]:SetTintColor(255, 255, 255)
        elseif (self.m_IsEnabled == true) then
            -- tint icons for invalid targets red
            self.m_Windows[BASE_ICON]:SetTintColor(200, 0, 0)
        elseif (self.m_IsEnabled == false) and (iconType == USE_ENABLED_ICON) then
            self.m_Windows[BASE_ICON]:SetTintColor(125, 125, 125)
        end
        
    end
    
    -- And now, post-hook the morale buttons' Update() function
    oldMoraleButtonUpdate = MoraleButton.Update
    MoraleButton.Update = function(self, ...)
        oldMoraleButtonUpdate(self, ...)
        
        local BASE_ICON         = 2
    
        if self.m_AbilityId > 0 then
                if self.m_IsTargetValid then
                    self.m_Windows[BASE_ICON]:SetTintColor (255, 255, 255)
                else
                    self.m_Windows[BASE_ICON]:SetTintColor (255, 0, 0)
                end
        end
        
    end
    
end