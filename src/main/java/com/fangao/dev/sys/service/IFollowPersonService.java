package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.dto.FollowPersonDTO;
import com.fangao.dev.sys.entity.FollowPerson;

import java.util.List;

/**
 * <p>
 * 随访人信息表
 * </p>
 *
 * @author ykb
 * @since 2019-03-24
 */
public interface IFollowPersonService extends IService<FollowPerson> {

    List<FollowPersonDTO> getFollowPersonListByEventId(long id);
}
