package com.sky.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {

	//用户id
    private Long id;
    //微信唯一openid
    private String openid;
    //jwt令牌
    private String token;

}
