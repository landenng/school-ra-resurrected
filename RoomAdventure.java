/////////////////////////////////////////////////////////////////////////////////
// name: Landen Nguyen
// date: May 10, 2023
// desc: Room adventure implemented in Java... yay...
//////////////////////////////////////////////////////////////////////////////////

// allows us to use dictionaries and array lists in Java I think
import java.util.*;

class Room {

    // declare instance variables
    private String name;
    public HashMap <String, Room> exits = new HashMap <String, Room>();
    public HashMap <String, String> items = new HashMap <String, String>();
    public ArrayList <String> grabbables = new ArrayList <String>();
    
    // room constructor
    public Room(String roomName) {
        name = roomName;
    }
    
    // accessors and mutators for instance variables
    public String getName() {
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public HashMap <String, Room> getExits() {
        return exits;
    }
    
    public void setExits(HashMap <String, Room> exits) {
        this.exits = exits;
    }

    public HashMap <String, String> getItems() {
        return items;
    }

    public void setItems(HashMap <String, String> items) {
        this.items = items;
    }
    
    public ArrayList <String> getGrabbables () {
        return grabbables;
    }
    
    public void setGrabbables (ArrayList <String> grabbables) {
        this.grabbables = grabbables;
    }
    
    // creates a room
    public void addExit(String exit, Room room) {
        exits.put(exit, room);
    }

    // adds an item to a room
    public void addItem(String item, String desc) {
        items.put(item, desc);
    }
    
    // adds grabbable to a room
    public void addGrabbable(String item) {
        grabbables.add(item);
    }
    
    // removes grabbable from the room
    public void delGrabbable(String item) {
        grabbables.remove(item);
    }
    
    // string function to print location and what you see
    public String toString(){
        String result = "You are in " + name + ".\n" + "You see: ";

        for (String item: items.keySet()) {
            result += item + ", ";
        }
        
        result += "/n";

        result += "Exits: ";
        for (String exit: exits.keySet()) {
            result += exit + ", ";
        }
        result += "\n";

        return result;
    }
}

class Game {

    ArrayList <String> exitActions = new ArrayList<String>(Arrays.asList("quit", "exit", "bye", "q"));

    // statuses
    String statusCmdErr = "I don't understand. Try <verb> <noun>. Valid verbs are 'go', 'look', and 'take'.";
    String statusDead = "You are dead.";
    String statusBadExit = "Invalid exit.";
    String statusRoomChange = "Room changed.";
    String statusGrabbed = "Item grabbed.";
    String statusBadGrabbable = "I can't grab that.";
    String statusBadItem = "I don't see that.";
    String status;

    // instance variable for the inventory and current room
    private Room currentRoom;
    private ArrayList <String> inventory = new ArrayList <String>();
    
    public void Game() {
        setupGame();
        setStatus("");
    }

    public void setupGame () {
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
        currentRoom = r1;
    }
    
    public void setStatus(String status) {
        if (currentRoom == null) {
            status = statusDead;
        } else {
            Collections.sort(inventory);
            String content = currentRoom + "\nYou are carrying: " + inventory + "\n\n" + status;
        }
    }
    
    public void handleGo (String destination) {
        status = statusBadExit;
        
        for (String exit: currentRoom.exits.keySet()) {
            if (destination == exit) {
                currentRoom = currentRoom.exits.get(destination);
                status = statusRoomChange;
            }
        }
        setStatus(status);
    }
    
    public void handleLook (String item) {
        for (String thing: currentRoom.items.keySet()) {
            if (item == thing) {
                status = currentRoom.items.get(item);
            }
        }
        setStatus(status);
    }
    
    public void handleTake(String grabbable) {
        status = statusBadGrabbable;

        for (int i = 1; i < currentRoom.grabbables.size(); i++ ) {
            if (grabbable == currentRoom.grabbables.get(i)) {
                inventory.add(grabbable);
                currentRoom.delGrabbable(grabbable);
                status = statusGrabbed;
            }
        }
        setStatus(status);
    }
}