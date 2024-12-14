package com.yuo.endless.Items.Tool;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

public class ColorText {
	private static final TextFormatting[] fabulousness = new TextFormatting[] {TextFormatting.RED, TextFormatting.GOLD, TextFormatting.YELLOW,
			TextFormatting.GREEN, TextFormatting.AQUA, TextFormatting.BLUE, TextFormatting.LIGHT_PURPLE};
	public static String makeFabulous(String input) {
		return ludicrousFormatting(input, fabulousness, 1, 1);
	}
	
	private static final TextFormatting[] sanic = new TextFormatting[] {TextFormatting.BLUE, TextFormatting.BLUE, TextFormatting.BLUE,
			TextFormatting.BLUE, TextFormatting.WHITE, TextFormatting.BLUE, TextFormatting.WHITE, TextFormatting.WHITE, TextFormatting.BLUE,
			TextFormatting.WHITE, TextFormatting.WHITE, TextFormatting.BLUE, TextFormatting.RED, TextFormatting.WHITE, TextFormatting.GRAY,
			TextFormatting.GRAY, TextFormatting.GRAY, TextFormatting.GRAY, TextFormatting.GRAY, TextFormatting.GRAY, TextFormatting.GRAY,
			TextFormatting.GRAY, TextFormatting.GRAY, TextFormatting.GRAY, TextFormatting.GRAY, TextFormatting.GRAY, TextFormatting.GRAY,
			TextFormatting.GRAY, TextFormatting.GRAY, TextFormatting.GRAY};
	public static String makeSANIC(String input) {
		return ludicrousFormatting(input, sanic, 1, 1);
	}
	
	public static String ludicrousFormatting(String input, TextFormatting[] colours, double delay, int posstep) {
		StringBuilder sb = new StringBuilder(input.length()*3);
		if (delay <= 0) {
			delay = 0.001;
		}
		Minecraft mc = Minecraft.getInstance();
		long gameTime = 0;
		if (mc.world != null) {
			gameTime = mc.world.getGameTime();
		}
		int offset = (int) Math.floor(gameTime / delay) % colours.length;
		
		for (int i=0; i<input.length(); i++) {
			char c = input.charAt(i);
			
			int col = ((i * posstep) + colours.length - offset) % colours.length;
			
			sb.append(colours[col].toString());
			sb.append(c);
		}
		
		return sb.toString();
	}
}
