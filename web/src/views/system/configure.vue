<template>
  <div class="warpMain">
    <div class="head_rq" />
    <el-table
      ref="multipleTable"
      :data="tableData.pageItems"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column
        prop="dataId"
        :label="$t('form.dataId')"
        show-overflow-tooltip
      />
      <el-table-column
        prop="group"
        :label="$t('form.group')"
        show-overflow-tooltip
      />
      <el-table-column
        prop="registry_name"
        :label="$t('form.operation')"
        width="100"
      >
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
              <div class="icon_cz" @click="clickEditBtn(scope.row)">
                <i class="el-icon-edit" />
                {{ $t("form.editOrView") }}
              </div>

              <el-button
                slot="reference"
                icon="el-icon-more"
                class="czbtn right_czbtn"
              />
            </el-popover>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { configs } from "@/api/mainApi";
import initData from "@/mixins/initData";

export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      tableData: [],
    };
  },
  watch: {},
  created() {
    this.init();
  },
  computed: {
    ...mapGetters(["kind"]),
  },
  mounted() {},
  methods: {
    async init() {
      const params = {};
      params.system = true;
      const list = await configs(params);
      this.tableData = list;
    },
    clickEditBtn(row) {
      this.$router.push({
        path: "/configureDetail",
        query: {
          id: row.id,
        },
      });
    },
  },
};
</script>

<style lang="scss" scoped></style>
