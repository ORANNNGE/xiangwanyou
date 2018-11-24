package com.jeeplus.modules.game.web.fore;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.enterprise.inject.New;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jeeplus.modules.game.entity.admin.*;
import com.jeeplus.modules.game.service.admin.*;
import com.jeeplus.modules.game.util.MyResourceUtil;
import com.jeeplus.modules.monitor.entity.Task;
import com.sun.jmx.remote.internal.ArrayQueue;
import org.aspectj.weaver.loadtime.Aj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.game.util.AppResponse;
import com.jeeplus.modules.game.util.MsgUtil;
import com.jeeplus.modules.sys.service.SystemService;
import com.thoughtworks.xstream.mapper.Mapper.Null;

@Controller
@RequestMapping(value="app")
public class AppInterfaceController {
	@Autowired
	UsersService usersService;
	@Autowired
	TasksService tasksService;
	@Autowired
	UsersTasksItemService itemService;
	@Autowired
	WithdrawService withdrawService;
	@Autowired
	TasksStatService tasksStatService;
	@Autowired
	ShopService shopService;
	@Autowired
	NoticeService noticeService;
	@Autowired
	InformService informService;
	@Autowired
	UsersNumService usersNumService;
	@Autowired
	SysInfoService sysInfoService;
	@Autowired
	PromoAwardService promoAwardService;


	private Logger logger = LoggerFactory.getLogger(AppInterfaceController.class.getName());

	/**
	 * 根据用户id判断是否存在该用户，若存在，则返回该用户，不存在，返回null
	 * @param userId
	 * @return
	 */
	private Users isExistUsers(String userId){
		if(userId == null || "".equals(userId)){
			return null;
		}
		Users users = usersService.get(userId);
		if(users == null){
			return null;
		}
		return users;
	}

	//去除任务该用户不能抢的任务
	private List<Tasks> gameGroupTasks(List<Tasks> tasks,Users users){
		List<Tasks> tasksList = new ArrayList<>();
		if(users == null){
			return tasks;
		}
		for (Tasks task : tasks) {
			if(StringUtils.isEmpty(task.getGameGroup())){
				tasksList.add(task);
				continue;
			}
			if(users != null && task.getGameGroup().contains(users.getGameGroup())){
				continue;
			}
		}
		return tasksList;
	}
	/**
	 * app接口登录
	 * @param phoneNum 手机号
	 * @param password 密码
	 * @return '1'密码正确， '2'密码错误，'3'用户不存在
	 */
	@RequestMapping("login")
	@ResponseBody
	public AppResponse<Users> login(String phoneNum,String password,HttpServletRequest request) {
		
//		System.out.println("*****phoneNum:"+phoneNum+" password:"+password+"*****");
		HttpSession session = request.getSession();
//		System.out.println("login sessionId:"+session.getId());
		//根据请求中的phoneNum向数据库查询
		Users uniUser = usersService.findUniqueByProperty("phone_num", phoneNum);
		if(uniUser != null) {
//			System.out.println(uniUser.toString());
			//若用户存在，则把请求中的密码和加密后的用户密码比较
			if(SystemService.validatePassword(password, uniUser.getPassword())) {
				// '1' 代表密码正确
				session.setAttribute("userId", uniUser.getId());
//				System.out.println("userId:"+uniUser.getId());
				UsersNum num = usersNumService.findUniqueByProperty("users_id", uniUser.getId());
				if(num != null){
					//userNum不为空，则将用户编号设置为num.getId()
					uniUser.setUsersNum(num.getId());
				}else {
					//userNum为空，则将用户编号设置为 用户id
					uniUser.setUsersNum(uniUser.getId());
				}
				if(uniUser.getGameGroup() == null){
					uniUser.setGameGroup("");
				}
				return new AppResponse<Users>(1,"登录成功",uniUser);
			}else {
				//2 密码错误
				return new AppResponse<Users>(2,"密码错误",null);
			}
		}else {
			//3 用户不存在
			return new AppResponse<Users>(3,"用户不存在",null);
		}
	}


