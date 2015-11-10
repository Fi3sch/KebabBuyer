package main;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Npc;

public class Recourses {

    public static String status;
    public static long startTime;	
    public static int kebabCount;
    
    //Muss aus dem Internet geholt werden ^^
    public static int kebabPrice = 48;																	
    
    //Never Change
    public static Npc karim;	//NPC und values
    public static int karimId = 529;                                      								
    public static int doorId = 7122;																	//KA
    public static int kebabId = 1971;																
    public static int goldId = 995;																		
    
    public static int bankBooth = 11744;																
    public static Area bankArea = new Area(new Tile(3268, 3163, 0), new Tile(3268, 3170, 0), new Tile(3271, 3170, 0), new Tile(3271, 3163, 0)); 			//Stimmt
    public static Area kebabArea = new Area(new Tile(3276, 3178, 0), new Tile(3276, 3184, 0), new Tile(3270, 3178, 0), new Tile(3270, 3184, 0));			//Change This

    public static Tile[] path = {new Tile(3269, 3167), new Tile(3276, 3170), new Tile(3274, 3180)};		

}
