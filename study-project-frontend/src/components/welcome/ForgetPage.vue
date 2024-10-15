<template>
    <div style="text-align: center;margin:0 20px;">
        <div style="margin-top: 150px;">
            <div style="font-size: 25px;">
                重置密码
            </div>
            <div style="font-size: 14px;color: gray;">
                请输入重置密码的电子邮件地址
            </div>
        </div>
        <div style="margin-top: 50px;">
            <el-form :model="form" :rules="rules" label-width="10px" :inline="false" size="default"
                @validate="onValidateEmail" ref="formRef">
                <el-form-item label="" size="default" prop="email">
                    <el-input v-model="form.email" placeholder="电子邮箱地址" :prefix-Icon="Message" size="default" clearable
                        type="email" style="margin-top: 0px;"></el-input>
                </el-form-item>

                <el-form-item label="" size="default">
                    <el-row :gutter="10" style="width: 100%;">
                        <el-col :span="17" :offset="0">
                            <el-input v-model="form.code" placeholder="请输入电子邮箱验证码" :prefix-icon="EditPen" size="default"
                                clearable type="text" style="margin-top: 0px;">
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
    </div>
</template>

<script setup>
import router from '@/router';
import { User, Lock, Message, EditPen } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { reactive } from 'vue';
import { post } from '@/net';
import { ref } from 'vue';

const rules = {
    email: [
        { required: true, message: '邮箱不能为空', trigger: ['blur', 'change'] },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] },
    ],
    code: [
        { required: true, message: '验证码不能为空', trigger: ['blur', 'change'] },
    ],
}
const form = reactive({
    username: '',
    code: '',
    password: '',
    email: '',
    password_repeat: '',
})

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

<style scoped></style>