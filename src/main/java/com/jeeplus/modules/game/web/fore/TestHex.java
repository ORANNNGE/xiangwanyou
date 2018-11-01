package com.jeeplus.modules.game.web.fore;

import org.junit.Test;

public class TestHex {
    @Test
    public void test(){
        String hex = "6f0a4abdf18";
        System.out.println(Integer.valueOf(hex, 16).toString());
    }

}
