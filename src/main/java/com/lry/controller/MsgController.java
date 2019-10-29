package com.lry.controller;

import com.lry.base.BaseController;
import com.lry.bean.Msg;
import com.lry.bean.MsgVo;
import com.lry.bean.User;
import com.lry.common.Common;
import com.lry.service.MsgService;
import com.lry.service.UserService;
import com.lry.util.StringUtil;
import org.omg.CORBA.CODESET_INCOMPATIBLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/10/19
 */
@RestController
@RequestMapping
public class MsgController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private MsgService msgService;

    /**
     * 查询用户的好友列表以及最后一条消息
     * @param id 用户id
     * @return
     */
    @RequestMapping("/friendsAndMsg")
    @ResponseBody
    public Map<String,Object> friendsAndMsg(int id){
        User user = userService.getUser(id);
        if(null==user||StringUtil.isEmpty(user.getFriends())){
            return toResponseNOT("您还没有好友");
        }
        String friends = user.getFriends();
        String[] fs = friends.split(",");
        List<MsgVo> resList = new ArrayList<>();
        for (String friendName:fs) {
            MsgVo mv = new MsgVo();
            //查询好友
            User friend = userService.getUserByName(friendName);
            if(friend!=null){
                mv.setId(friend.getId());
                mv.setUsername(friend.getUsername());
                Msg msg = null;
                //查询群聊室最后一条消息记录
                if(Common.ALL.equals(friendName)){
                    msg = msgService.getGroupLastMsg(Common.ALL);
                }else{
                    //查询私聊最后一条消息
                    msg =  msgService.getLastMsg(friend.getUsername(),user.getUsername());
                }
                if(null!=msg){
                    mv.setMsg(msg.getMsg());
                    mv.setSendTime(msg.getSendTime());
                }
                resList.add(mv);
            }
        }
        return toResponseOK(resList);
    }

    /**
     * 查询我和某个好友的聊天记录
     * @param friendName
     * @param myName
     * @return
     */
    @RequestMapping("/chatMsg")
    @ResponseBody
    public Map<String,Object> chatMsg(String friendName,String myName){
        List<Msg> msgs = null;
        if(Common.ALL.equals(friendName)){
            msgs = msgService.getGroupMsgList(Common.ALL);
        }else{
            msgs = msgService.getMsgList(friendName,myName);
        }
        return toResponseOK(msgs);
    }
}
