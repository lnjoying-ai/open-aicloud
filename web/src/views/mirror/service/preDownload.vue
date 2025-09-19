<template>
  <div class="gatewayForm">
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      size="small"
      label-width="140px"
    >
      <el-row>
        <el-col :span="16">
          <el-form-item :label="$t('form.repositoryAddress') + ':'" prop="url">
            <el-input
              v-model="form.url"
              :disabled="true"
              :placeholder="$t('form.pleaseInputRepositoryAddress')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="16">
          <el-form-item
            :label="$t('form.repositoryName') + ':'"
            prop="registry_name"
          >
            <el-input
              v-model="form.registry_name"
              :placeholder="$t('form.pleaseInputRepositoryName')"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('form.node') + ':'" prop="nodes">
            <el-select
              ref="selectReport"
              v-model="form.nodes"
              :placeholder="$t('form.pleaseSelect')"
              style="width: 250px"
              multiple
              filterable
              @change="changeValue"
            >
              <el-option
                v-if="regions.length > 0"
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
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10" style="margin-right: 55px">
          <el-form-item :label="$t('form.userName') + ':'">
            <el-select
              v-model="form.user_id"
              :placeholder="$t('form.pleaseSelectUser')"
              style="width: 250px"
              clearable
              filterable
              @change="handleChangeUser"
            >
              <el-option
                v-for="(item, index) in usersData"
                :key="index"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item :label="$t('form.projectName') + ':'" prop="project">
            <el-select
              v-model="form.project_id"
              style="width: 250px"
              :placeholder="$t('form.pleaseSelect')"
              filterable
              @change="changeProjectId"
            >
              <el-option
                v-for="(item, index) in projectsList"
                :key="index"
                :value="item.project_id"
                :label="item.project_name"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item :label="$t('form.imageRepository') + ':'" prop="repos">
        <tree-transfer
          ref="transfer"
          :title="[
            $t('form.originalImageRepositoryList'),
            $t('form.targetImageRepositoryList'),
          ]"
          :from_data="repos"
          :to_data="toData"
          :default-props="{ label: 'label', children: 'children' }"
          mode="transfer"
          height="445px"
          style="width: 93% !important"
          filter
          open-all
          @add-btn="addMode"
          @remove-btn="removeMode"
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import treeTransfer from "el-tree-transfer";

