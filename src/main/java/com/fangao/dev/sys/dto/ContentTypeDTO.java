package com.fangao.dev.sys.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ContentTypeDTO implements Comparable<ContentTypeDTO>{
    private long id;
    private long pid;
    private String name;
    private String s_name;
    private long count;

    @Override
    public int compareTo(ContentTypeDTO contentTypeDTO) {
        return new Long(this.pid - contentTypeDTO.getPid()).intValue();
    }
}
