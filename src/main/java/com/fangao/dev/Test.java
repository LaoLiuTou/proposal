package com.fangao.dev;

import com.baomidou.kisso.common.encrypt.MD5Salt;

public class Test {
    public static void main(String args[]){
        String password  = MD5Salt.md5SaltEncode("djc"+"e4OV1947", "1");
        System.out.println(password);
    }
}
