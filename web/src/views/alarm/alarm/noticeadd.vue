<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="isAdd ? $t('form.addNoticeObject') : $t('form.editNoticeObject')"
      width="600px"
      :close-on-click-modal="false"
      :before-close="cancel"
      @open="onOpen"
    >
      <el-form
        ref="noticeAddForm"
        :rules="rules"
        :model="fromdata"
        label-width="100px"
        size="small"
        class="mt-2"
      >
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input v-model="fromdata.name" />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" prop="description">
          <el-input
            v-model="fromdata.description"
            type="textarea"
            maxlength="255"
            show-word-limit
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.userName') + ':'"
          prop="receiver_user_id"
        >
          <el-select
            v-model="fromdata.receiver_user_id"
            :placeholder="$t('form.pleaseSelectUser')"
            clearable
            filterable
            style="width: 100%"
            value-key="item"
            @change="handleUserChange"
          >
            <el-option
              v-for="(item, index) in usersData"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.notifyType') + ':'" prop="notify_type">
          <el-checkbox-group
            v-model="fromdata.notify_type"
            @change="handleNotify_typeChange"
          >
            <el-checkbox label="0">{{ $t("form.email") }}</el-checkbox>
            <el-checkbox label="1">{{ $t("form.phone") }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item :label="$t('form.configInfo') + ':'" prop="config">
          <el-input v-model="fromdata.config" />
        </el-form-item>
        <el-form-item
          v-if="fromdata.notify_type.includes('0')"
          :label="$t('form.email') + ':'"
          prop="email"
        >
          <el-input v-model="fromdata.email" />
        </el-form-item>
        <el-form-item
          v-if="fromdata.notify_type.includes('1')"
          :label="$t('form.phone') + ':'"
          prop="phone"
        >
          <el-input v-model="fromdata.phone" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" size="small" @click="cancel">{{
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
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { addReceivers, editReceivers, getUsers } from "@/api/mainApi";
export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    info: Object,
  },
  data() {
    const validatePhone = (rule, value, callback) => {
      if (!/^1[35789]\d{9}$/.test(value)) {
        callback(new Error(this.$t("form.pleaseInputPhoneTips")));
      }
      callback();
    };
    return {
      // 用户的列表
      usersData: [],
      id: "",
      isAdd: true,
      rules: {
        name: [
          {
            required: true,
            message: this.$t("form.pleaseInputName"),
            trigger: "blur",
          },
        ],
        email: [
          {
            required: true,
            message: this.$t("form.pleaseInputEmail"),
            trigger: "blur",
          },
          {
            type: "email",
            message: this.$t("form.pleaseInputEmailTips"),
            trigger: "blur",
          },
        ],
        phone: [
          {
            required: true,
            message: this.$t("form.pleaseInputPhone"),
            trigger: "blur",
          },
          {
            validator: validatePhone,
            message: this.$t("form.pleaseInputPhoneTips"),
            trigger: "blur",
          },
        ],
        notify_type: [
          {
            required: true,
            message: this.$t("form.pleaseSelectNotifyType"),
            trigger: "blur",
          },
        ],
      },
      dialog: false,
      loading: false,
      fromdata: {
        config: "",
        name: "",
        description: "",
        receiver_user_id: "",
        notify_type: [],
        email: "",
        phone: "",
      },
    };
  },
  watch: {
    "fromdata.notify_type": {
      deep: true,
      handler: function (newVal, oldVal) {
        // let array = JSON.parse(JSON.stringify(newVal));
        // if (!(0 in array)) {
        //   this.fromdata.email = "";
        // }
        // if (!(1 in array)) {
        //   this.fromdata.phone = "";
        // }
      },
    },
  },
  computed: {
    ...mapGetters(["kind", "userInfo", "filesize"]),
  },
  created() {},
  mounted() {
    this.usersInit();
  },
  methods: {
    handleNotify_typeChange(value) {
      console.log(value);
      console.log(this.fromdata);
    },
    // 当我改变用户的时候回显它的邮箱和手机号
    async handleUserChange(val) {
      const selectedItem = this.usersData.find((item) => item.id === val);
      if (selectedItem.contact_info.phone) {
        this.fromdata.notify_type = ["1"];
      }
      if (selectedItem.contact_info.email) {
        this.fromdata.notify_type = ["0"];
      }
      if (selectedItem.contact_info.phone && selectedItem.contact_info.email) {
        this.fromdata.notify_type = ["0", "1"];
      }
      this.fromdata.phone = selectedItem.contact_info.phone;
      this.fromdata.email = selectedItem.contact_info.email;
    },
    async usersInit() {
      const listUsers = await getUsers();
      this.usersData = listUsers.users;
    },
    resetFrom() {
      this.$refs["noticeAddForm"].resetFields();
    },
    cancel() {
      this.dialog = false;
      this.$refs["noticeAddForm"].resetFields();
    },
    createNotice() {
      this.loading = true;
      var data = {
        config: this.fromdata.config,
        name: this.fromdata.name,
        description: this.fromdata.description,
        receiver_user_id: this.fromdata.receiver_user_id,
        notify_type: this.fromdata.notify_type.join(","),
        email: this.fromdata.email,
        phone: this.fromdata.phone,
      };
      addReceivers(data)
        .then((res) => {
          this.$notify({
            title: this.$t("message.createSuccess"),
            type: "success",
            duration: 2500,
          });
          this.resetFrom();
          this.loading = false;
          this.dialog = false;
          this.$parent.init_notice();
        })
        .catch(() => {
          this.loading = false;
        });
    },
    updateNotice() {
      this.loading = true;
      var data = {
        config: this.fromdata.config ? this.fromdata.config : "",
        name: this.fromdata.name ? this.fromdata.name : "",
        description: this.fromdata.description ? this.fromdata.description : "",
        receiver_user_id: this.fromdata.receiver_user_id
          ? this.fromdata.receiver_user_id
          : "",
        notify_type: this.fromdata.notify_type.join(","),
        email: this.fromdata.email ? this.fromdata.email : "",
        phone: this.fromdata.phone ? this.fromdata.phone : "",
      };
      editReceivers(this.info.id, data)
        .then((res) => {
          this.$notify({
            title: this.$t("message.editSuccess"),
            type: "success",
            duration: 2500,
          });
          this.resetFrom();
          this.loading = false;
          this.dialog = false;
          this.$parent.init_notice();
        })
        .catch(() => {
          this.loading = false;
        });
    },
    doSubmit() {
      this.$refs["noticeAddForm"].validate((valid) => {
        if (valid) {
          if (this.isAdd) {
            this.createNotice();
          } else {
            this.updateNotice();
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    onOpen() {
      if (this.isAdd) {
        this.fromdata = {
          config: "",
          name: "",
          description: "",
          receiver_user_id: "",
          notify_type: [],
          email: "",
          phone: "",
        };
        this.$refs["noticeAddForm"].resetFields();
      } else {
        var data = JSON.parse(JSON.stringify(this.info));
        this.fromdata.config = data.config ? data.config : "";
        this.fromdata.name = data.name ? data.name : "";
        this.fromdata.description = data.description ? data.description : "";
        this.fromdata.receiver_user_id = data.iam_user_id
          ? data.iam_user_id
          : "";
        this.fromdata.notify_type = data.notify_type
          ? data.notify_type.split(",")
          : [];
        this.fromdata.email = data.email ? data.email : "";
        this.fromdata.phone = data.phone ? data.phone : "";
      }
    },
  },
};
</script>

<style lang="scss" scoped></style>
