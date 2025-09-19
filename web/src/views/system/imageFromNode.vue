<template>
  <div class="warpMain">
    <div class="itemMian">
      <el-table
        :data="tableData"
        style="width: 100%"
        :default-sort="{ prop: 'date', order: 'descending' }"
      >
        <el-table-column :label="$t('form.name')" prop="image_name">
          <template slot-scope="scope">
            {{ scope.row.image_name }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.size')" prop="size">
          <template slot-scope="scope">
            {{ scope.row.size }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('form.createTime')"
          prop="create_time"
          sortable
        >
          <template slot-scope="scope">
            {{ scope.row.create_time ? formatDate(scope.row.create_time) : "" }}
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import {} from "vuex";
import { getImagesFromEdges } from "@/api/mainApi";
import initData from "@/mixins/initData";
export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      tableData: [],
      queryData: {
        page_size: 8,
        page_num: 1,
        total_num: 0,
      },
    };
  },

  created() {
    this.init();
  },
  mounted() {
    this.$route.meta.smalltitle =
      this.$t("form.belongNode") + "：" + this.$route.params.name;
  },

  methods: {
    // 时间戳转换时间
    formatDate(date) {
      var date = new Date(date);
      var YY = date.getFullYear() + "-";
      var MM =
        (date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1) + "-";
      var DD = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      var hh =
        (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
      var mm =
        (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) +
        ":";
      var ss =
        date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      return YY + MM + DD + " " + hh + mm + ss;
    },
    sizeChange(val) {
      var data;
      if (val < 1024) {
        data = val + "B";
      } else {
        if (val / 1024 < 1024) {
          const num = val / 1024;
          data = this.$script.processing_numbers(num) + "KB";
        } else if (val / 1024 / 1024 < 1024) {
          const num = val / 1024 / 1024;
          data = this.$script.processing_numbers(num) + "MB";
        } else if (val / 1024 / 1024 / 1024 < 1024) {
          const num = val / 1024 / 1024 / 1024;
          data = this.$script.processing_numbers(num) + "GB";
        }
      }
      return data;
    },
    async init() {
      const list = await getImagesFromEdges(this.$route.params.id);
      this.tableData = list;
    },
  },
};
</script>

<style lang="scss" scoped>
.itemMian {
  overflow: hidden;
}
</style>
