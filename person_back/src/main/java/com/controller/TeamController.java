package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.BaseResponse;
import com.common.ErrorCode;
import com.common.ResultUtils;
import com.exception.BusinessException;
import com.model.Team;
import com.model.User;
import com.model.dto.TeamDto;
import com.model.request.addTeamRequest;
import com.service.TeamService;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 创建队伍
     * @param addTeamRequest
     * @param request
     * @return
     */
    @PostMapping("/save")
    public BaseResponse<Long> addTeam(@RequestBody addTeamRequest addTeamRequest, HttpServletRequest request) {
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
     * 队伍删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 更新队伍信息
     *
     * @param team
     * @return
     */

    @PostMapping("/update")
    public BaseResponse<Boolean> upDateTeam(Team team) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.updateById(team);
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
    public BaseResponse<List<Team>> getTeamList(TeamDto teamDto) {
        // 检查团队信息对象是否为空，如果为空则抛出业务异常
        if (teamDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 初始化团队实体对象，并将DTO中的属性复制到实体对象中
        Team team = new Team();
        BeanUtils.copyProperties(team, teamDto);
        // 构建查询条件包装器，用于后续的查询操作
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        // 使用构建的查询条件查询所有匹配的团队信息
        List<Team> teamList = teamService.list(queryWrapper);
        // 返回查询结果，包装在成功的结果对象中
        return ResultUtils.success(teamList);
    }

    /**
     * 分页查询
     *
     * @param teamDto
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> getTeamPage(TeamDto teamDto) {
        // 检查团队信息对象是否为空，如果为空则抛出业务异常
        if (teamDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 初始化团队实体对象，并将DTO中的属性复制到实体对象中
        Team team = new Team();
        Page<Team> page = new Page<>(teamDto.getPageNum(), teamDto.getPageSize());
        BeanUtils.copyProperties(teamDto, team);
        // 构建查询条件包装器，用于后续的查询操作
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        // 使用构建的查询条件查询所有匹配的团队信息
        Page<Team> resultPage = teamService.page(page, queryWrapper);
        // 返回查询结果，包装在成功的结果对象中
        return ResultUtils.success(resultPage);
    }

}
