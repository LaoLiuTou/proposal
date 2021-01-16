package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.LinkCoordinationUnit;

import java.io.Serializable;

/**
 * <p>
 * 配合单位事项关系表 服务类
 * </p>
 *
 * @author jobob
 * @since 2019-03-27
 */
public interface ILinkCoordinationUnitService extends IService<LinkCoordinationUnit> {
     /**
      * <p>
      * 检查是否可以删除单位
      * </p>
      *
      * @param id     单位 ID
      * @return
      */
     boolean checkCanDelete(Serializable id);
}
