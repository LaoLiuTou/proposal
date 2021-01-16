package com.fangao.dev.sys.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LeafContentTypeDTO {
    private long id;
    private String name;
    private long count;
    private String ids;

    public LeafContentTypeDTO(long id, String name, long count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public LeafContentTypeDTO(long id, String name, long count, String ids) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.ids = ids;
    }
}
