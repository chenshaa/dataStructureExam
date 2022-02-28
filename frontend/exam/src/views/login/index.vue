<template>
    <div class="login" id="login">
        <a href="javascript:;" class="log-close"><i class="icons close"></i></a>
        <div class="log-bg">

            <div class="log-logo">欢迎!</div>

        </div>
        <div class="log-form">

            <el-form ref="userForm">
                <el-form-item prop="account" class="login-input">
                    <el-input placeholder="用户名" v-model="account"></el-input>
                </el-form-item>

                <el-form-item prop="password" class="login-input">
                    <el-input placeholder="密码" type="password" v-model="password"></el-input>
                </el-form-item>

                <el-form-item class="login-button">
                    <el-button class="loginBtn" type="primary" @click.native.prevent="loginBtn()">登录</el-button>
                </el-form-item>

            </el-form>

        </div>

    </div>
</template>

<script>
    import {
        loginApi
    } from '@/api/user.js'

    export default {
        name: 'Login',
        data() {
            return {
                isLoging: false,
                account: '',
                password: ''
            }
        },
        methods: {

            loginBtn() {
                let that = this

                loginApi({
                    account: that.account,
                    password: that.password
                }).then(function(res) {
                    if (res.data.code == 200) {
                        //登录成功
                        localStorage.setItem("account", res.data.data.account);
                        localStorage.setItem("nickname", res.data.data.nickname);
                        localStorage.setItem("token", res.data.data.token);

                        that.$router.push({
                            path: '/admin'
                        });
                    } else {
                        //登录失败
                        that.$message({
                            message: res.data.msg,
                            type: 'error',
                            showClose: true
                        });
                    }
                });
            }
        },

    }
</script>

<style>
    .log-form {
        margin-top: 20px;
    }

    .loginBtn {
        color: #fff;
        width: 370px;
        margin: 0 auto 15px;
    }

    .login-input {
        width: 370px;
        margin: 0 auto 15px;
    }

    .login {
        position: fixed;
        overflow: hidden;
        left: 50%;
        margin-left: -250px;
        top: 50%;
        margin-top: -350px;
        width: 500px;
        min-height: 555px;
        z-index: 10;
        right: 140px;
        background: #fff;
        -webkit-border-radius: 5px;
        -moz-border-radius: 5px;
        -ms-border-radius: 5px;
        -o-border-radius: 5px;
        border-radius: 5px;
        -webkit-box-shadow: 0px 3px 16px -5px #070707;
        box-shadow: 0px 3px 16px -5px #070707
    }

    .log-close {
        display: block;
        position: absolute;
        top: 12px;
        right: 12px;
        opacity: 1;
    }

    .log-close:hover .icons {
        transform: rotate(180deg);
    }

    .log-close .icons {
        opacity: 1;
        transition: all .3s
    }

    .log-bg {
        background: url(../../images/login-bg.jpg);
        width: 100%;
        height: 312px;
        overflow: hidden;
    }

    .log-logo {
        height: 80px;
        margin: 120px auto 25px;
        text-align: center;
        color: #1fcab3;
        font-weight: bold;
        font-size: 40px;
    }

    .log-text {
        color: #57d4c3;
        font-size: 13px;
        text-align: center;
        margin: 0 auto;
    }

    .log-logo,
    .log-text {
        z-index: 2
    }

    .icons {
        background: url(../../images/icons.png) no-repeat;
        display: inline-block;
    }

    .close {
        height: 16px;
        width: 16px;
        background-position: -13px 0;
    }
</style>
