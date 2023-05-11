/////////////////////////////////////////////////////////////////////////////////
// name: Landen Nguyen
// date: May 10, 2023
// desc: Room adventure implemented in Java... yay...
//////////////////////////////////////////////////////////////////////////////////

// allows us to use dictionaries and array lists in Java I think

import java.util.*;

class Room {

    // declare instance variables
    private String              name;
    public  Map<String, Room>   exits      = new HashMap<>();
    public  Map<String, String> items      = new HashMap<>();
    public  List<String>        grabbables = new ArrayList<>();

    // room constructor
    public Room(String name) {
        this.name = name;
    }

    // accessors and mutators for instance variables
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Room> getExits() {
        return this.exits;
    }

    public void setExits(HashMap<String, Room> exits) {
        this.exits = exits;
    }

    public Map<String, String> getItems() {
        return this.items;
    }

    public void setItems(Map<String, String> items) {
        this.items = items;
    }

    public List<String> getGrabbables() {
        return this.grabbables;
    }

    public void setGrabbables(List<String> grabbables) {
        this.grabbables = grabbables;
    }

    // creates a room
    public void addExit(String exit, Room room) {
        this.exits.put(exit, room);
    }

    // adds an item to a room
    public void addItem(String item, String desc) {
        this.items.put(item, desc);
    }

    // adds grabbable to a room
    public void addGrabbable(String item) {
        this.grabbables.add(item);
    }

    // removes grabbable from the room
    public void delGrabbable(String item) {
        this.grabbables.remove(item);
    }

    // string function to print location and what you see
    public String toString() {
        String result = "You are in " + this.name + ".\n" + "You see: ";

        result += String.join(",", this.items.keySet());

        result += "\nExits: " + String.join(",", exits.keySet()) + "\n";

        return result;
    }
}

class Game {

    private static final Set<String> EXIT_ACTIONS = Set.of("quit", "exit", "bye", "q");

    // statuses
    private static final String STATUS_CMD_ERR       = "I don't understand. Try <verb> <noun>. Valid verbs are 'go', 'look', and 'take'.";
    private static final String STATUS_DEAD          = "You are dead.";
    private static final String STATUS_BAD_EXIT      = "Invalid exit.";
    private static final String STATUS_ROOM_CHANGE   = "Room changed.";
    private static final String STATUS_GRABBED       = "Item grabbed.";
    private static final String STATUS_BAD_GRABBABLE = "I can't grab that.";
    private static final String STATUS_BAD_ITEM      = "I don't see that.";
    private              String status;

    // instance variable for the inventory and current room
    private       Room         currentRoom;
    private final List<String> inventory = new ArrayList<>();

    public static void main(String[] args) {
        Game game = new Game();
        game.setupGame();
        game.setStatus("");
        Scanner sc = new Scanner(System.in);
        while (true) {
            String line = sc.nextLine();
            if (EXIT_ACTIONS.contains(line.toLowerCase())) {
                break;
            }
        }
    }

    public void setupGame() {
        Room r1 = new Room("Room 1");
        Room r2 = new Room("Room 2");
        Room r3 = new Room("Room 3");
        Room r4 = new Room("Room 4");

        r1.addExit("east", r2);
        r1.addExit("south", r3);

        r2.addExit("west", r1);
        r2.addExit("south", r4);

        r3.addExit("north", r1);
        r3.addExit("east", r4);

        r4.addExit("west", r3);
        r4.addExit("north", r2);
        r4.addExit("south", null);

        // items added to rooms
        r1.addItem("stools", "The stools are around the island.");
        r1.addItem("island", "The island is made of marble like the counter and pristine.");
        r1.addItem("counter", "The counter is made of marble like the island.");
        r1.addItem("fridge", "The fridge is touchscreen because I'm rich.");

        r2.addItem("chair", "lots of wicker");
        r2.addItem("table", "got a key on it or sum");

        r3.addItem("dog", "its lickin itself");

        r4.addItem("fireplace", "blazin");

        // grabbables added to the room
        r1.addGrabbable("cookie");

        r2.addGrabbable("key");

        r3.addGrabbable("dog");

        r4.addGrabbable("book");

        // set current room to room 1
        this.currentRoom = r1;
    }

    public void setStatus(String status) {
        if (this.currentRoom == null) {
            this.status = STATUS_DEAD;
        } else {
            Collections.sort(inventory);
            String content = currentRoom + "\nYou are carrying: " + inventory + "\n\n" + status;
            // I assume that this not doing anything is intentional.
        }
    }

    public void handleGo(String destination) {
        this.status = STATUS_BAD_EXIT;

        Room dest = this.currentRoom.exits.get(destination);
        if (dest != null) {
            this.currentRoom = dest;
            this.status = STATUS_ROOM_CHANGE;
        }
        this.setStatus(this.status);
    }

    public void handleLook(String item) {
        String status = this.currentRoom.items.get(item);
        if (status != null) {
            this.status = this.currentRoom.items.get(item);
        }
        this.setStatus(this.status);
    }

    public void handleTake(String grabbable) {
        this.status = STATUS_BAD_GRABBABLE;

        if (this.currentRoom.grabbables.contains(grabbable)) {
            this.inventory.add(grabbable);
            this.currentRoom.delGrabbable(grabbable);
            this.status = STATUS_GRABBED;
        }
        this.setStatus(this.status);
    }
}