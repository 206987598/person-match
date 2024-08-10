package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.ErrorCode;
import com.exception.BusinessException;
import com.mapper.TeamMapper;
import com.model.Team;
import com.model.User;
import com.model.UserTeam;
import com.model.dto.TeamDTO;
import com.model.enums.TeamStatusEnum;
import com.model.request.TeamUpdateRequest;
import com.model.vo.TeamUserVO;
import com.model.vo.UserVO;
import com.service.TeamService;
import com.service.UserService;
import com.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author qings
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2024-08-09 14:27:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Resource
    private UserTeamService userTeamService;
    @Resource
    private UserService userService;

    @Override
    public long addTeam(Team team, User loginUser) {
        final long userId = loginUser.getId();
        //1.请求参数是否为空？
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        //3.校验信息
        //i.队伍人数>1且<＝20
        Integer maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        //ii.队伍标题<＝20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() >= 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        //ii.描述<＝512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述不满足要求");
        }

        // iv.status 是否公开(int)不传默认为0(公开)
        Integer status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getTeamStatusEnumByValue(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        //v.如果status是加密状态，一定要有密码，且密码<=32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
            }
        }
        //vi.超时时间>当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间>当前时间");
        }
        //vii.校验用户最多创建5个队伍
        //todo 存在bug，用户可以同时创建队伍，后续可以增加锁
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long count = this.count(queryWrapper);
        if (count >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多创建5个队伍");
        }
        //4.插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        //5.插入用户=>队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        return teamId;
    }

    @Override
    public List<TeamUserVO> listTeams(TeamDTO teamDto, boolean isAdmin) {
        // 根据团队数据对象TeamDto查询匹配的团队信息
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if (teamDto != null) {
            Long id = teamDto.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id); // 根据id精确查询
            }
            String name = teamDto.getName();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name); // 根据名称模糊查询
            }
            String searchText = teamDto.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText)); // 根据搜索文本在名称或描述中模糊查询
            }
            String description = teamDto.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description); // 根据描述模糊查询
            }
            Long userId = teamDto.getUserId();
            if (userId != null && userId > 0) {
                queryWrapper.eq("userId", userId); // 根据用户id精确查询
            }
            Integer maxNum = teamDto.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("maxNum", maxNum); // 根据最大人数精确查询
            }
            Integer status = teamDto.getStatus();
            TeamStatusEnum teamStatusEnum = TeamStatusEnum.getTeamStatusEnumByValue(status);
            if (teamStatusEnum == null) {
                teamStatusEnum = TeamStatusEnum.PUBLIC; // 如果状态无效，默认为公开状态
            }
            if (!isAdmin && !teamStatusEnum.equals(TeamStatusEnum.PUBLIC)) {
                throw new BusinessException(ErrorCode.NO_AUTH, "无权限"); // 非管理员用户查询非公开团队抛出无权限异常
            }
            queryWrapper.eq("status", teamStatusEnum.getValue()); // 根据团队状态精确查询
            queryWrapper.and(qw -> qw.gt("expireTime", new Date()).or().isNull("expireTime")); // 查询过期时间在当前时间之前的团队或没有设置过期时间的团队
            List<Team> teamList = this.list(queryWrapper); // 执行查询获取团队列表
            if (CollectionUtils.isEmpty(teamList)) {
                return new ArrayList<>(); // 如果查询结果为空，返回空列表
            }
            // 关联查询创建人的信息
            List<TeamUserVO> teamUserVOList = new ArrayList<>();
            for (Team team : teamList) {
                Long teamUserId = team.getId();
                if (teamUserId == null) {
                    continue; // 如果团队id为空，跳过当前循环
                }
                User user = userService.getById(teamUserId); // 根据团队id查询创建人信息
                TeamUserVO teamUserVO = new TeamUserVO();
                BeanUtils.copyProperties(team, teamUserVO); // 将团队信息复制到视图对象
                if (user != null) {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(user, userVO); // 将创建人信息复制到视图对象
                    teamUserVO.setCreateUserVO(userVO); // 设置团队视图对象的创建人信息
                }
                teamUserVOList.add(teamUserVO); // 将团队视图对象添加到列表
            }
            // 关联查询创建人的信息
            // sql：select * from team t left join user u on t.userId = u.id
            // todo 查询队伍和已加成员的队伍信息
            // sql:select * from team t left join user_team ut on t.userId = ut.id
            return teamUserVOList; // 返回包含创建人信息的团队列表
        }
        return null;
    }

    @Override
    public boolean updateById(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        if (!oldTeam.getUserId().equals(loginUser.getId()) || !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
        }
        Team updateTeam = new Team();
//        updateTeam.setUpdateTime(new Date());
        BeanUtils.copyProperties(teamUpdateRequest, updateTeam);
        boolean result = this.updateById(updateTeam);
        return result;
    }
}




