<template>
    <div style="text-align: center;margin:0 20px;">
        <div style="margin-top: 150px;">
            <div style="font-size: 25px;">
                登录
            </div>
            <div style="font-size: 14px;color: gray;">
                在进入系统之前请先登录
            </div>
        </div>
        <div style="width: 370px;margin-top: 50px;">
            <el-input v-model="form.username" style="margin-top: 10px;" placeholder="用户名/邮箱" :prefix-icon="User">
                <!-- <template #prefix>
              <el-icon>
                <User />
              </el-icon>
            </template> -->
            </el-input>
            <el-input v-model="form.password" style="margin-top: 10px;" placeholder="密码" :prefixIcon="Lock">

            </el-input>
        </div>
        <el-row :gutter="0">
            <el-col :span="12" :offset="0" style="text-align: left;">
                <el-checkbox v-model="form.remember" label="记住我" :indeterminate="false"></el-checkbox>
            </el-col>
            <el-col :span="12" :offset="0" style="text-align: right;">
                <el-link type="info" :underline="true" href="" target="_blank">忘记密码</el-link>
            </el-col>
        </el-row>

        <div>
            <el-button type="success" size="default" plain style="margin-top: 40px;width: 270px"
                @click="login()">立即登录</el-button>
            <el-divider direction="horizontal" content-position="center">
                <span style="font-size: 13px;color: grey;">没有账号</span>
            </el-divider>
            <el-button type="warning" size="default" plain style="margin-top: 5px;width: 270px;">注册账号</el-button>
        </div>

    </div>
</template>

<script setup>
import router from '@/router';
import { User, Lock } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { reactive } from 'vue';
import { post } from '@/net';

const form = reactive({
    username: '',
    password: '',
    remember: false
})
const login = () => {
    if (!form.username || !form.password) {
        ElMessage.warning('请输入用户名和密码');
        return;
    } else {
        post('/api/auth/login', {
            username: form.username,
            password: form.password,
            remember: form.remember
        }, (message) => {
            ElMessage.success(message);
            router.push('/index')
        })
    }
    console.log(form)
}
</script>