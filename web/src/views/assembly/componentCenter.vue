<template>
  <div class="warpMain">
    <div class="itemMian">
      <div v-for="(item, index) in tableData" :key="index">
        <el-col
          :key="index"
          :xs="24"
          :sm="12"
          :md="8"
          :lg="8"
          :xl="6"
          style="padding: 5px"
        >
          <el-card class="box-card" style="position: relative">
            <div class="cardHeight">
              <div style="position: relative; height: 55px">
                <div>
                  <div>
                    <img
                      src="../../assets/images/appMarket/icons8-app-48.png"
                      alt=""
                      :onerror="defaultImg"
                      class="defaultImg"
                    />
                  </div>
                  <div class="type">
                    {{ item.type }}
                  </div>
                </div>
              </div>
              <div class="configs">
                <el-popover
                  placement="top-start"
                  :width="312"
                  popper-class="tablepopover"
                  trigger="hover"
                  :content="item.configs"
                >
                  <span slot="reference">
                    {{ item.configs ? item.configs : "-" }}
                  </span>
                </el-popover>
              </div>
              <!-- <div class="configs"
                   v-else>
                <span> {{ item.configs?item.configs:"-" }}
                </span>

              </div> -->
              <div style="margin-top: 15px; color: #79879c">
                <span class="buttons">
                  <el-button
                    type="primary"
                    size="mini"
                    icon="el-icon-plus"
                    @click="pushcomponentadd(item, item.id)"
                    >{{ $t("form.install") }}</el-button
                  >
                </span>

                <span v-if="item.nowVersion" class="version"
                  ><span>{{ $t("form.version") }}：</span>
                  <span>
                    <el-select
                      v-model="item.nowVersion"
                      :placeholder="$t('form.pleaseSelectVersion')"
                      size="mini"
                      style="width: 65%"
                      @change="changeVersions(item, $event)"
                    >
                      <el-option
                        v-for="item in item.versions"
                        :key="item.id"
                        :label="item.version"
                        :value="item.id"
                      />
                    </el-select>
                  </span>
                </span>
              </div>
            </div>
          </el-card>
        </el-col>
      </div>
    </div>
  </div>
</template>

<script>
import { templatesTags } from "@/api/mainApi";
import initData from "@/mixins/initData";
import { mapGetters } from "vuex";

export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      sup_this: this,
      defaultImg:
        'this.src="' +
        require("../../assets/images/appMarket/icons8-app-48.png") +
        '"',
      tableData: [
        // {
        //   id: "1",
        //   type: "轻量级节点",
        //   configs:
        //     "采集边缘轻量化节点及节点上容器的监控数据，如CPU、内存、磁盘IOPS和网络吞吐等。",
        //   targets: "监控目标",
        //   labels: "标签",
        // },
        {
          id: "2",
          type: this.$t("message.componentCenter.tableData.data2.type"),
          configs: this.$t("message.componentCenter.tableData.data2.configs"),
          targets: this.$t("message.componentCenter.tableData.data2.targets"),
          labels: this.$t("message.componentCenter.tableData.data2.labels"),
        },
        // {
        //   id: "3",
        //   type: "OpenStack云监控",
        //   configs: "采集OpenStack云节点及实例的监控数据，如CPU、内存、磁盘等。",
        //   targets: "监控目标",
        //   labels: "标签",
        // },
        // {
        //   id: "4",
        //   type: "GPU监控",
        //   configs: "采集节点GPU资源的监控数据，如核心、显存负载等。",
        //   targets: "监控目标",
        //   labels: "标签",
        // },
        // {
        //   id: "5",
        //   type: "从模板安装",
        //   configs: "采集节点GPU资源的监控数据，如核心、显存负载等。",
        //   targets: "监控目标",
        //   labels: "标签",
        // },
        // {
        //   id: "6",
        //   type: "自定义",
        //   configs: "采集节点GPU资源的监控数据，如核心、显存负载等。",
        //   targets: "监控目标",
        //   labels: "标签",
        // },
      ],
    };
  },

  created() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  mounted() {
    this.initTemplates();
  },

  methods: {
    async initTemplates() {
      const list = await templatesTags("prometheus_monitor");
      list.templates.forEach((item, index2) => {
        this.tableData.push({
          id: item.id,
          type: item.name,
          configs: item.versions[0].description
            ? item.versions[0].description
            : "-",
          targets: this.$t("message.monitoringTarget"),
          labels: item.versions[0].labels,
          nowVersion: item.versions[0].id,
          versions: item.versions,
        });
      });
      // console.log(this.tableData)
    },
    pushcomponentadd(item, id) {
      if (item.versions) {
        this.$router.push(
          `/component/installationaddTemplate/${item.nowVersion}/${item.type}`
        );
      } else {
        this.$router.push(`/component/installationadd/${id}`);
      }
    },
    // 版本选择后触发
    changeVersions(value, e) {
      value.versions.forEach((item, index) => {
        if (item.id == e) {
          (value.configs = item.description),
            (value.id = item.id),
            (value.labels = item.labels),
            (value.nowVersion = e),
            (value.type = item.name);
        }
      });
      // 强制更新
      this.$forceUpdate();
    },
  },
};
</script>

<style lang="scss" scoped>
.itemMian {
  overflow: hidden;

  .el-card {
    ::v-deep .el-card__body {
      padding: 15px 20px;
    }
  }
}

.defaultImg {
  width: 36px;
  height: 36px;
  line-height: 36px;
  left: 0;
  position: absolute;
  top: 50%;
  -webkit-transform: translateY(-50%);
  -ms-transform: translateY(-50%);
  transform: translateY(-50%);
}

.cardHeight {
  height: 165px;
  padding: 15px 24px;
  border-radius: 4px;
  background-color: #fff;
}

.type {
  color: #242e42;
  font-weight: 500;
  font-size: 14px;
  position: absolute;
  left: 60px;
  top: 16px;
}

.configs {
  color: #79879c;
  font-size: 14px;
  height: 40px;
  text-overflow: ellipsis;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  margin-top: 5px;
  word-break: break-all;
}

.buttons {
  -o-text-overflow: ellipsis;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-wrap: normal;
  overflow: hidden;
  font-size: 14px;
  width: 60%;
  float: left;
  margin-bottom: 10px;
}

.version {
  float: right;
  -o-text-overflow: ellipsis;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-wrap: normal;
  overflow: hidden;
  font-size: 14px;
  position: absolute;
  left: 48%;
  max-width: 200px;
}

.btn1 {
  margin: 0 auto;
  display: block;
}

.tempdialog {
  .el-form-item {
    margin-bottom: 0px;
  }
}

.icon_cz {
  display: block;
  height: 24px;
  color: #666666;
  align-items: center;
  cursor: pointer;
  margin-top: 5px;
  margin-bottom: 5px;
  line-height: 24px;
  text-align: center;
}

.icon_cz:hover {
  background: #daefff;
}

.cz_icon {
  margin-left: 16px;
  margin-right: 23px;
}

.czlb {
  position: absolute;
  top: 8px;
  right: 4px;
}

.czbtn {
  width: 23px;
  height: 26px;
  border: 0px solid #d0d0d0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.left_czbtn {
  border-radius: 3px 0 0 3px;
  border-right: 0px;
  cursor: pointer;
}

.left_czbtn img {
  height: 11px;
}

.right_czbtn {
  border-radius: 3px;
}
</style>
