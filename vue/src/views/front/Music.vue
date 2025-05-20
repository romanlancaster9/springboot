<template>
  <aplayer :music="audio[0]" :list="audio" :showlrc="true"></aplayer>
</template>

<script>
import aplayer from 'vue-aplayer';
export default {
  components: { aplayer },
  data() {
    return {
      // 音频列表
      audio: [
        {
          title: 'dome1', // 歌曲名字
          artist: '豆腐厂厂长', // 演唱者
          url: process.env["VUE_APP_PRE_API"] + '/file/21b8b1b5cff44a8ea4bb44ca1a1ab96d.mp3', // 音频文件url
          pic: '', // 演唱者封面
          lrc: '', // 歌词或者歌词文件
          theme: '', // 歌曲主题
        },
        {
          title: 'dome2',
          artist: '杀猪队队长',
          url: process.env["VUE_APP_PRE_API"] + '/file/a50d8e3ce11243cda897a8fa782a7ce0.mp3',
          pic: 'https://edu-guil-1010.oss-cn-beijing.aliyuncs.com/%E4%BB%A5%E5%AE%B6%E4%B9%8B%E5%90%8D.jpg',
          lrc: '[00:00.00] (,,•́ . •̀,,) 抱歉，当前歌曲暂无歌词',
          theme: '', // 歌曲主题
        }
      ]
    };
  },
  mounted() {},

  methods: {}
};
</script>

<style scoped></style>



