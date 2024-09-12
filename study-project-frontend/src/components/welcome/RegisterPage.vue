<template>

  <div style="text-align: center;margin:0 20px;">
    <div style="margin-top: 100px;">
      <div style="font-size: 25px;font-weight: bold;">
        注册新用户
      </div>
      <div style="font-size: 14px;color: gray;">
        欢迎注册我们的学习新平台,请在下方填写注册信息
      </div>
      <div style="margin-top: 50px;">
        <el-input v-model="form.username" placeholder="用户名" :prefix-Icon="User" size="normal" clearable type="text"></el-input>
        <el-input v-model="form.password" placeholder="密码" :prefix-Icon="Lock" size="normal" clearable type="password"
          style="margin-top: 10px;"></el-input>
        <el-input v-model="form.password_repeat" placeholder="确认密码" :prefix-Icon="Lock" size="normal" clearable type="password"
          style="margin-top: 10px;"></el-input>
        <el-input placeholder="电子邮箱地址" :prefix-Icon="Message" size="normal" clearable type="email"
          style="margin-top: 10px;"></el-input>
        <div style="margin-top: 10px;">
          <el-row :gutter="10" style="width: 100%;">
            <el-col :span="17" :offset="0">
              <el-input v-model="form.code" placeholder="请输入电子邮箱验证码" :prefix-Icon="EditPen" size="normal" clearable
                type="text" style="margin-top: 0px;">
              </el-input>
            </el-col>
            <el-col :span="7" :offset="0">
              <el-button type="success" size="default">获取验证码</el-button>
            </el-col>

          </el-row>

        </div>
        <div style="margin-top: 100px;">
          <el-button type="warning" style="width: 270px;" size="default" @click="null" plain>立即注册</el-button>

        </div>
        <div style="margin-top: 15px;color: gray;">
          已有账号?
          <el-link type="primary" :underline="false" @click="router.push('/')" href="" target="_blank"
            style="translate: 0 -2px;">立即登录</el-link>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import router from '@/router';
import { User, Lock, Message, EditPen } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { reactive } from 'vue';
import { post } from '@/net';

const form = reactive({
  username: '',
  code: '',
  password: '',
  email: '',
  password_repeat: '',
})
</script>


<style></style>