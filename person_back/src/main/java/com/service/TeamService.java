package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.model.Team;
import com.model.User;
import com.model.dto.TeamDTO;
import com.model.request.TeamUpdateRequest;
import com.model.vo.TeamUserVO;

import java.util.List;

/**
 * @author qings
 * @description 针对表【team(队伍)】的数据库操作Service
 * @createDate 2024-08-09 14:27:56
 */
public interface TeamService extends IService<Team> {
    /**
     * 添加队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    List<TeamUserVO> listTeams(TeamDTO teamDto, boolean isAdmin);

    boolean updateById(TeamUpdateRequest teamUpdateRequest, User loginUser);
}
