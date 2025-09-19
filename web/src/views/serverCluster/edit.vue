<template>
  <div class="warpMain">
    <el-form ref="form" :model="clusterForm" label-width="80px">
      <el-form-item :label="$t('form.clusterName') + ':'">
        <el-input
          v-model="clusterForm.name"
          disabled="true"
          :placeholder="$t('form.pleaseInput')"
        />
      </el-form-item>
      <el-form-item :label="$t('form.desc') + ':'">
        <el-input
          v-model="clusterForm.desc"
          type="textarea"
          maxlength="255"
          show-word-limit
          :placeholder="$t('form.pleaseInput')"
        />
      </el-form-item>
    </el-form>
    <el-card v-if="false" class="box-card" shadow="never">
      <div class="text item">
        <!-- 节点编排计划 -->
        <el-collapse v-model="nodeCollapse" class="collapseStyle">
          <el-form
            ref="form"
            :inline="true"
            :model="clusterForm"
            label-width="140px"
          >
            <el-collapse-item
              v-for="(item, index) in clusterForm.dev_needs"
              :key="index"
              :name="index + 1"
            >
              <template slot="title">
                {{ $t("cluster.nodeRoleAndConfiguration") }} {{ index + 1 }}
              </template>

              <div class="formItem">
                <div class="formItemTitle">
                  {{ $t("cluster.nodeConfiguration") }}
                </div>
                <el-form-item label=" " style="width: 100%">
                  <el-checkbox-group v-model="item.cluster_roles">
                    <el-checkbox label="etcd">etcd</el-checkbox>
                    <el-checkbox label="controller">controller</el-checkbox>
                    <el-checkbox label="worker">worker</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </div>
              <div class="formItem">
                <div class="formItemTitle">
                  {{ $t("cluster.roleConfiguration") }}
                  ({{ item.cluster_roles.toString() }})
                </div>
                <el-form-item
                  :label="$t('cluster.number') + ':'"
                  style="width: 100%"
                >
                  <el-input-number
                    v-model="item.num"
                    controls-position="right"
                    :min="1"
                  />
                </el-form-item>
                <el-form-item
                  :label="$t('cluster.customNodeSpecification') + ':'"
                  style="width: 100%"
                >
                  <el-switch v-model="clusterDevSpec" />
                </el-form-item>
                <div v-if="item.dev_spec">
                  <div class="formSmallItem">
                    <div class="formSmallItemTitle">
                      {{ $t("cluster.cpuResourceDemand") }}
                    </div>
                    <el-form-item
                      :label="$t('cluster.number') + ':'"
                      style="width: 100%"
                    >
                      <el-input-number
                        v-model="item.dev_spec.cpu.cpu_num"
                        controls-position="right"
                        :min="0"
                      />
                    </el-form-item>
                    <el-form-item
                      :label="$t('cluster.model') + ':'"
                      style="width: 100%"
                    >
                      <el-input
                        v-model="item.dev_spec.cpu.cpu_model"
                        :placeholder="$t('form.pleaseInput')"
                      />
                    </el-form-item>
                  </div>
                  <div class="formSmallItem">
                    <div class="formSmallItemTitle">
                      {{ $t("cluster.memoryResourceDemand") }}
                    </div>
                    <el-form-item
                      :label="$t('cluster.size') + ':'"
                      style="width: 100%"
                    >
                      <el-input-number
                        v-model="item.dev_spec.mem.mem_limit"
                        controls-position="right"
                        :min="0"
                      />
                    </el-form-item>
                  </div>
                  <div class="formSmallItem">
                    <div class="formSmallItemTitle">
                      {{ $t("cluster.diskResourceDemand") }}
                    </div>
                    <el-form-item
                      :label="$t('cluster.type') + ':'"
                      style="width: 100%"
                    >
                      <el-select
                        v-model="item.dev_spec.disk.disk_type"
                        :placeholder="$t('form.pleaseSelect')"
                      >
                        <el-option label="SSD" value="SSD" />
                        <el-option label="HDD" value="HDD" />
                      </el-select>
                    </el-form-item>
                    <el-form-item
                      :label="$t('cluster.size') + ':'"
                      style="width: 100%"
                    >
                      <el-input-number
                        v-model="item.dev_spec.disk.disk_limit"
                        controls-position="right"
                        :min="0"
                      />
                    </el-form-item>
                  </div>
                  <div class="formSmallItem">
                    <div class="formSmallItemTitle">
                      {{ $t("cluster.networkBandwidthResourceDemand") }}
                    </div>
                    <el-form-item
                      :label="$t('cluster.sendBandwidth') + ':'"
                      style="width: 100%"
                    >
                      <el-input-number
                        v-model="item.dev_spec.network_band_need.transmit_band"
                        :min="0"
                        :placeholder="$t('form.pleaseInput')"
                      />
                    </el-form-item>
                    <el-form-item
                      :label="$t('cluster.receiveBandwidth') + ':'"
                      style="width: 100%"
                    >
                      <el-input-number
                        v-model="item.dev_spec.network_band_need.recv_band"
                        :min="0"
                        :placeholder="$t('form.pleaseInput')"
                      />
                    </el-form-item>
                  </div>

                  <el-form-item label="GPU" style="width: 100%">
                    <el-radio-group v-model="clustergpu">
                      <el-radio :label="true">
                        {{ $t("form.yes") }}
                      </el-radio>
                      <el-radio :label="false">
                        {{ $t("form.no") }}
                      </el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <div>
                    <el-form-item :label="$t('cluster.gpuType') + ':'">
                      <el-input
                        v-model="item.dev_spec.gpu.gpu_type"
                        type="text"
                        :placeholder="$t('form.pleaseInput')"
                      />
                    </el-form-item>
                    <el-form-item :label="$t('cluster.gpuModel') + ':'">
                      <el-input
                        v-model="item.dev_spec.gpu.gpu_model"
                        type="text"
                        :placeholder="$t('form.pleaseInput')"
                      />
                    </el-form-item>
                    <el-form-item :label="$t('cluster.gpuNumber') + ':'">
                      <el-input-number
                        v-model="item.dev_spec.gpu.gpu_num"
                        :min="0"
                        :placeholder="$t('form.pleaseInput')"
                        style="width: 130px"
                      />
                      <span
                        class="tip"
                        style="
                          line-height: 32px;
                          color: #333;
                          margin-left: 10px;
                        "
                      >
                        {{ $t("cluster.gpuNumberTip") }}
                      </span>
                    </el-form-item>
                    <el-form-item :label="$t('cluster.driverVersion') + ':'">
                      <el-input
                        v-model="item.dev_spec.gpu.driver_version"
                        type="text"
                        :placeholder="$t('form.pleaseInput')"
                      />
                    </el-form-item>
                  </div>
                  <div class="Dev_need_info_item">
                    <el-form-item label="CUDA:">
                      <el-input
                        v-model="item.dev_spec.gpu.cuda_version"
                        type="text"
                        :placeholder="$t('form.pleaseInput')"
                      />
                    </el-form-item>
                    <el-form-item label="cuDNN:">
                      <el-input
                        v-model="item.dev_spec.gpu.cudnn_version"
                        type="text"
                        :placeholder="$t('form.pleaseInput')"
                      />
                    </el-form-item>

                    <el-form-item
                      :label="$t('cluster.driverCapabilities') + ':'"
                    >
                      <el-input
                        v-model="item.dev_spec.gpu.driver_capabilities"
                        type="text"
                        :placeholder="$t('form.pleaseInput')"
                      />
                    </el-form-item>
                  </div>
                </div>
              </div>
            </el-collapse-item>
          </el-form>
        </el-collapse>
      </div>
    </el-card>
    <div class="btnMain" style="text-align: center">
      <el-button @click="cancel">{{ $t("form.cancel") }}</el-button>
      <el-button type="primary" @click="onSubmit">{{
        $t("form.confirm")
      }}</el-button>
    </div>
  </div>
