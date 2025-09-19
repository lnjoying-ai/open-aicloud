<template>
  <div>
    <div :id="id" :class="className" :style="{ height: height, width: width }" style="min-height: 435px" />
  </div>
</template>

<script>
import * as echarts from "echarts";
import "echarts-gl";
import geoJson from "../js/geo";
// console.log(geoJson);
echarts.registerMap("china", geoJson);

export default {
  props: {
    className: {
      type: String,
      default: "chart",
    },
    id: {
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
  data () {
    return {
      chart: null,
      location: {
        longitude: 115.114129,
        latitude: 37.550339,
      },
    };
  },
  mounted () {
    // 获取客户端当前经纬度
    this.chart = echarts.init(document.getElementById(this.id));
    navigator.geolocation.getCurrentPosition((position) => {
      this.location.longitude = position.coords.longitude;
      this.location.latitude = position.coords.latitude;
      this.initChart();

    });

    this.initChart();
    window.addEventListener("resize", () => {
      this.chart.resize();
    });
  },
  watch: {
    curData (val) {
      this.initChart();
    },
  },

  methods: {
    listenerZoom () {

      this.chart.on('georoam', (params) => {
        var zoom = this.chart.getOption().geo[0].zoom;
        // 如果zoom大于5，symbolSize为10，否则为20
        var symbolSize = 10;
        if (zoom < 7) {
          symbolSize = 10;
        } else if (zoom < 14) {
          symbolSize = 12;
        } else if (zoom < 20) {
          symbolSize = 14;
        } else if (zoom < 30) {
          symbolSize = 16;
        } else if (zoom < 40) {
          symbolSize = 18;
        } else {
          symbolSize = 20;
        }
        this.chart.setOption({
          series: [
            {
              symbolSize: symbolSize,
            },
          ],
        });

      });
    },
    initChart () {
      var charts = this.curData.map((item) => {
        return {
          name: item.name,
          node_num: item.node_num,
          cloud_num: item.cloud_num,
          value: [item.location.longitude, item.location.latitude],
        };
      });
      this.chart.setOption({
        backgroundColor: "#fff",
        title: {
          text: " ",
          left: "center",
          textStyle: {
            color: "#fff",
          },
        },
        tooltip: {
          trigger: "item",
          formatter: (params) => {
            console.log(params)
            // 显示name node_num cloud_num
            var html = "";
            html += `<div style="font-size: 14px;">${params.name}</div>`;
            html += `<div style="font-size: 14px;">节点数量：${params.data.node_num}</div>`;
            html += `<div style="font-size: 14px;">云数量：${params.data.cloud_num}</div>`;
            return html;

          },

        },
        geo: {
          map: "china",
          roam: true,
          center: [this.location.longitude, this.location.latitude],
          zoom: 12,
          top: "9%",
          scaleLimit: {
            min: 1,
            max: 50,
          },
          silent: true,


          itemStyle: {
            normal: {
              areaColor: "rgba(135,206,250,0.4)",
              borderColor: "rgba(135,206,250,1)",
            },
          },
        },
        series: [
          {
            type: "scatter",
            coordinateSystem: "geo",
            data: charts,

            symbol: "image://data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAAXNSR0IArs4c6QAAAddJREFUaEPtmk2OgzAMhZ2btXfqrKezbu9UerKMIDCNIODn2CaDRDdUahT8+ec5pgQy/MQHXSjQd7blZfzeDddI7/4Svuhuddug3SgzejIW2zLSjwWICiA+6D7zOGZ8vkoJUgVQ7fV1vI4SSEo1wUcMMBr/EtwDXxrpKoUQAbgaP2EKIWQAT4q4O+tXhhvBdsELTQoWZ+rCja7IcghgZ+OT3WAq/V8AIigKGMBOub9IGSAKLECT9PmQsFHgAVp5P0EcHoA4Sd2MwC6Ni9NKpg5OAM6B6t/PCATyOXmiodFEYOjobWVUp0IjQB8B2biIepdfZ9AH0qDeJo2AcZPvxA0BuCbWB5AFaJZGgPdxgAZRQLwPAwxRsHiEwhftNMzAz4ygFPqbt59DMXsrEqs8uR9EADvUg8h4UQrl1NEnEmLjqwEcIlFlvArAEKLaeD2AgbyicrkmYOIinm+kmtqApw6c8qoBqnsE2Gl3ARBDGBmvroFFOoHyqs17VSPbCilUD4beN48AK63GxvsAbB36DgKwPsEZyOY8hU1ktFDMxX9yLIt3uqcXwPLY7ZA+LjWwWsiHAigV8glQ7kA+NVA6pTookF8NFAA8FMgNYHa469+DeFu8mVJKol8PpNcxsbo9kwAAAABJRU5ErkJggg==",
            symbolSize: 10,
            // symbol的位置
            symbolOffset: [0, "-50%"],



            label: {
              normal: {
                formatter: (params) => {
                  return params.data.name;
                },
                position: 'right',
                // 对齐方式
                // align: "left",
                show: false,
                textStyle: {
                  color: "#000",
                  fontWeight: "bold",
                },
              },
            },
            itemStyle: {
              // color: "rgb(20, 15, 2)",
              color: "#f00",

            },

          },

        ],
      });
      this.listenerZoom();
    },
  },
};
</script>
