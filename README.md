# Star Kerfuffle

> "Because Star Wars sounds too grandiose"

Objective: Stop the ship crashing into the planet! The rogue super computer, JCN-9000, has taken over, and you must stop him.

## Running the game

**Important: We were unable to create JAR files including JDOM, so the project must be imported into eclipse to be run.**

### Server

Main class: `control.Server`

Arguments:

- `--filename <filename>` sets the file the server will save the game to. If the server is shut down before the game is
    complete, the game can be run from this file
- `--port <port>` sets the port the server will listen on

The server will output connection details which can be entered into the client GUI.

### Client

Main class: `gui.SplashScreen`

Click on the panel, and then enter a valid game to load from and save to, or if that game does not exist a game to save to.
Entering a player name that exists in a previous game will start you from that player.

If you enter an IP and port, it will attempt to connect to a server. Entering just a game name will run singleplayer.
Note that clients can connect to servers, but cannot perform actions or disconnect - full network functionality is not yet
up and running.

## Playing the game

The game is based around a point-and-click interface. Hover over an item to display possible actions that can be taken,
and use the left and right mouse buttons to perform the actions. Navigate to different points within a room using the
arrow keys.

The objective of the game is to divert the rocket from its collision course with your home planet. To do this, you need
to find keys to open the spaceship's many locked doors and chests and kill the monsters which stand in your way. Each
monster can only be killed by a specific weapon - be careful, as using the wrong weapon will result in your death!

*Enjoy!*
