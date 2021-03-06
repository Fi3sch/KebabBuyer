package tasks;

import main.*;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;


public class BuyKebab extends Task<ClientContext>{

	public BuyKebab(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.inventory.select().count() < 28
				&& ctx.players.local().animation() == -1
				&& !ctx.inventory.select().id(Recourses.goldId).isEmpty();
	}

	@Override
	public void execute() {
		Recourses.status = "Moving to karim.";

		if (ctx.bank.opened()) {
			ctx.bank.close();

			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return !ctx.bank.opened();
				}
			}, 250, 8);
		} else {
			if (Recourses.kebabArea.contains(ctx.players.local())) {
				Recourses.status = "Looking for Karim";
				if(Recourses.karim == null || !Recourses.karim.valid()){
					Recourses.karim = ctx.npcs.select().nearest().id(Recourses.karimId).poll();
				}
				
				if(Recourses.karim.inViewport()){
					Recourses.status = "Buying kebab";
					
					if(Recourses.karim.interact("Talk-to")){
						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() throws Exception {
								return ctx.widgets.widget(231).component(2).visible();
							}
						}, 250, 5);
						
						if(ctx.widgets.widget(231).component(2).click()){
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() throws Exception {
									return ctx.widgets.widget(219).component(0).component(2).visible();
								}
							}, 250, 5);
						}
						
						
						if(ctx.widgets.widget(219).component(0).component(2).click()){
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() throws Exception {
									return ctx.widgets.widget(217).component(2).visible();
								}
							}, 250, 5);
						}
						
						
						if(ctx.widgets.widget(217).component(2).click()){
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() throws Exception {
									return !ctx.widgets.widget(217).component(2).visible();
								}
							}, 250, 5);							
						}
					}
						
				}else{
					ctx.camera.turnTo(Recourses.karim);
					ctx.movement.step(Recourses.karim);
				}
				
			}else{
				Recourses.status = "Moving to Karim";
				if(ctx.movement.newTilePath(Recourses.path).traverse()){
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return Math.abs(ctx.players.local().tile().x() - ctx.movement.destination().x()) < 5
									&& Math.abs(ctx.players.local().tile().y() - ctx.movement.destination().y()) < 5;
						}
					}, 250, 5);
				}
			}
		}
	}
}
