<template>
    <div>
        <div>
            欢迎进入到学习平台
        </div>
        <div>
            <el-button @click="logout()" type="danger" size="default" plain>退出登录</el-button>

        </div>
    </div>

</template>

<script setup>
import { get } from '@/net';
import { ElMessage } from 'element-plus';
// import router from '@/router';
import { useRouter } from 'vue-router';
const logout = () => {
    get('/api/auth/logout', (message) => {
        ElMessage.success(message)
        router.push('/')// 跳转到登录页面
    })
}
</script>

