package com.lry.controller;

import com.lry.base.BaseController;
import com.lry.bean.Msg;
import com.lry.bean.User;
import com.lry.common.Common;
import com.lry.service.MsgService;
import com.lry.service.UserService;
import com.lry.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/10/25
 */
@RestController
@RequestMapping
public class FriendController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private MsgService msgService;
    /**
     * 发送好友请求
     * @param myName 我的名字
     * @param friendName 朋友的名字 小李,小明(一次可以添加多个好友)
     * @return 发送好友请求成功
     */
    @RequestMapping("/addFriend")
    @ResponseBody
    public Map<String,Object> addFriend(String myName,String friendName){
        if(StringUtil.isEmpty(myName)){
            return toResponseNOT("请登录");
        }
        if(StringUtil.isEmpty(friendName)){
            return toResponseNOT("请输入好友名字");
        }
        String[] friendNames = friendName.split(",");
        for (String fName:friendNames) {
            //如果已经发送了好友请求,跳过
            if(msgService.getAskMsg(Common.FRIEND_REQUIRE,myName,fName)!=null){
                continue;
            }
            //保存一条好友请求数据到msg表
            Msg msg = new Msg();
            msg.setMsg(Common.FRIEND_REQUIRE);
            msg.setSendName(myName);
            msg.setReceiveName(fName);
            msg.setSendTime(new Date());
            msgService.saveMsg(msg);
        }
        return toResponseOK("发送好友请求成功");
    }

    /**
     * 查询不是我好友的人
     * @param myName 我的名字
     * @param queryName 要查询的人的名字
     * @return 查询结果
     */
    @RequestMapping("/queryNotMyFriend")
    @ResponseBody
    public Map<String,Object>queryNotMyFriend(String myName,String queryName){
        User user = userService.getUserByName(myName);//我
        if(null==user)
            return toResponseNOT("非法登录");
        //查找结果需要过滤filterList
        List<String>filterList = new ArrayList<>();
        filterList.add(myName);//过滤自己
        //过滤已经是朋友的记录
        String friendStr  =user.getFriends();
        String[] friends = null;
        if(!StringUtil.isEmpty(friendStr)){
            friends = friendStr.split(",");
        }
        if(friends!=null){
            for (String friendName:friends) {
                filterList.add(friendName);
            }
        }
        List<String> users = userService.queryNotMyFriend(queryName,filterList);
        return toResponseOK(users);
    }

    /**
     * 查询receiveName的所有好友请求
     * @param receiveName 接收者
     * @return
     */
    @RequestMapping("/queryFriendAsk")
    @ResponseBody
    public Map<String,Object> queryFriendAsk(String receiveName){
        if(StringUtil.isEmpty(receiveName)){
            return toResponseNOT("请登录");
        }
        List<Msg> msgs = msgService.getFriendAskMsg(Common.FRIEND_REQUIRE,receiveName);
        return toResponseOK(msgs);
    }

    /**
     * 接受好友请求
     * @param id
     * @return
     */
    @RequestMapping("/accept")
    @ResponseBody
    public Map<String,Object>accept(int id){
        boolean res = msgService.accept(id);
        if(!res){
            return toResponseNOT("接受好友请求失败");
        }
        return toResponseOK("接受好友请求");
    }

    /**
     * 拒绝好友请求
     * @param id
     * @return
     */
    @RequestMapping("/reject")
    @ResponseBody
    public Map<String,Object>reject(int id){
        int update = msgService.updateMsg(id);
        if(update==0){
            return toResponseNOT("拒绝好友请求失败");
        }
        return toResponseOK("拒绝好友请求");
    }

    //群聊设计
    //用户新建群，群主，别的人搜索群（筛掉自己已加入的群） ，申请加入，群主同意，成为群成员，群成员可以互相聊天，加好友等
    //群聊表（维护群成员的群关系）
    // id 群主 群名字 。。。
    // 群成员表
    //id 成员名字 群名字 加入时间 通过时间 备注信息 是否通过。。。
    //user 表 加上群字段
    //群聊消息表
    //id 群id 群名字 发送者 接收者
}
