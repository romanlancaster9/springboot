<template>

  <div style="width: 1200px;margin: 5px auto">
    <div style="margin: 20px 0 0 0">
      <div style="text-align: center">
        <el-input size="small" style="width: 700px" placeholder="please input a name" v-model="name" suffix-icon="el-icon-search" clearable></el-input>&nbsp;
        <el-button size="small" class="ml-5" type="info" @click="load">search</el-button>
      </div>
    </div>

    <div style="margin-top: 20px;">
      <h1 style="margin-bottom: 10px">Music List</h1>
    </div>
    <div>
      <el-row>
        <el-col :span="6" v-for="item in tableData" :key="item.id" style="margin-bottom: 10px">
          <div style="padding: 10px; cursor: pointer">
              <img :src="item.img" alt="" style="width: 100%; height: 200px; overflow: hidden; border-radius: 10px">
              <div style="color: #666; margin: 10px 0;text-align: center" class="line1" @click="$router.push('/front/musicDetail?id=' + item.id)">{{ item.name }}</div>
          </div>
        </el-col>
      </el-row>
      <div style="padding: 10px 0">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[2, 5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Film",
  data() {
    return {
      type: this.$route.query.id,
      imgs: [
        require('../../assets/27a28806bdfe7819933acada9742a9fa.jpg'),
        require('../../assets/b64b51060933625ac35826763af6a9be.jpeg'),
      ],
      tableData: [],
      total: 0,
      total2: 0,
      total3: 0,
      pageNum: 1,
      pageSize: 8,
      name: "",
      mark: 99999,
      age: 9999,
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      types: [],
      playlists: [],
      singers: [],
      timesData: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    handleClickType(tab, event) {
      this.load();
    },
    load() {
      console.log(this.type)
      this.request.get("/music/page/type/", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          typeId: this.type
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/type").then(res => {
        this.types = res.data
      })
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>

<style>
/deep/ .el-tabs__item {
  /* 修改为您想要的文字大小 */
  font-size: 30px !important;
}


</style>
