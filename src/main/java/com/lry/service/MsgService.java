package com.lry.service;

import com.lry.bean.Msg;
import com.lry.bean.User;
import com.lry.mapper.MsgMapper;
import com.lry.mapper.UserMapper;
import com.lry.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/10/23
 */
@Service
public class MsgService {
    @Autowired
    private MsgMapper msgMapper;
    @Autowired
    private UserMapper userMapper;

    public Msg getLastMsg(String sendName, String receiveName){
        return msgMapper.getLastMsg(sendName,receiveName);
    }

    public List<Msg> getMsgList(String sendName, String receiveName){
        return msgMapper.getMsgList(sendName,receiveName);
    }

    public List<Msg> getFriendAskMsg(String msg,String receiveName){
        return msgMapper.getFriendAskMsg(msg,receiveName);
    }

    public Msg getAskMsg(String msg,String sendName,String receiveName){
        return msgMapper.getAskMsg(msg,sendName,receiveName);
    }
    public Msg getMsg(int id){
        return msgMapper.getMsg(id);
    }
    public int updateMsg(int id){
        return msgMapper.updateMsg(id);
    }
    public int saveMsg(Msg msg){
        return msgMapper.saveMsg(msg);
    }

    /**
     * 接受好友请求
     * msg is_delete设置为1
     * 两个好友互相加对方为好友
     * @param id msg的id
     */
    @Transactional
    public boolean accept(int id){
        Msg msg = msgMapper.getMsg(id);
        if(null==msg)return false;

        int update = msgMapper.updateMsg(id);
        if(update==0)return false;

        String sendName = msg.getSendName();
        String receiveName = msg.getReceiveName();

        User sendUser = userMapper.getUserByName(sendName);
        User receiveUser = userMapper.getUserByName(receiveName);
        if(sendUser==null||receiveUser==null)
            return false;

        if(!updateUser(sendUser,receiveName))return false;
        if(!updateUser(receiveUser,sendName))return false;
        return true;
    }

    /**
     *
     * @param user 用户
     * @param username 朋友名字
     * @return
     */
    private boolean updateUser(User user,String username){
        String userFriends = user.getFriends();
        int update=0;
        //没有朋友
        if(StringUtil.isEmpty(userFriends)){
            update = userMapper.updateUser(user.getId(),username);
            if(update==0)return false;
        }else{
            String[]friends = userFriends.split(",");
            //没有这个朋友才加
             if(!Arrays.asList(friends).contains(username)){
                update = userMapper.updateUser(user.getId(),user.getFriends()+","+username);
                if(update==0)return false;
            }
        }
        return true;
    }

    public Msg getGroupLastMsg(String receiveName) {
        return msgMapper.getGroupLastMsg(receiveName);
    }

    public List<Msg> getGroupMsgList(String receiveName) {
        return msgMapper.getGroupMsgList(receiveName);
    }
}
