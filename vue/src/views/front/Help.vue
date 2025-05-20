<template>
  <div class="help-center-container">
    <div class="header">Help Center</div>

    <el-row :gutter="20" class="content">
      <el-col :span="24" class="section">
        <h3>System Introduction</h3>
        <p>{{ systemInfo }}</p>
      </el-col>

      <el-col :span="24" class="section">
        <h3>Contact Us</h3>
        <ul>
          <p>Email：{{ contact.email }}</p>
          <p>Hotline：{{ contact.phone }}</p>
          <p>Service Time：{{ contact.hours }}</p>
        </ul>
      </el-col>

      <el-col :span="24" class="section">
        <h3>Frequently Asked Questions（FAQ）</h3>
        <div style="padding: 0 14px">
          <el-collapse accordion>
            <el-collapse-item style="border-bottom: 1px solid #aaaaaa;" v-for="(item, index) in faqList" :key="index" :title="item.question">
              <p>{{ item.answer }}</p>
            </el-collapse-item>
          </el-collapse>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'HelpCenter',
  data() {
    return {
      systemInfo:
        'The music resource management system is a convenient and efficient music file management platform. You can easily upload, categorize, search, and manage your music resources, making music file storage and management simpler and more efficient.',
      contact: {
        email: 'aaa@163.com',
        phone: '0536-xxxxxxxxxxxx',
        hours: 'Monday to Friday 09:00 - 18:00',
      },
      faqList: [
        {
          question: 'How to upload music files？',
          answer:
            'After logging in, click the "Upload Music" button at the top of the page, select the MP3 file, fill in the relevant information, and submit it.',
        },
        {
          question: 'What formats are supported for music files？',
          answer:
            'Currently supports standard MP3 format, with a recommended bit rate of 128kbps or higher to ensure sound quality.',
        },
        {
          question: 'How to search and find music？',
          answer:
            'Enter keywords in the search bar at the top of the page to quickly search. Advanced Search allows for precise filtering by category, upload time, tag, and more.',
        },
        {
          question: 'What should I do if I forget my password？',
          answer: 'Click on "Forgot Password" on the login page and follow the prompts to reset your password.',
        },
        {
          question: 'What should I do if the file upload fails？',
          answer:
            'Check the stability of the network connection and whether the file format and size meet the requirements (maximum single file size is 50MB). If the issue still cannot be resolved, please contact our customer service team for assistance.',
        },
      ],
    };
  },
};
</script>

<style scoped>
.help-center-container {
  max-width: 900px;
  margin: 20px auto;
  padding: 15px;
  color: #000000;
  background-color: #fff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
}

.header {
  font-size: 22px;
  font-weight: bold;
  padding-bottom: 10px;
  border-bottom: 2px solid #f0f0f0;
  text-align: center;
  color: #000;
  margin-bottom: 20px;
}

.content {
  margin-top: 10px;
}

.section {
  margin-bottom: 20px;
}

.section h3 {
  font-size: 18px;
  color: #000;
  margin-bottom: 10px;
  /* border-left: 4px solid orangered; */
  padding-left: 12px;
  position: relative;
}
.section h3::before {
  content: '';
  display: inline-block;
  width: 4px;
  border-radius: 2px;
  height: 100%;
  background: linear-gradient(to right, #f6ab8e, #ef6e46);
  position: absolute;
  left: 0;
  top: 0;
}

.section p,
.section li {
  font-size: 16px;
  line-height: 1.8;
  padding: 0 14px;
  box-sizing: border-box;
}

.el-collapse-item__header {
  font-weight: bold;
  font-size: 16px;
}

.el-collapse-item__content {
  padding: 10px 20px;
  color: #606266;
  background-color: #fafafa;
  border-radius: 4px;
}
</style>
