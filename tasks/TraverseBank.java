package tasks;

import main.*;


import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;


public class TraverseBank extends Task<ClientContext>{

	public TraverseBank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.inventory.select().count() == 28 
				&& ctx.players.local().animation() == -1
				&& !Recourses.bankArea.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		Recourses.status = "Moving to Bank";
		ctx.movement.newTilePath(Recourses.path).reverse().traverse();

		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return Math.abs(ctx.players.local().tile().x() - ctx.movement.destination().x()) < 5
						&& Math.abs(ctx.players.local().tile().y() - ctx.movement.destination().y()) < 5;
			}
		}, 250, 5);
		
	}

}
