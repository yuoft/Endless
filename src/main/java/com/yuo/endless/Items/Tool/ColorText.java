package com.yuo.endless.Items.Tool;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;

public class ColorText {
	private static final ChatFormatting[] fabulousness = new ChatFormatting[] {ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW,
			ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.BLUE, ChatFormatting.LIGHT_PURPLE};
	public static String makeFabulous(String input) {
		return ludicrousFormatting(input, fabulousness, 1, 1);
	}
	
	private static final ChatFormatting[] sanic = new ChatFormatting[] {ChatFormatting.BLUE, ChatFormatting.BLUE, ChatFormatting.BLUE,
			ChatFormatting.BLUE, ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.BLUE,
			ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.RED, ChatFormatting.WHITE, ChatFormatting.GRAY,
			ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY,
			ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY,
			ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY};
	public static String makeSANIC(String input) {
		return ludicrousFormatting(input, sanic, 1, 1);
	}
	
	public static String ludicrousFormatting(String input, ChatFormatting[] colours, double delay, int posstep) {
		StringBuilder sb = new StringBuilder(input.length()*3);
		if (delay <= 0) {
			delay = 0.001;
		}
		Minecraft mc = Minecraft.getInstance();
		long gameTime = 0;
		if (mc.level != null) {
			gameTime = mc.level.getGameTime();
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
