<template>
  <el-card style="width: 1000px;  margin: 10px auto">
    <h2 style="margin: 20px 0">公告列表</h2>
    <el-collapse accordion  v-model="active">
      <el-collapse-item v-for="(item,index) in notices" :key="item.id" :name="'' + index">
        <template slot="title">
          <span style="font-size: 20px;">{{ item.name }}</span>
          <span style="margin-left: 10px">{{ item.time }}</span>
        </template>
        <div>{{ item.content }}</div>
      </el-collapse-item>
    </el-collapse>
  </el-card>

</template>

<script>
export default {
  name: 'notice',
  data() {
    return {
      notices: [],
      active: '0',
    }
  },
  created() {
    this.request.get("/notice").then(res => {
      this.notices = res.data.splice(0, 5)
    })
  }
}
</script>