/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.game.mapper.admin;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.game.entity.admin.Tasks;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务管理MAPPER接口
 * @author orange
 * @version 2018-10-10
 */
@MyBatisMapper
public interface TasksMapper extends BaseMapper<Tasks> {
    List<Tasks> listTasks(@Param(value = "gameGroup") String gameGroup);
    List<Tasks> getTodayTasks(@Param(value = "gameGroup") String gameGroup);
    List<Tasks> getAllTasks(@Param(value = "gameGroup") String gameGroup);
    List<Tasks> getLimitTasks(@Param(value = "gameGroup") String gameGroup);
}