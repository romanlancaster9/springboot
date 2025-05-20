<template>
  <div style="margin-top: 15px">
    <el-card>
      <el-form size="small" style="width: 100%">
        <div
          style="padding: 10px 0; margin: 0 0 0 0; font-weight: bold; border-bottom: 1px solid #ccc; font-size: 20px"
        >
          Feedback
        </div>
        <div style="margin: 10px 0 0 0">
          <el-form-item label="feedback type">
            <el-checkbox-group v-model="typeCheckedList">
              <el-checkbox :label="item.type" v-for="(item, index) in typeList" :key="index">{{
                item.name
              }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item prop="img" label="feedback img">
            <el-upload
              :action="api"
              ref="img"
              :file-list="fileList"
              on-exceed="handleExceed"
              :before-upload="beforeUpload"
              :on-success="handleImgUploadSuccess"
            >
              <el-button size="small" type="primary">click upload</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="feedback content">
            <el-input type="textarea" v-model="content" autocomplete="off" :rows="3"></el-input>
          </el-form-item>
        </div>
      </el-form>
      <div style="margin-top: 10px; text-align: right">
        <el-button size="small" type="success" @click="submit">submit</el-button>
      </div>
    </el-card>

    <div
      style="padding: 10px 0; margin: 30px 0 10px 0; font-weight: bold; border-bottom: 1px solid #ccc; font-size: 20px"
    >
      <!-- What Are Everyone Saying -->
      Comments Section
    </div>
    <div v-for="item in tableData" style="padding: 5px 0">
      <el-card>
        <div style="display: flex" class="comment-item">
          <div style="width: 80px">
            <el-avatar :size="50" :src="item.user.avatarUrl"></el-avatar>
          </div>
          <div style="flex: 1">
            <div style="display: flex">
              {{ item.user.nickname }} <span style="margin-left: 10px">{{ item.time }}</span>
            </div>
            <div class="type-list">
              <div class="type-item" v-for="(type, index) in item.typeList" :key="index">
                {{ getType(typeList, type) }}
              </div>
            </div>
            <div class="img-item" v-if="item.img">
              <el-image style="width: 100px; height: 100px" :src="item.img" :preview-src-list="[item.img]"></el-image>
            </div>
            <div style="margin-top: 10px; color: #000">{{ item.content }}</div>
          </div>
        </div>
      </el-card>
    </div>
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
</template>

<script>
import { getTypeList } from '@/utils/message';

export default {
  name: 'ShortComment',
  data() {
    return {
      api: process.env['VUE_APP_PRE_API'] + '/file/upload',
      typeList: getTypeList(),
      typeCheckedList: [],
      content: '',
      img: '',
      fileList: [],
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {},
      types: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
    };
  },
  created() {
    this.load();
  },
  methods: {
    submit() {
      if (this.user.username == null) {
        this.$message.warning('请登录');
        return;
      }
      let data = {
        type: this.typeCheckedList.join(','),
        img: this.img,
        content: this.content,
        userId: this.user.id,
      };
      console.log(data);
      this.request.post('/message', data).then((res) => {
        if (res.code === '200') {
          this.$message.success('publish successful');
          this.load();
          this.typeCheckedList = [];
          this.img = '';
          this.content = '';
          this.fileList = [];
        } else {
          this.$message.error('publish failed');
        }
      });
    },
    load() {
      this.request
        .get('/message/page', {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          },
        })
        .then((res) => {
          this.tableData = res.data.records.map((e) => {
            if (!!e.type) {
              e.typeList = e.type.split(',');
            }
            return e;
          });
          this.total = res.data.total;
        });
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    getType(typeList, type) {
      const typeMap = Object.fromEntries(typeList.map((item) => [item.type, item.name]));
      return typeMap[type];
    },
    handleExceed(files, fileList) {
      // 当文件超出限制时，用新文件替换旧文件
      this.fileList = [files[0]]; // 只保留最新选择的文件
    },
    beforeUpload(file) {
      // 上传前的处理
      this.fileList = [file]; // 确保只保留一个文件
      return true;
    },
    handleImgUploadSuccess(res, file, fileList) {
      // 上传成功后的处理
      this.fileList = fileList;
      this.img = res;
    },
  },
};
</script>

<style scoped>
.type-list,
.img-item {
  display: flex;
  margin-top: 10px;
}
.type-item {
  padding: 3px 10px;
  margin-right: 10px;
  box-shadow: 1px 1px 4px rgb(23 23 23 / 38%);
  border-radius: 5px;
}
.comment-item > div {
  color: #000 !important;
}
/deep/ .el-form-item__label,
/deep/ .el-checkbox__label {
  color: #000 !important;
}

/deep/ .el-pagination__total {
  color: #000 !important;
}
</style>
