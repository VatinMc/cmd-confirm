# cmd-confirm
Minecraft Fabric Mod. Requests confirmation, when using commands.  
Cmds-file is located at "/config/cmd-confirm/cmds.txt". Commands "`/kill`", "`/kill @p`" and "`kill @e`" are pre defined as example. Delete them, if you don't need them.  

Use command "`/ccconfig`" to add and remove commands ingame.
  
Minecraft version: 1.21.4  
Fabric-API: 0.112.0+1.21.4  

![Config Screen to add and remove commands](https://cdn.modrinth.com/data/c5VRfEYd/images/e2064ac2c315020f1f34beeedf72a95e5c976152.png)
Changelog:  
**v1.1.0** 
- changed commands
  - `/ccconfig` added
  - `/cccreload` removed
- ConfigScreen added, to add and remove commands via gui
- Mod Menu support to access ConfigScreen without command
- ConfirmationScreen changed
  - added texture behind confirmation-message
  - set button text using translation keys
  - button width is smaller now
- filename set to cmds.txt
  - auto import from last version
   
**v1.0.1** 
- config file is now located in `.minecraft/config/cmd-confirm/config.txt`  
- command `/cccreload` is now lowercase  
  
Changelog:  
**v1.0.0**  
Added basic functionality.
- create, read and load config file
- "/cccreload"-command to reload config ingame
- opens screen to request confirmation, when command on list wants to be executed
- shows command on confirmation screen
- go back to ChatScreen, when abort execution
