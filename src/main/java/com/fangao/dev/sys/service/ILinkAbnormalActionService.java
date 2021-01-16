package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.LinkAbnormalAction;

import java.io.Serializable;

/**
 * <p>
 * 异常行为事项关系表 服务类
 * </p>
 *
 * @author jobob
 * @since 2019-03-27
 */
public interface ILinkAbnormalActionService extends IService<LinkAbnormalAction> {
     /**
      * <p>
      * 检查是否可以删除异常行为
      * </p>
      *
      * @param id     异常行为 ID
      * @return
      */
     boolean checkCanDelete(Serializable id);
}
