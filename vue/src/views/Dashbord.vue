<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 10px">
      <el-col :span="8">
        <el-card style="color: #409EFF">
          <div><i class="el-icon-user-solid" /> user size</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ counts.userCount}}
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card style="color: #F56C6C">
          <div><i class="el-icon-money" /> music size</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ counts.musicCount}}

          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card style="color: #E6A23C">
          <div><i class="el-icon-s-shop" /> music type size</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ counts.typeCount}}

          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10" style="margin-bottom: 10px">
      <el-col :span="24" style="">
        <el-card style="color: #409EFF;text-align: center; justify-content: center;align-items: center;r">
          <div style="padding: 10px 0; font-weight: bold">
            <div id="pie" style="width: 100%; height: 400px;"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Home",
  data() {
    return {
      activeNames: ['0'],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      tableData: [],
      myChartData: [
        // {name: "收入", value: 10000}, {name: "支出", value: 3000}
      ],
      dataId: '',
      pieChart: null, // 饼图
      counts: []
    }
  },
  mounted() {  // 页面元素渲染之后再触发

    this.pieChart = echarts.init(
        document.getElementById('pie')
    );

    this.request.get("/echarts/user").then(res => {
      this.myChartData = res.data
      this.pieEchart(this.myChartData);
    })

    this.request.get("/echarts/count").then(res => {
      this.counts = res.data
    })

  },
  methods: {
    pieEchart(data) {
      // 饼图
      this.pieChart.setOption(
          {
            title: {
              text: 'Gender ratio of users',
              left: 'center'
            },
            tooltip: {
              trigger: 'item'
            },
            legend: {
              orient: 'vertical',
              left: 'left'
            },
            series: [
              {
                type: 'pie',
                radius: '50%',
                data: data,
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          }
      );
    }
  }

}
</script>

<style scoped>

</style>