	/**
	 * 验证手机号是否存在
	 * @param phoneNum
	 * @return '1' 用户不存在 ， '2' 用户已存在
	 */
	@RequestMapping(value="validatePhoneNum")
	@ResponseBody
	public AppResponse<String> validatePhoneNum(String phoneNum) {
//		System.out.println("validate phoneNum-----"+phoneNum);
		//验证唯一性
		Users uniUser = usersService.findUniqueByProperty("phone_num", phoneNum);
		if(uniUser == null) {
			//用户不存在
			return new AppResponse<String>(1,"用户不存在",null);
		}else {
			//用户已存在
			return new AppResponse<String>(0,"用户已存在",null);
		}
	}


	/**
	 * 发送验证码
	 * @param phoneNum 手机号
	 * @param request http请求
	 * @return '1' 已发送
	 */
	@RequestMapping(value="sendVerifyCode")
	@ResponseBody
	public AppResponse<String> sendVerifyCode(String phoneNum,HttpServletRequest request) {
		HttpSession session = request.getSession();
		 String code = MsgUtil.sendVerifyCode(phoneNum, request);
		//'1' 已发送
//		System.out.println("session.getId():"+session.getId());
		return new AppResponse<String>(1,"已发送",code);
	}
	/**
	 * 用户注册
	 * @param users
	 * @param verifyCode 验证码
	 * @param req
	 * @return '0'注册失败， '1'注册成功， '2'验证码不正确
	 */
	@RequestMapping(value="register")
	@ResponseBody
	public AppResponse<String> register(Users users,String verifyCode,HttpServletRequest req,HttpServletResponse response) {
//		System.out.println(users.toString());
		//验证唯一性
		HttpSession session =req. getSession();
//		System.out.println("****register sessionid"+session.getId());
//		System.out.println(" request.getRequestedSessionId()"+ req.getRequestedSessionId());
		String code = (String) session.getAttribute("code");
//		System.out.println("****code:"+code);
//		System.out.println("verifyCode:"+verifyCode);
		Users uniUser = usersService.findUniqueByProperty("phone_num", users.getPhoneNum());
		if(uniUser == null) {
			//加密密码，并且存入数据库
			if(!"".equals(users.getPromoCode()) && users.getPromoCode() != null){	//判断用户是否输入了推广码（目前用用户的编号作为推广码）
				UsersNum referrerNum = usersNumService.get(users.getPromoCode());	//用户编号
				if(referrerNum == null){
					return new AppResponse<String>(0,"推广码无效",null);
				}
				Users referrerUsers = usersService.get(referrerNum.getUsers().getId());//推广人
				if(referrerUsers == null){
					return new AppResponse<String>(0,"推广码无效",null);
				}
				users.setReferrer(referrerUsers); 	//设置推荐人
			}
			if("".equals(users.getSex()) || users.getSex() == null || users.getSex() == "null") {
				users.setSex("1");
			}
			users.setPassword(SystemService.entryptPassword(users.getPassword()));
			users.setStatus(0);
			if(!verifyCode.equals(code)) {
				//验证码不正确
				return new AppResponse<String>(2,"验证码不正确",null);
			}
			users.setBalance(0.0);
			users.setExpireDate(new Date());
			usersService.save(users);
			UsersNum num = new UsersNum();

			num.setUsers(users);
			usersNumService.save(num);
			//注册成功
			return new AppResponse<String>(1,"注册成功",null);
		}else {
			//注册失败
			return new AppResponse<String>(0,"注册失败",null);
		}
	}
	
