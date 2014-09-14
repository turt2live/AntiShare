[![AntiShare](http://home.turt2live.com/AntiShare-BukkitDev-Logo.png)](http://dev.bukkit.org/server-mods/antishare)

What is it?
-----------

AntiShare is a plugin designed to target Creative Mode players and stop them from dominating your server.

Check out the [BukkitDev page](http://dev.bukkit.org/server-mods/antishare) or the [Bukkit Forum post](http://forums.bukkit.org/threads/56523) for more information such as commands.

Enterprise Operation
-----

AntiShare is designed for high-profile servers (or servers with lots of players). This is to enhance the server's ability to operate without having to worry about creative mode players. AntiShare can still be run on smaller servers, but those servers will not see a significant gain from the performance enhancements within AntiShare.

Maven
-----

POM Information:

```xml
<!-- ... --->

<!-- Bukkit Dependency -->
<dependency>
    <groupId>com.turt2live.antishare</groupId>
    <artifactId>AntiShare</artifactId>
    <version>LATEST</version> <!-- Replace with current version in the pom.xml! -->
    <type>jar</type>
    <scope>compile</scope>
</dependency>

<!-- ... --->

<!-- Repository for all dependencies -->
<repository>
	<id>turt2live-repo</id>
	<url>http://repo.turt2live.com</url>
</repository>

<!-- ... --->
```

Compiling
---------

AntiShare requires Maven 3 to compile.

Other Information
-----------------

Released under GNU LGPL v2.1

Please see the post about AntiShare for more information.

[BukkitDev](http://dev.bukkit.org/server-mods/antishare)

[Bukkit Forums](http://forums.bukkit.org/threads/56523)

Feel free to copy/use my work, just link back to here. :)

Credits
------------------

Thanks to mbaxter for walking me through the maven module thingy.