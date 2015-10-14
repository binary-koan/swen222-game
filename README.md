# Star Kerfuffle

> "Because Star Wars sounds too grandiose"

## Running the game

**Important: The executables should both be run from the directory they are saved in - attempting to run from another
directory will mean that images and sound files are not found.**

### Server

Usage: `java -jar server.jar`

Arguments:

- `--filename <filename>` sets the file the server will save the game to. If the server is shut down before the game is
    complete, the game can be run from this file
- `--port <port>` sets the port the server will listen on

The server will output connection details which can be entered into the client GUI.

### Client

Usage: `java -jar client.jar`

Enter the server IP address and port (displayed in the output from `server.jar`) into the client GUI to connect to a
server.

## Playing the game

The game is based around a point-and-click interface. Hover over an item to display possible actions that can be taken,
and use the left and right mouse buttons to perform the actions.

The objective of the game is to divert the rocket from its collision course with your home planet. To do this, you need
to find keys to open the spaceship's many locked doors and chests and kill the monsters which stand in your way. Each
monster can only be killed by a specific weapon - be careful, as using the wrong weapon will result in your death!

*Enjoy!*
