## Commands

`creative-central` only has the necessary commands to administrate server's resource-pack
and to apply it to players. The commands are:



### Command `/central help`
**Permission:** `creative-central.command.help` *(Default for everyone)*

Or just `/central`, sends a list of all commands to the player. Similar
to this page.


### Command `/central reload`
**Permission:** `creative-central.command.reload` *(Default for operators)*

**Reloads** the plugin's configuration, **regenerates** and **exports** the server
resource-pack, and **sends** it to all players connected to the server.


### Command `/central apply [target]`

**Permissions:**
- `creative-central.command.apply` *(Default for everyone, but won't let them apply the resource-pack for others)*
- `creative-central.command.apply.others` *(Default for operators)*

**Applies** the server resource-pack to the given player *(Specified in the [target]
argument)* or to the player who executed the command if no target is specified.

As the `[target]` argument, you can use a player's name or `@a` to apply the resource-pack
to all players connected to the server.
