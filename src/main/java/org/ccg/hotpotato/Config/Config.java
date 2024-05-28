package org.ccg.hotpotato.Config;

import lombok.Getter;

import java.io.IOException;

@Getter
public class Config {
    
    private String _configName;

    public boolean Save(){
        return ConfigFactory.SaveConfig(this);
    }
    
    public final void SetConfigName(String configName)
    {
        _configName = configName;
    }

}
