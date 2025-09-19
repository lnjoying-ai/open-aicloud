<template>
  <div class="mt-2 relative" style="height: calc(100vh - 144px - 0.5rem)">
    <el-card
      class="mb-2 overflow-auto"
      shadow="never"
      style="height: calc((100vh - 185px) - 0.5rem)"
    >
      <el-form
        ref="ruleForm"
        :model="dataform"
        :rules="rules"
        label-width="140px"
        class="demo-ruleForm"
        size="small"
      >
        <h1 class="mb-5 ml-4 componentadd_title">
          1.{{ $t("form.basicInfo") }}:
        </h1>
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input
            v-model="dataform.name"
            style="width: 500px"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" prop="description">
          <el-input
            v-model="dataform.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            style="width: 500px"
            :placeholder="$t('form.pleaseInputDescription')"
          />
        </el-form-item>
        <h1 class="mb-5 ml-4 componentadd_title">
          2.{{ $t("form.serviceConfig") }}:
        </h1>
        <el-form-item :label="$t('form.selectType') + ':'" prop="target_type">
          <div>
            <el-radio-group
              v-model="dataform.target_type"
              size="medium"
              @change="handleresetFrom()"
            >
              <el-radio-button label="edge">{{
                $t("form.node")
              }}</el-radio-button>
              <el-radio-button label="container">{{
                $t("form.container")
              }}</el-radio-button>
              <el-radio-button label="compose">{{
                $t("form.composeApplication")
              }}</el-radio-button>
            </el-radio-group>
          </div>
        </el-form-item>
        <div v-if="dataform.target_type == 'edge'">
          <!-- 这个下面是根据不同的类型选择显示不同的模板 -->
          <el-form-item :label="$t('form.node') + ':'" prop="deployment">
            <el-select
              v-model="dataform.deployment"
              :placeholder="$t('form.pleaseSelectNode')"
              @change="changeNode"
            >
              <el-option
                v-for="item in nodes_list"
                :key="item.node_id"
                :label="item.node_name"
                :value="item.node_id"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            :label="$t('form.portMapping') + ':'"
            prop="data_source_id"
          >
            <div class="portmapping_border">
              <div
                v-for="(item, index) in dataform.target_services"
                :key="index"
                class="mb-4 overflow-hidden"
              >
                <div class="portmapping_content">
                  <!-- 基本信息 start -->
                  <div class="block">
                    <div class="portmapping_content_left">
                      <div class="mr-5">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          class="styles__StyledSVGIconPathComponent-sc-gbp7ch-0 gFGDaY svg-icon-path-icon fill"
                          viewBox="0 0 32 32"
                          width="44"
                          height="44"
                        >
                          <defs data-reactroot="" />
                          <g>
                            <path
                              d="M9 3.333c3.13 0 5.667 2.537 5.667 5.667v0 5.667h-5.667c-3.13 0-5.667-2.537-5.667-5.667s2.537-5.667 5.667-5.667v0zM12 12v-3c0-1.657-1.343-3-3-3s-3 1.343-3 3c0 1.657 1.343 3 3 3v0h3zM9 17.333h5.667v5.667c0 3.13-2.537 5.667-5.667 5.667s-5.667-2.537-5.667-5.667c0-3.13 2.537-5.667 5.667-5.667v0zM9 20c-1.657 0-3 1.343-3 3s1.343 3 3 3c1.657 0 3-1.343 3-3v0-3h-3zM23 3.333c3.13 0 5.667 2.537 5.667 5.667s-2.537 5.667-5.667 5.667v0h-5.667v-5.667c0-3.13 2.537-5.667 5.667-5.667v0zM23 12c1.657 0 3-1.343 3-3s-1.343-3-3-3c-1.657 0-3 1.343-3 3v0 3h3zM17.333 17.333h5.667c3.13 0 5.667 2.537 5.667 5.667s-2.537 5.667-5.667 5.667c-3.13 0-5.667-2.537-5.667-5.667v0-5.667zM20 20v3c0 1.657 1.343 3 3 3s3-1.343 3-3c0-1.657-1.343-3-3-3v0h-3z"
                            />
                          </g>
                        </svg>
                        <span class="block" style="width: 55px"
                          ><small>{{ $t("form.nodePort") }}</small></span
                        >
                      </div>
                      <div class="mr-5">
                        <el-form-item
                          class="mb-2"
                          label-width="55px"
                          :label="$t('form.node') + ':'"
                        >
                          {{
                            nowNode ? nowNode.node_name : $t("form.notSelected")
                          }}
                        </el-form-item>
                        <el-form-item
                          class="mb-2"
                          label-width="55px"
                          :label="$t('form.port') + ':'"
                          :rules="[
                            {
                              required: true,
                              message: $t('form.pleaseInputPort'),
                              trigger: 'blur',
                            },
                          ]"
                          :prop="'target_services.' + index + '.target_port'"
                        >
                          <el-input
                            v-model="
                              dataform.target_services[index].target_port
                            "
                            class="w-32"
                            :placeholder="$t('form.pleaseInputPort')"
                          />
                        </el-form-item>
                      </div>

                      <div class="mx-5">
                        <el-form-item
                          label-width="95px"
                          :label="$t('form.protocol') + ':'"
                          :rules="[
                            {
                              required: true,
                              message: $t('form.pleaseSelectProtocol'),
                              trigger: 'blur',
                            },
                          ]"
                          :prop="'target_services.' + index + '.protocol'"
                        >
                          <el-select
                            v-model="dataform.target_services[index].protocol"
                            class="w-32"
                            :placeholder="$t('form.pleaseSelectProtocol')"
                          >
                            <el-option
                              v-for="item in agreementlist"
                              :key="item.value"
                              :label="item.label"
                              :value="item.value"
                            />
                          </el-select>
                        </el-form-item>

                        <el-form-item>
                          <div class="arrow" />
                        </el-form-item>
                        <el-form-item
                          v-if="
                            dataform.target_services[index].protocol == 'HTTPS'
                          "
                          label-width="95px"
                          :label="$t('form.certificate')"
                          :prop="'target_services.' + index + '.cert'"
                          :rules="[
                            {
                              required: true,
                              message: $t('form.pleaseInputCertificateContent'),
                              trigger: 'blur',
                            },
                          ]"
                        >
                          <el-popover
                            :ref="`node-popover-${dataform.target_services[index].cert}`"
                            placement="top"
                            :title="$t('form.certificate')"
                            width="600"
                            popper-class="tablepopover"
                            trigger="hover"
                            :content="dataform.target_services[index].cert"
                          >
                            <span
                              v-show="dataform.target_services[index].cert"
                              slot="reference"
                              class="w-32"
                              >{{
                                dataform.target_services[index].cert
                                  ? dataform.target_services[index].cert.slice(
                                      0,
                                      12
                                    ) + "..."
                                  : ""
                              }}</span
                            >
                          </el-popover>
                          <el-button
                            type="primary"
                            size="mini"
                            class="w-32"
                            @click="clickLoad(item, index)"
                            >{{ $t("form.import") }}</el-button
                          >
                          <input
                            id="files"
                            ref="refFile"
                            type="file"
                            style="display: none"
                            @change="fileLoad(item, index)"
                          />
                        </el-form-item>
                      </div>
                      <div class="ml-2 mr-2">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          class="styles__StyledSVGIconPathComponent-sc-gbp7ch-0 gFGDaY svg-icon-path-icon fill"
                          viewBox="0 0 48 48"
                          width="44"
                          height="44"
                        >
                          <defs data-reactroot="" />
                          <g>
                            <rect
                              width="48"
                              height="48"
                              fill="white"
                              fill-opacity="0.01"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M8 12C10.2091 12 12 10.2091 12 8C12 5.79086 10.2091 4 8 4C5.79086 4 4 5.79086 4 8C4 10.2091 5.79086 12 8 12Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M10 42C13.3137 42 16 39.3137 16 36C16 32.6863 13.3137 30 10 30C6.68629 30 4 32.6863 4 36C4 39.3137 6.68629 42 10 42Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M38 44C41.3137 44 44 41.3137 44 38C44 34.6863 41.3137 32 38 32C34.6863 32 32 34.6863 32 38C32 41.3137 34.6863 44 38 44Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M22 28C26.4183 28 30 24.4183 30 20C30 15.5817 26.4183 12 22 12C17.5817 12 14 15.5817 14 20C14 24.4183 17.5817 28 22 28Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M34 12C36.2091 12 38 10.2091 38 8C38 5.79086 36.2091 4 34 4C31.7909 4 30 5.79086 30 8C30 10.2091 31.7909 12 34 12Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M11 11L15 15"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M30 12L28 14"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M34 33.5L28 26"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M14 31L18 27"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                          </g>
                        </svg>
                        <span class="block" style="width: 70px"
                          ><small>{{ $t("form.servicePort") }}</small></span
                        >
                      </div>
                      <div class="ml-5 mr-2 w-72">
                        <el-form-item
                          class="mb-2"
                          label-width="72px"
                          :label="$t('form.gateway') + ':'"
                        >
                          <el-select
                            v-model="item.svc_node"
                            class="w-56"
                            clearable
                            :placeholder="$t('form.defaultSystemAssigned')"
                          >
                            <el-option
                              v-for="(item, index) in serviceGatewaysList"
                              :key="index"
                              :label="item.name"
                              :value="item.service_gateway_id"
                            />
                          </el-select>
                        </el-form-item>
                        <el-form-item
                          class="mb-0"
                          label-width="72px"
                          :label="$t('form.port') + ':'"
                        >
                          <el-input
                            v-model="item.svc_port"
                            class="w-56"
                            clearable
                            :placeholder="$t('form.defaultSystemAssigned')"
                          />
                        </el-form-item>
                      </div>
                      <div class="portmapping_content_delete ml-2">
                        <i class="el-icon-delete" @click="clickdelete(index)" />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <el-button
                class="mt-2"
                size="mini"
                type="primary"
                @click="pushmap"
                >{{ $t("form.addMapping") }}</el-button
              >
            </div>
            <!-- 基本信息 end -->
          </el-form-item>
        </div>
        <div v-if="dataform.target_type == 'container'">
          <!-- 这个下面是根据不同的类型选择显示不同的模板 -->
          <el-form-item
            :label="$t('form.containerDeployment') + ':'"
            prop="deployment"
          >
            <el-select
              v-model="dataform.deployment"
              :placeholder="$t('form.pleaseSelectContainerDeployment')"
              @change="changeContainerDeploy"
            >
              <el-option
                v-for="item in containerDeploysList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            :label="$t('form.portMapping') + ':'"
            prop="data_source_id"
          >
            <div class="portmapping_border">
              <div
                v-for="(item, index) in dataform.target_services"
                :key="index"
                class="mb-4 overflow-hidden"
              >
                <div class="portmapping_content">
                  <!-- 基本信息 start -->
                  <div class="block">
                    <div class="portmapping_content_left">
                      <div class="mr-5">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          class="styles__StyledSVGIconPathComponent-sc-gbp7ch-0 gFGDaY svg-icon-path-icon fill"
                          viewBox="0 0 32 32"
                          width="44"
                          height="44"
                        >
                          <defs data-reactroot="" />
                          <g>
                            <path
                              d="M9 3.333c3.13 0 5.667 2.537 5.667 5.667v0 5.667h-5.667c-3.13 0-5.667-2.537-5.667-5.667s2.537-5.667 5.667-5.667v0zM12 12v-3c0-1.657-1.343-3-3-3s-3 1.343-3 3c0 1.657 1.343 3 3 3v0h3zM9 17.333h5.667v5.667c0 3.13-2.537 5.667-5.667 5.667s-5.667-2.537-5.667-5.667c0-3.13 2.537-5.667 5.667-5.667v0zM9 20c-1.657 0-3 1.343-3 3s1.343 3 3 3c1.657 0 3-1.343 3-3v0-3h-3zM23 3.333c3.13 0 5.667 2.537 5.667 5.667s-2.537 5.667-5.667 5.667v0h-5.667v-5.667c0-3.13 2.537-5.667 5.667-5.667v0zM23 12c1.657 0 3-1.343 3-3s-1.343-3-3-3c-1.657 0-3 1.343-3 3v0 3h3zM17.333 17.333h5.667c3.13 0 5.667 2.537 5.667 5.667s-2.537 5.667-5.667 5.667c-3.13 0-5.667-2.537-5.667-5.667v0-5.667zM20 20v3c0 1.657 1.343 3 3 3s3-1.343 3-3c0-1.657-1.343-3-3-3v0h-3z"
                            />
                          </g>
                        </svg>
                        <span class="block" style="width: 55px"
                          ><small>{{ $t("form.nodePort") }}</small></span
                        >
                      </div>
                      <div class="mr-5">
                        <!-- <el-form-item
                          class="mb-2"
                          label-width="100px"
                          :label="$t('form.containerDeployment')"
                        >
                          <div class="ellipsis">
                            {{
                              nowContainerDeploy
                                ? nowContainerDeploy.name
                                : $t("form.notSelected")
                            }}
                          </div>
                        </el-form-item> -->
                        <el-form-item
                          :label="$t('form.container') + ':'"
                          class="mb-2"
                          label-width="80px"
                        >
                          <el-select
                            v-model="item.service"
                            class="w-32"
                            :placeholder="$t('form.pleaseSelectContainer')"
                          >
                            <el-option
                              v-for="item in containerList"
                              :key="item.id"
                              :label="item.name"
                              :value="item.id"
                            />
                          </el-select>
                        </el-form-item>
                        <el-form-item
                          class="mb-2"
                          label-width="80px"
                          :label="$t('form.port') + ':'"
                          :rules="[
                            {
                              required: true,
                              message: $t('form.pleaseInputPort'),
                              trigger: 'blur',
                            },
                          ]"
                          :prop="'target_services.' + index + '.target_port'"
                        >
                          <el-input
                            v-model="
                              dataform.target_services[index].target_port
                            "
                            class="w-32"
                            :placeholder="$t('form.pleaseInputPort')"
                          />
                        </el-form-item>
                      </div>

                      <div class="mx-5">
                        <el-form-item
                          label-width="95px"
                          :label="$t('form.protocol') + ':'"
                          :rules="[
                            {
                              required: true,
                              message: $t('form.pleaseSelectProtocol'),
                              trigger: 'blur',
                            },
                          ]"
                          :prop="'target_services.' + index + '.protocol'"
                        >
                          <el-select
                            v-model="dataform.target_services[index].protocol"
                            class="w-32"
                            :placeholder="$t('form.pleaseSelectProtocol')"
                          >
                            <el-option
                              v-for="item in agreementlist"
                              :key="item.value"
                              :label="item.label"
                              :value="item.value"
                            />
                          </el-select>
                        </el-form-item>
                        <el-form-item>
                          <div class="arrow" />
                        </el-form-item>
                        <el-form-item
                          v-if="
                            dataform.target_services[index].protocol == 'HTTPS'
                          "
                          label-width="95px"
                          :label="$t('form.certificate') + ':'"
                          :prop="'target_services.' + index + '.cert'"
                          :rules="[
                            {
                              required: true,
                              message: $t('form.pleaseInputCertificateContent'),
                              trigger: 'blur',
                            },
                          ]"
                        >
                          <el-popover
                            :ref="`node-popover-${dataform.target_services[index].cert}`"
                            placement="top"
                            :title="$t('form.certificate')"
                            width="600"
                            popper-class="tablepopover"
                            trigger="hover"
                            :content="dataform.target_services[index].cert"
                          >
                            <span
                              v-show="dataform.target_services[index].cert"
                              slot="reference"
                              class="w-32"
                              >{{
                                dataform.target_services[index].cert
                                  ? dataform.target_services[index].cert.slice(
                                      0,
                                      12
                                    ) + "..."
                                  : ""
                              }}</span
                            >
                          </el-popover>
                          <el-button
                            type="primary"
                            size="mini"
                            class="w-32"
                            @click="clickLoad(item, index)"
                            >{{ $t("form.import") }}</el-button
                          >
                          <input
                            id="files"
                            ref="refFile"
                            type="file"
                            style="display: none"
                            @change="fileLoad(item, index)"
                          />
                        </el-form-item>
                      </div>
                      <div class="ml-2 mr-2">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          class="styles__StyledSVGIconPathComponent-sc-gbp7ch-0 gFGDaY svg-icon-path-icon fill"
                          viewBox="0 0 48 48"
                          width="44"
                          height="44"
                        >
                          <defs data-reactroot="" />
                          <g>
                            <rect
                              width="48"
                              height="48"
                              fill="white"
                              fill-opacity="0.01"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M8 12C10.2091 12 12 10.2091 12 8C12 5.79086 10.2091 4 8 4C5.79086 4 4 5.79086 4 8C4 10.2091 5.79086 12 8 12Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M10 42C13.3137 42 16 39.3137 16 36C16 32.6863 13.3137 30 10 30C6.68629 30 4 32.6863 4 36C4 39.3137 6.68629 42 10 42Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M38 44C41.3137 44 44 41.3137 44 38C44 34.6863 41.3137 32 38 32C34.6863 32 32 34.6863 32 38C32 41.3137 34.6863 44 38 44Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M22 28C26.4183 28 30 24.4183 30 20C30 15.5817 26.4183 12 22 12C17.5817 12 14 15.5817 14 20C14 24.4183 17.5817 28 22 28Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M34 12C36.2091 12 38 10.2091 38 8C38 5.79086 36.2091 4 34 4C31.7909 4 30 5.79086 30 8C30 10.2091 31.7909 12 34 12Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M11 11L15 15"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M30 12L28 14"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M34 33.5L28 26"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M14 31L18 27"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                          </g>
                        </svg>
                        <span class="block" style="width: 70px"
                          ><small>{{ $t("form.servicePort") }}</small></span
                        >
                      </div>
                      <div class="ml-4 w-68">
                        <el-form-item
                          class="mb-2"
                          label-width="72px"
                          :label="$t('form.gateway') + ':'"
                        >
                          <el-select
                            v-model="item.svc_node"
                            class="w-52"
                            clearable
                            :placeholder="$t('form.defaultSystemAssigned')"
                          >
                            <el-option
                              v-for="(item, index) in serviceGatewaysList"
                              :key="index"
                              :label="item.name"
                              :value="item.service_gateway_id"
                            />
                          </el-select>
                        </el-form-item>
                        <el-form-item
                          class="mb-0"
                          label-width="72px"
                          :label="$t('form.port') + ':'"
                        >
                          <el-input
                            v-model="item.svc_port"
                            class="w-52"
                            clearable
                            :placeholder="$t('form.defaultSystemAssigned')"
                          />
                        </el-form-item>
                      </div>
                      <div class="portmapping_content_delete ml-2">
                        <i class="el-icon-delete" @click="clickdelete(index)" />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <el-button
                class="mt-2"
                size="mini"
                type="primary"
                @click="pushmap"
                >{{ $t("form.addMapping") }}</el-button
              >
            </div>
            <!-- 基本信息 end -->
          </el-form-item>
        </div>
        <div v-if="dataform.target_type == 'compose'">
          <!-- 这个下面是根据不同的类型选择显示不同的模板 -->
          <el-form-item
            :label="$t('form.composeDeployment') + ':'"
            prop="deployment"
          >
            <el-select
              v-model="dataform.deployment"
              :placeholder="$t('form.pleaseSelectApplicationDeployment')"
              @change="changeApplicationDeploy"
            >
              <el-option
                v-for="item in ApplicationDeployList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            :label="$t('form.portMapping') + ':'"
            prop="data_source_id"
          >
            <div class="portmapping_border">
              <div
                v-for="(item, index) in dataform.target_services"
                :key="index"
                class="mb-4 overflow-hidden"
              >
                <div class="portmapping_content">
                  <!-- 基本信息 start -->
                  <div class="block">
                    <div class="portmapping_content_left">
                      <div class="mr-5">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          class="styles__StyledSVGIconPathComponent-sc-gbp7ch-0 gFGDaY svg-icon-path-icon fill"
                          viewBox="0 0 32 32"
                          width="44"
                          height="44"
                        >
                          <defs data-reactroot="" />
                          <g>
                            <path
                              d="M9 3.333c3.13 0 5.667 2.537 5.667 5.667v0 5.667h-5.667c-3.13 0-5.667-2.537-5.667-5.667s2.537-5.667 5.667-5.667v0zM12 12v-3c0-1.657-1.343-3-3-3s-3 1.343-3 3c0 1.657 1.343 3 3 3v0h3zM9 17.333h5.667v5.667c0 3.13-2.537 5.667-5.667 5.667s-5.667-2.537-5.667-5.667c0-3.13 2.537-5.667 5.667-5.667v0zM9 20c-1.657 0-3 1.343-3 3s1.343 3 3 3c1.657 0 3-1.343 3-3v0-3h-3zM23 3.333c3.13 0 5.667 2.537 5.667 5.667s-2.537 5.667-5.667 5.667v0h-5.667v-5.667c0-3.13 2.537-5.667 5.667-5.667v0zM23 12c1.657 0 3-1.343 3-3s-1.343-3-3-3c-1.657 0-3 1.343-3 3v0 3h3zM17.333 17.333h5.667c3.13 0 5.667 2.537 5.667 5.667s-2.537 5.667-5.667 5.667c-3.13 0-5.667-2.537-5.667-5.667v0-5.667zM20 20v3c0 1.657 1.343 3 3 3s3-1.343 3-3c0-1.657-1.343-3-3-3v0h-3z"
                            />
                          </g>
                        </svg>
                        <span class="block" style="width: 55px"
                          ><small>{{ $t("form.nodePort") }}</small></span
                        >
                      </div>
                      <div class="mr-5">
                        <!-- <el-form-item
                          class="mb-2"
                          label-width="100px"
                          :label="$t('form.composeDeployment')"
                        >
                          <div class="ellipsis">
                            {{
                              nowApplicationDeploy
                                ? nowApplicationDeploy.name
                                : $t("form.notSelected")
                            }}
                          </div>
                        </el-form-item> -->
                        <el-form-item
                          :label="$t('form.application') + ':'"
                          class="mb-2"
                          label-width="90px"
                        >
                          <el-select
                            v-model="item.service"
                            :placeholder="$t('form.pleaseSelectApplication')"
                          >
                            <el-option
                              v-for="item in ApplicationList"
                              :key="item.id"
                              :label="item.name"
                              :value="item.id"
                            />
                          </el-select>
                        </el-form-item>
                        <el-form-item
                          class="mb-2"
                          label-width="90px"
                          :label="$t('form.port') + ':'"
                          :rules="[
                            {
                              required: true,
                              message: $t('form.pleaseInputPort'),
                              trigger: 'blur',
                            },
                          ]"
                          :prop="'target_services.' + index + '.target_port'"
                        >
                          <el-input
                            v-model="
                              dataform.target_services[index].target_port
                            "
                            class="w-32"
                            :placeholder="$t('form.pleaseInputPort')"
                          />
                        </el-form-item>
                      </div>

                      <div class="mx-5">
                        <el-form-item
                          label-width="95px"
                          :label="$t('form.protocol') + ':'"
                          :rules="[
                            {
                              required: true,
                              message: $t('form.pleaseSelectProtocol'),
                              trigger: 'blur',
                            },
                          ]"
                          :prop="'target_services.' + index + '.protocol'"
                        >
                          <el-select
                            v-model="dataform.target_services[index].protocol"
                            class="w-32"
                            :placeholder="$t('form.pleaseSelectProtocol')"
                          >
                            <el-option
                              v-for="item in agreementlist"
                              :key="item.value"
                              :label="item.label"
                              :value="item.value"
                            />
                          </el-select>
                        </el-form-item>
                        <el-form-item>
                          <div class="arrow" />
                        </el-form-item>
                        <el-form-item
                          v-if="
                            dataform.target_services[index].protocol == 'HTTPS'
                          "
                          label-width="95px"
                          :label="$t('form.certificate')"
                          :prop="'target_services.' + index + '.cert'"
                          :rules="[
                            {
                              required: true,
                              message: $t('form.pleaseInputCertificateContent'),
                              trigger: 'blur',
                            },
                          ]"
                        >
                          <el-popover
                            :ref="`node-popover-${dataform.target_services[index].cert}`"
                            placement="top"
                            :title="$t('form.certificate')"
                            width="600"
                            popper-class="tablepopover"
                            trigger="hover"
                            :content="dataform.target_services[index].cert"
                          >
                            <span
                              v-show="dataform.target_services[index].cert"
                              slot="reference"
                              class="w-32"
                              >{{
                                dataform.target_services[index].cert
                                  ? dataform.target_services[index].cert.slice(
                                      0,
                                      12
                                    ) + "..."
                                  : ""
                              }}</span
                            >
                          </el-popover>
                          <el-button
                            type="primary"
                            size="mini"
                            class="w-32"
                            @click="clickLoad(item, index)"
                            >{{ $t("form.import") }}</el-button
                          >
                          <input
                            id="files"
                            ref="refFile"
                            type="file"
                            style="display: none"
                            @change="fileLoad(item, index)"
                          />
                        </el-form-item>
                      </div>
                      <div class="ml-2 mr-2">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          class="styles__StyledSVGIconPathComponent-sc-gbp7ch-0 gFGDaY svg-icon-path-icon fill"
                          viewBox="0 0 48 48"
                          width="44"
                          height="44"
                        >
                          <defs data-reactroot="" />
                          <g>
                            <rect
                              width="48"
                              height="48"
                              fill="white"
                              fill-opacity="0.01"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M8 12C10.2091 12 12 10.2091 12 8C12 5.79086 10.2091 4 8 4C5.79086 4 4 5.79086 4 8C4 10.2091 5.79086 12 8 12Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M10 42C13.3137 42 16 39.3137 16 36C16 32.6863 13.3137 30 10 30C6.68629 30 4 32.6863 4 36C4 39.3137 6.68629 42 10 42Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M38 44C41.3137 44 44 41.3137 44 38C44 34.6863 41.3137 32 38 32C34.6863 32 32 34.6863 32 38C32 41.3137 34.6863 44 38 44Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M22 28C26.4183 28 30 24.4183 30 20C30 15.5817 26.4183 12 22 12C17.5817 12 14 15.5817 14 20C14 24.4183 17.5817 28 22 28Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              fill-rule="evenodd"
                              clip-rule="evenodd"
                              d="M34 12C36.2091 12 38 10.2091 38 8C38 5.79086 36.2091 4 34 4C31.7909 4 30 5.79086 30 8C30 10.2091 31.7909 12 34 12Z"
                              fill="none"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M11 11L15 15"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M30 12L28 14"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M34 33.5L28 26"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                            <path
                              d="M14 31L18 27"
                              stroke="#333"
                              stroke-width="4"
                              stroke-linecap="round"
                              stroke-linejoin="round"
                            />
                          </g>
                        </svg>
                        <span class="block" style="width: 70px"
                          ><small>{{ $t("form.servicePort") }}</small></span
                        >
                      </div>
                      <div class="ml-2 w-68">
                        <el-form-item
                          class="mb-2"
                          label-width="72px"
                          :label="$t('form.gateway') + ':'"
                        >
                          <el-select
                            v-model="item.svc_node"
                            class="w-52"
                            clearable
                            :placeholder="$t('form.defaultSystemAssigned')"
                          >
                            <el-option
                              v-for="(item, index) in serviceGatewaysList"
                              :key="index"
                              :label="item.name"
                              :value="item.service_gateway_id"
                            />
                          </el-select>
                        </el-form-item>
                        <el-form-item
                          class="mb-0"
                          label-width="72px"
                          :label="$t('form.port') + ':'"
                        >
                          <el-input
                            v-model="item.svc_port"
                            class="w-52"
                            clearable
                            :placeholder="$t('form.defaultSystemAssigned')"
                          />
                        </el-form-item>
                      </div>
                      <div class="portmapping_content_delete ml-2">
                        <i class="el-icon-delete" @click="clickdelete(index)" />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <el-button
                class="mt-2"
                size="mini"
                type="primary"
                @click="pushmap"
                >{{ $t("form.addMapping") }}</el-button
              >
            </div>
            <!-- 基本信息 end -->
          </el-form-item>
        </div>
      </el-form>
    </el-card>
    <div
      class="absolute z-30 px-3 py-2 bg-white"
      style="
        bottom: -15px;
        right: -10px;
        left: -9px;
        border-top: 1px solid rgba(0, 0, 0, 0.09);
        box-shadow: 0 2px 30px 0 rgba(0, 0, 0, 0.09);
      "
    >
      <el-row>
        <el-col :span="12">
          <div>
            <span class="text-red-500" style="line-height: 32px">
              <span><span class="font-bold text-xl ml-2" /></span>
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="text-right">
            <el-button
              type="primary"
              size="small"
              @click="pushalarmmanagement()"
              >{{ $t("form.cancel") }}</el-button
            >
            <el-button
              type="primary"
              size="small"
              :loading="btnLoading"
              @click="submitForm('ruleForm')"
              >{{ $t("form.create") }}</el-button
            >
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  addServicePorts,
  getServiceGateways,
  edges,
  getDeploys,
  getApplicationDeploys,
  getDeploysInstances,
  getDeploysSystemInstances,
} from "@/api/mainApi";
export default {
  components: {},

  data() {
    return {
      btnLoading: false,
      nowNode: "",
      certShow: false,
      // 添加的数据
      agreementlist: [
        {
          value: "TCP",
          label: "TCP",
        },
        {
          value: "HTTP",
          label: "HTTP",
        },

        {
          value: "HTTPS",
          label: "HTTPS",
        },
      ],
      // 这是要添加的数据
      dataform: {
        target_type: "edge",
        deployment: "",
        name: "",
        description: "",
        target_services: [
          {
            // 高阶选项里面的端口和协议
            svc_node: "",
            svc_port: "",
            // 端口
            target_port: "",
            // 协议
            protocol: "",
            // 控制它的高阶选项是否展开
            // 容器/应用ID
            service: "",
            // 证书
            cert: "",
          },
        ],
      },
      serviceGatewaysList: [],
      nodes_list: [],
      rules: {
        name: [
          {
            required: true,
            message: this.$t("message.pleaseInputName"),
            trigger: "blur",
          },
        ],
        target_type: [
          {
            required: true,
            message: this.$t("message.pleaseSelectType"),
            trigger: "change",
          },
        ],
        deployment: [
          {
            required: true,
            message: this.$t("message.pleaseSelectContent"),
            trigger: "change",
          },
        ],
      },
      // 映射的数据
      // 容器部署数据
      containerDeploysList: [],
      nowContainerDeploy: "",
      // 应用部署
      ApplicationDeployList: [],
      nowApplicationDeploy: "",
      // 部署下容器
      containerList: [],
      nowContainer: "",
      // 部署下应用
      ApplicationList: [],
      nowApplication: "",
      nowNode: "",
    };
  },
  watch: {},
  watch: {},
  async created() {
    this.init();
  },
  mounted() {
    this.getedges();
    this.getContainerDeploys();
    this.getApplicationDeploys();
  },
  computed: {
    ...mapGetters(["userInfo", "filesize"]),
  },
  methods: {
    clickLoad(item, index) {
      var indexs = 0;
      this.dataform.target_services.forEach((element) => {
        if (element.protocol == "HTTPS") {
          (element.indexs = indexs), indexs++;
        }
      });
      this.$refs.refFile[item.indexs].dispatchEvent(new MouseEvent("click"));
    },
    fileLoad(item, index) {
      var _this = this;
      const selectedFile = _this.$refs.refFile[item.indexs].files[0];
      if (selectedFile.size / 1024 / 1024 > _this.filesize) {
        _this.$notify({
          title: this.$t("message.uploadFileSizeExceed", {
            filesize: _this.filesize,
          }),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      var reader = new FileReader();
      reader.readAsText(selectedFile);
      reader.onload = function () {
        // console.log(_this.dataform.target_services)
        _this.dataform.target_services[index].cert = this.result;
      };

      document.getElementById("files").value = "";
    },
    // 获取节点的信息
    async getedges() {
      const list = await edges({});
      this.nodes_list = list.nodes;
    },
    // 获取容器部署信息
    async getContainerDeploys() {
      const list = await getDeploys();
      this.containerDeploysList = list.deployments;
    },
    // 获取应用部署信息
    async getApplicationDeploys() {
      const list = await getApplicationDeploys();
      this.ApplicationDeployList = list.deployments;
    },
    // 获取容器信息
    async getContainers(id) {
      const list = await getDeploysInstances(id);
      this.containerList = list.containers;
    },
    // 获取应用信息
    async getApplications(id) {
      const list = await getDeploysSystemInstances(id);
      this.ApplicationList = list.stacks;
    },
    resetFrom() {
      (this.dataform = {
        target_type: this.dataform.target_type,
        deployment: "",
        name: "",
        description: "",
        target_services: [
          {
            // 高阶选项里面的端口和协议
            svc_node: "",
            svc_port: "",
            // 端口
            target_port: "",
            // 协议
            protocol: "",
            // 控制它的高阶选项是否展开
            service: "",
            // 证书
            cert: "",
          },
        ],
      }),
        (this.nowContainerDeploy = "");
      this.nowApplicationDeploy = "";
      this.nowContainer = "";
      this.nowApplication = "";
      this.nowNode = "";
      this.$refs["ruleForm"].resetFields();
    },
    handleresetFrom() {
      var data = JSON.parse(JSON.stringify(this.dataform));
      (this.dataform = {
        target_type: data.target_type,
        deployment: "",
        name: data.name,
        description: data.description,
        target_services: [
          {
            // 高阶选项里面的端口和协议
            svc_node: "",
            svc_port: "",
            // 端口
            target_port: "",
            // 协议
            protocol: "",
            // 控制它的高阶选项是否展开
            service: "",
            // 证书
            cert: "",
          },
        ],
      }),
        (this.nowContainerDeploy = "");
      this.nowApplicationDeploy = "";
      this.nowContainer = "";
      this.nowApplication = "";
      this.nowNode = "";
      this.$refs["ruleForm"].resetFields();
    },
    changeNode(val) {
      if (val == "" || val == null) {
        this.nowNode = "";
        return;
      }
      this.nodes_list.find((item) => {
        if (item.node_id == val) {
          this.nowNode = item;
        }
      });
    },
    changeContainerDeploy(val) {
      if (val == "" || val == null) {
        this.nowContainerDeploy = "";
        return;
      }
      this.containerDeploysList.find((item) => {
        if (item.id == val) {
          this.nowContainerDeploy = item;
          this.getContainers(val);
          return;
        }
      });
    },
    changeApplicationDeploy(val) {
      if (val == "" || val == null) {
        this.nowApplicationDeploy = "";
        return;
      }
      this.ApplicationDeployList.find((item) => {
        if (item.id == val) {
          this.nowApplicationDeploy = item;
          this.getApplications(val);
          return;
        }
      });
    },
    // 添加映射
    pushmap() {
      this.dataform.target_services.push({
        svc_node: "",
        svc_port: "",
        target_port: "",
        protocol: "",
        service: "",
        cert: "",
      });
    },
    // 删除映射
    clickdelete(index) {
      this.dataform.target_services.splice(index, 1);
    },

    async init() {
      const list = await getServiceGateways({});
      this.serviceGatewaysList = list.service_gateways;
    },
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.btnLoading = true;
          var data = {
            name: this.dataform.name,
            description: this.dataform.description,
            deployment: this.dataform.deployment,
            target_type: this.dataform.target_type,
            target_services: this.dataform.target_services.map((item) => {
              return {
                svc_node: item.svc_node,
                svc_port: item.svc_port,
                target_port: item.target_port,
                protocol: item.protocol,
                service: item.service ? item.service : "",
                cert: item.cert ? item.cert : "",
              };
            }),
          };
          data.target_services.map((item) => {
            if (item.svc_node == "") {
              delete item.svc_node;
            }
            if (item.svc_port == "") {
              delete item.svc_port;
            }
          });
          addServicePorts(data)
            .then((res) => {
              this.btnLoading = false;
              this.$notify({
                title: this.$t("message.createSuccess"),
                type: "success",
                duration: 2500,
              });
              this.$router.push("/middleware/service");
            })
            .catch((err) => {
              this.btnLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    pushalarmmanagement() {
      this.$router.path("/middleware/service");
    },
  },
};
</script>
<style lang="scss" scoped>
.ellipsis {
  max-width: 125px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.componentadd_title {
  font-size: 16px;
  font-weight: 600;
}

.portmapping_border {
  border: 1px solid rgb(187, 187, 187);
  padding: 15px;
  border-radius: 10px;
  display: inline-block;

  .portmapping_content {
    width: 1000px;
    float: left;
    border: 1px solid rgb(187, 187, 187);
    padding: 10px 15px;
    border-radius: 10px;
    justify-content: space-between;
    align-items: center;

    &:last-child {
      margin-bottom: 0 !important;
    }

    .portmapping_content_left {
      display: flex;
      align-items: center;

      .arrow {
        position: relative;
        margin-right: 10px;
        position: relative;
        width: 170px;
        height: 4px;
        /* 控制箭头高度 */
        background: rgb(108, 108, 108);

        &::before {
          content: "";
          display: block;
          position: absolute;
          right: 0;
          top: 50%;
          width: 0;
          height: 0;
          border-top: 10px solid transparent;
          /* 控制箭头高度 */
          border-bottom: 10px solid transparent;
          /* 控制箭头高度 */
          border-right: 10px solid rgb(108, 108, 108);
          /* 控制箭头宽度和颜色 */
          transform: translate(10px, -50%) rotate(180deg);
          /* 控制箭头朝向和位置 */
        }
      }

      .portmapping_content_delete {
        cursor: pointer;
        font-size: 18px;
      }
    }
  }
}
</style>
