# cmd-confirm
Minecraft Fabric Mod. Requests confirmation, when using commands.  
Config-file is located at "/mods/cmd-confirm/config.txt". Commands "/kill", "/kill @p" and "kill @e" are pre defined as example. Simply delete them, if you don't need them.  

Use command "/cccreload" to reload config ingame.
  
Minecraft version 1.21  
Fabric-API: 0.102.0+1.21  
  
Changelog:  
**v1.0.0**  
Added basic functionality.
- create, read and load config file
- "/cccreload"-command to reload config ingame
- opens screen to request confirmation, when command on list wants to be executed
- shows command on confirmation screen
- go back to ChatScreen, when abort execution
