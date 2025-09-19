<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="$t('form.addGateway')"
      width="800px"
      :close-on-click-modal="false"
      @open="onOpen"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="130px"
      >
        <el-form-item :label="$t('form.region') + ':'" prop="region_ids">
          <el-select
            v-model="form.region_ids"
            :placeholder="$t('form.pleaseSelect')"
            multiple
            filterable
          >
            <el-option
              v-for="item in regions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="$t('form.nodeName') + ':'"
          prop="node_name"
          :rules="[
            {
              required: true,
              message: $t('form.pleaseInputNodeName'),
              trigger: 'blur',
            },
            {
              pattern: /^[a-zA-Z0-9][a-zA-Z0-9-_@]{0,64}$/,
              message: $t('form.pleaseInputNodeNameTips'),
              trigger: 'blur',
            },
          ]"
        >
          <el-input
            v-model="form.node_name"
            :placeholder="$t('form.pleaseInput')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.advertise') + ':'" prop="advertise">
          <el-input
            v-model="form.advertise"
            :placeholder="$t('form.pleaseInputAdvertise')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.serviceBind') + ':'">
          <el-input
            v-model="form.service_bind"
            :placeholder="$t('form.pleaseInputServiceBind')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.localPort') + ':'" type="number">
          <el-input
            v-model="form.local_port"
            :placeholder="$t('form.pleaseInputLocalPort')"
          />
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
    <el-dialog
      :title="$t('form.generatedCommand')"
      :visible.sync="cmdModal"
      :close-on-click-modal="false"
      width="800px"
      @close="cmdInfo = null"
    >
      <div>{{ $t("form.pleaseCopyAndExecute") }}</div>
      <div class="text-lg text-red-500">{{ cmdInfo }}</div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="small" @click="cmdModal = false">
          {{ $t("form.close") }}
        </el-button>
        <el-button
          type="primary"
          :data-clipboard-text="cmdInfo"
          size="small"
          @click="copy($event, cmdInfo)"
        >
          {{ $t("form.copy") }}
        </el-button>
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { addgws, getRegions } from "@/api/mainApi";
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
      loading: false,
      dialog: false,
      cmdModal: false,
      cmdInfo: null,
      form: {
        node_name: "",
        region_ids: [],
        labels: [],
        advertise: "",
        service_bind: "",
        local_port: "",
      },
      inputVisible: false,
      inputValue: "",
      rules: {
        region_ids: [
          {
            required: true,
            message: this.$t("message.pleaseSelectRegion"),
            trigger: "change",
          },
        ],
        node_name: [
          {
            required: true,
            message: this.$t("message.pleaseInput"),
            trigger: "blur",
          },
        ],
        service_bind: [
          {
            required: true,
            message: this.$t("message.pleaseInput"),
            trigger: "blur",
          },
        ],
        local_port: [
          {
            required: true,
            message: this.$t("message.pleaseInput"),
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    async onOpen() {
      if (this.$refs.form) this.$refs.form.resetFields();
      const list = await getRegions();
      this.regions = list.regions.map((item) => {
        return {
          value: item.id,
          label: item.name,
        };
      });
      if (this.regions != undefined && this.regions.length > 0) {
        this.form.region_ids = [this.regions[0].value];
      }
    },
    cancel() {
      this.resetForm();
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
      var data = JSON.parse(JSON.stringify(this.form));
      data.local_port = parseInt(data.local_port);
      if (data.labels && data.labels.length == 0) {
        delete data.labels;
      }
      addgws(data)
        .then((res) => {
          this.resetForm();
          this.cmdInfo = res.cmd;
          this.cmdModal = true;
          this.loading = false;
          this.$parent.init();
        })
        .catch((err) => {
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
        node_name: "",
        region_ids: [],
        labels: [],
        advertise: "",
        service_bind: "",
        local_port: "",
      };
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
    handleClose(tag) {
      this.form.labels.splice(this.form.labels.indexOf(tag), 1);
    },
    handleInputConfirm() {
      const inputValue = this.inputValue;
      if (inputValue) {
        this.form.labels.push(inputValue);
      }
      this.inputVisible = false;
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

<style lang="scss"></style>
