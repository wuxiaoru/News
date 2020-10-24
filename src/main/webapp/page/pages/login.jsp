<%--
  Created by IntelliJ IDEA.
  User: stl
  Date: 2018/1/19
  Time: 13:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>用户登录</title>
    <%@ include file="global/mylinks.jsp" %>
    <style>
        * {
            margin: 0;
            padding: 0;
            list-style-type: none;
        }

        header {
            margin: 0 auto;
            color: white;
            width: 100%;
            height: 100px;
            line-height: 100px;
            text-align: center; /*文本居中显示*/

        }

        header h1 {
            font-size: 25px;
        }

        .login-body {
            width: 300px;
            height: 100px;
            line-height: 100px; /*line-height和height搭配使用，让文本居中*/
            text-align: center; /*文本居中显示*/
            margin: 100px auto; /*盒子居中*/
        }

        .login-body * {
            margin-bottom: 10px;
        }

    </style>
</head>
<%--<%--%>
<%--    if(request.getSession().getAttribute(UserController.USER_SESSION_NAME)!=null){--%>
<%--        //如果用户已经登录，则移除登录信息--%>
<%--        request.getSession().removeAttribute(UserController.USER_SESSION_NAME);--%>
<%--    }--%>
<%--%>--%>
<body>
<header class="layui-bg-green"><h1>用户登录</h1></header>
<div class="login-body">
    <form action="#" method="post">
        <input type="text" id="input_username" required lay-verify="required" placeholder="请输入用户名" autocomplete="off"
               class="layui-input">
        <input type="password" id="input_password" required lay-verify="required" placeholder="请输入密码" autocomplete="off"
               class="layui-input">
    </form>
    <button id="btn_login" class="layui-btn layui-bg-green">登录</button>
    <button id="btn_reset" class="layui-btn layui-btn-primary">重置</button>
</div>
<script language="javascript">
    //显示消息
    function showMessage(msg) {
        layer.msg(msg);
    }

    //检查输入内容
    function checkInput(operCode, operPwd) {
        if (0 == operCode.length) {
            showMessage("请输入用户名");
            $("#input_username").focus();
            return false;
        }
        if (0 == operPwd.length) {
            showMessage("请输入密码");
            $("#input_password").focus();
            return;
        }
        return true;
    }

    //请求成功后回调方法
    function onSuccess(data) {
        layer.close(index);
        //在这里对服务器返回的JSON数据进行处理，可通过 “对象名.属性名”获取对应的属性
        showMessage(data.message);
        if (200 == data.state) {
            $(window).attr('location', '${ctx}/home');
        } else {
            $("#input_username").val("");
            $("#input_password").val("");
            $("#input_username").focus();
        }
    }

    //请求失败后回调方法
    function onError(msg) {
        showMessage(msg);
    }

    var index;

    //执行登录
    function login() {
        var username = $("#input_username").val();
        var password = $("#input_password").val();
        if (checkInput(username, password)) {
            index = layer.load(); //打开登录动画
            setTimeout(function () {
                $.ajax({
                    type: "POST",
                    url: "${ctx}/user/testLogin",   //请求URL地址
                    dataType: "json",           //服务器返回数据类型
                    data: {username: username, password: password}, //请求参数
                    success: onSuccess, //请求成功回调方法
                    error: onError      //请求失败回调方法
                });
            }, 1000);

        }
    }

    $(function () {
        //监听回车键
        $('.login-box-body').bind('keypress', function (event) {
            var theEvent = event || window.event;
            var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
            if (13 == code) {
                login();
            }
        });
        $("#btn_login").click(function () {
            login();
        });

        //清除输入的内容
        $("#btn_reset").click(function () {
            $("#input_username").val("");
            $("#input_password").val("");
            $("#input_username").focus();
        });
    });
</script>

</body>
</html>
