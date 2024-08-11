package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.model.Team;
import com.model.User;
import com.model.dto.TeamDTO;
import com.model.request.JoinTeamRequest;
import com.model.request.QuitTeamRequest;
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

    /**
     * 获取队伍列表
     *
     * @param teamDto
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamDTO teamDto, boolean isAdmin);

    /**
     * 更新队伍
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */

    boolean updateById(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     *
     * @param joinTeamRequest
     * @param loginUser
     * @return
     */

    boolean joinTeam(JoinTeamRequest joinTeamRequest, User loginUser);

    /**
     * 退出队伍
     *
     * @param quitTeamRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(QuitTeamRequest quitTeamRequest, User loginUser);

    /**
     * 解散队伍
     *
     * @param id
     * @return
     */
    boolean removeTeamById(long id, User loginUser);
}
