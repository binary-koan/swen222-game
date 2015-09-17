# Activities

## Objective

Escape the house! 2-storey haunted house with a backyard, you have to get out the front door.

## Look

Point-and-click. Arrow controls for moving around rooms? Or on-screen buttons. Basic menu bar at the top and inventory at the bottom. See https://app.atomic.io/d/oVey6QY2GU8G

## Player objectives/abilities

- Furniture
- Movable stuff (ie. pick-up-able items)
- Keys

- Pick up
- Drop
- Attack
- Unlock

## Location representation

- X-Y-Z coordinates
- Player can be at 4 points in the room (facing inwards)
- Square rooms

## Player representation

- Select an avatar
- Displayed slightly to the side of the current player if at the same point

## NPCs

- Monsters
- Fixed in rooms

# Use cases

## Nagivation

- On-screen buttons aliased to arrow keys for moving around rooms
- Click on a door to go through it

## Interactions

- Point and click
- Dropdown list on right click
- Later: drag and drop
- Later: trade and send messages to players

## System interactions

- Server stores state of world and players

When loading an existing game
- Players can log back in with their name (and get their position and items back)
- Alternatively, new players can log in (and get put in a random position)