</template>

<script>
import { getClustersDetail, putClustersDetail } from "@/api/mainApi";
export default {
  components: {},
  data() {
    return {
      detailMain: "", // 详情内容
      clustergpu: true, // 自定义gpu
      clusterDevSpec: true, // 自定义节点规格
      clusterForm: {
        // 集群信息
        name: "",
        desc: "",
        dev_needs: [
          {
            cluster_roles: [], // 节点配置 多选
            num: 1, // 角色配置 数量
            dev_spec: {
              cpu: {
                cpu_num: 0, // CPU资源需求 数量
                cpu_model: "", // CPU资源需求 型号
              },
              mem: {
                mem_limit: 0, // 内存资源需求 大小(MB)
              },
              disk: {
                disk_limit: 0, // 磁盘资源需求 大小(MB)
                disk_type: "", // 磁盘资源需求 类型
              },
              network_band_need: {
                transmit_band: "", // 网络带宽资源需求 发送带宽
                recv_band: "", // 网络带宽资源需求 接收带宽
              },
              gpu: {
                gpu_num: 0, // 显卡数量
                gpu_model: "", // 显卡型号
                driver_capabilities: "", // 驱动能力
                gpu_type: "nvidia", // 显卡类型
                cuda_version: "", // CUDA
                cudnn_version: "", // cuDNN
                driver_version: "", // 驱动版本
              },
            },
          },
        ], // 集群设备数量需求
      },

      nodeCollapse: ["1"], // 节点手风琴
    };
  },
  mounted() {
    this.getDetail(this.$route.params.id);
  },
  created() {},

  methods: {
    cancel() {
      this.$router.push({
        path: "/serverCluster/list",
      });
    },
    onSubmit() {
      var data = {
        desc: this.clusterForm.desc,
      };
      putClustersDetail(this.detailMain.id, data).then((res) => {
        this.$router.push({
          path: "/serverCluster/list",
        });
      });
    },
    getDetail(id) {
      // 获取详情
      getClustersDetail(id).then((res) => {
        if (res.id) {
          this.detailMain = res;
          this.clusterForm.name = res.name;
          this.clusterForm.desc = res.desc;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-collapse {
  border-top: 0px solid #ebeef5;
  border-bottom: 0px solid #ebeef5;
}

::v-deep .el-collapse-item__header {
  background-color: rgba(250, 250, 250, 1) !important;
  margin-bottom: 5px;
}

.box-card {
  border: none;

  ::v-deep .el-card__header {
    padding: 10px 20px;
    font-weight: bold;
  }
}

.el-collapse {
  border-bottom: none;

  ::v-deep .el-collapse-item__wrap {
    border-bottom: none;
  }

  &.collapseStyle {
    ::v-deep .el-collapse-item__arrow {
      margin: 0 8px 0 0;
    }
  }
}

.formItem {
  padding: 10px 20px;

  .formItemTitle {
    font-weight: bold;
    font-size: 16px;
    padding-bottom: 10px;
  }

  .formSmallItem {
    padding: 10px 20px;
    width: 50%;
    float: left;
    height: 180px;
    min-width: 400px;
    max-width: 600px;

    .formSmallItemTitle {
      font-weight: bold;
      font-size: 14px;
      padding-bottom: 10px;
    }
  }
}
</style>