	/**首页内容
	 * @return 从数据库获取index.jsp页面的内容，放入model
	 */
	@RequestMapping(value="index")
	@ResponseBody
	public AppResponse<Map<String, Object>> index(String userId){

		Users users = isExistUsers(userId);
		String gameGroup = "";
		if(users!=null){
			gameGroup = users.getGameGroup();
		}
		//全部任务
		List<Tasks> tasksList = tasksService.listTasks(gameGroup);


		List<Tasks> todayTasksList = tasksService.getTodayTasks(gameGroup);			//今日任务

		List<Tasks> limitTasksList = tasksService.getLimitTasks(gameGroup);			//限时任务
		String tasksStatId = MyResourceUtil.getConfigByName("tasksStatId");	//从配置文件sysConfig中获取任务统计的id
		TasksStat stat = new TasksStat();
		stat.setId(tasksStatId);
		List<TasksStat> statList = tasksStatService.findList(stat);			//任务统计
		Map<String, Object> map = new HashMap<>();
		map.put("tasks", tasksList);
		map.put("limitTasksList", limitTasksList);
		map.put("todayTasks", todayTasksList);
		map.put("stat", statList);
		return new AppResponse<>(1,"任务列表",map);
	}

	@RequestMapping(value="getAllTasks")
	@ResponseBody
	public AppResponse<Object> getAllTasks(Integer pageNum, Integer pageSize, String userId){
		Users users = isExistUsers(userId);
		String gameGroup = "";
		if(users!=null){
			gameGroup = users.getGameGroup();
		}
		PageHelper.startPage(pageNum, pageSize);
		List<Tasks> tasksList = tasksService.getAllTasks(gameGroup);
//		List<Tasks> tasks = gameGroupTasks(tasksList, users);
		PageInfo page = new PageInfo(tasksList);
		return new AppResponse<>(1,"",page);
	}

	@RequestMapping(value="getTodayTasks")
	@ResponseBody
	public AppResponse<Object> getTodayTasks(Integer pageNum, Integer pageSize, String userId){
		Users users = isExistUsers(userId);
		String gameGroup = "";
		if(users!=null){
			gameGroup = users.getGameGroup();
		}
		PageHelper.startPage(pageNum, pageSize);
		List<Tasks> tasksList = tasksService.getTodayTasks(gameGroup);
//		List<Tasks> tasks = gameGroupTasks(tasksList, users);

		PageInfo page = new PageInfo(tasksList);
		return new AppResponse<>(1,"",page);
	}

	@RequestMapping(value="getLimitTasks")
	@ResponseBody
	public AppResponse<Object> getLimitTasks(Integer pageNum, Integer pageSize, String userId){
		Users users = isExistUsers(userId);
		String gameGroup = "";
		if(users!=null){
			gameGroup = users.getGameGroup();
		}
		PageHelper.startPage(pageNum, pageSize);
		List<Tasks> tasksList = tasksService.getLimitTasks(gameGroup);
//		List<Tasks> tasks = gameGroupTasks(tasksList, users);
		PageInfo page = new PageInfo(tasksList);
		return new AppResponse<>(1,"",page);
	}

