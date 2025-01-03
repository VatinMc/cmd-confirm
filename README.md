# cmd-confirm
Minecraft Fabric Mod. Requests confirmation, when using commands.  
Config-file is located at "/config/cmd-confirm/config.txt". Commands "/kill", "/kill @p" and "kill @e" are pre defined as example. Delete them, if you don't need them.  

Use command "/cccreload" to reload config ingame.
  
Minecraft versions: 1.21
Fabric-API: 0.102.0+1.21
  
Changelog:  
**v1.0.1** 
- config file is now located in `.minecraft/config/cmd-confirm/config.txt`  
- command `/cccreload` is now lowercase  
  
**v1.0.0**  
Added basic functionality.
- create, read and load config file
- "/cccreload"-command to reload config ingame
- opens screen to request confirmation, when command on list wants to be executed
- shows command on confirmation screen
- go back to ChatScreen, when abort execution
