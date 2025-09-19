<template>
  <div class="snapsAddPage h-full">
    <el-form
      ref="addVmForm"
      v-loading="loading"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      :rules="rules"
      label-width="120px"
      :element-loading-text="$t('domain.loading')"
    >
      <el-card class="!border-none mb-3 mt-2">
        <!-- <template #header>
          <div class="">
            <span>基本信息</span>
          </div>
        </template> -->
        <div class="text item">
          <el-form-item :label="$t('form.name') + ':'" prop="name">
            <span>{{ form.name }}</span>
          </el-form-item>
          <el-form-item :label="$t('form.id') + ':'" prop="instanceGroupId">
            <span>{{ form.instanceGroupId || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.description') + ':'"
            prop="description"
          >
            <span class="w-96 block">{{ form.description || "-" }}</span>
          </el-form-item>
          <el-form-item :label="$t('form.instance') + ':'" prop="description">
            <el-table
              :data="vmTableData"
              class="!overflow-y-auto"
              stripe
              :scrollbar-always-on="false"
            >
              <el-table-column prop="date" :label="$t('form.name')">
                <template #default="scope">
                  <!-- 新窗口打开 -->
                  <router-link
                    :to="'/nextStack/vm/detail/' + scope.row.instanceId"
                    target="_blank"
                  >
                    <span class="text-blue-400 cursor-pointer">{{
                      scope.row.name
                    }}</span>
                  </router-link>
                </template>
              </el-table-column>

              <el-table-column prop="hostname" :label="$t('form.hostname')" />
              <el-table-column prop="portInfo.ipAddress" :label="$t('form.ip')">
                <template #default="scope">
                  <span>{{ scope.row.portInfo.ipAddress || "-" }}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="vpcInfo.cidr"
                :label="$t('form.networkAddress')"
              >
                <template #default="scope">
                  <span>{{ scope.row.subnetInfo.cidr || "-" }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="eip" :label="$t('form.eip')">
                <template #default="scope">
                  <span v-if="!scope.row.boundPhaseStatus">{{ "-" }}</span>
                  <span
                    v-if="scope.row.boundType && scope.row.boundType === 'port'"
                  >
                    <span v-if="scope.row.boundPhaseStatus == 82">{{
                      scope.row.publicIp || scope.row.eip || "-"
                    }}</span>
                    <el-tag
                      v-else
                      :size="$store.state.nextStack.viewSize.tagStatus"
                      :type="
                        filtersFun.getVmToEipStatus(
                          scope.row.boundPhaseStatus,
                          'tag'
                        )
                      "
                      >{{
                        filtersFun.getVmToEipStatus(
                          scope.row.boundPhaseStatus,
                          "status"
                        )
                      }}</el-tag
                    >
                  </span>
                  <span
                    v-if="scope.row.boundType && scope.row.boundType === 'nat'"
                  >
                    <span v-if="scope.row.boundPhaseStatus == 7">
                      <span v-if="scope.row.eip">
                        <el-tag
                          :size="$store.state.nextStack.viewSize.tagStatus"
                          >{{ $t("form.nat") }}</el-tag
                        >
                        {{ scope.row.publicIp || scope.row.eip }}
                      </span>
                      <span v-else>-</span>
                    </span>
                    <el-tag
                      v-else
                      :size="$store.state.nextStack.viewSize.tagStatus"
                      :type="
                        filtersFun.getNatStatus(
                          scope.row.boundPhaseStatus,
                          'tag'
                        )
                      "
                      >{{
                        filtersFun.getNatStatus(
                          scope.row.boundPhaseStatus,
                          "status"
                        )
                      }}</el-tag
                    >
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="phaseStatus" :label="$t('form.status')">
                <template #default="scope">
                  <el-tag
                    :size="$store.state.nextStack.viewSize.tagStatus"
                    :type="filtersFun.getVmStatus(scope.row.phaseStatus, 'tag')"
                    >{{
                      filtersFun.getVmStatus(scope.row.phaseStatus, "status")
                    }}</el-tag
                  >
                </template>
              </el-table-column>
              <el-table-column prop="" :label="$t('form.os')">
                <template #default="scope">
                  <span v-if="scope.row.imageInfo">{{
                    scope.row.imageInfo.name ? scope.row.imageInfo.name : "-"
                  }}</span>
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="createTime"
                :label="$t('form.createTime')"
              />
            </el-table>
          </el-form-item>
        </div>
      </el-card>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
export default {
  data() {
    return {
      filtersFun: filtersFun,
      loading: false,
      form: {
        name: "",
        instanceGroupId: "",
        description: "",
      },
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
      },
      vmTableData: [],
    };
  },

  created() {
    this.getDetail();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    getDetail() {
      // 获取详情
      var id = this.$route.params.id;
      mainApi
        .vmInstanceGroupsDetail(id)
        .then((res) => {
          this.form.name = res.name;
          this.form.instanceGroupId = res.instanceGroupId;
          this.form.description = res.description;
          this.getVmsInstabcesDetail(res.instanceGroupId);
        })
        .catch((error) => {});
    },
    getVmsInstabcesDetail(id) {
      mainApi
        .vmsInstabcesList({ instance_group_id: id })
        .then((res) => {
          this.vmTableData = res.vmInstancesInfo;
        })
        .catch((error) => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.snapsAddPage {
}
</style>
