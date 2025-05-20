<template>
  <div style="padding: 10px">
    <el-row :gutter="10">
      <el-col :span="12">
        <el-card >
          <div >
            <img :src="music.img" :preview-src-list="[music.img]" style="width: 100%;height: 450px"></img>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" >
        <el-card style="height: 495px;">
          <div class="item1" style="margin: 100px 0 0 30px;display: flex">
            <h1 style="color: #E6A23C">
              {{ music.name }}
            </h1>
          </div>
          <div class="item1" style="font-size: 14px; margin: 20px 0 0 30px;font-size: 20px"><span style="color: #666666">歌曲类别</span>&nbsp;&nbsp;:&nbsp;&nbsp;<span style="color: #222">{{ music.typeName }}</span></div>
          <div class="item1" style="font-size: 14px; margin: 10px 0 0 30px;font-size: 20px"><span style="color: #666666">歌手</span>&nbsp;&nbsp;:&nbsp;&nbsp;<span style="">{{ music.singer }}</span></div>

        </el-card>
      </el-col>

    </el-row>

    <el-card style="margin-top: 15px">
      <div style="padding: 10px 0; border-bottom: 1px solid #ccc; color: #1E90FF; font-size: 20px;font-weight: bold">
        歌曲介绍
      </div>

      <div style="margin-top: 15px" v-html="music.content"></div>
    </el-card>

    <el-card style="margin-top: 15px">
      <div style="padding: 10px 0;  color: #1E90FF; font-size: 20px;font-weight: bold">
        歌曲播放
      </div>

      <aplayer :music="audio"  :showlrc="true"></aplayer>

    </el-card>
  </div>
</template>

<script>
import aplayer from 'vue-aplayer';

export default {
  components: { aplayer },
  name: "Detail",
  data() {
    return {
      id: this.$route.query.id,
      music: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      comments: [],
      comment: {
        content: ''
      },
      replyComment: {},
      dialogFormVisible3: false,
      audio: {
        title: 'dome1', // 歌曲名字
        artist: '豆腐厂厂长', // 演唱者
        url: process.env["VUE_APP_PRE_API"] + '/file/21b8b1b5cff44a8ea4bb44ca1a1ab96d.mp3', // 音频文件url
        pic: '', // 演唱者封面
        lrc: '', // 歌词或者歌词文件
        theme: '', // 歌曲主题
      },
      state: true
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/music/" + this.id).then(res => {
        this.music = res.data

        this.audio = {
          title: this.music.name,
          artist: this.music.singer, // 演唱者
          url: this.music.url, // 音频文件url
          pic: this.music.img, // 演唱者封面
          lrc: '', // 歌词或者歌词文件
          theme: '', // 歌曲主题
        }
      })
    }
  }
}
</script>

<style scoped>
.item1 {
  padding: 10px;
  color: #666;
}
</style>