	@RequestMapping(value="logout")
	@ResponseBody
	public String logout(HttpServletRequest req) {
		
		//移除用户id
//		System.out.println("-----移除session");
		HttpSession session = req.getSession();
		session.removeAttribute("userId");
		session.removeAttribute("usersStatus");
//		System.out.println("-----移除session成功");
		return "1";
	}
	/**
	 * 任务详情
	 * @param taskId 任务id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="taskDetail")
	@ResponseBody
	public AppResponse<Map<String, Object>> getTasks(String taskId,HttpServletRequest request){
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		//将要返回的tasks
		Tasks uniTasks = tasksService.get(taskId);
		uniTasks.setIcon(uniTasks.getIcon().substring(1, uniTasks.getIcon().length()));

		//返回内容
		Map<String , Object> content = new HashMap<>();
		if(uniTasks == null) {
			return new AppResponse<>(0,"系统错误，请返回首页",null);
		}
		/*
		 * 查询UsersTasksItem,返回state
		 */
		List<UsersTasksItem> items = itemService.selectByUsersIdAndTasksId(userId, taskId);
		if(items.size() >= 1) {
//			System.out.println("*****items.size():"+items.size());
			for (UsersTasksItem item : items) {
				if(item.getUsers().getId().equals(userId) && item.getTasks().getId().equals(taskId)) {
//					System.out.println(item.toString());
					content.put("userTaskItemId", item.getId());
					content.put("userTaskItemState", item.getState());
					content.put("userTaskItemCommit", item.getRemarks());
//					System.out.println("item.getRemarks:"+item.getRemarks());
				}
			}
		}else {
			content.put("userTaskItemState", "");
			content.put("userTaskItemId", "");
			content.put("userTaskItemCommit", "");
		}
		content.put("task", uniTasks);
		return new AppResponse<>(1,"任务详情",content);
	}
	/**
	 * 抢任务
	 * @param taskId 任务id
	 * @param request
	 * @return 
	 */
	@RequestMapping(value="addUsersTasks")
	@ResponseBody
	public AppResponse<Object> addUsersTasksItem(String taskId ,String userId,HttpServletRequest request){
//		HttpSession session = request.getSession();
//		String userId = (String) session.getAttribute("userId");
		UsersTasksItem item = new UsersTasksItem();
		if(userId == null) {
			return new AppResponse<>(0,"抢任务失败",null);
		}
//		System.out.println(session.getId());
		Users users = new Users();
		Tasks tasks = new Tasks();
		tasks.setId(taskId);
		users.setId(userId);
		//设置item的各个属性
		List<UsersTasksItem> itemList = itemService.selectByUsersIdAndTasksId(userId, taskId);
		if(itemList.size() > 0){
			return new AppResponse<>(0,"已抢过该任务",null);
		}
		item.setUsers(users);
		item.setTasks(tasks);
		item.setPicture("未提交");
		item.setRemarks("未提交");
		item.setState("1");
		itemService.save(item);
		//返回item的状态
		return new AppResponse<>(1,"抢任务成功",item);
	}
	
	/**
	 * 提交图片
	 * @param request
	 * @return 图片路径
	 * @throws IOException
	 */
	@RequestMapping(value = "getImg")
	@ResponseBody
	public String getImg(MultipartHttpServletRequest request,HttpServletRequest req) throws IOException {
	          Iterator<String> itr = request.getFileNames(); 
//	          System.out.println("-------getImg");
	          MultipartFile mpf = null;  
	          String relativePath = "";
	          while (itr.hasNext()) {    
	               mpf = request.getFile(itr.next());
	                    try {    
	                         if(mpf.getSize()<104857600){  
	                             // String attachmentName = StringUtils.substringBeforeLast(mpf.getOriginalFilename(), ".");         
	                              String ext = StringUtils.substringAfterLast(mpf.getOriginalFilename(), "."); 
	                              //需要在配置文件（sysConfig.properties）里面加上    根路径fileUploadPath=F:\\
	                              String rootPath = MyResourceUtil.getConfigByName("fileUploadPath");
	                              Date createDate = new Date();
	                              SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	                              String d = format.format(createDate);
//	                              String rootPath = "D://idea_workspace/xiangwanyou/target/jeeplus";

	                              String filePath = "userfiles/commitImgupload/leave/"+d;
	                              String fileName = UUID.randomUUID()+"."+ext;  
	                              relativePath = filePath+"/"+fileName; 
	                              File realPath = new File(rootPath+"/"+filePath); 
	                              if(!realPath.exists()){    
	                                   //建立多级文件夹
	                                   realPath.mkdirs();     
	                              }  
	                              FileCopyUtils.copy(mpf.getBytes(),new File(realPath.getAbsolutePath()+"\\"+fileName));    
	                         }else{     
	                         } 
	                    } catch (Exception e) {     
	                    }
	          }
//	    System.out.println("relativePath:------"+relativePath);
		return relativePath;
	}
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value="commitTask")
	@ResponseBody
	public String commitTask(String commitContent,String userTaskItemId,String userId,String imgUrl,HttpServletRequest request) {
//		HttpSession session = request.getSession();
//		String userId = (String) session.getAttribute("userId");
//		System.out.println("************userId:"+userId);
		if(userId == null ) {
			return "0";
		}
		//拼接图片路径 以便jeeplus后台查看
		if(imgUrl != null) {
//			System.out.println("imgUrl.length():"+imgUrl.length());
			String url = "|/";
			imgUrl = url+imgUrl;
		}else {
			imgUrl="无";
		}
//		System.out.println("---imgUrl:"+imgUrl);
//		System.out.println("********"+userTaskItemId);
		itemService.updateUsersTasksItem(imgUrl, commitContent, userTaskItemId,"2");
		return "1";
	}
	
	/**
	 * 用户提现接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="withdraw")
	@ResponseBody
	public AppResponse<Map<String , Object>> withdraw(HttpServletRequest request) {
		//获取session中的用户id，判断用户是否登录
		HttpSession session = request.getSession();
		String usersId = (String) session.getAttribute("userId");
		//查询user
		Users users = usersService.findUniqueByProperty("id", usersId);
		//创建一个map充当容器
		Map<String, Object> map = new HashMap<>();
		if(users != null) {
			//用户登录则把用户的信息返回
			session.setAttribute("usersStatus", users.getStatus());
			Integer userStatus = users.getStatus();
			String alipayAccount = users.getAlipayAccount();
			String alipayName = users.getAlipayName();
			Double balance	= users.getBalance();
			map.put("userStatus", userStatus);
			map.put("alipayAccount", alipayAccount);
			map.put("alipayName", alipayName);
			map.put("balance", balance);
		}else {
			return new AppResponse<>(0,"error",null);
		}
		return new AppResponse<>(1,"success",map);
	}
	
	/**
	 * 用户提交提现申请
	 * @param withdrawNum 提现数目
	 * @param remarks 提现备注
	 * @param request
	 * @return
	 */
	@RequestMapping(value="addWithdraw")
	@ResponseBody
	public String addWithdraw(Double withdrawNum,String remarks,String userId,HttpServletRequest request) {
//		HttpSession session = request.getSession();
//		String userId = (String) session.getAttribute("userId");
//		System.out.println("*****withdrawNum:"+withdrawNum+"remarks:"+remarks+"userId"+userId+"******");
		Users users = usersService.findUniqueByProperty("id", userId);
		if(users == null) {
			return "0";
		}
		Double balance = users.getBalance();
		if(withdrawNum < 20) {
			//最低提现20
			return "3";
		}
		if(withdrawNum < 100) {
			if (withdrawNum+2 > balance) {
				//余额不足
				return "2";
			}
		}
		if(withdrawNum >=100) {
			if(withdrawNum*1.02>balance) {
				//余额不足
				return "2";
			}
		}
		Withdraw withdraw = new Withdraw();
		withdraw.setUsers(users);
		withdraw.setSum(withdrawNum);
		//提现超过100,自动扣除2%手续费
		withdraw.setSum(withdrawNum);
		withdraw.setRemarks(remarks);
		withdraw.setState("1");
		withdrawService.save(withdraw);
//		System.out.println("balance after withdraw:"+balance);
		return "1";
	}
	
	/**
	 * 用户提现页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="withdrawList")
	@ResponseBody
	public AppResponse<List<Withdraw>> withdrawList(HttpServletRequest request) {
		//获取session中的用户id，判断用户是否登录
		HttpSession session = request.getSession();
		String usersId = (String) session.getAttribute("userId");
		//查询user
		
		Users users = usersService.findUniqueByProperty("id", usersId);
		if(users != null) {
			//
			List<Withdraw> withdraws = withdrawService.listWithdrawByUserId(usersId);
			if(withdraws.size() == 0) {
				return  new AppResponse<>(2,"查询到0条记录",new ArrayList<>());
			}
			session.setAttribute("usersStatus", users.getStatus());
			return  new AppResponse<>(1,"查询到"+withdraws.size()+"条记录",withdraws);
		}else {
			return  new AppResponse<>(0,"用户未登录",null);
		}
	}
	
	/**
	 * 验证密码s
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value="validatePassword")
	@ResponseBody
	public AppResponse<Object> validatePassword(String password,HttpServletRequest request) {
//		System.out.println("-----validatePassword password:"+password+"-----");
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		Users users = usersService.findUniqueByProperty("id", userId);
		if(users != null) {
			if(SystemService.validatePassword(password, users.getPassword())) {
				//密码正确
				return new AppResponse<>(1,"密码正确",null);
			}else {
				//密码错误
				return new AppResponse<>(2,"密码错误",null);
			}
		}else {
			//用户不存在
			return new AppResponse<>(0,"用户不存在",null);
		}
	}
	/**
	 * 修改密码
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value="updatePassword")
	@ResponseBody
	public AppResponse<Object> updatePassword(String password,HttpServletRequest request) {
//		System.out.println("-----updatePassword password:"+password);
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		Users users = usersService.findUniqueByProperty("id", userId);
		if(users != null) {
			users.setPassword(SystemService.entryptPassword(password));
			usersService.save(users);
			//修改成功
			return new AppResponse<>(1,"修改成功",null);
		}else {
			//用户不存在
			return new AppResponse<>(0,"用户不存在",null);
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
	public AppResponse<Object> forgotPassword(String phoneNum,String password,String verifyCode,HttpServletRequest request) {
		HttpSession session = request.getSession();
		String verifyPhoneNum = (String) session.getAttribute("verifyPhoneNum");
		String rightVerifyCode = (String) session.getAttribute("code");
//		System.out.println("-----verifyPhoneNum:"+verifyPhoneNum);
//		System.out.println("-----rightVerifyCode:"+rightVerifyCode);
//		System.out.println("-----verifyCode:"+verifyCode);
//		System.out.println("-----phoneNum:"+phoneNum);
		if(!phoneNum.equals(verifyPhoneNum)) {
			//手机号和发送验证码的手机号不一致
			return new AppResponse<>(0,"手机号和发送验证码的手机号不一致",null);
		}
		Users users = usersService.findUniqueByProperty("phone_num", phoneNum);
		if(users != null) {
			//更新密码
			if(rightVerifyCode.equals(verifyCode)) {
				users.setPassword(SystemService.entryptPassword(password));
				usersService.save(users);
				//更新成功
				return new AppResponse<>(1,"更新成功",null);
			}else {
				//验证码错误
				return new AppResponse<>(2,"验证码错误",null);
			}
		}else {
			//用户不存在
			return new AppResponse<>(3,"用户不存在",null);
		}
	}
	
	/**
	 * 账户余额页查询所有已通过审核的usersTasksItem
	 * @param request
	 * @return
	 */
	@RequestMapping(value="selectPassedTasks")
	@ResponseBody
	public AppResponse<List<UsersTasksItem>> selectPassedTasks(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		if(userId == "" || userId == "null") {
			return new AppResponse<>(2,"用户未登录",null);
		}
		List<UsersTasksItem> items = itemService.selectPassed(userId);
		if(items.size() >= 1 ) {
			return new AppResponse<>(1,"查询到"+items.size()+"项",items);
		}
		return new AppResponse<>(0,"查询到0项",null);
	}
	
	/**
	 * 个人中心 通知
	 * 原通知是查询出来通过和没通过审核的任务，返回为前台，前台将任务名、任务的期数、最后更新的时间、是否通过审核显示出来。
	 * 现更新为可定向给某一用户发送通知，所以将原本返回的List<UsersTasksItem>改为返回List<SysInfo>。具体做法是将通过
	 * 和未通过审核任务查询出来，再将定向给该用户发送的通知查询出来，将UsersTasksItem的任务名、任务的期数、最后更新的时间、是否通过审核
	 * 改为SysInfo中的name、content、updateDate
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="selectPassedAndNot")
	@ResponseBody
	public AppResponse<Object> selectPassedAndNot(HttpServletRequest request,String userId,Model model) {
		HttpSession session = request.getSession();
//		String userId = (String) session.getAttribute("userId");

		if(userId == "" || userId == "null") {
			return new AppResponse<>(2,"用户未登录",null);
		}
		Users users = usersService.get(userId);
		if(users == null) {
			return new AppResponse<>(2,"用户未登录",null);
		}


		List<UsersTasksItem> items = itemService.selectPassedAndNot(userId);//
		//系统公告
		SysInfo sysInfo = new SysInfo();
		sysInfo.setUsers(users);
		List<SysInfo> sysInfoList = sysInfoService.findList(sysInfo);
		List<SysInfo> itemSysInfoList = new ArrayList<>();
		//将UsersTasksItem的任务名、任务的期数、最后更新的时间、是否通过审核改为SysInfo中的name、content、updateDate
		for (UsersTasksItem item : items) {
			SysInfo info = new SysInfo();
			info.setName(item.getTasks().getName()+item.getTasks().getPhase());
			if("3".equals(item.getState())){
				info.setContent("已通过");
			}
			if("4".equals(item.getState())){
				info.setContent("未通过");
			}
			info.setUpdateDate(item.getUpdateDate());
			sysInfoList.add(info);
		}

		if(sysInfoList.size() >= 1 ) {
			return new AppResponse<>(1,"查询到"+sysInfoList.size()+"项",sysInfoList);
		}
		return new AppResponse<>(0,"查询到0项",null);
	}
	
	/**
	 * 个人中心 查看任务
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="taskView")
	@ResponseBody
	public AppResponse<List<UsersTasksItem>>  getUsersTasksItemByUserId(HttpServletRequest request,Model model) {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		if(userId == "" || userId == "null") {
			return new AppResponse<>(2,"用户未登录",null);
		}
		List<UsersTasksItem> items= itemService.selectByUsersId(userId);
		if(items.size() >= 1 ) {
			for (UsersTasksItem item : items) {
//				System.out.println(item.getTasks().toString());
			}
			return new AppResponse<>(1,"查询到"+items.size()+"项",items);
		}
		return new AppResponse<>(0,"查询到0项",null);
	}
	
	/**
	 * 商城
	 * @return
	 */
	@RequestMapping(value="shop")
	@ResponseBody
	public AppResponse<List<Shop>> shop() {
		Shop shop = new Shop();
		List<Shop> shops = shopService.findList(shop);
		for (Shop s : shops) {
			s.setPicture(s.getPicture().substring(1,s.getPicture().length()));
//			System.out.println(s.toString());
		}
		
		return new AppResponse<>(1, "查询到"+shops.size()+"项商品", shops);
	}
	
	@RequestMapping(value="shopDetails")
	@ResponseBody
	public AppResponse<Shop> shopDetails(String id){
		Shop shop = shopService.findUniqueByProperty("id", id);
		if(shop != null) {
			shop.setPicture(shop.getPicture().substring(1, shop.getPicture().length()));
			return new AppResponse<>(1,"success",shop);
		}
		return new AppResponse<>(0,"fail",null);
	}
	/**
	 * 公告
	 * @return
	 */
	@RequestMapping("getNotice")
	@ResponseBody
	public AppResponse<List<Notice>> getNotice(){
		List<Notice> notices = noticeService.findList(new Notice());
		List<List<Object>> lists = new ArrayList<>();
		for (Notice notice : notices) {
			notice.setPicture(notice.getPicture().substring(1, notice.getPicture().length()));
		}
		return new AppResponse<>(1,"查询到了"+notices.size()+"条通告",notices);
	}
	/**
	 * 通告
	 * @return
	 */
	@RequestMapping("getInform")
	@ResponseBody
	public AppResponse<List<Inform>> getInform(){
		List<Inform> informs = informService.findList(new Inform());
		return new AppResponse<>(1,"查询到了"+informs.size()+"条通告",informs);
	}

	/**
	 * 获取版本
	 * @return
	 */
	@RequestMapping("getVersion")
	@ResponseBody
	public AppResponse<Object> getVersion(){
		String version = MyResourceUtil.getConfigByName("version");
		String url = MyResourceUtil.getConfigByName("appDownloadURL");
		Map<String,String > data = new HashMap<>();
		data.put("version", version);
		data.put("url", url);
		return new AppResponse<>(1,"成功",data);
	}

	@RequestMapping("getPromoAwardList")
	@ResponseBody
	public AppResponse<Object> getPromoAwardList(String userId){
		if("".equals(userId)){
			return new AppResponse<>(0,"未登录",null);
		}

		Users users = usersService.get(userId);
		if(users == null){
			return new AppResponse<>(0,"用户不存在",null);
		}

		PromoAward directPromoAward = new PromoAward();
		PromoAward indirectPromoAward = new PromoAward();
		directPromoAward.setReferrer(users);
		directPromoAward.setType("1");
		indirectPromoAward.setReferrer(users);
		indirectPromoAward.setType("2");

		List<PromoAward> directList = promoAwardService.findList(directPromoAward);
		List<PromoAward> indirectList = promoAwardService.findList(indirectPromoAward);

		Map<String ,Object > data = new HashMap<>();
		data.put("directList", directList);
		data.put("indirectList", indirectList);
		return new AppResponse<>(1,"success",data);
	}

	@RequestMapping("generateUsersNum")
	@ResponseBody
	public String generateUsersNum(){
		List<Users> usersList = usersService.findList(new Users());

		for (Users users : usersList) {
			UsersNum num = new UsersNum();
			num.setUsers(users);
			usersNumService.save(num);
		}
		return "success";
	}

	@RequestMapping("download")
	public void download(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String downloadFileName = MyResourceUtil.getConfigByName("downloadFileName");
		String pathName = request.getSession().getServletContext().getRealPath(downloadFileName) ;
		InputStream bis = null;
//		InputStream bis = new FileInputStream(new File(pathName));
		downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
		logger.debug(pathName);
		logger.debug(downloadFileName);
		response.addHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
		response.setContentType("multipart/form-data");
		try{
			bis = new BufferedInputStream(new FileInputStream(new File(pathName)));
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
	//		FileOutputStream out = new FileOutputStream(response.getOutputStream());
			byte[] b = new byte[bis.available()+1024];
			int len = 0;
			while((len = bis.read()) != -1){
				out.write(b,0,len);
			}
			out.flush();
			out.close();
			bis.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 获取下载文件的信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getDownloadFileInfo")
	@ResponseBody
	public AjaxJson getDownloadFileSize(HttpServletRequest request,HttpServletResponse response) throws Exception{
		AjaxJson json = new AjaxJson();
		String downloadFileName = MyResourceUtil.getConfigByName("downloadFileName");
		String pathName = request.getSession().getServletContext().getRealPath(downloadFileName) ;
		File file = new File(pathName);
		Long fileSize = file.length()/1024/1024;//文件大小
		long lastModified = file.lastModified();//最后修改时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(lastModified);
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
		String lastModifiedTime = format.format(calendar.getTime());
		json.put("size", fileSize);
		json.put("date", lastModifiedTime);
		return json;
	}
}