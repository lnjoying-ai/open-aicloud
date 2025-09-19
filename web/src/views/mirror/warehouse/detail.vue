<template>
  <div class="bg">
    <div
      style="
        background: #fff;
        padding: 10px 20px;
        display: flex;
        justify: space-between;
      "
      class="msg"
    >
      <div style="flex: 1">
        <div class="title">{{ form.name }}</div>
        <div class="label">
          <el-tag
            v-for="(item, index) in form.label"
            :key="index"
            size="mini"
            style="margin-right: 10px"
            type="info"
            >{{ item }}</el-tag
          >
        </div>
        <div class="hint">
          {{ $t("form.descriptionInfo") }}:{{ form.description }}
        </div>
        <div class="hint">
          {{ $t("form.digest") }}:{{ form.digest }}
          <i
            class="el-icon-document-copy"
            style="font-size: 17px"
            @click="copy($event, form.digest)"
          />
        </div>
      </div>
      <el-button
        style="height: 42px"
        type="primary"
        @click="$router.back(-1)"
        >{{ $t("button.back") }}</el-button
      >
    </div>

    <el-table
      ref="tagTable"
      :data="tagsData.tags"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      size="small"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" :label="$t('form.name')" />
      <el-table-column :label="$t('form.pullCommand')">
        <template slot-scope="scope">
          <i
            class="el-icon-document-copy"
            style="font-size: 24px"
            @click="copy($event, scope.row.pull_command)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="pull_time" :label="$t('form.pullTime')" />
      <el-table-column prop="push_time" :label="$t('form.pushTime')" />
      <el-table-column :label="$t('form.operation')" width="100">
        <template slot-scope="scope">
          <div class="czlb">
            <div
              style="color: #409eff; cursor: pointer"
              @click="clickDelBtn(scope.row)"
            >
              {{ $t("form.delete") }}
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :current-page="paramsData.page_num"
        :page-sizes="[5, 10, 20, 50, 100]"
        :page-size="paramsData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="tagsData.total_num"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <div class="table_head">{{ $t("form.buildHistory") }}</div>
    <el-table
      ref="hisTable"
      :data="tableData"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column
        prop="created"
        :label="$t('form.createTime')"
        width="200"
      />
      <el-table-column prop="created_by" :label="$t('form.command')" />
    </el-table>
  </div>
</template>

<script>
import { getReoInfo, getTags, delTags, getBuildHistory } from "@/api/mainApi";
import Clipboard from "clipboard";
export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      regions: [],
      sites: [],
      tableData: [],
      tagsData: {},
      tagsList: [],
      paramsData: {
        page_size: 10,
        page_num: 1,
      },
      queryData: {
        page_size: 10,
        page_num: 1,
      },
      loading: false,
      dialog: false,
      form: {
        repo_name: "",
        description: "",
        label: [],
      },
      inputVisible: false,
      inputValue: "",
      rules: {
        description: [
          {
            required: true,
            message: this.$t("message.pleaseInputDescription"),
            trigger: "change",
          },
        ],
      },
      project_id: "",
      registry_id: "",
      repo_name: "",
    };
  },
  watch: {},
  mounted() {
    const params = this.$route.query;
    this.registry_id = params.registry_id;
    this.project_id = params.project_id;
    this.repo_name = params.repo_name;
    this.getTagsList();
    this.getHisList();
    this.getInfo();
  },
  methods: {
    onOpen() {},
    async getInfo() {
      if (this.repo_name != "" && this.repo_name != undefined) {
        this.repo_name = encodeURIComponent(this.repo_name);
      } else {
        this.repo_name = "";
      }

      if (this.registry_id && this.project_id && this.repo_name) {
        this.repo_name = encodeURIComponent(this.repo_name);
        const form = await getReoInfo(
          this.registry_id,
          this.project_id,
          this.repo_name
        );
        this.form = Object.assign({}, form);
      }
    },
    async getTagsList() {
      if (this.registry_id && this.project_id && this.repo_name) {
        for (var key in this.paramsData) {
          if (
            this.paramsData[key] === undefined ||
            this.paramsData[key] === ""
          ) {
            delete this.paramsData[key];
          }
        }
        if (this.repo_name != "" && this.repo_name != undefined) {
          this.repo_name = encodeURIComponent(this.repo_name);
        } else {
          this.repo_name = "";
        }
        const list = await getTags(
          this.registry_id,
          this.project_id,
          this.repo_name,
          this.paramsData
        );
        this.tagsData = list;
      }
    },
    async getHisList() {
      if (this.registry_id && this.project_id && this.repo_name) {
        for (var key in this.queryData) {
          if (this.queryData[key] === undefined || this.queryData[key] === "") {
            delete this.paramsData[key];
          }
        }
        if (this.repo_name != "" && this.repo_name != undefined) {
          this.repo_name = encodeURIComponent(this.repo_name);
        } else {
          this.repo_name = "";
        }
        const list = await getBuildHistory(
          this.registry_id,
          this.project_id,
          this.repo_name
        );
        const that = this;
        this.tableData = JSON.parse(list).map((item) => {
          item.created = that.parseTime(item.created);
          return item;
        });
      }
    },

    handleSizeChange(val) {
      this.paramsData.page_size = val;
      this.getTagsList();
    },
    handleCurrentChange(val) {
      this.paramsData.page_num = val;
      this.getTagsList();
    },
    parseTime(time, cFormat) {
      const format = cFormat || "{y}-{m}-{d} {h}:{i}:{s}";
      let date;
      if (typeof time === "object") {
        date = time;
      } else {
        if (("" + time).length == 10) time = parseInt(time) * 1000;
        date = new Date(time);
      }
      const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay(),
      };
      const time_str = format.replace(/{(y|m|d|h|i|s)+}/g, (result, key) => {
        let value = formatObj[key];
        if (result.length > 0 && value < 10) {
          value = "0" + value;
        }
        return value || 0;
      });
      if (time_str == "0-0-0") return " ";
      return time_str + " ";
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteTag"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delTags(this.registry_id, this.project_id, this.repo_name, value.name)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.getTagsList();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    copy(e, text) {
      const clipboard = new Clipboard(e.target, { text: () => text });
      clipboard.on("success", () => {
        this.$notify({
          title: this.$t("message.copySuccess"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.on("error", () => {
        // 不支持复制
        this.$notify({
          title: this.$t("message.copyFailed"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.onClick(e);
    },
    show(info) {
      this.resetForm();
      this.dialog = true;
      this.project_id = info.project_id;
      this.registry_id = info.registry_id;
      this.repo_name = info.repo_name;
      if (info.description) {
        this.$set(this.form, "description", info.description);
      }
      if (info.label && info.label.length > 0) {
        this.$set(this.form, "label", info.label);
      }
    },
    cancel() {
      this.resetForm();
      this.dialog = false;
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
  },
};
</script>

<style lang="scss" scoped>
.msg {
  .title {
    font-size: 17px;
    font-weight: bold;
    color: #333;
  }

  .label {
    padding: 5px 0;
  }

  .hint {
    font-size: 12px;
    padding: 5px 0;
    color: #666;
  }
}

.table_head {
  display: none;
}

::v-deep .app-main {
  background: #f2f2f2;
}

.p-4 {
  background: #f2f2f2;
}

.bg {
  background: #f2f2f2;
  padding: 10px;

  .table_head {
    margin: 10px 0;
    padding: 10px 20px;
    height: 44px;
    display: block;
  }
}

.el-table {
  margin-bottom: 10px;
}
</style>
