package com.jeeplus.modules.game.util;

import org.junit.Test;

import java.util.ResourceBundle;

/**2018年10月9日15点10分
 * @author orange
 *
 */
public class MyResourceUtil {

    private static ResourceBundle bundle = ResourceBundle.getBundle("properties/sysConfig");


    public static String getConfigByName(String name){
        System.out.println(bundle.getString(name));
        return bundle.getString(name);
    }

    @Test
    public void testGetConfigByName(){
        getConfigByName("fileUploadPath");
    }
}