import {
  getnodesbyId,
  reposjcck,
  registriesById,
  predown,
  getUsers,
  projects,
} from "@/api/mainApi";
export default {
  components: { treeTransfer },
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      regions: [],
      usersData: [],
      loading: false,
      dialog: false,
      cmdModal: false,
      cmdInfo: null,
      defaultProps: [],
      projectsList: [],
      form: {
        nodes: [],
        repos: [],
        user_id: "",
        project_id: "",
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
            message: this.$t("message.pleaseSelectNode"),
            trigger: "change",
          },
        ],
      },
      selectObj: {},
      selectNode: [],
      selectArr: [],
      toData: [],
      inputValue: "",
      rules: {
        project: [],
      },
    };
  },
  mounted() {},
  methods: {
    add(obj) {
      this.form = Object.assign({}, obj, {
        nodes: [],
        repos: [],
        user_id: "",
        project_id: "",
      });
      this.form.url = obj.url;
      this.registry_id = obj.registry_id;
      this.dialog = true;
      this.getUsers();
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
    changeProjectId(e) {
      if (this.form.user_id != "" && this.form.user_id != undefined) {
        this.form.project_id = e;
      }
      this.loadrepos(e);
    },

    getRegistriesById() {
      registriesById(this.registry_id);
    },
    async loadrepos(e) {
      const params = {};
      params.project_id = e;
      var reposList = await reposjcck(this.registry_id, params);

      if (reposList.repos != undefined && reposList.repos.length > 0) {
        const reposArr = reposList.repos.map((element) => {
          return element;
        });
        const tree = [];
        reposArr.forEach(function (item, index) {
          var obj = {
            id: "",
            label: "",
            children: [],
          };
          var objChildren = {
            id: "",
            label: "",
            children: [],
          };
          var objChilds = {
            id: "",
            label: "",
            children: [],
          };
          var num = obj.children.length + 1;
          var num2 = objChilds.children.length + 1;
          var a = index + 1; // 0
          var b = a + "_" + num; // 1
          var lebal = item.split("/")[0];
          obj.pid = 0;
          obj.id = a;
          obj.label = item.split(lebal)[1].slice(1);

          tree.push(obj);
        });
        this.repos = tree;
      } else {
        this.repos = [];
      }
    },
    // 监听穿梭框组件添加
    addMode(fromData, toData, obj) {
      this.toData;
      this.otherRepos = [];
      if (toData.length > 0) {
        for (var i = 0; i < toData.length; i++) {
          this.otherRepos.push(this.form.url + "/" + toData[i].label);
        }
      }
    },
    // 监听穿梭框组件移除
    removeMode(fromData, toData, obj) {
      // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的{keys,nodes,halfKeys,halfNodes}对象
      // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表
      // this.toData = obj.nodes[1]
      // if(toData.length>0){
      //   for(var i = 0;i <toData.length;i++ ){
      //     this.otherRepos.push(this.form.url+"/"+toData[i].children[0].label+"/"+toData[i].children[0].children[0].label)
      //   }
      // }
      // this.otherRepos
    },
    async getUsers() {
      const params = {};
      var listUsers = await getUsers(params);
      this.usersData = listUsers.users;
    },
    getProjectList() {
      const params = {};
      params.page_size = 100;
      params.page_num = 1;
      params.registry_id = this.registry_id;
      params.user_id = this.form.user_id;
      projects(this.registry_id, params)
        .then((res) => {
          this.projectsList = res.projects;
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    async nodeInit() {
      const list = await getnodesbyId(this.registry_id);
      const arr = list.regions;
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
      const reposList = await reposjcck(this.registry_id);

      if (reposList.repos != undefined && reposList.repos.length > 0) {
        const reposArr = reposList.repos.map((element) => {
          return element;
        });
        const tree = [];
        reposArr.forEach(function (item, index) {
          var obj = {
            id: "",
            label: "",
            children: [],
          };
          var objChildren = {
            id: "",
            label: "",
            children: [],
          };
          var objChilds = {
            id: "",
            label: "",
            children: [],
          };
          var num = obj.children.length + 1;
          var num2 = objChilds.children.length + 1;
          var a = index + 1; // 0
          var b = a + "_" + num; // 1

          var lebal = item.split("/")[0];
          obj.pid = 0;
          obj.id = a;
          obj.label = item.split(lebal)[1].slice(1);

          tree.push(obj);
        });
        this.repos = tree;
      } else {
        this.repos = [];
      }
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
      if (this.form.nodes.length > 0) {
        for (var i = 0; i < this.form.nodes.length; i++) {
          if (this.form.nodes[i] == null || this.form.nodes[i] == undefined) {
            this.form.nodes.splice(i, 1);
          }
        }
      }
      if (this.form.nodes.length > 0 && this.otherRepos.length > 0) {
        predown(this.registry_id, {
          nodes: this.form.nodes,
          repos: this.form.repos.concat(this.otherRepos),
        })
          .then(() => {
            // 调用父级方法
            this.sup_this.preDownloadCancel();
          })
          .catch(() => {});
      } else {
        this.loading = false;
      }
    },
    resetForm() {
      this.$nextTick(() => {
        if (this.$refs["form"] !== undefined) {
          this.$refs["form"].resetFields();
        }
      });
      this.form = {
        nodes: [],
        repos: [],
      };
      this.regions = [];
      this.otherRepos = [];
    },
    handleClose(tag) {
      this.form.labels.splice(this.form.labels.indexOf(tag), 1);
    },
    handleChangeUser(val) {
      this.form.user_id = val;
      this.form.project_id = "";
      if (this.form.user_id != "" && this.form.user_id != undefined) {
        this.getProjectList();
      } else {
        this.projectsList = [];
        this.loadrepos();
        // this.repos = []
      }
    },
    handleInputConfirm() {
      const inputValue = this.inputValue;
      if (inputValue) {
        this.otherRepos.push(inputValue);
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
