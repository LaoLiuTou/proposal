package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.LinkAbnormalPlace;

import java.io.Serializable;

/**
 * <p>
 * 异常地事项关系表 服务类
 * </p>
 *
 * @author jobob
 * @since 2019-03-27
 */
public interface ILinkAbnormalPlaceService extends IService<LinkAbnormalPlace> {
     /**
      * <p>
      * 检查是否可以删除异常地
      * </p>
      *
      * @param id     异常地 ID
      * @return
      */
     boolean checkCanDelete(Serializable id);
}
