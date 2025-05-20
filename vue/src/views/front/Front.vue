<template>
  <div>
    <!--    头部-->
    <div
      style="display: flex; height: 60px; line-height: 60px; border-bottom: 1px solid #eee; background-color: #cbc1ae"
    >
      <div style="width: 400px; display: flex; padding-left: 30px">
        <div style="width: 50px">
          <img src="../../assets/音乐.png" alt="" style="width: 40px; position: relative; top: 8px; right: 0" />
        </div>
        <div style="flex: 1">Welcome Music Management System</div>
      </div>
      <div style="flex: 1">
        <el-menu
          :default-active="$route.path"
          class="el-menu-demo"
          mode="horizontal"
          router
          style="border: none; height: 100%; margin-left: 200px"
        >
          <el-menu-item index="/front/home"> <span class="scale-text">Home</span> </el-menu-item>
          <el-menu-item index="/front/type"> <span class="scale-text">Music Type</span> </el-menu-item>
          <el-menu-item index="/front/musicAdd"> <span class="scale-text">Upload Music</span> </el-menu-item>
          <el-menu-item index="/front/message"> <span class="scale-text">Feedback</span> </el-menu-item>
          <el-menu-item index="/front/help"> <span class="scale-text">Help Center</span> </el-menu-item>
        </el-menu>
      </div>
      <div style="width: 200px; text-align: right; padding-right: 10px">
        <div v-if="!user.username" style="text-align: right; padding-right: 30px">
          <el-button @click="$router.push('/login')">Login</el-button>
          <el-button @click="$router.push('/register')">Register</el-button>
        </div>
        <div v-else>
          <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
            <div style="display: inline-block">
              <img
                :src="user.avatarUrl"
                alt=""
                style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px"
              />
              <span>{{ user.nickname }}</span
              ><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 150px; text-align: center">
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/password"><div>update password</div></router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/person"><div>self info</div></router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <span style="text-decoration: none" @click="logout"><div>logout</div></span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <div style="width: 1200px; margin: 5px auto">
      <router-view @refreshUser="getUser" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'Front',
  data() {
    return {
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {},
    };
  },
  created() {},
  methods: {
    logout() {
      this.$store.commit('logout');
      this.$message.success('logout successful');
    },
    getUser() {
      let username = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).username : '';
      if (username) {
        // 从后台获取User数据
        this.request.get('/user/username/' + username).then((res) => {
          // 重新赋值后台的最新User数据
          this.user = res.data;
        });
      }
    },
  },
};
</script>

<style>
.item {
  display: inline-block;
  width: 100px;

  text-align: center;
  cursor: pointer;
}
.item a {
  color: #1e90ff;
}
.item:hover {
  background-color: LightPink;
}
.el-menu-demo {
  background-color: #cbc1ae;
}
.el-menu-demo > .el-menu-item {
  /* background-color: #cbc1ae !important;
  transform: scale(1.1);
  font-weight: bold;
  font-size: 18px;
  transition: all 0.3s ease; */
  position: relative;
  overflow: hidden;
}

.scale-text {
  display: inline-block;
  transition: all 0.3s ease;
  color: #868992;
}

.el-menu-demo > .el-menu-item:hover {
  background-color: #cbc1ae !important;
}
.el-menu-demo > .is-active > .scale-text {
  font-weight: bold;
  color: #000 !important;
}

.el-menu-demo > .el-menu-item:hover > .scale-text {
  transform: scale(1.05);
  font-weight: bold;
  color: #000 !important;
  /* font-size: 16px; */
}
</style>
