package com.jeeplus.modules.game.util;

import java.util.Random;

public class SecurityCode {
	private String code;

	public static String getSecurityCode() {
		
		Random random = new Random();
		
		for (int i = 0; i < 10 ;i++) {
			System.out.println(random.nextInt(10));
		}
		return "";
	}
	
}
