<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
    <style>

    </style>
</head>
<body>
<div id="app" style="background-color:whitesmoke;width:100%;height:1000px">
    <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b">
        <el-menu-item index="1" style="margin-left:300px">处理中心</el-menu-item>
        <el-menu-item index="2">我的工作台</el-menu-item>
        <el-menu-item index="3" >消息中心</el-menu-item>
        <el-menu-item index="4">订单管理</el-menu-item>

        <div v-show="!userId" style="margin-top:10px; margin-right:350px;float:right">
            <el-button type="text" style="color:white" @click="dialogFormVisible = true">登录</el-button>
            <el-button type="text" style="color:white" @click="registerDialog = true">注册</el-button>
        </div>
        <div  v-show="userId" style="margin-top:30px; margin-right:300px;float:right" @mouseover="msgenter" @mouseleave="msgleave" @click="msgclick">
            <el-image style="width: 20px; height: 20px;" :src="msgUrl" fit="fit">
            </el-image>
        </div>

        <div v-show="userId"  style="margin-top:11px; margin-right:50px;float:right">
            <el-dropdown>
                <el-avatar style="background-color:#ffd04b" >{{username}}</el-avatar>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>个人主页</el-dropdown-item>
                    <el-dropdown-item>设置</el-dropdown-item>
                    <el-dropdown-item>帮助</el-dropdown-item>
                    <el-dropdown-item>退出</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>

    </el-menu>

    <el-card style="background-color: white; margin-top:20px;margin-left:300px;margin-right:300px;height:87%;padding: 20px" >
        内容
    </el-card>

    <el-dialog title="登录 LOGIN" :visible.sync="dialogFormVisible" width="20%">
        <el-form :model="loginForm" :rules="loginRules" ref="loginForm">

            <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
                <el-input placeholder="请输入用户名" v-model="loginForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
                <el-input placeholder="请输入密码" v-model="loginForm.password" show-password></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="resetLoginForm('loginForm')">取 消</el-button>
            <el-button type="primary" @click="submitLoginForm('loginForm')">确 定</el-button>
        </div>
    </el-dialog>


    <el-dialog title="注册 REGISTER" :visible.sync="registerDialog" width="20%">

        <el-form :model="registerForm" :rules="loginRules" ref="registerForm">
            <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
                <el-input placeholder="请输入用户名" v-model="registerForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
                <el-input placeholder="请输入密码" v-model="registerForm.password" show-password></el-input>
            </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
            <el-button @click="resetRegisterForm('registerForm')">取 消</el-button>
            <el-button type="primary" @click="submitRegisterForm('registerForm')">确 定</el-button>
        </div>

    </el-dialog>


    <el-drawer
            title="聊天室"
            :visible.sync="showMsg"
            direction="rtl"
            size="20%">
            <div style="padding: 20px;background-color: beige">
                <el-avatar style="background-color:#ffd04b" >{{username}}</el-avatar>
                <el-image style="width: 20px; height: 20px;" :src="msgUrl" fit="fit" @click="queryFriendAsk">
                </el-image>
            </div>
                <div style="height: 40px;">
                <el-select
                        v-model="searchContent"
                        multiple
                        filterable
                        remote
                        reserve-keyword
                        placeholder="搜索"
                        :remote-method="search"
                        :loading="loading"
                        style="width:70%;">
                    <el-option
                            v-for="item in searchData"
                            :key="item"
                            :label="item"
                            :value="item">
                    </el-option>
                </el-select>
                <el-button style="width:25%" size="medium" type="success" @click="addFriend">添加好友</el-button>
                </div>
        <el-table
                :data="tableData"
                style="width: 100%"
                @cell-dblclick="chat">

            <el-table-column  label="好友" width="100px">
                <template slot-scope="scope">
                    <el-avatar style="background-color:#ff0000;" >{{scope.row.username}}</el-avatar>
                </template>
            </el-table-column>

            <el-table-column prop="msg" label="消息" width="270px">
            </el-table-column>
        </el-table>
    </el-drawer>

    <el-dialog :title="chatFriend" :visible.sync="showChat" width="30%">
        <div  style="overflow:auto;height: 400px">
            <div v-for="msg in msgData" style="margin-bottom: 50px;">
                <div v-if="msg.username!=username" style="">
                    <el-avatar style="background-color:#409EFF;" >{{msg.username }}</el-avatar>
                    <div style="width: 50%; display: inline-block">
                        {{ msg.msg }}
                    </div>
                </div>
                <div v-else style=" width:100%;">
                    <div style="width: 30%;margin-left: 320px;display: inline-block">
                        {{ msg.msg }}
                    </div>
                    <el-avatar style="background-color:#67c23a;float:right" >{{msg.username }}</el-avatar>
                </div>
            </div>
        </div>
    <el-input
            style="width: 100%;"
            type="textarea"
            :rows="3"
            v-model="msgContent"
            placeholder="请输入内容">
    </el-input>
    <el-button style="float:right" type="success" size="mini" plain @click="send">发&nbsp送</el-button>
    </el-dialog>


    <el-dialog title="请求添加好友列表" :visible.sync="showFriendAsk" width="20%">
        <div  style="overflow:auto;height: 400px">
            <div v-for="friendAsk in friendAskData" style="margin-bottom: 20px;">
                {{friendAsk.sendName}} {{friendAsk.msg}}
                <el-button size="mini" @click="accept(friendAsk.id,friendAsk.sendName)">接受</el-button>
                <el-button size="mini" @click="reject(friendAsk.id)">拒绝</el-button>
            </div>
        </div>
    </el-dialog>

