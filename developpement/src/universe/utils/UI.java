package universe.utils;

import java.util.Scanner;
import universe.World;
import universe.entities.Entity;
import universe.entities.Item;
/**
 * @author claire
 * 
 */

public class UI {
    private World world;
    private static final String question = "What do you want to do ?\n";
    private static final String methodError = "this action can not be carried out\n";
    private static final String exception = "An exception has been raised :\n";
    private static final String unexpected = "Unexpected answer\n";
    private static final String possibilities = "Possible actions : \n"
            + " -get informations from the zone (enter : getinfo)\n"
            + " -talk to someone in particular (enter : talkto <name_of_the_person>)\n"
            + " -ask information about an entity to someone (enter askinfo <name_of_the_entity> <name_of_the_person_to_ask>)\n"
            + " -give an objet to someone (enter : give <name_of_the_object> <name_of_the_person>)\n"
            + " -kill someone (enter : kill <name_of_the_person>)\n"
            + " -loot someone (enter : loot <name_of_the_person>)\n"
            + " -read an object you have (enter : read <name_of_the_object>)\n"
            + " -go to a position in particular (enter : goto <x> <y>)\n";

    public UI() {
        this.world = null;
    }
    
    public UI(World world) {
        this.world = world;
    }

    private void display(String message){
        System.out.println(message);
    }

    private String readUserMessage() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
    
    // All displays
    public void displayQuestion(){
        this.display(this.question);
    }
    
    public void displayWorld(){
        this.display(this.world.toAsciiArt());
    }
    
    public void displayPossibilities(){
        this.display(this.possibilities);
    }
    
    public void displayMethodError(){
        this.display(this.methodError);
    }
    
    public void displayException(String m) {
        this.display(this.exception + m);
    }
    
    public String getUserAction() {
        String result = "";
        String action = this.readUserMessage();
        if (action.equals("getinfo")) {
            result = "learnFromUnderstandabilityZone";
        }
        else if(action.startsWith("talkto")) {
            String npcName = action.split(" ")[1];
            Entity npc = this.world.getEntityByName(npcName);
            result = "discussWith;Character [" + npc.toString() +"]";
        }
        else if(action.startsWith("askinfo")) {
            String entityName = action.split(" ")[1];
            Entity entity = this.world.getEntityByName(entityName);
            String npcName = action.split(" ")[2];
            Entity npc = this.world.getEntityByName(npcName);
            result = "askInformationAboutAnEntity;Character [" + npc.toString() +"],Entity "+ entity.toString()+"]";
        }
        else if(action.startsWith("give")) {
            String itemName = action.split(" ")[1];
            Entity item = this.world.getEntityByName(itemName);
            String npcName = action.split(" ")[2];
            Entity npc = this.world.getEntityByName(npcName);
            result = "give;Character [" + npc.toString() +"],Item ["+ item.toString() +"]";
        }
        else if(action.startsWith("askobject")) {
            String itemName = action.split(" ")[1];
            Entity item = this.world.getEntityByName(itemName);
            String npcName = action.split(" ")[2];
            Entity npc = this.world.getEntityByName(npcName);
            result = "askObject;Character [" + npc.toString() +"],Item ["+ item.toString() +"]";
        }
        else if(action.startsWith("kill")) {
            String npcName = action.split(" ")[1];
            Entity npc = this.world.getEntityByName(npcName);
            result = "kill;Character [" + npc.toString() +"]";
        }
        else if(action.startsWith("loot")) {
            String npcName = action.split(" ")[1];
            Entity npc = this.world.getEntityByName(npcName);
            result = "loot;Character [" + npc.toString() +"]";
        }
        else if(action.startsWith("read")) {
            String itemName = action.split(" ")[1];
            Entity item = this.world.getEntityByName(itemName);
            result = "readObject;Item [" + item.toString() +"]";
        }
        else if(action.startsWith("goto")) {
            String x = action.split(" ")[1];
            String y = action.split(" ")[2];
            result = "move;Position [x=" + x + ", y=" + y + "]";
        }
        else {
            this.display(unexpected);
        }
        return result;
    }
}
