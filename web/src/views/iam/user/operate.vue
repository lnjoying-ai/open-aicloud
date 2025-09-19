<template>
  <div class="operatePage">
    <div v-if="propShowType == 1">
      <el-popover placement="bottom" width="140" trigger="click">
        <div
          v-if="propuserDetail.status == 0"
          class="icon_cz"
          @click="activation(propuserDetail)"
        >
          <i class="el-icon-unlock" />
          {{ $t("form.activation") }}
        </div>
        <div
          v-if="propuserDetail.status == 1"
          class="icon_cz"
          @click="locking(propuserDetail)"
        >
          <i class="el-icon-lock" />
          {{ $t("form.lock") }}
        </div>
        <div class="icon_cz" @click="openuser">
          <i class="el-icon-edit" />
          {{ $t("form.edit") }}
        </div>
        <div class="icon_cz" @click="dialogFormpassword = true">
          <img
            src="@/assets/nextStack/btn/resetPassword.png"
            class="w-3 inline-block mr-1"
            alt=""
          />
          {{ $t("form.modifyPassword") }}
        </div>
        <div class="icon_cz" @click="clickDelBtn(propuserDetail)">
          <i class="el-icon-delete" />
          {{ $t("form.delete") }}
        </div>
        <el-button
          slot="reference"
          icon="el-icon-more"
          class="czbtn right_czbtn"
        />
      </el-popover>
    </div>
    <div v-if="propShowType == 2">
      <el-button
        v-if="propuserDetail.status == 0"
        icon="el-icon-unlock"
        size="small"
        @click="activation(propuserDetail)"
        >{{ $t("form.activation") }}</el-button
      >
      <el-button
        v-if="propuserDetail.status == 1"
        icon="el-icon-lock"
        size="small"
        @click="locking(propuserDetail)"
        >{{ $t("form.lock") }}</el-button
      >
      <el-button icon="el-icon-edit" size="small" @click="openuser">{{
        $t("form.edit")
      }}</el-button>
      <el-button size="small" @click="dialogFormpassword = true"
        ><img
          src="@/assets/nextStack/btn/resetPassword.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.modifyPassword") }}</el-button
      >
      <el-button
        icon="el-icon-delete"
        size="small"
        @click="clickDelBtn(propuserDetail)"
        >{{ $t("form.delete") }}</el-button
      >
    </div>
    <!-- 修改密码 -->
    <el-dialog
      :visible.sync="dialogFormpassword"
      :close-on-click-modal="false"
      width="600px"
      destroy-on-close
      :before-close="passwordClose"
      :append-to-body="true"
      :title="$t('form.modifyPassword')"
    >
      <el-form
        ref="passwordform"
        :model="passwordform"
        :rules="passwordrules"
        :size="$store.state.nextStack.viewSize.main"
        label-width="80px"
        :element-loading-text="$t('domain.loading')"
      >
        <el-form-item
          :label="$t('form.loginPassword') + ':'"
          prop="newpassword"
        >
          <el-input
            v-model="passwordform.newpassword"
            :placeholder="$t('form.pleaseInputLoginPassword')"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button
            :loading="loadingpassword"
            type="primary"
            size="small"
            @click="changepassword(propuserDetail)"
            >{{ $t("form.confirm") }}</el-button
          >
        </span>
      </template>
    </el-dialog>
    <!-- 编辑用户 -->
    <el-dialog
      :visible.sync="dialogFormuser"
      :close-on-click-modal="false"
      width="600px"
      destroy-on-close
      :before-close="userClose"
      :append-to-body="true"
      :title="$t('form.editUser')"
    >
      <el-form
        ref="userform"
        :model="userdata"
        :rules="userrules"
        :size="$store.state.nextStack.viewSize.main"
        label-width="120px"
        :element-loading-text="$t('domain.loading')"
      >
        <el-form-item :label="$t('form.userName') + ':'" prop="name">
          <el-input
            v-model="userdata.name"
            :placeholder="$t('form.pleaseInputUserName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.contactEmail') + ':'" prop="email">
          <div v-if="userdata.contact_info">
            <el-input
              v-model="userdata.contact_info.email"
              :placeholder="$t('form.pleaseInputEmail')"
            />
          </div>
        </el-form-item>
        <el-form-item :label="$t('form.phone') + ':'" prop="phone">
          <div v-if="userdata.contact_info">
            <el-input
              v-model="userdata.contact_info.phone"
              :placeholder="$t('form.pleaseInputPhone')"
            />
          </div>
        </el-form-item>
        <el-form-item :label="$t('form.belongOrganization') + ':'" prop="bp_id">
          <el-select
            v-model="userdata.bp_id"
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
      <template #footer>
        <span class="dialog-footer">
          <el-button
            :loading="loadinguser"
            type="primary"
            size="small"
            @click="changeuser(userdata)"
            >{{ $t("form.confirm") }}</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { editUsers, delUsers } from "@/api/iam/user";
