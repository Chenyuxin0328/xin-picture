import { ref } from 'vue'
import { defineStore } from 'pinia'
import { getLoginUserUsingGet } from '@/api/userController.ts'

/**
 * 存储登录用户信息的状态
 */
export const useLoginUserStore = defineStore('loginUser', () => {
  const loginUser = ref<API.UserLoginVo>({

  })
  const isLoading = ref(true); // 新增 loading 状态

  /**
   * 远程获取登录用户信息
   */
  async function fetchLoginUser() {
    isLoading.value = true; // 开始加载
    try {
      const res = await getLoginUserUsingGet();
      if (res.data.code === 0 && res.data.data) {
        loginUser.value = res.data.data;
      }
    } finally {
      isLoading.value = false; // 结束加载
    }
  }

  /**
   * 设置登录用户
   * @param newLoginUser
   */
  function setLoginUser(newLoginUser: any) {
    loginUser.value = newLoginUser;
  }

  // 返回
  return { loginUser, fetchLoginUser, setLoginUser, isLoading };
})
