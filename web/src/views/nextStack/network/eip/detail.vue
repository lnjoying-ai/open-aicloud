<template>
  <div class="subNetAddPage h-full">
    <el-form
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      label-width="160px"
    >
      <el-card class="!border-none mb-3">
        <template #header>
          <div class="">
            <span>{{ $t("form.basicInfo") }}</span>
          </div>
        </template>
        <div class="text item">
          <el-form-item :label="$t('form.name') + ':'">
            <span>{{ form.name || "-" }}</span>
          </el-form-item>

          <el-form-item :label="$t('form.description') + ':'">
            <span>
              {{ form.description || "-" }}
            </span>
          </el-form-item>
          <el-form-item :label="$t('form.updateTime') + ':'">
            <span>
              {{ form.updateTime || "-" }}
            </span>
          </el-form-item>
          <el-form-item :label="$t('form.createTime') + ':'">
            <span>
              {{ form.createTime || "-" }}
            </span>
          </el-form-item>
        </div>
      </el-card>
      <el-card class="!border-none mb-3">
        <template #header>
          <div class="">
            <span>{{ $t("form.basicInfo") }}</span>
          </div>
        </template>
        <div class="text item">
          <el-form-item :label="$t('form.subnetName') + ':'">
            <span>{{ form.name || "-" }}</span>
          </el-form-item>
          <el-form-item :label="$t('form.subnetId') + ':'">
            <span>{{ form.subnetId || "-" }}</span>
          </el-form-item>
          <el-form-item :label="$t('form.subnetCidr') + ':'">
            <span>{{ form.cidr || "-" }}</span>
          </el-form-item>
          <el-form-item :label="$t('form.gateway') + ':'">
            <span>{{ form.gatewayIp || "-" }}</span>
          </el-form-item>
          <el-form-item :label="$t('form.status') + ':'">
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="filtersFun.getStatus(form.phaseStatus, 'tag')"
              >{{ filtersFun.getStatus(form.phaseStatus, "status") }}</el-tag
            >
          </el-form-item>
          <el-form-item :label="$t('form.createTime') + ':'">
            <span>{{ form.createTime || "-" }}</span>
          </el-form-item>
          <el-form-item :label="$t('form.updateTime') + ':'">
            <span>{{ form.updateTime || "-" }}</span>
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
  components: {},
  data() {
    return {};
  },
  created() {},
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {},
};

const router: any = useRouter();
const form: any = ref({});

const goBack = () => {
  router.push("/eip");
};
const getDetail = () => {
  // 获取详情

  mainApi
    .subnetsDetail(router.currentRoute.value.params.id)
    .then((res: any) => {
      form.value = res;
    })
    .catch((error: any) => {});
};
onMounted(() => {
  getDetail(); // 获取详情
});
</script>

<style lang="scss" scoped>
.subNetAddPage {
}
</style>
