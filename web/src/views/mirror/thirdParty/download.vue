<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="$t('form.imagePreDownload')"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="108px"
      >
        <el-form-item :label="$t('form.warehouseAddress') + ':'" prop="url">
          <el-input
            v-model="form.url"
            :disabled="true"
            :placeholder="$t('form.pleaseInput')"
          />
        </el-form-item>
        <el-row>
          <el-col :span="16">
            <el-form-item
              :label="$t('form.warehouseName') + ':'"
              prop="registry_name"
            >
              <el-input
                v-model="form.registry_name"
                :placeholder="$t('form.pleaseInputWarehouseName')"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('form.node') + ':'" prop="nodes">
          <el-select
            ref="selectReport"
            v-model="form.nodes"
            :placeholder="$t('form.pleaseSelect')"
            style="width: 100%"
            multiple
            filterable
            @change="changeValue"
          >
            <el-option
              :value="selectObj.id"
              :label="selectObj.label"
              style="
                width: 100%;
                height: 200px;
                overflow: auto;
                background-color: #fff;
              "
            >
              <el-tree
                ref="tree"
                show-checkbox
                :data="regions"
                :default-checked-keys="defaultProps"
                node-key="id"
                @check-change="currentChange"
              />
            </el-option>
            <el-option
              v-for="(item, index) in selectNode"
              v-show="false"
              :key="index"
              :value="item.id"
              :label="item.label"
            />
          </el-select>
        </el-form-item>

        <a-row>
          <el-form-item
            prop="inputValue"
            :label="$t('form.imageRepository') + ':'"
          >
            <a-col :span="24">
              <el-row v-if="otherRepos.length > 0">
                <el-tag
                  v-for="tag in otherRepos"
                  :key="tag"
                  style="margin-bottom: 10px"
                  class="mr-3"
                  :disable-transitions="false"
                >
                  {{ tag }}
                </el-tag>
              </el-row>
            </a-col>

            <div style="width: 100%">
              <el-tooltip
                class="item"
                effect="dark"
                :content="
                  $t('form.pleaseInputDownloadImageNotWarehouseAddress')
                "
                placement="bottom"
              >
                <a-col :span="24">
                  <el-input
                    ref="saveTagInput"
                    v-model="inputValue"
                    placeholder="+  project/repo:tag"
                    size="small"
                    style="width: 100%"
                    @keyup.enter.native="handleInputConfirm"
                    @blur="handleInputConfirm"
                  />
                </a-col>
              </el-tooltip>
            </div>
          </el-form-item>
        </a-row>
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
import { getnodesby3rdId, predown3rd, registries3rdById } from "@/api/mainApi";
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
      loading: false,
      dialog: false,
      cmdModal: false,
      cmdInfo: null,
      defaultProps: [],
      form: {
        nodes: [],
        repos: [],
      },
      otherRepos: [],
      select: "",
      inputVisible: false,
      inputValue: "",
      registry_id: "",
      nodes: [],
      repos: [],
      rules: {
        nodes: [
          {
            required: true,
            message: this.$t("form.pleaseSelectNode"),
            trigger: "change",
          },
        ],
      },
      selectObj: {},
      selectNode: [],
      selectArr: [],
      toData: [],
    };
  },
  methods: {
    add(obj) {
      this.form = Object.assign({}, obj, { nodes: [], repos: [] });
      this.registry_id = obj.registry_id;
      this.dialog = true;
      this.nodeInit();
      this.getRegistriesById();
    },
    cancel() {
      this.resetForm();
    },
    currentChange(e) {
      this.selectNode = this.$refs.tree.getCheckedNodes(true);
      this.form.nodes = this.selectNode.map((item) => {
        return item.id;
      });
    },
    changeValue(e) {
      this.$refs.tree.setCheckedKeys(e);
    },
    getRegistriesById() {
      registries3rdById(this.registry_id);
    },
    async nodeInit() {
      const list = await getnodesby3rdId();
      const arr = list.regions;
      if (list.regions.length > 0) {
        arr.forEach(function (item1) {
          item1["id"] = item1.region_id;
          item1["label"] = item1.region_name;
          delete item1["region_id"];
          delete item1["region_name"];
          if (item1.children && item1.children.length > 0) {
            item1.children.forEach(function (item2) {
              item2["id"] = item2.site_id;
              item2["label"] = item2.site_name;
              delete item2["site_id"];
              delete item2["site_name"];
              if (item2.children && item2.children.length > 0) {
                item2.children.forEach(function (item3) {
                  item3["id"] = item3.node_id;
                  item3["label"] = item3.node_name;
                  delete item3["node_id"];
                  delete item3["node_name"];
                });
              }
            });
          }
        });
        this.regions = [].concat(arr);
      }
    },
    doSubmit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.otherRepos.length == 0) {
            this.$notify({
              title: this.$t("message.imageRepositoryCannotBeEmpty"),
              type: "warning",
              duration: 2500,
            });
          } else {
            this.loading = true;
            this.doAdd();
          }
        } else {
          return false;
        }
      });
    },
    doAdd(obj) {
      if (this.form.nodes.length > 0) {
        for (var i = 0; i < this.form.nodes.length; i++) {
          if (this.form.nodes[i] == null || this.form.nodes[i] == undefined) {
            this.form.nodes.splice(i, 1);
          }
        }
      }
      predown3rd(this.registry_id, {
        nodes: this.form.nodes,
        repos: this.form.repos.concat(this.otherRepos),
      })
        .then(() => {
          this.resetForm();
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    resetForm() {
      this.dialog = false;
      this.$nextTick(() => {
        if (this.$refs["form"] !== undefined) {
          this.$refs["form"].resetFields();
        }
      });
      this.form = {
        nodes: [],
        repos: [],
      };
      this.otherRepos = [];
    },
    handleClose(tag) {
      this.form.labels.splice(this.form.labels.indexOf(tag), 1);
    },
    handleInputConfirm() {
      const inputValue = this.inputValue;
      if (inputValue) {
        this.otherRepos.push(this.form.url + "/" + "" + inputValue);
      }
      this.inputValue = "";
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick((_) => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.el-transfer-panel {
  width: 400px;
}
</style>
