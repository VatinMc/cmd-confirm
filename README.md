# cmd-confirm
Minecraft Fabric Mod. Requests confirmation, when using commands.  
Cmds-file is located at "/config/cmd-confirm/cmds.txt". Commands "`/kill`", "`/kill @p`" and "`kill @e`" are pre defined as example. Delete them, if you don't need them.  

Use command "`/ccconfig`" to add and remove commands ingame.
  
Minecraft version: 1.21.4  
Fabric-API: 0.112.0+1.21.4  
  
Changelog:  
**v1.0.0**  
Added basic functionality.
- create, read and load config file
- "/cccreload"-command to reload config ingame
- opens screen to request confirmation, when command on list wants to be executed
- shows command on confirmation screen
- go back to ChatScreen, when abort execution
