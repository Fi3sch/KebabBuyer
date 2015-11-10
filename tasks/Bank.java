package tasks;

import main.*;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class Bank extends Task<ClientContext> {

	public Bank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.inventory.select().count() == 28 
				&& ctx.players.local().animation() == -1
				&& Recourses.bankArea.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		GameObject bank = ctx.objects.select().id(Recourses.bankBooth).nearest().poll();

		if (bank.inViewport()) {
			Recourses.status = "Banking";
			if (ctx.bank.opened()) {
				ctx.bank.depositInventory();
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.inventory.select().count() == 0;
					}
				}, 250, 8);
				ctx.bank.withdraw(Recourses.goldId, 10000);

				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return !ctx.inventory.select().id(Recourses.goldId).isEmpty();
					}
				}, 250, 8);

				ctx.bank.close();

				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return !ctx.bank.opened();
					}
				}, 250, 8);

			} else {
				bank.interact("Bank", "Bank booth");
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.bank.opened();
					}
				}, 250, 8);
			}
		} else {
			Recourses.status = "Moving to bank";
			ctx.movement.step(bank); 
			ctx.camera.turnTo(bank);

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
