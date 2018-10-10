package com.jeeplus.modules.game.web.fore;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.game.entity.admin.Inform;
import com.jeeplus.modules.game.entity.admin.Notice;
import com.jeeplus.modules.game.entity.admin.Shop;
import com.jeeplus.modules.game.entity.admin.Tasks;
import com.jeeplus.modules.game.entity.admin.TasksStat;
import com.jeeplus.modules.game.entity.admin.Users;
import com.jeeplus.modules.game.entity.admin.UsersTasksItem;
import com.jeeplus.modules.game.entity.admin.Withdraw;
import com.jeeplus.modules.game.service.admin.InformService;
import com.jeeplus.modules.game.service.admin.NoticeService;
import com.jeeplus.modules.game.service.admin.ShopService;
import com.jeeplus.modules.game.service.admin.TasksService;
import com.jeeplus.modules.game.service.admin.TasksStatService;
import com.jeeplus.modules.game.service.admin.UsersService;
import com.jeeplus.modules.game.service.admin.UsersTasksItemService;
import com.jeeplus.modules.game.service.admin.WithdrawService;
import com.jeeplus.modules.game.util.AppResponse;

@Controller
@RequestMapping(value="fore")
public class ForeUsersTasksController {
	@Autowired
	UsersTasksItemService itemService;
	@Autowired
	TasksService tasksService;
	@Autowired
	UsersService usersService;
	@Autowired
	WithdrawService withdrawService;
	@Autowired
	ShopService shopService;
	@Autowired
	TasksStatService tasksStatService;
	@Autowired
	NoticeService noticeService;
	@Autowired
	InformService informService;
	
	/**任务详情
	 * @param tasksid 前台传过来的任务id
	 * @param model 自动注入的model
	 * @return 跳转到taskDetail.jsp
	 */
	@RequestMapping(value="getTasksDetails")
	public String getTasksDetails(String tasksid,Model model,HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		//获取sesion中的userid,若不存在,代表用户未登录
		String userId = (String) session.getAttribute("userId");
		System.out.println("userId:*****"+userId);
//		String phoneNum = (String) session.getAttribute("phoneNum");
		
		//若userid在session中存在，代表用户已登录,在session中设置用户vip状态
		if(!"".equals(userId) || userId.length()>0 || userId!=null || userId != "null") {
			//用userid向数据库中查询user,若用户存在，将用户的vip状态放入session
			Users uniUser = usersService.findUniqueByProperty("id", userId);
			if (uniUser != null) {
				session.setAttribute("usersStatus", uniUser.getStatus());
			}
		}
		
		/*
		 * 查询UsersTasksItem,返回state
		 */
		List<UsersTasksItem> items = itemService.selectByUsersIdAndTasksId(userId, tasksid);
		if(items.size() >= 1) {
			System.out.println("*****items.size():"+items.size());
			for (UsersTasksItem item : items) {
				if(item.getUsers().getId().equals(userId) && item.getTasks().getId().equals(tasksid)) {
					System.out.println(item.toString());
					model.addAttribute("item", item);
				}
			}
		}
		
		/*
		 * 返回查询的Tasks
		 */
		Tasks t = new Tasks();
		//查询listTasks
		List<Tasks> tss = tasksService.findList(t);
//		System.out.println("tss"+tss.size() + "***taskid"+ tasksid);
		//将要返回的tasks
		Tasks uniTasks = null;
		//若查询不为空
		if(tss.size() >= 1) {
			//遍历，并且将id=tasksid的对象给unitasks
			for (Tasks tasks : tss) {
//				System.out.println("taskid:"+ tasks.getId());
				if (tasks.getId().equals(tasksid)) {
					tasks.setIcon(tasks.getIcon().substring(10, tasks.getIcon().length()));
					uniTasks = tasks;
				}
			}
		}
		model.addAttribute("tasks", uniTasks);
		return "modules/game/fore/taskDetail";
	}
	
