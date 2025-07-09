<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="200px">
        <RouterLink to="/">
          <div class="title-bar">
            <img class="logo" src="../assets/favicon.png" alt="logo" />
            <div class="title">新新云图库</div>
          </div>
        </RouterLink>
      </a-col>
      <a-col flex="auto">
        <a-menu v-model:selectedKeys="current" mode="horizontal" :items="items" @click="doMenuClick" />
      </a-col>
      <a-col flex="120px">
        <div v-if="!loginUserStore.isLoading" class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            <a-dropdown>
              <a-space>
                <a-avatar v-if="!loginUserStore.loginUser.userAvatar" style="color: #f56a00; background-color: #fde3cf">
                  {{loginUserStore.loginUser.userName?.charAt(0)}}
                </a-avatar>
                <a-avatar v-else :src="loginUserStore.loginUser.userAvatar" />
                {{ loginUserStore.loginUser.userName ?? '新用户' }}
              </a-space>
              <template #overlay>
                <a-menu>
                  <a-menu-item @click="showUserInfoModal">
                    <FormOutlined style="margin-right: 2px"/>
                    编辑信息
                  </a-menu-item>
                  <a-menu-item @click="doLogout">
                    <LogoutOutlined style="margin-right: 2px"/>
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
  <a-modal
    v-model:open="isUserInfoModalVisible"
    title="编辑用户信息"
    :footer="null"
    width="700px"
    @cancel="handleUserInfoModalCancel"
  >
    <UserInfoForm @close="handleUserInfoModalCancel" />
  </a-modal>
</template>
<script lang="ts" setup>
import { computed, h, ref } from 'vue'
import { MailOutlined, AppstoreOutlined, SettingOutlined } from '@ant-design/icons-vue'
import { type MenuProps, message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { userLogoutUsingGet } from '@/api/userController.ts'
import {  LogoutOutlined, FormOutlined } from '@ant-design/icons-vue'
import UserInfoForm from './UserInfoForm.vue'
// 用户登录信息
const loginUserStore = useLoginUserStore();
const originItems = [
  {
    key: '/',
    icon: () => h(MailOutlined),
    label: '首页',
    title: '首页',
  },
  {
    key: '/admin/userManage',
    icon: () => h(AppstoreOutlined),
    label: '用户管理',
    title: '用户管理',
  },
  {
    key: 'others',
    label: h('a', { href: 'http://code-life.chenyuxin0328.cn', target: '_blank' }, 'code&life'),
    title: 'code&life',
  },
]
// 根据权限过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    // 管理员才能看到 /admin 开头的菜单
    if (typeof menu?.key=='string' && menu?.key?.startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser;
      if (!loginUser || loginUser.userRole != 'admin') {
        return false
      }
    }
    return true
  })
}

// 展示在菜单的路由数组
const items = computed(() => filterMenus(originItems))

const router = useRouter();
// 路由跳转事件
const doMenuClick = ({key}:{key:string}) => {
  router.push({
    path: key
  })
}
// 用户注销
const doLogout = async () => {
  const res = await userLogoutUsingGet();
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}

const current = ref<string[]>([])
// 监听路由变化，更新菜单变化项
router.afterEach((to,from,next)=>{
  current.value = [to.path];
})

// 修改用户信息表单
const isUserInfoModalVisible = ref(false);

const showUserInfoModal = () => {
  isUserInfoModalVisible.value = true;
};

const handleUserInfoModalCancel = () => {
  isUserInfoModalVisible.value = false;
};
</script>
<style scoped>
#basicLayout .title-bar {
  display: flex;
  align-items: center;
}

.logo {
  height: 45px;
}

.title {
  color: black;
  margin-left: 20px;
  font-size: 20px;
}

.head{
  width: 40px;
  margin-right: 10px;
}
</style>
