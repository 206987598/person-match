package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.ErrorCode;
import com.exception.BusinessException;
import com.model.User;
import com.model.UserTeam;
import com.model.enums.teamStatusEnum;
import com.service.TeamService;
import com.mapper.TeamMapper;
import com.model.Team;
import com.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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

    @Override
    public long addTeam(Team team, User loginUser) {
        final long userId = loginUser.getId();
        //1.请求参数是否为空？
        if (team==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.是否登录，未登录不允许创建
        if(loginUser==null){
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        //3.校验信息
        //i.队伍人数>1且<＝20
        Integer maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0) ;
        if (maxNum<1||maxNum>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数不满足要求");
        }
        //ii.队伍标题<＝20
        String name = team.getName();
        if( StringUtils.isBlank(name) ||name.length()<=20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍标题不满足要求");
        }
        //ii.描述<＝512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description)&&description.length()>512){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍描述不满足要求");
        }

        // iv.status 是否公开(int)不传默认为0(公开)
        Integer status = Optional.ofNullable(team.getStatus()).orElse(0);
        teamStatusEnum statusEnum = teamStatusEnum.getTeamStatusEnumByValue(status);
        if (statusEnum==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍状态不满足要求");
        }
        //v.如果status是加密状态，一定要有密码，且密码<=32
        String password = team.getPassword();
        if (teamStatusEnum.SECRET.equals(statusEnum)){
            if (StringUtils.isBlank(password)||password.length()>32){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码设置不正确");
            }
        }
        //vi.超时时间>当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"超时时间>当前时间");
        }
        //vii.校验用户最多创建5个队伍
        //todo 存在bug，用户可以同时创建队伍，后续可以增加锁
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        long count = this.count(queryWrapper);
        if (count>=5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"最多创建5个队伍");
        }
        //4.插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result||teamId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建队伍失败");
        }
        //5.插入用户=>队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建队伍失败");
        }
        return 0;
    }
}




