package com.jeeplus.modules.game.web.fore;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeeplus.modules.game.entity.admin.Users;
import com.jeeplus.modules.game.service.admin.UsersService;
import com.jeeplus.modules.game.service.admin.WithdrawService;
import com.jeeplus.modules.game.util.MsgUtil;
import com.jeeplus.modules.sys.service.SystemService;

@Controller
@RequestMapping(value="fore")
public class ForeUserController {

	@Autowired
	UsersService usersService;
	@Autowired
	WithdrawService withdrawService;
	
	/**
	 * @param phoneNum 手机号
	 */
	@RequestMapping(value="login")
	@ResponseBody
	public String login(String phoneNum,String password, HttpServletRequest req) {
		System.out.println("*****phoneNum:"+phoneNum+" password:"+password+"*****");
		HttpSession session = req.getSession();
		
		//根据请求中的phoneNum向数据库查询
		Users uniUser = usersService.findUniqueByProperty("phone_num", phoneNum);
		if(uniUser != null) {
			System.out.println(uniUser.toString());
			//若用户存在，则把请求中的密码和加密后的用户密码比较
			if(SystemService.validatePassword(password, uniUser.getPassword())) {
				//密码正确，向session中存入userId
				session.setAttribute("userId", uniUser.getId());
				session.setAttribute("phoneNum", uniUser.getPhoneNum());
				session.setAttribute("usersStatus", uniUser.getStatus());
				
				System.out.println("set session status:*****"+session.getAttribute("status"));
				// '1' 代表密码正确
				return "1";
			}else {
				//2 密码错误
				return "2";
			}
		}else {
			//3 用户不存在
			return "3";
		}
	}
	/**
	 * 
	 * @param users
	 * @param area
	 * @param phoneOS
	 * @param verifyCode
	 * @param req
	 * @return
	 */
	
	@RequestMapping(value="register")
	@ResponseBody
	public String register(Users users,String area,String phoneOS,String verifyCode,HttpServletRequest req) {
		System.out.println(users.toString()+"*****area:"+area+"*****phoneOS:"+phoneOS);
		//验证唯一性
		HttpSession session = req.getSession();
		String verifyPhoneNum = (String) session.getAttribute("verifyPhoneNum");
		System.out.println("-----verifyPhoneNum:"+verifyPhoneNum);
		if(!users.getPhoneNum().equals(verifyPhoneNum)) {
			//手机号和发送验证码的手机号不一致
			return "3";
		}
		String code = (String) session.getAttribute("code");
		System.out.println("code:"+code);
		Users uniUser = usersService.findUniqueByProperty("phone_num", users.getPhoneNum());
		
		if(uniUser == null) {
			//加密密码，并且存入数据库
			users.setPassword(SystemService.entryptPassword(users.getPassword()));
			users.setStatus(0);
			users.setBalance(0.0);
			if(!verifyCode.equals(code)) {
				//验证码不正确
				return "2";
			}
			users.setExpireDate(new Date());
			usersService.save(users);
			System.out.println("test register users.getId():"+users.getId());
			//注册成功
			return "1";
		}else {
			//注册失败
			return "0";
		}
	}
	/**
	 * 发送验证码
	 * @param phoneNum 手机号
	 * @param request http请求
	 * @return ‘1’ 已发送
	 */
	@RequestMapping(value="sendVerifyCode")
	@ResponseBody
	public String sendVerifyCode(String phoneNum,HttpServletRequest request) {
		MsgUtil.sendVerifyCode(phoneNum, request);
		//'1' 已发送
		return "1";
	}
	/**
	 * 验证手机号是否存在
	 * @param phoneNum
	 * @return
	 */
	@RequestMapping(value="validatePhoneNum")
	@ResponseBody
	public String validatePhoneNum(String phoneNum) {
		System.out.println("validate phoneNum-----"+phoneNum);
		//验证唯一性
		Users uniUser = usersService.findUniqueByProperty("phone_num", phoneNum);
		if(uniUser == null) {
			//用户不存在
			return "1";
		}else {
			//用户已存在
			return "0";
		}
	}
	/**
	 * 用户找回密码
	 * @param phoneNum
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value="forgotPassword")
	@ResponseBody
	public String forgotPassword(String phoneNum,String password,String verifyCode,HttpServletRequest request) {
		HttpSession session = request.getSession();
		String verifyPhoneNum = (String) session.getAttribute("verifyPhoneNum");
		String rightVerifyCode = (String) session.getAttribute("code");
		System.out.println("-----verifyPhoneNum:"+verifyPhoneNum);
		System.out.println("-----rightVerifyCode:"+rightVerifyCode);
		System.out.println("-----phoneNum:"+phoneNum);
		if(!phoneNum.equals(verifyPhoneNum)) {
			//手机号和发送验证码的手机号不一致
			return "0";
		}
		Users users = usersService.findUniqueByProperty("phone_num", phoneNum);
		if(users != null) {
			//更新密码
			if(rightVerifyCode.equals(verifyCode)) {
				users.setPassword(SystemService.entryptPassword(password));
				usersService.save(users);
				//更新成功
				return "1";
			}else {
				//验证码错误
				return "2";
			}
		}else {
			//用户不存在
			return "3";
		}
	}
	/**
	 * 
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value="validatePassword")
	@ResponseBody
	public String validatePassword(String password,HttpServletRequest request) {
		System.out.println("-----validatePassword password:"+password);
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		Users users = usersService.findUniqueByProperty("id", userId);
		if(users != null) {
			if(SystemService.validatePassword(password, users.getPassword())) {
				//密码正确
				return "1";
			}else {
				//密码错误
				return "2";
			}
		}else {
			//用户不存在
			return "0";
		}
	}
	@RequestMapping(value="updatePassword")
	@ResponseBody
	public String updatePassword(String password,HttpServletRequest request) {
		System.out.println("-----updatePassword password:"+password);
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		Users users = usersService.findUniqueByProperty("id", userId);
		if(users != null) {
			users.setPassword(SystemService.entryptPassword(password));
			usersService.save(users);
			//修改成功
			return "1";
		}else {
			//用户不存在
			return "0";
		}
	}
	@RequestMapping(value="logout")
	public String logout(HttpServletRequest req) {
		
		//移除用户id
		System.out.println("移除session");
		HttpSession session = req.getSession();
		session.removeAttribute("userId");
		session.removeAttribute("phoneNum");
		session.removeAttribute("usersStatus");
		System.out.println("移除session成功");
		return "redirect:/fore/index";
	}
}
