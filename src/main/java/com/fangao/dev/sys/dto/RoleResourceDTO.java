package com.fangao.dev.sys.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class RoleResourceDTO implements Serializable {

    private Long roleId;
    private List<Long> resourceIds;

}
