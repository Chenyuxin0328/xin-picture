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
      <a-col flex="100px">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            {{loginUserStore.loginUser.name ?? "xxx"}}
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts" setup>
import { h, ref } from 'vue'
import { MailOutlined, AppstoreOutlined, SettingOutlined } from '@ant-design/icons-vue'
import  type { MenuProps } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'

const items = ref<MenuProps['items']>([
  {
    key: '/',
    icon: () => h(MailOutlined),
    label: '首页',
    title: '首页',
  },
  {
    key: '/about',
    icon: () => h(AppstoreOutlined),
    label: '关于',
    title: '关于',
  },
  {
    key: 'others',
    label: h('a', { href: 'http://code-life.chenyuxin0328.cn', target: '_blank' }, 'code&life'),
    title: 'code&life',
  },
])
const router = useRouter();
// 路由跳转事件
const doMenuClick = ({key}:{key:string}) => {
  router.push({
    path: key
  })
}

const current = ref<string[]>([])
// 监听路由变化，更新菜单变化项
router.afterEach((to,from,next)=>{
  current.value = [to.path];
})
// 用户登录信息
const loginUserStore = useLoginUserStore();
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
</style>
