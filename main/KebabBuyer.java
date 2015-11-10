package main;

import tasks.*;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.powerbot.script.Script;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;

@Script.Manifest(name = "[OS]KebabBuyer by Fiesch", description = "Buys kebabs at Karim in Al-Karid. Please start in Al-Karid Bank!", properties = "client=4")
public class KebabBuyer extends PollingScript<ClientContext>implements PaintListener, MessageListener {
	public List<Task> taskList = new ArrayList<>();

	@Override
	public void start() {
		Recourses.status = "Started";
		Recourses.startTime = System.currentTimeMillis();
		Recourses.kebabCount = 0;
		Collections.addAll(Arrays.asList(new Bank(ctx), new BuyKebab(ctx), new TraverseBank(ctx), new OpenDoor(ctx)));
		//taskList.addAll(Arrays.asList(new Bank(ctx), new BuyKebab(ctx), new TraverseBank(ctx), new OpenDoor(ctx)));
	}

	@Override
	public void poll() {
		for (Task t : taskList) {
			if (t.activate()) {
				t.execute();
			}
		}
	}

	@Override
	public void stop() {
		System.out.println("Runtime: " + runTime(Recourses.startTime));
		System.out.println("Kebabs bougt: " + Recourses.kebabCount);
		System.out.println("Profit: " + Recourses.kebabCount * Recourses.kebabPrice);
	}

	@Override
	public void repaint(Graphics g) {
		g.setColor(new Color(90, 71, 32, 180));
		g.fillRect(9, 26, 150, 20);
		g.fillRect(9, 27, 150, 130);
		g.setColor(Color.BLACK);
		g.drawRect(8, 25, 151, 21);
		g.drawRect(8, 25, 151, 131);

		g.setColor(Color.WHITE);
		g.drawString("KebabBuyer by Fiesch", 12, 41);

		g.setColor(Color.WHITE);

		g.drawString("Runtime: " + runTime(Recourses.startTime), 12, 66);
		g.drawString("Kebabs bought: " + Recourses.kebabCount, 12, 82);
		g.drawString("Profit: " + Recourses.kebabCount * Recourses.kebabPrice, 12, 98);
		g.drawString("Kebabs/Hour: " + perHour(Recourses.kebabCount), 12, 114);
		g.drawString("Profit/Hour: " + Integer.parseInt(perHour(Recourses.kebabCount)) * Recourses.kebabPrice, 12, 130);
		g.drawString("Status: " + Recourses.status, 12, 146);
		
		
		int mX = ctx.input.getLocation().x;
		int mY = ctx.input.getLocation().y;

		int pX[] = { mX, mX + 10, mX + 5, mX + 9, mX + 7, mX + 3, mX, mX };
		int pY[] = { mY, mY + 8, mY + 8, mY + 14, mY + 16, mY + 9, mY + 13, mY };

		g.setColor(Color.WHITE);
		g.fillPolygon(pX, pY, 8);
		g.setColor(Color.BLACK);
		g.drawPolygon(pX, pY, 8);
	}

	public String runTime(long i) {
		DecimalFormat nf = new DecimalFormat("00");
		long millis = System.currentTimeMillis() - i;
		long hours = millis / (1000 * 60 * 60);
		millis -= hours * (1000 * 60 * 60);
		long minutes = millis / (1000 * 60);
		millis -= minutes * (1000 * 60);
		long seconds = millis / 1000;
		return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
	}

	public String perHour(int gained) {
		return formatNumber((int) ((gained) * 3600000D / (System.currentTimeMillis() - Recourses.startTime)));
	}

	public String formatNumber(int start) {
		DecimalFormat nf = new DecimalFormat("0.0");
		double i = start;
		if (i >= 1000000) {
			return nf.format((i / 1000000));
		}
		if (i >= 1000) {
			return nf.format((i / 1000));
		}
		return "" + start;
	}
	
	@Override
	public void messaged(MessageEvent e) {
		String msg = e.text();
		msg = msg.toLowerCase();
		if(msg.equals("you buy a kebab.")){
			Recourses.kebabCount++;
		}
	}
}
