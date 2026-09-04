package com.sky.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * C端用户登录
 */
@Data
public class UserLoginDTO implements Serializable {

	//微信用户授权码
    private String code;

}
