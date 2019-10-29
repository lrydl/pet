package com.lry.handler;

import com.lry.bean.Msg;
import com.lry.common.Common;
import com.lry.service.MsgService;
import com.lry.util.SpringContextUtil;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringWebSocketHandler extends TextWebSocketHandler{

    //Map来存储WebSocketSession，key是用户名（数据库的用户名唯一）
    private static final Map<String, WebSocketSession> users = new ConcurrentHashMap<>();

    public SpringWebSocketHandler() {}
    /**
     * 登录连接成功时候，会触发页面上onopen方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("成功建立websocket连接!");
        String username = (String) session.getAttributes().get("username");
        users.put(username,session);
        System.out.println("当前线上用户数量:"+users.size());
    }

    /**
     * 关闭连接时触发
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String username= (String) session.getAttributes().get("username");
        System.out.println("用户"+username+"已退出！");
        users.remove(username);
        System.out.println("剩余在线用户"+users.size());
    }

    /**
     * js调用websocket.send时候，会调用该方法
     * @param session 发送者session
     * @param message 发送内容  //0:发送者  1:接收者   2:内容
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        handleMsg(session,message);
    }

    private void handleMsg(WebSocketSession session,TextMessage message) throws IOException {
        String messages = message.getPayload();
        System.out.println("服务器收到消息："+messages);
        String[] strs = messages.split(":");
        //0:发送者  1:接收者   2:内容
        if(null==strs||strs.length!=3)return;

        //保存数据
        saveMsg(message,strs);
        //群聊
        if(Common.ALL.equals(strs[1])){
            for (Map.Entry<String, WebSocketSession> entry:users.entrySet()) {
                if(!strs[0].equals(entry.getKey())&&entry.getValue().isOpen()){
                    entry.getValue().sendMessage(message);
                }
            }
        }else{ //私聊
            //拿到接受者session
            WebSocketSession receiverSession = users.get(strs[1]);
            if(receiverSession!=null&&receiverSession.isOpen()){
                receiverSession.sendMessage(message);
            }
        }
    }

    private void saveMsg(TextMessage message,String[] strs){
        //保存数据到msg
        Msg msg = new Msg();
        msg.setMsg(message.getPayload());
        msg.setSendTime(new Date());
        msg.setSendName(strs[0]);
        msg.setMsg(strs[2]);
        msg.setReceiveName(strs[1]);
        MsgService msgService = (MsgService) SpringContextUtil.getBean("msgService");
        msgService.saveMsg(msg);
    }
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        System.out.println("传输出现异常，关闭websocket连接... ");
        String username= (String) session.getAttributes().get("username");
        users.remove(username);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}