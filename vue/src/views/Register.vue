<template>
  <div class="wrapper">
    <div style="height: 60px; line-height: 60px; font-size: 20px; padding-left: 50px; color: white;
      background-color: rgba(0,0,0,0.2)">Music Management System</div>
    <div style="margin: 150px auto; background-color: rgba(0,0,0,0.2); width: 350px; height: 500px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px;color: #eeeeee"><b>Register</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="please input username" size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="please input password" size="medium" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="please confirm password" size="medium" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input placeholder="please input email" size="medium" prefix-icon="el-icon-folder"  v-model="user.email"></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input style="width: 180px;margin-right: 10px" placeholder="please input code" size="medium" prefix-icon="el-icon-lock"  v-model="user.code"></el-input>
          <el-button @click="getCode" :disabled="codeDisabled">{{ codeButtonText }}</el-button>
        </el-form-item>


        <el-form-item prop="sex">
          <template>
            <el-radio v-model="user.sex" label="man" style="color: #eeeeee">man</el-radio>
            <el-radio v-model="user.sex" label="woman" style="color: #eeeeee">woman</el-radio>
          </template>
        </el-form-item>


        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/login')">return login</el-button>
          <el-button type="primary" size="small"  autocomplete="off" @click="login">confirm</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      codeDisabled: false,  // 控制按钮是否禁用
      codeButtonText: "get code",  // 按钮文字
      countdown: 60,  // 倒计时秒数
      timer: null,  // 定时器
      user: {},
      rules: {
        username: [
          { required: true, message: 'please input username', trigger: 'blur' },
          { min: 3, max: 10, message: 'Length of 3 to 5 characters', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'please input password', trigger: 'blur' },
          { min: 1, max: 20, message: 'Length of 1 to 20 characters', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: 'please input confirmPassword', trigger: 'blur' },
          { min: 1, max: 20, message: 'Length of 1 to 20 characters', trigger: 'blur' }
        ],
        email: [
          { required: true, message: 'please input email', trigger: 'blur' },
          { type: 'email', message: 'Please input correct email address', trigger: ['blur', 'change']},
        ],
        code: [
          { required: true, message: 'please select code', trigger: 'blur' },
        ],
        sex: [
          { required: true, message: 'please select sex', trigger: 'blur' },
        ],
      }
    }
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
      this.request.post("/user/registerSms", { email: this.user.email }).then(res => {
        if(res.code === '200') {
          this.$message.success("sms send successful")
          this.user.token = res.data
          this.startCountdown()
        } else {
          this.$message.error(res.msg)
        }
      })
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
      }, 1000)
    },
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if(res.code === '200') {
              this.$message.success("register successful")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  },
  // 组件销毁时清除定时器
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  //background-image: url("../assets/bg.jpg");
  background-size: cover;
  overflow: hidden;
}
</style>
