package com.jeeplus.modules.game.web.fore;

import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("page")
public class PageController {
	@RequestMapping("{toshow}")
	public String toShow(@PathVariable(value="toshow") String toshow,HttpServletRequest request) {
		HttpSession session = request.getSession();
		System.out.println("*****PageController toshow():"+toshow+"***session user:"+
							session.getAttribute("userId")+"*****");
		return "modules/game/fore/"+toshow;
	}
}
