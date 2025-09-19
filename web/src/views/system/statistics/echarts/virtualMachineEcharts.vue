<template>
  <div>
    <div
      :id="lineEchartID"
      :class="className"
      :style="{ height: height, width: width }"
      style="min-height: 110px"
    />
  </div>
</template>
<script>
import * as echarts from "echarts";
export default {
  props: {
    className: {
      type: String,
      default: "chart",
    },
    lineEchartID: {
      type: String,
      default: "chart",
    },
    width: {
      type: String,
      default: "200px",
    },
    height: {
      type: String,
      default: "200px",
    },
    curData: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      chart: null,
    };
  },
  watch: {
    curData(val) {
      this.initChart();
    },
  },
  mounted() {
    this.chart = echarts.init(document.getElementById(this.lineEchartID));
    this.initChart();
    window.addEventListener("resize", () => {
      this.chart.resize();
    });
  },

  methods: {
    initChart() {
      var xAxisData = [];
      var yAxisData = [];
      var lengendData = [];
      if (this.curData && this.curData.length > 0) {
        this.curData.forEach((element, index) => {
          xAxisData.push(element.alert_day);
          if (index == 0) {
            Object.keys(element.alert_log_level_count).forEach((item) => {
              lengendData.push(item);

              yAxisData.push({
                name: item,
                type: "line",
                stack: "Total",
                data: [element.alert_log_level_count[item]],
              });
            });
          } else {
            Object.keys(element.alert_log_level_count).forEach((item) => {
              // yAxisData[index].name等于 item的数据
              yAxisData
                .find((item1) => {
                  return item1.name == item;
                })
                .data.push(element.alert_log_level_count[item]);
            });
          }
        });
      }

      this.chart.setOption({
        tooltip: {
          trigger: "axis",
        },
        legend: {
          data: lengendData,
          right: "0%",
          top: -5,
        },
        color: [
          "#26A7FF",
          "#F06D34",
          "#FFAD26",
          "#67c23a",
          "#f5c874",
          "#5BD94C",
          "#E1447E",
          "#F5D930",
          "#524ADB",
          "#9a60b4",
          "#ea7ccc",
        ],
        grid: {
          left: "5%",
          top: "12%",
          right: "5%",
          bottom: "20px",
          containLabel: true,
        },
        xAxis: {
          data: xAxisData,
          // data: xAxisData,
          axisLine: {
            lineStyle: {
              color: "#9ca3af",
            },
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            show: true,
            color: "#333",
            fontSize: 13,
            showMaxLabel: true,
            formatter: function (value) {
              var str =
                "{name|" + value.split(" ")[1] + "}{time|" + ":" + "}00";
              var str2 =
                "\n" +
                "{value|" +
                value.split(" ")[0].split("/")[0] +
                "}{time2|" +
                "/" +
                "}{value|" +
                value.split(" ")[0].split("/")[1] +
                "}";
              return str + str2;
            },
            rich: {
              name: {
                fontSize: 13,
              },
              time: {
                padding: [0, 1.8, 0, 1],
              },
              time2: {
                padding: [0, 0.7, 0, 0.7],
              },
              value: {
                fontSize: 13,
                lineHeight: 18,
              },
            },
          },
          axisLabel: {
            show: true,
            color: "#333",
            interval: 3,
            fontSize: 13,
            showMaxLabel: true,
          },
        },
        yAxis: {
          type: "value",
          nameTextStyle: {
            color: "rgba(116, 114, 114, 1)",
            fontSize: 13,
          },
          minInterval: 1,
          // y轴线颜色

          axisLine: {
            lineStyle: {
              color: "#f5f5f5",
            },
          },
          // y轴字体颜色
          axisLabel: {
            color: "#333",
          },
          // y轴分割线颜色
          splitLine: {
            lineStyle: {
              color: "#f5f5f5",
            },
          },
        },
        series: yAxisData,
      });
    },
  },
};
</script>
