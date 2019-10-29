package com.lry.controller;

import com.lry.base.BaseController;
import com.lry.bean.User;
import com.lry.common.Common;
import com.lry.service.UserService;
import com.lry.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/10/19
 */
@RestController
@RequestMapping
public class LoginController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public Map<String,Object> login(@RequestBody User user, HttpServletRequest request){
        if(checkUser(user)){
            return toResponseNOT("请输入用户名或密码");
        }
        User actualUser = userService.checkUser(user.getUsername(),user.getPassword());
        if(null==actualUser){
            return toResponseNOT("用户名密码不匹配");
        }
        request.getSession(false).setAttribute("username",actualUser.getUsername());
        return toResponseOK(actualUser);
    }


    @RequestMapping("/register")
    @ResponseBody
    public Map<String,Object> register(@RequestBody User user){
        if(checkUser(user)){
            return toResponseNOT("请输入用户名或密码");
        }
        if(Common.ALL.equals(user.getUsername())){
            return toResponseNOT("all不可用作用户名");
        }
        User actualUser = userService.getUserByName(user.getUsername());
        if(null!=actualUser){
            return toResponseNOT("用户名已被占用");
        }
        user.setFriends(Common.ALL);//默认新注册用户直接加入群聊室
        userService.saveUser(user);
        return toResponseOK("注册成功");
    }

    private boolean checkUser(User user){
        if(null==user||StringUtil.isEmpty(user.getUsername())||StringUtil.isEmpty(user.getPassword())){
            return true;
        }
        return false;
    }

}
