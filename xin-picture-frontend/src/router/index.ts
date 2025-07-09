import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/admin/userManage',
      name: 'about',
      component: () => import('../page/admin/UserManagePage.vue'),
    },
    {
      path: '/user/login',
      name: 'login',
      component: () => import('../page/user/LoginPage.vue'),
    },
    {
      path: '/user/register',
      name: 'register',
      component: () => import('../page/user/RegisterPage.vue')
    },
    {
      path: '/test',
      name: 'test',
      component: () => import('../page/user/RegisterPage.vue')
    }
  ],
})

export default router
