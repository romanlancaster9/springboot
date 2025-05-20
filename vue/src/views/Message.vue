<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="please input a content" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-select
          v-model="type"
          style="margin-left: 20px;"
          placeholder="please select type">
        <el-option
            v-for="item in typeList"
            :key="item.type"
            :label="item.name"
            :value="item.type">
        </el-option>
      </el-select>
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
      <el-table-column prop="type" label="feedback type">
        <template slot-scope="scope">
          <div class="type-list">
            <div class="type-item" v-for="(type, index) in scope.row.typeList" :key="index">{{getType(typeList, type)}}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="content"></el-table-column>
      <el-table-column label="img"><template slot-scope="scope"><el-image style="width: 100px; height: 100px" :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image></template></el-table-column>
      <el-table-column prop="user.nickname" label="nickname"></el-table-column>
      <el-table-column prop="time" label="time"></el-table-column>

      <el-table-column label="operate"  width="200" align="center">
        <template slot-scope="scope">
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

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">
      <el-form label-width="100px" size="small" style="width: 90%">
        <el-form-item label="留言内容">
          <el-input v-model="form.content" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户">
          <el-input v-model="form.userId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="时间">
          <el-date-picker v-model="form.time" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">cancel</el-button>
        <el-button type="primary" @click="save">confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getTypeList} from "@/utils/message";

export default {
  name: "Message",
  data() {
    return {
      typeList: getTypeList(),
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      typeCheckedList: [],
      type: [],
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/message/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          type: this.type,
        }
      }).then(res => {
        this.tableData = res.data.records.map(e => {
          if (!!e.type) {
            e.typeList =  e.type.split(',');
          }
          return e;
        })
        this.total = res.data.total
      })
    },
    save() {
      this.request.post("/message", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("save successful")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("save failed")
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
      this.request.delete("/message/" + id).then(res => {
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
      this.request.post("/message/del/batch", ids).then(res => {
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
      this.type = ""
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
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },
    download(url) {
      window.open(url)
    },
    exp() {
      window.open(process.env["VUE_APP_PRE_API"] + "/message/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("import successful")
      this.load()
    },
    getType(typeList, type) {
      const typeMap = Object.fromEntries(typeList.map(item => [item.type, item.name]));
      return typeMap[type]
    },
  }
}
</script>


<style scoped>
.headerBg {
  background: #eee!important;
}

.type-list {
  margin-top: 10px;
}
.type-item {
  padding: 3px 10px;
  margin-right: 10px;
  box-shadow: 1px 1px 4px rgb(23 23 23 / 38%);
  border-radius: 5px;
  margin-bottom: 5px;
}
</style>
