<template>
  <div class="wrapper">
    <div
      style="
        height: 60px;
        line-height: 60px;
        font-size: 20px;
        padding-left: 50px;
        color: white;
        background-color: rgba(0, 0, 0, 0.2);
      "
    >
      Music Management System
    </div>
    <div
      style="
        margin: 150px auto;
        background-color: rgba(0, 0, 0, 0.2);
        width: 350px;
        height: 400px;
        padding: 20px;
        border-radius: 10px;
      "
    >
      <div style="margin: 20px 0; text-align: center; font-size: 24px; color: #eeeeee"><b>Login</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input
            placeholder="please input email"
            size="medium"
            prefix-icon="el-icon-folder"
            v-model="user.email"
          ></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input
            style="width: 180px; margin-right: 10px"
            placeholder="please input code"
            size="medium"
            prefix-icon="el-icon-lock"
            v-model="user.code"
          ></el-input>
          <el-button @click="getCode" :disabled="codeDisabled">{{ codeButtonText }}</el-button>
        </el-form-item>

        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')"
            >go register</el-button
          >
          <el-button type="primary" size="small" autocomplete="off" @click="login">login</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-dialog title="Retrieve password" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small">
        <el-form-item label="username">
          <el-input v-model="pass.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="phone">
          <el-input v-model="pass.phone" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">cancel</el-button>
        <el-button type="primary" @click="passwordBack">reset password</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { resetRouter, setRoutes } from '@/router';
import Identify from '@/components/Identify';

export default {
  name: 'Login',
  data() {
    return {
      codeDisabled: false,  // 控制按钮是否禁用
      codeButtonText: "get code",  // 按钮文字
      countdown: 60,  // 倒计时秒数
      timer: null,  // 定时器
      user: {},
      pass: {},
      code: '',
      dialogFormVisible: false,
      // 图片验证码
      identifyCode: '',
      // 验证码规则
      identifyCodes: '3456789ABCDEFGHGKMNPQRSTUVWXY',
      rules: {
        username: [
          { required: true, message: 'please input username', trigger: 'blur' },
          { min: 3, max: 10, message: 'Length of 3 to 5 characters', trigger: 'blur' },
        ],
        password: [
          { required: true, message: 'Please input a password', trigger: 'blur' },
          { min: 1, max: 20, message: 'Length of 1 to 20 characters', trigger: 'blur' },
        ],
        email: [
          { required: true, message: 'please input email', trigger: 'blur' },
          { type: 'email', message: 'Please input correct email address', trigger: ['blur', 'change'] },
        ],
        code: [{ required: true, message: 'please select code', trigger: 'blur' }],
      },
    };
  },
  components: { Identify },
  mounted() {
    // 重置路由
    resetRouter();
    this.refreshCode();
  },
  created() {
    localStorage.clear()
  },
  methods: {
    // 获取验证码方法
    getCode() {
      // 验证邮箱是否填写
      if (!this.user.email) {
        this.$message.error("please input email")
        return
      }
      // 发送获取验证码请求
      this.request.post("/user/loginSms", { email: this.user.email }).then(res => {
        if(res.code === '200') {
          this.$message.success("sms send successful")
          this.user.token = res.data
          this.startCountdown()
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    // 开始倒计时
    startCountdown() {
      this.codeDisabled = true
      this.codeButtonText = `${this.countdown} seconds later`

      this.timer = setInterval(() => {
        this.countdown--
        this.codeButtonText = `${this.countdown} seconds later`

        if (this.countdown <= 0) {
          clearInterval(this.timer)
          this.codeDisabled = false
          this.codeButtonText = "get code"
          this.countdown = 60
        }
      }, 1000);
    },
    login() {
      // if (this.code !== this.identifyCode.toLowerCase()) {
      //   this.$message({
      //     type: "error",
      //     message: "验证码错误"
      //   })
      //   return;
      // }
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          // 表单校验合法
          this.request.post('/user/login', this.user).then((res) => {
            if (res.code === '200') {
              localStorage.setItem('user', JSON.stringify(res.data)); // 存储用户信息到浏览器
              localStorage.setItem('menus', JSON.stringify(res.data.menus)); // 存储用户信息到浏览器

              // 动态设置当前用户的路由
              setRoutes();
              if (res.data.role === 'ROLE_USER') {
                this.$router.push('/front/home');
                this.$message.success('login successful');
              } else {
                this.$router.push('/');
                this.$message.success('login successful');
              }
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
    },
    // 切换验证码
    refreshCode() {
      this.identifyCode = '';
      this.makeCode(this.identifyCodes, 4);
    },
    // 生成随机验证码
    makeCode(o, l) {
      for (let i = 0; i < l; i++) {
        this.identifyCode += this.identifyCodes[Math.floor(Math.random() * this.identifyCodes.length)];
      }
    },
    handlePass() {
      this.dialogFormVisible = true;
      this.pass = {};
    },
    passwordBack() {
      this.request.put('/user/reset', this.pass).then((res) => {
        if (res.code === '200') {
          this.$message.success(
            'password reset successful, new password is 123, please change password as soon as possible'
          );
          this.dialogFormVisible = false;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
  },
};
</script>

<style>
.wrapper {
  height: 100vh;
  //background-image: url('../assets/bg1.jpg');
  background-size: cover;
  overflow: hidden;
}
.el-form {
  background-color: transparent !important;
}
</style>
