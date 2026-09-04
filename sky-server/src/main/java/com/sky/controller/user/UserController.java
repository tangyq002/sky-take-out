package com.sky.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * c端用户接口
 * @author tyq
 * @date 2026年9月4日
 * @project_name sky-server
 * @package_name com.sky.controller.user
 * @file_name UserController.java
 * @classname UserController
 * @version 2026年9月4日 下午4:16:51
 */
@RestController()
@RequestMapping("/user/user")
@Slf4j
@Api(tags = "c端用户相关接口")
public class UserController {
	
	@Autowired
	private UserService userService;
	
    @Autowired
    private JwtProperties jwtProperties;
	
	/**
	 * 微信登录
	 * @param userLoginDTO
	 * @return
	 */
	@PostMapping("/login")
	@ApiOperation("微信登录")
	public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
		log.info("用户传递的数据：{}",userLoginDTO);
		User user = userService.wxLogin(userLoginDTO);
		//生成jwt令牌
        Map<String, Object> map = new HashMap<>();
        map.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                map);
        //赋值并返回UserLoginVO
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
		return Result.success(userLoginVO);
	}
}
