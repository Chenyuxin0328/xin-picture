<template>
  <div id="userLoginPage">
    <h2 class="title">新新云图库 - 用户登录</h2>
    <div class="desc">企业级智能协同云图库</div>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 8, message: '密码长度不能小于 8 位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>
      <div class="remember-and-register-row">
        <a-form-item name="remember" :wrapper-col="{ offset: 0, span: 24 }" style="margin-bottom: 0;">
          <a-checkbox v-model:checked="formState.remember">记住我</a-checkbox>
        </a-form-item>
        <div class="tips">
          没有账号？
          <RouterLink to="/user/register">去注册</RouterLink>
        </div>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { reactive, onMounted } from 'vue'
import { userLoginUsingPost } from '@/api/userController.ts'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { message } from 'ant-design-vue'
import router from '@/router' // 用于接受表单输入的值

// 用于接受表单输入的值
const formState = reactive<API.UserLoginRequest & { remember: boolean }>({
  userAccount: '',
  userPassword: '',
  remember: false,
})

const loginUserStore = useLoginUserStore()

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async (values: any) => {
  const res = await userLoginUsingPost(values)
  // 登录成功，把登录态保存到全局状态中
  if (res.data.code === 0 && res.data.data) {
    await loginUserStore.fetchLoginUser()
    message.success('登录成功')
    // 记住账号密码逻辑
    if (formState.remember) {
      localStorage.setItem('userAccount', formState.userAccount ?? '');
      localStorage.setItem('userPassword', formState.userPassword ?? '');
    } else {
      localStorage.removeItem('userAccount');
      localStorage.removeItem('userPassword');
    }
    await router.push({
      path: '/',
      replace: true,
    })
  } else {
    message.error('登录失败，' + res.data.message)
  }
}

// 组件挂载时加载保存的账号密码
onMounted(() => {
  const savedAccount = localStorage.getItem('userAccount');
  const savedPassword = localStorage.getItem('userPassword');
  if (savedAccount && savedPassword) {
    formState.userAccount = savedAccount;
    formState.userPassword = savedPassword;
    formState.remember = true;
  }
});
</script>

<style scoped>
#userLoginPage {
  max-width: 360px;
  margin: 0 auto;
}

.title {
  text-align: center;
  margin-bottom: 16px;
}

.desc {
  text-align: center;
  color: #bbb;
  margin-bottom: 16px;
}

.tips {
  color: #bbb;
  font-size: 13px;
  margin-bottom: 16px; /* Keep this for spacing if this row is not the last form item */
}

.remember-and-register-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px; /* Adjust as needed */
}

/* Adjust checkbox alignment */
#userLoginPage .ant-form-item-control-input-content {
  display: flex;
  justify-content: center;
}
</style>
