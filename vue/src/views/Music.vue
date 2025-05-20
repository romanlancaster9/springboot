<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="please input a name" suffix-icon="el-icon-search" v-model="name"></el-input>
<!--      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>-->
<!--      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">search</el-button>
      <el-button type="warning" @click="reset">reset</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-popconfirm
          class="ml-5"
          confirm-button-text='confirm'
          cancel-button-text="I'll think about it again"
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure to delete these data in bulk？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">batch delete <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="name"></el-table-column>
      <el-table-column prop="url" label="music url"></el-table-column>
      <el-table-column prop="content" label="describe"></el-table-column>
      <el-table-column label="img"><template slot-scope="scope"><el-image style="width: 100px; height: 100px" :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image></template></el-table-column>
<!--      <el-table-column prop="lrc" label="lyric"></el-table-column>-->
      <el-table-column prop="typeName" label="music type"></el-table-column>
      <el-table-column prop="singer" label="singer"></el-table-column>
      <el-table-column prop="state" label="audit status"></el-table-column>
      <el-table-column prop="reason" label="audit description"></el-table-column>
      <el-table-column label="operate"  width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)" v-if="scope.row.state === 'pending approval'">approve</el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='confirm'
              cancel-button-text="I'll think about it again"
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure to delete it？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">delete <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="140px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
        <el-form-item prop="state" label="audit status">
          <el-radio v-model="form.state" label="pass">pass</el-radio>
          <el-radio v-model="form.state" label="not pass">not pass</el-radio>
        </el-form-item>
        <el-form-item prop="reason" label="audit description">
          <el-input type="textarea" v-model="form.reason" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">cancel</el-button>
        <el-button type="primary" @click="save">confirm</el-button>
      </div>
    </el-dialog>

    <el-dialog title="评论列表" :visible.sync="dialogFormVisible2" width="70%" :close-on-click-modal="false">
      <div style="margin: 10px 0" v-if="comments.length > 0">
        <div style="text-align: left;">
          <div style="padding: 10px 0; " v-for="item in comments" :key="item.id">
            <div style="display: flex">
              <div style="width: 80px">
                <el-avatar :size="50" :src="item.img"></el-avatar>
              </div>
              <div style="flex: 1">
                <div>{{ item.username }} <span style="margin-left: 10px">{{ item.time }}</span></div>
                <div style="margin-top: 10px; color: #666">{{ item.content }}</div>

                <!-- 回复列表 -->
                <div v-if="item.children.length"
                     style="margin-left: 50px; width: 52%;padding: 10px; border-radius: 10px">
                  <div v-for="sub in item.children" :key="sub.id">
                    <div style="padding:5px 0">
                      <div style="width: 100px;display: flex;align-items: center">
                        <el-avatar :size="40" :src="sub.img"></el-avatar>&nbsp;
                        <span style="cursor: pointer;font-weight:bold" @click="reply(sub.pid, sub.userId)">{{
                            sub.username
                          }}</span>
                      </div>
                      <br>
                      <span style="margin-left: 47px">
                        回复 <span style="color: cornflowerblue">@{{ sub.parentName }}</span>
                      <span style="color: #666;width: 100%; margin-left: 10px">{{ sub.content }}</span>
                    </span>
                      <span style="float: right; font-size: 13px; color: #666; margin-top: 3px">{{ sub.time }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </div>
      </div>

      <div style="margin: 10px 0" v-if="comments.length === 0">
        <el-empty description="暂无评论~"></el-empty>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Music",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      dialogFormVisible2: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        name: [
          { required: true, message: 'please input a name', trigger: 'blur'}
        ],
      },
      types:[],
      singers: [],
      comments: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    comment(row){
      this.request.get("/comment/tree", {
        params: {
          foreignId: row.id,
        }
      }).then(res => {
        this.comments = res.data
      })
      this.dialogFormVisible2 = true
    },
    load() {
      this.request.get("/music/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/type").then(res => {
        this.types = res.data
      })

      this.request.get("/singer/").then(res => {
        this.singers = res.data
      })
    },
    save() {
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            this.request.post("/music", this.form).then(res => {
              if (res.code === '200') {
                this.$message.success("save successful")
                this.dialogFormVisible = false
                this.load()
              } else {
                this.$message.error(res.msg)
              }
            })
          }
        })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {
        if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
       this.$nextTick(() => {
         if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
       })
    },
    del(id) {
      this.request.delete("/music/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("delete successful")
          this.load()
        } else {
          this.$message.error("delete failed")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("Please select the data to be deleted")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/music/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("batch delete successful")
          this.load()
        } else {
          this.$message.error("batch delete failed")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
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
    handleFileUploadSuccess(res) {
      this.form.url = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },
    download(url) {
      window.open(url)
    },
    exp() {
      window.open(process.env["VUE_APP_PRE_API"] + "/music/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("import successful")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
