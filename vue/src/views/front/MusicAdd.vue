<template>
  <div>
    <div style="margin: 10px 0">
      <el-input
        style="width: 200px"
        placeholder="please input a name"
        suffix-icon="el-icon-search"
        v-model="name"
        clearable
      ></el-input>
      <el-button class="ml-5" type="primary" @click="load">search</el-button>
      <el-button type="success" @click="handleAdd">add</el-button>
    </div>

    <el-card style="margin: 10px 0">
      <div style="margin: 10px 0"></div>

      <el-table :data="tableData" @selection-change="handleSelectionChange">
        <el-table-column prop="name" label="name"></el-table-column>
        <el-table-column prop="url" label="music url"></el-table-column>
        <el-table-column prop="content" label="describe"></el-table-column>
        <el-table-column label="img"
          ><template slot-scope="scope"
            ><el-image
              style="width: 100px; height: 100px"
              :src="scope.row.img || '/bg.png'"
              :preview-src-list="[scope.row.img || '/bg.png']"
            ></el-image></template
        ></el-table-column>
<!--        <el-table-column prop="lrc" label="lyric"></el-table-column>-->
        <el-table-column prop="typeName" label="music type"></el-table-column>
        <el-table-column prop="singer" label="singer"></el-table-column>
        <el-table-column prop="state" label="audit status"></el-table-column>
        <el-table-column prop="reason" label="audit description"></el-table-column>
        <el-table-column label="operate" width="200" align="center">
          <template slot-scope="scope">
            <el-button type="success" @click="handleEdit(scope.row)">update <i class="el-icon-edit"></i></el-button>
            <el-popconfirm
              class="ml-5"
              confirm-button-text="confirm"
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
          layout="total, prev, pager, next"
          :total="total"
        >
        </el-pagination>
      </div>
    </el-card>

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="140px" size="small" style="width: 85%" :model="form" :rules="rules" ref="ruleForm">
        <el-form-item prop="name" label="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="url" label="music url">
          <el-upload :action="api" ref="file" :on-success="handleFileUploadSuccess">
            <el-button size="small" type="primary">click upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="content" label="describe">
          <el-input type="textarea" v-model="form.content" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="img" label="cover">
          <el-upload :action="api" ref="img" :on-success="handleImgUploadSuccess">
            <el-button size="small" type="primary">click upload</el-button>
          </el-upload>
        </el-form-item>
<!--        <el-form-item prop="lrc" label="lyric">-->
<!--          <el-input v-model="form.lrc" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item prop="typeId" label="music type">
          <template>
            <el-select v-model="form.typeId" placeholder="please select">
              <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id"> </el-option>
            </el-select>
          </template>
        </el-form-item>
        <el-form-item prop="singer" label="singer">
          <el-input v-model="form.singer" autocomplete="off"></el-input>
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
export default {
  name: 'Music',
  data() {
    return {
      api: process.env['VUE_APP_PRE_API'] + '/file/upload',
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: '',
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {},
      rules: {
        name: [{ required: true, message: 'please input a name', trigger: 'blur' }],
      },
      types: [],
      singers: [],
    };
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      this.request
        .get('/music/page', {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name: this.name,
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
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          this.request.post('/music', this.form).then((res) => {
            if (res.code === '200') {
              this.$message.success('save successful');
              this.dialogFormVisible = false;
              this.load();
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
    },
    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {};
      this.$nextTick(() => {
        if (this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if (this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      });
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        if (this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if (this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      });
    },
    del(id) {
      this.request.delete('/music/' + id).then((res) => {
        if (res.code === '200') {
          this.$message.success('delete successful');
          this.load();
        } else {
          this.$message.error('delete failed');
        }
      });
    },
    handleSelectionChange(val) {
      console.log(val);
      this.multipleSelection = val;
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error('Please select the data to be deleted');
        return;
      }
      let ids = this.multipleSelection.map((v) => v.id); // [{}, {}, {}] => [1,2,3]
      this.request.post('/music/del/batch', ids).then((res) => {
        if (res.code === '200') {
          this.$message.success('batch delete successful');
          this.load();
        } else {
          this.$message.error('batch delete failed');
        }
      });
    },
    reset() {
      this.name = '';
      this.load();
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
    handleFileUploadSuccess(res) {
      this.form.url = res;
    },
    handleImgUploadSuccess(res) {
      this.form.img = res;
    },
    download(url) {
      window.open(url);
    },
    exp() {
      window.open(process.env['VUE_APP_PRE_API'] + '/music/export');
    },
    handleExcelImportSuccess() {
      this.$message.success('import successful');
      this.load();
    },
  },
};
</script>

<style>
.headerBg {
  background: #eee !important;
}
.cell {
  color: #000;
}
</style>
