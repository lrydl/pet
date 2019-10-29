package com.lry.mapper;

import com.lry.bean.Msg;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/10/23
 */
public interface MsgMapper {

    @Select("select * from  msg where is_delete=0 and id = (select max(id) from msg where (is_delete=0 and (send_name=#{sendName} and receive_name=#{receiveName})or(send_name=#{receiveName} and receive_name=#{sendName})) )")
    @Results(id="msgMap", value={
            @Result(column="id", property="id", id=true),
            @Result(column="msg", property="msg"),
            @Result(column="send_name", property="sendName"),
            @Result(column="receive_name", property="receiveName"),
            @Result(column="send_time", property="sendTime"),
            @Result(column="is_delete", property="isDelete")
    })
    Msg getLastMsg(@Param("sendName")String sendName,@Param("receiveName")String receiveName);//得到a发给b的最后一条消息

    @Select("select * from msg where is_delete = 0 and ((send_name=#{sendName} and receive_name=#{receiveName})or(send_name=#{receiveName} and receive_name=#{sendName}))")
    @ResultMap("msgMap")
    List<Msg>  getMsgList(@Param("sendName")String sendName,@Param("receiveName")String receiveName);//得到a发给b的所有消息

    @Insert( "insert into msg(id, msg, send_name, receive_name, send_time) values(#{id}, #{msg}, #{sendName}, #{receiveName}, #{sendTime, jdbcType=TIMESTAMP})")
    int saveMsg(Msg msg);//保存一条消息

    @Select("select * from msg where is_delete=0 and msg=#{msg} and receive_name=#{receiveName}")
    @ResultMap("msgMap")
    List<Msg> getFriendAskMsg(@Param("msg")String msg, @Param("receiveName")String receiveName);

    @Select("select * from msg where is_delete=0 and msg=#{msg} and receive_name=#{receiveName} and send_name=#{sendName}")
    @ResultMap("msgMap")
    Msg getAskMsg(@Param("msg")String msg, @Param("sendName")String sendName,@Param("receiveName")String receiveName);

    @Select("select * from msg where is_delete=0 and id=#{id}")
    @ResultMap("msgMap")
    Msg getMsg(@Param("id")int id);

    @Update("update msg set is_delete=1 where id=#{id}")
    int updateMsg(@Param("id")int id);


    @Select("select * from  msg where is_delete=0 and id = (select max(id) from msg where (is_delete=0 and  receive_name=#{receiveName}) )")
    @ResultMap("msgMap")
    Msg getGroupLastMsg(@Param("receiveName")String receiveName);

    @Select("select * from  msg where is_delete=0 and receive_name=#{receiveName}")
    @ResultMap("msgMap")
    List<Msg> getGroupMsgList(@Param("receiveName")String receiveName);
}
