package com.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.TagMapper;
import com.model.Tag;
import com.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author qings
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2024-07-29 10:22:31
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService {


}




