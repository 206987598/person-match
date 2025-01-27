package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.BaseResponse;
import com.common.ErrorCode;
import com.common.ResultUtils;
import com.exception.BusinessException;
import com.model.Team;
import com.model.User;
import com.model.UserTeam;
import com.model.dto.TeamDTO;
import com.model.request.*;
import com.model.vo.TeamUserVO;
import com.service.TeamService;
import com.service.UserService;
import com.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 组队控制器
 */
@RestController
@RequestMapping("/team")
@CrossOrigin(origins = {"http://localhost:5173/"}, allowCredentials = "true")
@Slf4j
public class TeamController {
    @Resource
    private TeamService teamService;
    @Resource
    private UserService userService;
    @Resource
    private UserTeamService userTeamService;

    /**
     * 创建队伍
     *
     * @param addTeamRequest
     * @param request
     * @return
     */
    @PostMapping("/save")
    public BaseResponse<Long> addTeam(@RequestBody AddTeamRequest addTeamRequest, HttpServletRequest request) {
        if (addTeamRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(addTeamRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }


    /**
     * 更新队伍信息
     *
     * @param teamUpdateRequest
     * @return
     */

    @PostMapping("/update")
    public BaseResponse<Boolean> upDateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateById(teamUpdateRequest, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 通过id获取队伍信息
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team result = teamService.getById(id);
        if (result == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取失败");
        }
        return ResultUtils.success(result);
    }

    /**
     * 获取队伍列表
     *
     * @param teamDto
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> getTeamList(TeamDTO teamDto, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        // 检查队伍信息对象是否为空，如果为空则抛出业务异常
        if (teamDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> teamList = teamService.listTeams(teamDto, isAdmin);
        List<Long> teamIdList = teamList.stream().map(TeamUserVO::getId).collect(Collectors.toList());
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.eq("userId", loginUser.getId());
            queryWrapper.in("teamId", teamIdList);
            List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
            //查询已加入队伍的id
            Set<Long> teamIdSet = userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
            //遍历队伍列表，符合条件的队伍设置hasJoin为true
            teamList.forEach(team -> {
                boolean hasJoin = teamIdSet.contains(team.getId());
                team.setHasJoin(hasJoin);
            });
        } catch (Exception e) {
        }
        // 创建一个查询包装器，用于查询用户与团队的关系
        QueryWrapper<UserTeam> teamNumQueryWrapper = new QueryWrapper<>();
        // 设置查询条件，过滤出特定的团队ID
        teamNumQueryWrapper.in("teamId", teamIdList);
        // 执行查询，获取所有符合条件的用户与团队关系数据
        List<UserTeam> TeamList = userTeamService.list(teamNumQueryWrapper);
        // 将查询结果按照团队ID分组
        Map<Long, List<UserTeam>> listMap = TeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        // 遍历团队列表，设置每个团队的加入人数
        teamList.forEach(team -> {
            // 如果当前团队的加入人数列表为空，则默认为空列表，避免NullPointerException
            team.setHasJoinNum(listMap.getOrDefault(team.getId(), new ArrayList<>()).size());
        });
        // 返回查询结果，包装在成功的结果对象中
        return ResultUtils.success(teamList);
    }

    /**
     * 获取我创建的队伍
     *
     * @param request
     * @return
     */
    @GetMapping("/list/myCreateTeam")
    public BaseResponse<List<Team>> getMyCreateList(HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);
        long userId = loginUser.getId();
        // 将登录用户的ID设置到队伍信息对象中
        // 查询并获取队伍列表信息
        QueryWrapper<Team> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        List<Team> teamList = teamService.list(queryWrapper);
//        List<TeamUserVO> teamList = teamService.listTeams(userId,true);
//        Map<Long, List<TeamUserVO>> userId = teamList.stream().collect(Collectors.groupingBy(TeamUserVO::getUserId));
//        // 创建一个查询包装器，用于查询用户与团队的关系
//        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
//        // 设置查询条件，过滤出特定的团队ID
//        queryWrapper.eq("userId", userId);
//        // 执行查询，获取所有符合条件的用户与团队关系数据
//        List<UserTeam> TeamList = userTeamService.list(queryWrapper);
//        // 将查询结果按照团队ID分组
//        Map<Long, List<UserTeam>> listMap = TeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
//        // 遍历团队列表，设置每个团队的加入人数
//        teamList.forEach(team -> {
//            // 如果当前团队的加入人数列表为空，则默认为空列表，避免NullPointerException
//            team.setHasJoinNum(listMap.getOrDefault(team.getId(), new ArrayList<>()).size());
//        });
        // 返回查询结果，包装在成功的结果对象中
        return ResultUtils.success(teamList);
    }

    /**
     * 获取我加入的队伍
     *
     * @param teamDto
     * @param request
     * @return
     */
    @GetMapping("/list/myJoinTeam")
    public BaseResponse<List<TeamUserVO>> getMyJoinTeamList(TeamDTO teamDto, HttpServletRequest request) {
        // 检查队伍信息对象是否为空，如果为空则抛出业务异常
        if (teamDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取当前登录用户信息
        User loginUser = userService.getLoginUser(request);
        // 构建查询条件，查询当前用户所在的所有队伍
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        // 执行查询，获取用户所在队伍的列表
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        // 根据队伍ID对查询结果进行分组
        //取出不重复的队伍ID
        //teamId userId
        //1:1
        //1:2
        //2:3
        //result
        //1:[1,2]
        //2:[3]
        Map<Long, List<UserTeam>> mapList = userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        // 获取所有队伍ID的列表
        ArrayList idList = new ArrayList<>(mapList.keySet());
        // 将队伍ID列表设置到队伍信息对象中，以便后续查询使用
        teamDto.setIdList(idList);
        // 根据队伍信息对象查询详细的队伍信息和成员信息
        List<TeamUserVO> teamList = teamService.listTeams(teamDto, true);
        // 返回查询结果，包装在成功的结果对象中
        final List<Long> teamIdList = teamList.stream().map(TeamUserVO::getId).collect(Collectors.toList());
        try {
            queryWrapper.eq("userId", loginUser.getId());
            queryWrapper.in("teamId", teamIdList);
            List<UserTeam> TeamList = userTeamService.list(queryWrapper);
            //查询已加入队伍的id
            Set<Long> teamIdSet = TeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
            //遍历队伍列表，符合条件的队伍设置hasJoin为true
            teamList.forEach(team -> {
                boolean hasJoin = teamIdSet.contains(team.getId());
                team.setHasJoin(hasJoin);
            });
        } catch (Exception e) {
        }
        // 创建一个查询包装器，用于查询用户与团队的关系
        QueryWrapper<UserTeam> teamNumQueryWrapper = new QueryWrapper<>();
        // 设置查询条件，过滤出特定的团队ID
        teamNumQueryWrapper.in("teamId", teamIdList);
        // 执行查询，获取所有符合条件的用户与团队关系数据
        List<UserTeam> TeamList = userTeamService.list(teamNumQueryWrapper);
        // 将查询结果按照团队ID分组
        Map<Long, List<UserTeam>> listMap = TeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        // 遍历团队列表，设置每个团队的加入人数
        teamList.forEach(team -> {
            // 如果当前团队的加入人数列表为空，则默认为空列表，避免NullPointerException
            team.setHasJoinNum(listMap.getOrDefault(team.getId(), new ArrayList<>()).size());
        });
       //  返回查询结果，包装在成功的结果对象中
        return ResultUtils.success(teamList);
    }

    /**
     * 分页查询
     *
     * @param teamDto
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> getTeamPage(TeamDTO teamDto) {
        // 检查队伍信息对象是否为空，如果为空则抛出业务异常
        if (teamDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 初始化队伍实体对象，并将DTO中的属性复制到实体对象中
        Team team = new Team();
        Page<Team> page = new Page<>(teamDto.getPageNum(), teamDto.getPageSize());
        BeanUtils.copyProperties(teamDto, team);
        // 构建查询条件包装器，用于后续的查询操作
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        // 使用构建的查询条件查询所有匹配的队伍信息
        Page<Team> resultPage = teamService.page(page, queryWrapper);
        // 返回查询结果，包装在成功的结果对象中
        return ResultUtils.success(resultPage);
    }

    /**
     * 加入队伍
     *
     * @param joinTeamRequest
     * @param request
     * @return
     */
    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody JoinTeamRequest joinTeamRequest, HttpServletRequest request) {
        if (joinTeamRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(joinTeamRequest, loginUser);
        return ResultUtils.success(result);

    }

    /**
     * 退出队伍
     *
     * @param quitTeamRequest
     * @param request
     * @return
     */
    @PostMapping("/quit")
    public BaseResponse<Boolean> joinTeam(@RequestBody QuitTeamRequest quitTeamRequest, HttpServletRequest request) {
        if (quitTeamRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.quitTeam(quitTeamRequest, loginUser);
        return ResultUtils.success(result);

    }

    /**
     * 队伍删除
     *
     * @param deleteTeamRequest
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteTeamRequest deleteTeamRequest, HttpServletRequest request) {
        Long id = deleteTeamRequest.getId();
        if (deleteTeamRequest == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.removeTeamById(id, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "解散失败");
        }
        return ResultUtils.success(true);
    }
}
