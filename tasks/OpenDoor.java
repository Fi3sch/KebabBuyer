package tasks;

import main.*;


import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class OpenDoor extends Task<ClientContext>{

	public OpenDoor(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		//return !ctx.objects.select().id(Recourses.doorId).nearest().isEmpty();	//Testen ka obs geht
		return false;
	}	

	@Override
	public void execute() {
		Recourses.status = "Opening Door";
		GameObject door = ctx.objects.select().id(Recourses.doorId).nearest().poll();
		
		if(door.inViewport()){
			door.interact("Open");
			
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return ctx.objects.select().id(Recourses.doorId).nearest().isEmpty();
				}
			}, 250, 5);
		}else{
			ctx.movement.step(door);
			
			ctx.camera.turnTo(door);
		}
	}
}