	/**
	 * 用户抢任务 ,新增UsersTasksItem
	 * @param usersid
	 * @param tasksid
	 * @return
	 */
	@RequestMapping(value="addUsersTasksItem")
	@ResponseBody
	public String addUsersTasksItem(String tasksid,HttpServletRequest request) {
		Tasks addTasks = tasksService.findUniqueByProperty("id", tasksid);
		System.out.println("11111111111");
		if(addTasks != null ) {
			Date addDate = new Date();
			Date createDate = addTasks.getCreateDate();
			Long current = addDate.getTime() - createDate.getTime();
			if(current/3600000 >= 24 ) {
				return "1";
			}
			System.out.println("testDate*********************");
			System.out.println(addDate.getTime());
			System.out.println(createDate.getTime());
			System.out.println();
			
		}
		HttpSession session = request.getSession();
		String usersid = (String) session.getAttribute("userId");
		System.out.println("usersid:---"+usersid+"-----tasksid---"+tasksid);
		Users users = new Users();
		users.setId(usersid);
		Tasks tasks = new Tasks();
		tasks.setId(tasksid);
		UsersTasksItem usersTasksItem = new UsersTasksItem();
		usersTasksItem.setUsers(users);
		usersTasksItem.setTasks(tasks);
		usersTasksItem.setPicture("未提交");
		usersTasksItem.setRemarks("未提交");
		usersTasksItem.setState("1");
		itemService.save(usersTasksItem);
		System.out.println("usersTasksItem:-----"+usersTasksItem.getId());
		return usersTasksItem.getId();
	}
	/**首页内容
	 * @return 从数据库获取index.jsp页面的内容，放入model
	 */
	@RequestMapping(value="index")
	public String index(Model model){
		//查询任务
		System.out.println("---------------");
		List<Tasks> tasks = tasksService.listTasks();
		for (Tasks tasks2 : tasks) {
			tasks2.setIcon(tasks2.getIcon().substring(10, tasks2.getIcon().length()));
			System.out.println(tasks2.toString());
		}
		//Collections.reverse(tasks);
		//任务统计
		List<TasksStat> stats = tasksStatService.selectStat();
		List<Notice> notices = noticeService.getNotice();
		for (Notice notice : notices) {
			notice.setPicture(notice.getPicture().substring(10, notice.getPicture().length()));
		}
		List<Inform> informs = informService.getInform();
		model.addAttribute("notices", notices);
		model.addAttribute("informs", informs);
		model.addAttribute("tasks", tasks);
		model.addAttribute("stat", stats.get(0));
		return "modules/game/fore/index";
	}
	
