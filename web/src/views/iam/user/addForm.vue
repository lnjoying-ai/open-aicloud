<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="108px"
      >
        <el-form-item :label="$t('form.userName') + ':'" prop="name">
          <el-input
            v-model="form.name"
            :placeholder="$t('form.pleaseInputUserName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.loginPassword') + ':'" prop="password">
          <el-input
            v-model="form.password"
            :placeholder="$t('form.pleaseInputLoginPassword')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.email') + ':'"
          prop="contact_info.email"
          :rules="[
            {
              required: true,
              message: $t('form.pleaseInputEmail'),
              trigger: 'blur',
            },
            {
              pattern:
                /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{1,6}$/,
              message: $t('message.pleaseInputCorrectEmailFormat'),
              trigger: 'blur',
            },
          ]"
        >
          <el-input
            v-model="form.contact_info.email"
            :placeholder="$t('form.pleaseInputEmail')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.phone') + ':'"
          prop="contact_info.phone"
          :rules="[
            {
              required: true,
              message: $t('form.pleaseInputPhone'),
              trigger: 'blur',
            },
            {
              pattern: /^1[3-9]\d{9}$/,
              message: $t('message.pleaseInputCorrectPhoneNumber'),
              trigger: 'blur',
            },
          ]"
        >
          <el-input
            v-model="form.contact_info.phone"
            :placeholder="$t('form.pleaseInputPhone')"
          />
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.belongOrganization') + ':'"
          prop="bp_id"
        >
          <el-select
            v-model="form.bp_id"
            :placeholder="$t('form.pleaseSelectBelongOrganization')"
            style="width: 100%"
          >
            <el-option
              v-for="item in bpsData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="mb-2">
      <empowerVue
        ref="empowerVue"
        :sup_this="sup_this"
        :is-add="true"
        :authorize-policy="[]"
        :authorize-role="[]"
      />
    </el-card>
    <el-card class="mb-2">
      <div class="flex justify-center px-4">
        <el-button size="small" type="text" @click="resetForm">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          :loading="loading"
          type="primary"
          size="small"
          @click="doSubmit"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-card>
  </div>
</template>

<script>
import { addUsers } from "@/api/iam/user";
import { getBps } from "@/api/iam/partners";
import { mapGetters } from "vuex";
import empowerVue from "./components/empower.vue";

export default {
  created() {
    this.bpsinit();
  },
  mounted() {},
  computed: {
    ...mapGetters(["userInfo"]),
  },
  components: { empowerVue },
  data() {
    return {
      sup_this: this,
      loading: false,
      bpsData: [],
      form: {
        name: "",
        password: "",
        bp_id: "",
        contact_info: {
          email: "",
          phone: "",
        },
      },
      rules: {
        name: [
          {
            required: true,
            message: this.$t("form.pleaseInputUserName"),
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: this.$t("form.pleaseInputLoginPassword"),
            trigger: "blur",
          },
        ],
        bp_id: [
          {
            required: true,
            message: this.$t("form.pleaseSelectBelongOrganization"),
            trigger: "change",
          },
        ],
      },
    };
  },
  methods: {
    // 组织机构
    async bpsinit() {
      const list = await getBps();
      this.bpsData = list.bps;
    },
    doSubmit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loading = true;
          this.doAdd();
        } else {
          return false;
        }
      });
    },
    doAdd() {
      this.userInfo.kind != 0 && this.userInfo.kind != 1
        ? (this.form.bp_id = this.userInfo.bp_id)
        : "";
      var data = JSON.parse(JSON.stringify(this.form));
      addUsers(data)
        .then((res) => {
          this.$refs.empowerVue.AuthorzUserAdd(res.id);
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.resetForm();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    resetForm() {
      this.form = {
        name: "",
        password: "",
        bp_id: "",
        contact_info: {
          email: "",
          phone: "",
        },
      };
      this.$router.push({
        path: "/iam/user",
      });
    },
  },
};
</script>
<style></style>