</div>
</body>
<script src="https://unpkg.com/vue/dist/vue.js"></script>
<script src="https://unpkg.com/element-ui/lib/index.js"></script>
<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/sockjs-client/1.1.1/sockjs.js"></script>
<script type="text/javascript">
    var vue = new Vue({
        el: '#app',
        data:{
            registerDialog:false,
            searchContent:"",
            websocket:"",
            chatFriendId:'',
            msgContent:'',
            friendAskData:'',
            msgData: [],
            tableData: [],
            searchData:[],
            showMsg:false,
            msgUrl:"./img/msg.png",
            userId:null,
            username:null,
            activeIndex: '1',
            dialogFormVisible:false,
            showChat:false,
            showFriendAsk:false,
            chatFriend:'',
            loginForm: {
                username: '',
                password:''
            },
            registerForm: {
                username: '',
                password:''
            },
            formLabelWidth: '70px',
            loginRules: {
                username: [
                    {required: true, message: '请输入用户名', trigger: 'blur'},
                    {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
                ],
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'},
                    {min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: 'blur'}
                ],
            },
        },

        methods: {
            init:function(){
                if ('WebSocket' in window) {
                    vue.websocket = new WebSocket("ws://192.168.31.25:9090/NoneConfig_war_exploded/ws/server");
                }
                else if ('MozWebSocket' in window) {
                    vue.websocket = new MozWebSocket("ws://192.168.31.25:9090/NoneConfig_war_exploded/ws/server");
                }
                else {//轮询处理
                    vue.websocket = new SockJS("http://192.168.31.25:9090/NoneConfig_war_exploded/sockjs/server");
                }
                vue.websocket.onmessage = this.onMessage;
            },
            search:function(query){
                $.ajax({
                    type: "POST",
                    url: "./queryNotMyFriend?queryName="+query+"&myName="+vue.username,
                    contentType: "application/json",
                    success: function (res) {
                        if(res.code==0){
                            vue.searchData = res.data;
                        }
                    }
                });
            },
            removeFromFriendAsk:function(id){
                for (var i = 0; i < vue.friendAskData.length; i++) {
                    if(vue.friendAskData[i].id==id){
                        vue.friendAskData.splice(i,1);
                    }
                }
            },
            reject:function(id){
                $.ajax({
                    type: "POST",
                    url: "./reject?id="+id,
                    contentType: "application/json",
                    success: function (res) {
                        if(res.code==0){
                            vue.removeFromFriendAsk(id);
                        }else{
                            //vue.msgTip(res.msg);
                            vue.$message.success(res.msg);
                        }
                    }
                });
            },

            msgTip:function(content){
                this.$alert(content, '消息提示',
                    {
                        confirmButtonText: '确定'
                    });
            },
            accept:function(id,sendName){
                $.ajax({
                    type: "POST",
                    url: "./accept?id="+id,
                    contentType: "application/json",
                    success: function (res) {
                        if(res.code==0){
                            vue.removeFromFriendAsk(id);
                            vue.tableData.push({username:sendName});
                        }else{
                            vue.$message.success(res.msg);
                            //vue.msgTip(res.msg);
                        }
                    }
                });
            },
            queryFriendAsk:function(){
                vue.showFriendAsk = true;
                $.ajax({
                    type: "POST",
                    url: "./queryFriendAsk?receiveName="+vue.username,
                    contentType: "application/json",
                    success: function (res) {
                        if(res.code==0){
                            vue.friendAskData = res.data;
                        }else{
                            //vue.msgTip(res.msg);
                            vue.$message.success(res.msg);
                        }
                    }
                });
            },
            addFriend:function(){
                $.ajax({
                    type: "POST",
                    url: "./addFriend?friendName="+vue.searchContent+"&myName="+vue.username,
                    contentType: "application/json",
                    success: function (res) {
                        if(res.code==0){
                            vue.$message.success(res.data);
                            // vue.msgTip(res.data);
                            vue.searchContent = "";
                        }else{
                            vue.$message.success(res.msg);
                            //vue.msgTip(res.msg);
                            //(res.msg)
                        }
                    }
                });
            },
            send:function () {
                    if (vue.websocket.readyState == vue.websocket.OPEN) {
                        vue.websocket.send(vue.username+":"+vue.chatFriend+":"+vue.msgContent);//调用后台handleTextMessage方法

                        vue.msgData.push({username:vue.username,msg: vue.msgContent});

                        vue.tableData.forEach(item=>{
                            if(item.username==vue.chatFriend){
                            item.msg = vue.msgContent;
                            }
                        });
                        vue.msgContent = "";
                    } else {
                        vue.$message.success("连接websocket失败!");
                        //vue.msgTip("连接websocket失败!");
                    }
            },
            onMessage:function (evt) {
                var sendName = evt.data.split(":")[0];
                var receiveName = evt.data.split(":")[1];
                var msg = evt.data.split(":")[2];
                if(receiveName=='all'){
                    vue.tableData.forEach(item=>{
                        if(item.username==receiveName){
                            item.msg = msg;
                        }
                    });
                    if(receiveName==vue.chatFriend){
                        vue.msgData.push({username:sendName,msg:msg});
                    }
                }else{
                    vue.tableData.forEach(item=>{
                        if(item.username==sendName){
                            item.msg = msg;
                        }
                    });
                    if(receiveName==vue.username){
                        vue.msgData.push({username:sendName,msg:msg});
                    }
                }
            },

            handleSelect:function(key, keyPath) {
                console.log(key, keyPath);
            },
            chat:function(row){
                vue.chatFriendId = row.id;
                $.ajax({
                    type: "POST",
                    url: "./chatMsg?friendName="+row.username+"&myName="+vue.username,
                    contentType: "application/json",
                    success: function (res) {
                        if(res.code==0){
                            res.data.forEach(item => {
                                if(item.sendName==vue.username){
                                    item.username = vue.username;
                                }else{
                                    item.username = item.sendName;
                                    //item.username = vue.chatFriend;
                                }
                            });
                            vue.msgData = res.data;
                        }
                    }
                });

                vue.chatFriend = row.username;
                vue.showChat = true;
            },
            msgenter:function(){
                vue.msgUrl =  "./img/select_msg.png";

            },
            msgleave:function(){
                vue.msgUrl = "./img/msg.png";
            },
            msgclick:function(){
                $.ajax({
                    type: "POST",
                    url: "./friendsAndMsg?id="+vue.userId,
                    contentType: "application/json",
                    success: function (res) {
                        if(res.code==0){
                            vue.tableData = res.data;
                        }
                    }
                });
                vue.showMsg = true;
            },
            submitLoginForm:function(formName) {
                this.$refs[formName].validate(function(valid){
                    if(valid) {
                        //访问接口
                        $.ajax({
                            type: "POST",
                            url: "./login",
                            contentType: "application/json",
                            data: JSON.stringify(vue.loginForm),
                            success: function (res) {
                                //成功
                                if(res.code==0){
                                    vue.dialogFormVisible = false;
                                    vue.userId = res.data.id;
                                    vue.username = res.data.username;
                                    vue.$message.success('登录成功');
                                    vue.init();
                                }
                                //失败
                                else{
                                    vue.$message.error(res.msg);
                                }
                            }
                        });
                    }else{
                        return false;
                    }
                 });
            },
            submitRegisterForm:function(formName) {
                this.$refs[formName].validate(function(valid){
                    if(valid) {
                        //访问接口
                        $.ajax({
                            type: "POST",
                            url: "./register",
                            contentType: "application/json",
                            data: JSON.stringify(vue.registerForm),
                            success: function (res) {
                                //成功
                                if(res.code==0){
                                    vue.registerDialog = false;
                                    vue.$message.success('注册成功');
                                }
                                //失败
                                else{
                                    vue.$message.error(res.msg);
                                }
                            }
                        });
                    }else{
                        return false;
                    }
                });
            },
            resetLoginForm:function(formName) {
                vue.dialogFormVisible = false;
                this.$refs[formName].resetFields();
            },
            resetRegisterForm:function(formName) {
                vue.registerDialog = false;
                this.$refs[formName].resetFields();
            }
        }
    });
</script>
</html>