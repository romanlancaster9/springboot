<template>
  <div style="color: #666;font-size: 14px;">
    <el-card>
      hello！<b>{{ user.nickname }}</b>，welcome to use this system
    </el-card>

    <el-card style="width: 100%;  margin: 10px 0">
      <h2 style="margin: 20px 0">notice list</h2>
      <div >
        <el-timeline  reverse slot="reference">
          <el-timeline-item v-for="item in tableData" :key="item.id" :timestamp="item.time">
            <el-popover
                placement="right"
                width="200"
                trigger="hover"
                :content="item.content">
              <span slot="reference">{{ item.name }}</span>
            </el-popover>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-card>

  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      activeNames: ['0'],
      tableData: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.request.get("/notice", {}).then(res => {
      this.tableData = res.data.splice(0, 5)
      this.total = res.data.total
    })
  }
}
</script>
