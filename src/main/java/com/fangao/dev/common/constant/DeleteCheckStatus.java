package com.fangao.dev.common.constant;


import com.fangao.dev.common.CommonEnum;

/**
 * <p>
 * 事项状态
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */

public enum DeleteCheckStatus implements CommonEnum{

    WAIT_CHECK(0, "待审核"), CHECK_YES(1, "审核通过"), CHECK_NO(2, "审核不通过");

    private final int code;
    private final String msg;

    DeleteCheckStatus(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return String.format(" ErrorCode:{code=%s, msg=%s} ", code, msg);
    }
}