package com.fangao.dev.sys.dto;

import com.fangao.dev.sys.entity.PetitionReceptor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 接待员 DTO
 * </p>
 *
 */
@Data
@Accessors(chain = true)
public class PetitionReceptorDTO extends PetitionReceptor {

    /**
     * 组织 ID 集合
     */
    List<Long> orgIds;

}
