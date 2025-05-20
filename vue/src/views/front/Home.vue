<template>
  <div style="width: 1200px; margin: 5px auto">
    <div style="margin: 10px 0">
      <el-carousel height="550px" :interval="10000">
        <el-carousel-item v-for="item in imgs" :key="item">
          <img :src="item" alt="" style="width: 100%; height: 100%" />
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="margin-top: 20px">
      <h1 style="margin-bottom: 10px">Play music</h1>
    </div>
    <div>
      <el-row>
        <el-col :span="6" v-for="item in types" :key="item.id" style="margin-bottom: 10px">
          <div style="background-color: #d0ccc6; padding: 10px; cursor: pointer">
            <img
              :src="item.img || '/bg.png'"
              alt=""
              style="width: 100%; height: 200px; overflow: hidden; border-radius: 10px"
            />
            <div
              style="color: #666; margin: 10px 0; text-align: center"
              class="line1"
              @click="$router.push('/front/musicList?id=' + item.id)"
            >
              {{ item.name }}
            </div>
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
          :total="total"
        >
        </el-pagination>
      </div>
    </div>

    <el-dialog
        :visible.sync="dialogVisible"
        width="60%">
        <img :src="alertImage" alt="" style="width: 100%; height: 100%" />
        <span slot="footer" class="dialog-footer">
          <el-button style="font-size: 20px" type="primary"  @click="alertKnow">I already know</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Film',
  data() {
    return {
      dialogVisible: true,
      alertImage: require('../../assets/home/alert.png'),
      imgs: [
        require('../../assets/home/Good Day For Dreaming.jpg'),
        require('../../assets/home/Starboy.jpg'),
        require('../../assets/home/Stay.jpg'),
        require('../../assets/home/Welcome To New York.jpg'),
      ],
      tableData: [],
      total: 0,
      total2: 0,
      total3: 0,
      pageNum: 1,
      pageSize: 8,
      name: '',
      mark: 99999,
      age: 9999,
      form: {},
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {},
      type: 0,
      types: [],
      playlists: [],
      singers: [],
      timesData: [],
    };
  },
  created() {
    this.type = 0;
    this.load();
    var alertKnow =  localStorage.getItem("alertKnow");
    if (!!alertKnow && "true" === alertKnow) {
      this.dialogVisible = false
    }
  },
  methods: {
    handleClickType(tab, event) {
      this.load();
    },
    load() {
      console.log(this.type);
      this.request
        .get('/music/page/type/', {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name: this.name,
            typeId: this.type,
          },
        })
        .then((res) => {
          this.tableData = res.data.records;
          this.total = res.data.total;
        });

      this.request.get('/type').then((res) => {
        this.types = res.data;
      });
    },
    handleSizeChange(pageSize) {
      console.log(pageSize);
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum);
      this.pageNum = pageNum;
      this.load();
    },
    alertKnow() {
      this.dialogVisible = false
      localStorage.setItem("alertKnow", "true");
    }
  },
};
</script>

<style>
/deep/ .el-tabs__item {
  /* 修改为您想要的文字大小 */
  font-size: 30px !important;
}

.line1:hover {
  color: #000000 !important;
  font-size: 16px !important;
  transform: scale(1.1);
  transition: all 0.3s ease-in-out;
}
</style>
