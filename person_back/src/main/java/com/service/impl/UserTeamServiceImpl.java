package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.service.UserTeamService;
import com.mapper.UserTeamMapper;
import com.model.UserTeam;
import org.springframework.stereotype.Service;

/**
* @author qings
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-08-09 14:28:04
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService {

}




