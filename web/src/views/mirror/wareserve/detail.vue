<template>
  <div class="warpMain">
    <div style="background: #fff; padding: 10px 20px" class="msg">
      <div class="label">
        <el-tag
          v-for="(item, index) in form.label"
          :key="index"
          size="mini"
          style="margin-right: 10px"
          type="info"
          :index="index"
          >{{ item }}</el-tag
        >
      </div>
      <div class="hint">
        {{ $t("form.descriptionInfo") }}:{{ form.description || "-" }}
      </div>
    </div>
    <el-tabs v-model="activeIndex">
      <el-tab-pane label="tags" name="1">
        <el-table
          ref="tagTable"
          :data="tagsData.tags"
          stripe
          tooltip-effect="dark"
          style="width: 100%"
          size="small"
        >
          <el-table-column
            prop="name"
            :label="$t('form.name')"
            width="200px"
            show-overflow-tooltip
          />
          <el-table-column
            :label="$t('form.pullCommand')"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <i
                class="el-icon-document-copy"
                style="font-size: 24px"
                @click="copy($event, scope.row.pull_command)"
              />
              {{ scope.row.pull_command }}
            </template>
          </el-table-column>
          <el-table-column :label="$t('form.digest')" show-overflow-tooltip />
          <el-table-column :label="$t('form.pushTime')" />
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
      </el-tab-pane>
      <el-tab-pane :label="$t('form.buildHistory')" name="2">
        <el-table
          ref="hisTable"
          :data="tableData"
          stripe
          tooltip-effect="dark"
          style="width: 100%"
          class="tableName"
          :default-sort="{ prop: 'date', order: 'descending' }"
        >
          <el-table-column
            prop="created"
            :label="$t('form.createTime')"
            width="200px"
            sortable
          >
            <template slot-scope="scope">
              {{ scope.row.created ? parseTime(scope.row.created) : "" }}
            </template>
          </el-table-column>
          <el-table-column prop="created_by" :label="$t('form.command')" />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { getReoInfo, getTags, delTags, getBuildHistory } from "@/api/mainApi";
import Clipboard from "clipboard";
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      activeIndex: "1",
      tableData: [],
      tagsData: {},
      paramsData: {
        page_size: 10,
        page_num: 1,
      },
      queryData: {
        page_size: 10,
        page_num: 1,
      },
      loading: false,
      form: {
        repo_name: "",
        description: "",
        label: [],
      },
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
  computed: {
    ...mapGetters(["userInfo"]),
  },
  mounted() {
    this.registry_id = this.$route.params.registryId;
    this.project_id = this.$route.params.projectId;
    this.repo_name = encodeURIComponent(
      encodeURIComponent(this.$route.params.repoName)
    );

    this.getInfo();

    this.getTagsList();
    this.getHisList();
  },
  methods: {
    async getInfo() {
      if (this.registry_id && this.project_id && this.repo_name) {
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

        const list = await getTags(
          this.registry_id,
          this.project_id,
          this.repo_name,
          this.paramsData
        );
        this.tagsData = list;
        console.log(this.tagsData);
      }
    },
    async getHisList() {
      if (this.registry_id && this.project_id && this.repo_name) {
        for (var key in this.queryData) {
          if (this.queryData[key] === undefined || this.queryData[key] === "") {
            delete this.paramsData[key];
          }
        }

        const list = await getBuildHistory(
          this.registry_id,
          this.project_id,
          this.repo_name
        );
        this.tableData = JSON.parse(list);
        8;
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
      if (!arguments[0] || arguments.length == 0) {
        return null;
      }
      if (arguments[0] && arguments[0].length <= 10) {
        return arguments[0] + " ";
      }
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
      const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
        let value = formatObj[key];
        // Note: getDay() returns 0 on Sunday
        if (key == "a") {
          return ["日", "一", "二", "三", "四", "五", "六"][value];
        }
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
            .catch((err) => {});
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
    font-size: 14px;
    padding: 5px 0;
    color: #666;
  }
}

.tableName table {
  width: 100%;
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
