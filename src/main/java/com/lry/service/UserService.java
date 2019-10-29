package com.lry.service;

import com.lry.bean.User;
import com.lry.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/8/28
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> queryUser(Integer offset, Integer limit){
        return userMapper.query(offset,limit);
    }

    public List<String> queryNotMyFriend(String queryName, List<String>filterList){
        return userMapper.queryNotMyFriend(queryName,filterList);
    }
    public User getUser(int userId){
        return userMapper.get(userId);
    }
    public int saveUser(User user){
        return userMapper.saveUser(user);
    }
    public User getUserByName(String username){
        return userMapper.getUserByName(username);
    }


    public User checkUser(String username, String password) {
        return userMapper.checkUser(username,password);
    }
}
