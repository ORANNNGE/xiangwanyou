package com.jeeplus.modules.game.web.fore;

import com.jeeplus.modules.game.entity.admin.UsersTasksItem;
import com.jeeplus.modules.game.service.admin.TasksService;
import com.jeeplus.modules.game.service.admin.UsersService;
import com.jeeplus.modules.game.service.admin.UsersTasksItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("del")
public class DeleteTasksController {
    @Autowired
    UsersService usersService;
    @Autowired
    TasksService tasksService;
    @Autowired
    UsersTasksItemService usersTasksItemService;

    @RequestMapping("delRepetItem")
    @ResponseBody
    public String delRepetItem(){

        return "";
    }
}
