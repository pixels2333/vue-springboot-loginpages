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
        <el-form :model="form" :rules="rules" label-width="10px" :inline="false" size="default"
          @validate="onValidateEmail" ref="formRef">
          <el-form-item label="" prop="username">
            <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" size="default" clearable
              type="text"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" placeholder="密码" :prefix-icon="Lock" size="default" clearable
              type="password" style="margin-top: 0px;"></el-input>
          </el-form-item>
          <el-form-item label="" size="default" prop="password_repeat">
            <el-input v-model="form.password_repeat" placeholder="确认密码" :prefix-icon="Lock" size="default" clearable
              type="password" style="margin-top: 0px;"></el-input>
          </el-form-item>
          <el-form-item label="" size="default" prop="email">
            <el-input v-model="form.email" placeholder="电子邮箱地址" :prefix-Icon="Message" size="default" clearable
              type="email" style="margin-top: 0px;"></el-input>
          </el-form-item>
          <el-form-item label="" size="default">
            <el-row :gutter="10" style="width: 100%;">
              <el-col :span="17" :offset="0">
                <el-input v-model="form.code" placeholder="请输入电子邮箱验证码" :prefix-icon="EditPen" size="default" clearable
                  type="text"  style="margin-top: 0px;">
                </el-input>
              </el-col>
              <el-col :span="7" :offset="0">
                <el-button type="success" size="default" :disabled="!isEmailValid || coldTime > 0"
                  @click="validateEmail">
                  {{ coldTime > 0 ? "请稍后" + coldTime + '秒' : '获取验证码' }}
                </el-button>
              </el-col>

            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 100px;">
        <el-button type="warning" style="width: 270px;" size="default" @click="register" plain>立即注册</el-button>
      </div>
      <div style="margin-top: 15px;color: gray;">
        已有账号?
        <el-link type="primary" :underline="false" @click="router.push('/')" href="" target="_blank"
          style="translate: 0 -2px;">立即登录</el-link>
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
import { ref } from 'vue';


const form = reactive({
  username: '',
  code: '',
  password: '',
  email: '',
  password_repeat: '',
})

const validateUsername = (rules, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  }
  else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名只能由中文，英文，数字组成'))
  }
  else {
    callback()
  }
}

const validatePassword = (rules, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  }
  else if (value !== form.password) {
    callback(new Error('两次密码输入不一致'))
  }
  else {
    callback()
  }
}

const rules = {
  username: [
    { validator: validateUsername, trigger: ['blur', 'change'] },
    { min: 3, max: 18, message: '长度在 3 到 18 个字符', trigger: ['blur', 'change'] },
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: ['blur', 'change'] },
    { min: 6, max: 18, message: '长度在 6 到 18 个字符', trigger: ['blur', 'change'] },
  ],
  password_repeat: [
    { validator: validatePassword, trigger: ['blur', 'change'] },
    { required: true, message: '确认密码不能为空', trigger: ['blur', 'change'] },
  ],
  email: [
    { required: true, message: '邮箱不能为空', trigger: ['blur', 'change'] },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] },
  ],
  code: [
    { required: true, message: '验证码不能为空', trigger: ['blur', 'change'] },
  ],
}


const formRef = ref()

const isEmailValid = ref(false)//邮箱是否合法
const coldTime = ref(0)//倒计时

const onValidateEmail = (prop, isValid) => {
  if (prop === 'email')//邮箱验证
    isEmailValid.value = isValid
}

const register = () => {
  // ElMessage.info('测试')
  formRef.value.validate(isValid => {
    if (isValid) {
      post('/api/auth/register', form, message => ElMessage.success(message))
      router.push('/')
    }
    else {
      ElMessage.error('请检查输入是否正确')
    }
  })
}

const validateEmail = () => {
  post('/api/auth/validate-email', { email: form.email }, (message) => {
    ElMessage.success(message)
    coldTime.value = 60
    setInterval(() => { coldTime.value--, 1000 })
    
  }

  )



}
</script>


<style></style>