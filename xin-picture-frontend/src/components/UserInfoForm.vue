<template>
  <div class="user-info-form">
    <h2 class="title" style="text-align: center;margin-bottom: 20px">编辑用户信息</h2>
    <a-form
      :model="formState"
      name="userInfo"
      autocomplete="off"
      @finish="handleSubmit"
    >
      <a-form-item
        label="用户名"
        name="userName"
        :rules="[{ required: true, message: '请输入用户名!' }]"
      >
        <a-input v-model:value="formState.userName" placeholder="请输入用户名" />
      </a-form-item>

      <a-form-item label="用户头像" name="userAvatar">
        <a-upload
          name="avatar"
          list-type="picture-card"
          class="avatar-uploader"
          :show-upload-list="false"
          :before-upload="beforeUpload"
          @change="handleChange"
        >
          <img v-if="formState.userAvatar" :src="formState.userAvatar" alt="avatar" style="width: 100%" />
          <div v-else>
            <plus-outlined />
            <div class="ant-upload-text">上传</div>
          </div>
        </a-upload>
      </a-form-item>

      <a-form-item
        label="用户简介"
        name="userProfile"
      >
        <a-textarea v-model:value="formState.userProfile" placeholder="请输入用户简介" :rows="4" />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 11, span: 8 }">
        <a-button type="primary" html-type="submit">保存</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, defineEmits } from 'vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { modifyUserUsingPost, updateUserUsingPost } from '@/api/userController.ts'
const loginUserStore = useLoginUserStore();

const emits = defineEmits(['close']);

interface UserInfoFormState {
  id?: number;
  userName?: string;
  userAvatar?: string;
  userProfile?: string;
}

const formState = reactive<UserInfoFormState>({
  id:loginUserStore.loginUser.id,
  userName: loginUserStore.loginUser.userName,
  userAvatar: loginUserStore.loginUser.userAvatar,
  userProfile: loginUserStore.loginUser.userProfile,
});

const handleSubmit = async(values: UserInfoFormState) => {
  const res = await modifyUserUsingPost(formState);
  if (res.data.code === 0 && res.data.data) {
    await loginUserStore.fetchLoginUser();
    message.success('修改用户信息成功');
    emits('close'); // Trigger the close event
  }else {
    message.error(res.data.message);
  }
};

const beforeUpload = (file: File) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    message.error('只能上传 JPG/PNG 格式的图片!');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    message.error('图片大小不能超过 2MB!');
  }
  return isJpgOrPng && isLt2M;
};

const getBase64 = (img: File, callback: (imageUrl: string) => void) => {
  const reader = new FileReader();
  reader.addEventListener('load', () => callback(reader.result as string));
  reader.readAsDataURL(img);
};

const handleChange = (info: any) => {
  if (info.file.status === 'uploading') {
    // Handle uploading state if needed
    return;
  }
  if (info.file.status === 'done') {
    // Get this url from response in real world.
    getBase64(info.file.originFileObj, (imageUrl: string) => {
      formState.userAvatar = imageUrl;
    });
  }
  if (info.file.status === 'error') {
    message.error('图片上传失败!');
  }
};
</script>

<style scoped>


.avatar-uploader > .ant-upload {
  width: 128px;
  height: 128px;
}

.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