	/**
	 * 提交图片
	 * @param request
	 * @return 图片路径
	 * @throws IOException
	 */
	@RequestMapping(value = "getImg")
	@ResponseBody
	public AppResponse<String> getImg(MultipartHttpServletRequest request,HttpServletRequest req) throws IOException {
	          Iterator<String> itr = request.getFileNames(); 
	          MultipartFile mpf = null;  
	          String relativePath = "";
	          while (itr.hasNext()) {    
	               mpf = request.getFile(itr.next());
	                    try {    
	                         if(mpf.getSize()<104857600){  
	                              //String attachmentName = StringUtils.substringBeforeLast(mpf.getOriginalFilename(), ".");         
	                              String ext = StringUtils.substringAfterLast(mpf.getOriginalFilename(), "."); 
	                              //需要在配置文件（sysConfig.properties）里面加上    根路径fileUploadPath=F:\\
	                              //String rootPath = ResourceUtil.getConfigByName("fileUploadPath"); 
	                              Date createDate = new Date();
	                              SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	                              String d = format.format(createDate);
	                              String rootPath = "D://apache-tomcat-7.0.73/webapps/jeeplus"; 
	                              String filePath = "userfiles/commitImgupload/leave/"+d;  
	                              String fileName = UUID.randomUUID()+"."+ext;  
	                              relativePath = filePath+"/"+fileName; 
	                              File realPath = new File(rootPath+"//"+filePath); 
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
	    System.out.println("relativePath:------"+relativePath);
	    HttpSession session = req.getSession();
	    session.setAttribute("imgUrl", relativePath);
		return new AppResponse<>(1,"success",relativePath);
	}
	
	@RequestMapping(value="commitTask")
	@ResponseBody
	public String commitTask(String commitContent,String usersTasksId,String imgUrl, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		if(userId == null) {
			return "0";
		}
		if(imgUrl != null) {
			System.out.println("imgUrl.length():"+imgUrl.length());
			String url = "|/jeeplus/";
			imgUrl = url+imgUrl;
		}else {
			imgUrl="无";
		}
		System.out.println("---imgUrl:"+imgUrl);
		System.out.println("********"+usersTasksId);
		UsersTasksItem item = new UsersTasksItem();
		itemService.updateUsersTasksItem(imgUrl, commitContent, usersTasksId,"2");
		return "1";
	}
	/**
	 * 跳转到个人中心
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="personal")
	public String personal(HttpServletRequest request,Model model) {
		//获取session中的用户id，判断用户是否登录
		HttpSession session = request.getSession();
		String usersId = (String) session.getAttribute("userId");
		Users users = usersService.findUniqueByProperty("id", usersId);
		if(users != null) {
			//用户登录则把用户的信息放入返回
			session.setAttribute("usersStatus", users.getStatus());
			model.addAttribute("balance",users.getBalance());
			Date date = users.getExpireDate();
			SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
			String expireDate = "";
			System.out.println(date);
			if(date != null) {
				expireDate = format.format(date);
				model.addAttribute("expireDate",expireDate);
			}
			return "modules/game/fore/personal";
		}else{
			return "modules/game/fore/personal";
		}
	}
	/**
	 * 用户提现页面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="withdraw")
	public String withdraw(Model model,HttpServletRequest request) {
		//获取session中的用户id，判断用户是否登录
		HttpSession session = request.getSession();
		String usersId = (String) session.getAttribute("userId");
		//查询user
		
		Users users = usersService.findUniqueByProperty("id", usersId);
		if(users != null) {
			//
			List<Withdraw> withdraws = withdrawService.listWithdrawByUserId(usersId);
			if(withdraws.size() >= 1 ) {
				model.addAttribute("withdraws",withdraws);
			}
			//用户登录则把用户的信息放入model并返回
			String alipayAccount = users.getAlipayAccount();
			String alipayName = users.getAlipayName();
			model.addAttribute("alipayAccount", alipayAccount);
			model.addAttribute("alipayName", alipayName);
			session.setAttribute("usersStatus", users.getStatus());
			model.addAttribute("balance",users.getBalance());
		}
		return "modules/game/fore/withdraw";
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
	public String addWithdraw(Double withdrawNum,String remarks,HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		System.out.println("*****withdrawNum:"+withdrawNum+"remarks:"+remarks+"userId"+userId+"******");
		Users users = usersService.findUniqueByProperty("id", userId);
		if(users == null ) {
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
		System.out.println("balance after withdraw:"+balance);
		return "1";
	}
	
	/**
	 * 个人中心 查看任务
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="taskView")
	public String  getUsersTasksItemByUserId(HttpServletRequest request,Model model) {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		if(userId == "" || userId == "null") {
			return "";
		}
		List<UsersTasksItem> items= itemService.selectByUsersId(userId);
		for (UsersTasksItem item : items) {
			System.out.println(item.getTasks().toString());
		}
		System.out.println("userId:"+userId);
		System.out.println("items.size():"+items.size());
		model.addAttribute("items", items);
		return "modules/game/fore/taskView";
	}
	/**
	 * 账户余额页查询所有已通过审核的usersTasksItem
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="selectPassedTasks")
	public String selectPassedTasks(HttpServletRequest request,Model model) {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		List<UsersTasksItem> items = itemService.selectPassed(userId);
		if(items.size() >= 1 ) {
			model.addAttribute("items", items);
		}
		return "modules/game/fore/accountInformation";
	}
	/**
	 * 个人中心 通知
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="selectPassedAndNot")
	public String selectPassedAndNot(HttpServletRequest request,Model model) {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		List<UsersTasksItem> items = itemService.selectPassedAndNot(userId);
		if(items.size() >= 1 ) {
			model.addAttribute("items", items);
		}
		return "modules/game/fore/notify";
	}
	/**
	 * 商城
	 * @param model
	 * @return
	 */
	@RequestMapping(value="shop")
	public String shop(Model model) {
		Shop shop = new Shop();
		List<Shop> shops = shopService.findList(shop);
		for (Shop s : shops) {
			s.setPicture(s.getPicture().substring(10,s.getPicture().length()));
			System.out.println(s.toString());
		}
		model.addAttribute("goods", shops);
		return "modules/game/fore/shop";
	}
}
