package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.service.TeamService;
import com.usercenter.mapper.TeamMapper;
import com.usercenter.model.Team;
import org.springframework.stereotype.Service;

/**
* @author qings
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-08-09 14:27:56
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService {

}