import { getBps } from "@/api/iam/partners";
export default {
  components: {},
  props: {
    propShowType: {
      type: Number,
      default: 1,
    },
    propuserDetail: {
      type: Object,
      default: {},
    },
  },
  data() {
    var validateEmail = (rule, value, callback) => {
      if (this.userdata.contact_info.email === "") {
        callback(new Error(this.$t("form.pleaseInputEmail")));
      } else {
        const emailPattern =
          /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{1,6}$/;
        if (!emailPattern.test(this.userdata.contact_info.email)) {
          callback(new Error(this.$t("form.pleaseInputEmailTips")));
        } else {
          callback();
        }
      }
    };
    var validatePhone = (rule, value, callback) => {
      if (this.userdata.contact_info.phone === "") {
        callback(new Error(this.$t("form.pleaseInputPhone")));
      } else {
        const phonePattern = /^1[3-9]\d{9}$/;
        if (!phonePattern.test(this.userdata.contact_info.phone)) {
          callback(new Error(this.$t("form.pleaseInputPhoneTips")));
        } else {
          callback();
        }
      }
    };
    return {
      // 修改密码的弹框
      dialogFormpassword: false,
      // 修改密码内容
      passwordform: {
        newpassword: "",
      },
      // 修改密码的校验
      passwordrules: {
        newpassword: [
          {
            required: true,
            message: this.$t("form.pleaseInputLoginPassword"),
            trigger: "blur",
          },
        ],
      },
      // 编辑用户的校验
      userrules: {
        name: [
          {
            required: true,
            message: this.$t("form.pleaseInputUserName"),
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
        email: [{ required: true, validator: validateEmail, trigger: "blur" }],
        phone: [{ required: true, validator: validatePhone, trigger: "blur" }],
      },
      // 修改密码确定的加载效果
      loadingpassword: false,
      // 修改用户的加载效果
      loadinguser: false,
      // 编辑用户的弹框
      dialogFormuser: false,
      // 组织列表
      bpsData: [],
      // 用户的数据
      userdata: {},
    };
  },
  created() {},
  mounted() {
    // this.bpsinit();
  },
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    // 删除用户
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteUser"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delUsers(value.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              if (this.propShowType == 1) {
                this.$emit("getuserList");
              } else if (this.propShowType == 2) {
                this.$router.push("/iam/user");
              }
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    // 激活用户
    activation(value) {
      this.$confirm(
        this.$t("message.confirmActivateUser"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          value.status = 1;
          value.is_allowed ? (value.is_allowed = 1) : (value.is_allowed = 0);
          editUsers(value.id, value).then((res) => {
            this.$notify({
              title: this.$t("message.activateSuccess"),
              type: "success",
            });
            this.$emit("getuserList");
          });
        })
        .catch(() => {});
    },
    // 锁定用户
    locking(value) {
      this.$confirm(
        this.$t("message.confirmLockUser"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          value.status = 0;
          value.is_allowed ? (value.is_allowed = 1) : (value.is_allowed = 0);
          editUsers(value.id, value).then((res) => {
            this.$notify({
              title: this.$t("message.lockSuccess"),
              type: "success",
            });
            this.init();
          });
        })
        .catch(() => {});
    },
    // 密码框的重置
    restpassword() {
      this.$refs["passwordform"].resetFields();
      this.passwordfor = {
        newpassword: "",
      };
    },
    // 关闭密码弹框
    passwordClose() {
      this.dialogFormpassword = false;
      this.restpassword();
    },
    // 修改密码
    changepassword(value) {
      this.$refs["passwordform"].validate((valid) => {
        if (valid) {
          this.loadingpassword = true;
          value.password = this.passwordform.newpassword;
          value.is_allowed ? (value.is_allowed = 1) : (value.is_allowed = 0);
          editUsers(value.id, value)
            .then((res) => {
              this.$notify({
                title: this.$t("message.modifyPasswordSuccess"),
                type: "success",
                duration: 2500,
              });
              this.$emit("getuserList");
              this.loadingpassword = false;
              this.dialogFormpassword = false;
            })
            .catch((err) => {
              this.loadingpassword = false;
              console.error(err.response.data.message);
            });
        } else {
          return false;
        }
      });
    },
    // 获取组织列表
    async bpsinit() {
      const list = await getBps();
      this.bpsData = list.bps;
    },
    // 编辑用户的重置
    restpassword() {
      this.$refs["userform"].resetFields();
    },
    // 编辑用户关闭弹框
    userClose() {
      this.dialogFormuser = false;
    },
    // 修改用户
    changeuser(value) {
      this.$refs["userform"].validate((valid) => {
        if (valid) {
          this.loadinguser = true;
          value.is_allowed ? (value.is_allowed = 1) : (value.is_allowed = 0);
          editUsers(value.id, value)
            .then((res) => {
              this.$notify({
                title: this.$t("message.modifyUserSuccess"),
                type: "success",
                duration: 2500,
              });
              this.$emit("getuserList");
              this.loadinguser = false;
              this.dialogFormuser = false;
            })
            .catch((err) => {
              this.loadinguser = false;
              console.error(err.response.data.message);
            });
        } else {
          return false;
        }
      });
    },
    // 打开用户弹窗
    openuser() {
      this.bpsinit();
      this.dialogFormuser = true;
      this.userdata = JSON.parse(JSON.stringify(this.$props.propuserDetail));
    },
  },
};
</script>

<style lang="scss" scoped>
.operatePage {
  position: relative;
  z-index: 999;
}

.operatePage {
  ::v-deep .el-table {
    .success-row {
      background-color: rgba(149, 212, 117, 0.3);
    }
  }
}

::v-deep .el-popconfirm__main {
  padding: 10px 0px;
}

::v-deep .el-dropdown-menu {
  font-size: 14px;
}

.ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
