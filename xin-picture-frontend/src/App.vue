<template>
  <div id="app">
    <a-config-provider :locale="zhCN">
      <BasicLayout/>
    </a-config-provider>
  </div>
</template>
<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import { healthUsingGet } from '@/api/testController.ts'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
const loginUserStore = useLoginUserStore();
import zhCN from 'ant-design-vue/es/locale/zh_CN';

loginUserStore.fetchLoginUser();
healthUsingGet().then((res)=>{
  console.log(res)
})
</script>
