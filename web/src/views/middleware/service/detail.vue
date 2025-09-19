<template>
  <div class="mt-2">
    <el-form
      ref="form"
      :model="dataform"
      :rules="editpage ? '' : rules"
      size="small"
      label-position="right"
      style="display: flex; flex-wrap: wrap"
      label-width="140px"
    >
      <el-card class="mb-2 w-full" shadow="never">
        <el-row class="w-full">
          <el-col :span="12">
            <el-form-item :label="$t('form.name') + ':'" prop="name">
              <el-input
                v-if="!editpage"
                v-model="dataform.name"
                class="w-72"
                :placeholder="$t('form.pleaseInputName')"
              />
              <span v-else>{{ dataform.name ? dataform.name : "-" }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.description') + ':'"
              prop="description"
            >
              <el-input
                v-if="!editpage"
                v-model="dataform.description"
                type="textarea"
                maxlength="255"
                show-word-limit
                class="w-72"
                :placeholder="$t('form.pleaseInputDescription')"
              />
              <span v-else>{{
                dataform.description ? dataform.description : "-"
              }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('form.creator') + ':'">
              <span>{{ dataform.user_name ? dataform.user_name : "-" }}</span>
            </el-form-item>
            <el-form-item :label="$t('form.createTime') + ':'">
              {{ dataform.create_time ? dataform.create_time : "-" }}
            </el-form-item>
            <el-form-item :label="$t('form.lastModifiedTime') + ':'">
              <span>{{
                dataform.update_time ? dataform.update_time : "-"
              }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="mb-2 w-full">
        <div class="edit">
          <el-button
            v-if="editpage"
            size="small"
            type="primary"
            @click="editpage = false"
            >{{ $t("form.edit") }}</el-button
          >
          <el-button v-if="!editpage" size="small" @click="cancelEdit()">{{
            $t("form.cancel")
          }}</el-button>
          <el-button
            v-if="!editpage"
            size="small"
            :loading="btnLoading"
            type="primary"
            @click="saveEdit('form')"
            >{{ $t("form.save") }}</el-button
          >
        </div>
        <!-- 这是编辑 -->
        <div>
          <el-form-item :label="$t('form.selectType') + ':'" prop="target_type">
            <div>
              <el-radio-group
                v-model="dataform.target_type"
                :disabled="true"
                size="medium"
              >
                <el-radio-button label="edge">{{
                  $t("form.node")
                }}</el-radio-button>
                <el-radio-button label="container">{{
                  $t("form.container")
                }}</el-radio-button>
                <el-radio-button label="compose">{{
                  $t("form.application")
                }}</el-radio-button>
              </el-radio-group>
            </div>
          </el-form-item>
          <div v-if="dataform.target_type == 'edge'">
            <!-- 这个下面是根据不同的类型选择显示不同的模板 -->
            <el-form-item :label="$t('form.node') + ':'" prop="deployment">
              <el-select
                v-model="dataform.deployment"
                :disabled="true"
                :placeholder="$t('form.pleaseSelectNode')"
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
              <div
                v-if="
                  dataform.target_services &&
                  dataform.target_services.length > 0
                "
                class="portmapping_border"
              >
                <div
                  v-for="(item, index) in dataform.target_services"
                  :key="index"
                  class="mb-4 overflow-hidden"
                >
                  <div class="portmapping_content">
                    <!-- 基本信息 start -->
                    <div class="block">
                      <div class="portmapping_content_left">
                        <div class="mr-2">
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
                        <div class="mr-2">
                          <el-form-item
                            class="mb-2"
                            label-width="55px"
                            :label="$t('form.node') + ':'"
                          >
                            <el-popover
                              v-if="nowData.length > 12"
                              placement="top-start"
                              :title="$t('form.node')"
                              popper-class="tablepopover"
                              width="200"
                              trigger="hover"
                              :content="nowData"
                            >
                              <span
                                slot="reference"
                                class="ellipsis block w-32"
                                style="cursor: pointer"
                                >{{
                                  nowData ? nowData : $t("form.notSelected")
                                }}</span
                              >
                            </el-popover>
                            <span v-else class="ellipsis block w-32">
                              {{
                                nowData ? nowData : $t("form.notSelected")
                              }}</span
                            >
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
                              v-if="!editpage"
                              v-model="
                                dataform.target_services[index].target_port
                              "
                              class="w-32"
                              :placeholder="$t('form.pleaseInputPort')"
                            />
                            <span v-else class="block w-32 ellipsis">{{
                              dataform.target_services[index].target_port
                            }}</span>
                          </el-form-item>
                        </div>

                        <div class="mx-5">
                          <el-form-item
                            label-width="95px"
                            :label="$t('form.protocol') + ':'"
                            :class="!editpage ? '' : 'mb-0'"
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
                              v-if="!editpage"
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
                            <span v-else class="block w-32">{{
                              dataform.target_services[index].protocol
                            }}</span>
                          </el-form-item>
                          <el-form-item :class="editpage ? 'mb-0' : ''">
                            <div class="arrow" />
                          </el-form-item>
                          <el-form-item
                            v-if="
                              dataform.target_services[index].protocol ==
                              'HTTPS'
                            "
                            label-width="95px"
                            :label="$t('form.certificate') + ':'"
                            :prop="'target_services.' + index + '.cert'"
                            :rules="[
                              {
                                required: true,
                                message: $t(
                                  'form.pleaseEnterCertificateContent'
                                ),
                                trigger: 'blur',
                              },
                            ]"
                            style="margin-bottom: 0px"
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
                                class="w-32 flex"
                                style="white-space: nowrap; cursor: pointer"
                                >{{
                                  dataform.target_services[index].cert
                                    ? dataform.target_services[
                                        index
                                      ].cert.slice(0, 12) + "..."
                                    : ""
                                }}</span
                              >
                            </el-popover>
                            <span
                              v-if="
                                dataform.target_services[index].protocol ==
                                  'HTTPS' && !editpage
                              "
                            >
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
                            </span>

                            <!-- <el-input v-model="item.cert"
                                      class="w-28"
                                      v-if="dataform.target_services[index].protocol =='HTTPS'&&!editpage"
                                      placeholder="请输入内容"></el-input>
                            <span v-else-if="dataform.target_services[index].protocol =='HTTPS'&& editpage"
                                  class="block w-32">{{item.cert? item.cert.slice(0,40):"" }}</span> -->
                          </el-form-item>
                          <el-form-item v-if="item.status" class="mb-0">
                            <div class="text-center">
                              <span
                                v-if="item.status.code == 0"
                                class="text-blue-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 1"
                                class="text-yellow-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 2"
                                class="text-green-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 50"
                                class="text-red-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 51"
                                class="text-red-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 52"
                                class="text-red-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 90"
                                class="text-red-300"
                                >{{ item.status.desc.cn }}</span
                              >
                            </div>
                          </el-form-item>
                        </div>
                        <div class="ml-5 mr-2">
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
                        <div class="ml-2 mr-2 w-72">
                          <el-form-item
                            class="mb-2"
                            label-width="72px"
                            :label="$t('form.gateway') + ':'"
                          >
                            <el-select
                              v-if="!editpage"
                              v-model="item.svc_node"
                              class="w-52"
                              clearable
                              :placeholder="$t('form.pleaseSelectGateway')"
                            >
                              <el-option
                                v-for="(item, index) in serviceGatewaysList"
                                :key="index"
                                :label="item.name"
                                :value="item.service_gateway_id"
                              />
                            </el-select>
                            <span
                              v-else
                              class="block w-72"
                              style="line-height: 1 !important"
                            >
                              <span class="block">
                                {{
      item.svc_node_name || item.old_svc_node || item.target_svc_node_name ||
      item.target_svc_node || '
                                ' }}</span
                              >
                              <small v-if="item.target_svc_node">
                                {{ $t("form.userSpecified") }}
                              </small>
                              <small v-else>
                                {{ $t("form.systemAssigned") }}
                              </small>
                            </span>
                          </el-form-item>
                          <el-form-item
                            class="mb-0"
                            label-width="72px"
                            :label="$t('form.port') + ':'"
                          >
                            <el-input
                              v-if="!editpage"
                              v-model="item.svc_port"
                              class="w-52"
                              clearable
                              :placeholder="$t('form.externalPort')"
                            />
                            <span
                              v-else
                              class="block w-72"
                              style="line-height: 1 !important"
                            >
                              <span class="block"> {{ item.svc_port }}</span>
                              <small v-if="item.target_svc_port">
                                {{ $t("form.userSpecified") }}
                              </small>
                              <small v-else>
                                {{ $t("form.systemAssigned") }}
                              </small>
                            </span>
                          </el-form-item>

                          <span
                            v-if="item.svc_ip && editpage"
                            class="block pt-1.5"
                            >{{ $t("form.accessibleIP") }}:{{
                              item.svc_ip
                            }}</span
                          >
                        </div>
                        <div
                          v-if="!editpage"
                          class="portmapping_content_delete"
                        >
                          <i
                            class="el-icon-delete"
                            @click="clickdelete(index)"
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <el-button
                  v-if="!editpage"
                  class="mt-2"
                  size="mini"
                  type="primary"
                  @click="pushmap"
                  >{{ $t("form.addMapping") }}</el-button
                >
              </div>
              <el-button
                v-if="
                  !editpage &&
                  !(
                    dataform.target_services &&
                    dataform.target_services.length > 0
                  )
                "
                class=""
                size="mini"
                type="primary"
                @click="pushmap"
                >{{ $t("form.addMapping") }}</el-button
              >
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
                :placeholder="$t('form.pleaseSelect')"
                :disabled="true"
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
              <div
                v-if="
                  dataform.target_services &&
                  dataform.target_services.length > 0
                "
                class="portmapping_border"
              >
                <div
                  v-for="(item, index) in dataform.target_services"
                  :key="index"
                  class="mb-4 overflow-hidden"
                >
                  <div class="portmapping_content">
                    <!-- 基本信息 start -->
                    <div class="block">
                      <div class="portmapping_content_left">
                        <div class="mr-2">
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
                          <span class="block" style="width: 70px"
                            ><small>{{ $t("form.nodePort") }}</small></span
                          >
                        </div>
                        <div class="mr-2">
                          <!-- <el-form-item
                            class="mb-2"
                            label-width="100px"
                            :label="$t('form.containerDeployment')"
                          >
                            <el-popover
                              v-if="nowData.length > 12"
                              placement="top-start"
                              :title="$t('form.containerDeployment')"
                              popper-class="tablepopover"
                              width="200"
                              trigger="hover"
                              :content="nowData"
                            >
                              <span
                                slot="reference"
                                class="ellipsis block w-32"
                                style="cursor: pointer"
                                >{{
                                  nowData ? nowData : $t("form.notSelected")
                                }}</span
                              >
                            </el-popover>
                            <span v-else class="ellipsis block w-32">
                              {{
                                nowData ? nowData : $t("form.notSelected")
                              }}</span
                            >
                          </el-form-item> -->
                          <el-form-item
                            :label="$t('form.container') + ':'"
                            class="mb-2"
                            label-width="80px"
                          >
                            <el-select
                              v-if="!editpage"
                              v-model="item.service"
                              :placeholder="$t('form.pleaseSelectContainer')"
                              class="w-32"
                            >
                              <el-option
                                v-for="item in containerList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                              />
                            </el-select>
                            <div v-else>
                              <el-popover
                                v-if="
                                  dataform.target_services[index].service_name
                                    .length > 12
                                "
                                placement="top-start"
                                :title="$t('form.container')"
                                popper-class="tablepopover"
                                width="200"
                                trigger="hover"
                                :content="
                                  dataform.target_services[index].service_name
                                "
                              >
                                <span
                                  slot="reference"
                                  class="block w-32 ellipsis"
                                  style="cursor: pointer"
                                  >{{
                                    dataform.target_services[index].service_name
                                  }}</span
                                >
                              </el-popover>
                              <span v-else class="block w-32 ellipsis">{{
                                dataform.target_services[index].service_name
                              }}</span>
                            </div>
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
                              v-if="!editpage"
                              v-model="
                                dataform.target_services[index].target_port
                              "
                              class="w-32"
                              :placeholder="$t('form.pleaseInputPort')"
                            />
                            <span v-else class="block w-32">{{
                              dataform.target_services[index].target_port
                            }}</span>
                          </el-form-item>
                        </div>

                        <div class="mx-5">
                          <el-form-item
                            label-width="95px"
                            :label="$t('form.protocol') + ':'"
                            :class="!editpage ? '' : 'mb-0'"
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
                              v-if="!editpage"
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
                            <span v-else class="block w-32">{{
                              dataform.target_services[index].protocol
                            }}</span>
                          </el-form-item>
                          <el-form-item :class="editpage ? 'mb-0' : ''">
                            <div class="arrow" />
                          </el-form-item>

                          <el-form-item
                            v-if="
                              dataform.target_services[index].protocol ==
                              'HTTPS'
                            "
                            label-width="95px"
                            :label="$t('form.certificate') + ':'"
                            :prop="'target_services.' + index + '.cert'"
                            :rules="[
                              {
                                required: true,
                                message: $t(
                                  'form.pleaseInputCertificateContent'
                                ),
                                trigger: 'blur',
                              },
                            ]"
                            style="margin-bottom: 0px"
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
                                class="w-32 flex"
                                style="white-space: nowrap; cursor: pointer"
                                >{{
                                  dataform.target_services[index].cert
                                    ? dataform.target_services[
                                        index
                                      ].cert.slice(0, 12) + "..."
                                    : ""
                                }}</span
                              >
                            </el-popover>
                            <span
                              v-if="
                                dataform.target_services[index].protocol ==
                                  'HTTPS' && !editpage
                              "
                            >
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
                            </span>

                            <!-- <el-input v-model="item.cert"
                                      class="w-28"
                                      v-if="dataform.target_services[index].protocol =='HTTPS'&&!editpage"
                                      placeholder="请输入内容"></el-input>
                            <span v-else-if="dataform.target_services[index].protocol =='HTTPS'&& editpage"
                                  class="block w-32">{{item.cert? item.cert.slice(0,40):"" }}</span> -->
                          </el-form-item>

                          <el-form-item v-if="item.status" class="mb-0">
                            <div class="text-center">
                              <span
                                v-if="item.status.code == 0"
                                class="text-blue-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 1"
                                class="text-yellow-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 2"
                                class="text-green-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 50"
                                class="text-red-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 51"
                                class="text-red-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 52"
                                class="text-red-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 90"
                                class="text-red-300"
                                >{{ item.status.desc.cn }}</span
                              >
                            </div>
                          </el-form-item>
                        </div>
                        <div class="ml-5 mr-2">
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
                        <div class="ml-2 mr-2 w-68">
                          <el-form-item
                            class="mb-2"
                            label-width="72px"
                            :label="$t('form.gateway') + ':'"
                          >
                            <el-select
                              v-if="!editpage"
                              v-model="item.svc_node"
                              class="w-52"
                              clearable
                              :placeholder="$t('form.pleaseSelectGateway')"
                            >
                              <el-option
                                v-for="(item, index) in serviceGatewaysList"
                                :key="index"
                                :label="item.name"
                                :value="item.service_gateway_id"
                              />
                            </el-select>
                            <span
                              v-else
                              class="block w-72"
                              style="line-height: 1 !important"
                            >
                              <span class="block">
                                {{
      item.svc_node_name || item.old_svc_node || item.target_svc_node_name ||
      item.target_svc_node || '
                                ' }}</span
                              >
                              <small v-if="item.target_svc_node">
                                {{ $t("form.userSpecified") }}
                              </small>
                              <small v-else>
                                {{ $t("form.systemAssigned") }}
                              </small>
                            </span>
                          </el-form-item>
                          <el-form-item
                            class="mb-0"
                            label-width="72px"
                            :label="$t('form.port') + ':'"
                          >
                            <el-input
                              v-if="!editpage"
                              v-model="item.svc_port"
                              class="w-52"
                              clearable
                              :placeholder="$t('form.externalPort')"
                            />
                            <span
                              v-else
                              class="block w-72"
                              style="line-height: 1 !important"
                            >
                              <span class="block"> {{ item.svc_port }}</span>
                              <small v-if="item.target_svc_port">
                                {{ $t("form.userSpecified") }}
                              </small>
                              <small v-else>
                                {{ $t("form.systemAssigned") }}
                              </small>
                            </span>
                          </el-form-item>
                          <span
                            v-if="item.svc_ip && editpage"
                            class="block pt-1.5"
                            >{{ $t("form.accessibleIP") }}:{{
                              item.svc_ip
                            }}</span
                          >
                        </div>
                        <div
                          v-if="!editpage"
                          class="portmapping_content_delete"
                        >
                          <i
                            class="el-icon-delete"
                            @click="clickdelete(index)"
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <el-button
                  v-if="!editpage"
                  class="mt-2"
                  size="mini"
                  type="primary"
                  @click="pushmap"
                  >{{ $t("form.addMapping") }}</el-button
                >
              </div>
              <el-button
                v-if="
                  !editpage &&
                  !(
                    dataform.target_services &&
                    dataform.target_services.length > 0
                  )
                "
                class=""
                size="mini"
                type="primary"
                @click="pushmap"
                >{{ $t("form.addMapping") }}</el-button
              >
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
                :disabled="true"
                :placeholder="$t('form.pleaseSelect')"
              >
                <el-option
                  v-for="item in applicationDeployList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('form.portMapping')" prop="data_source_id">
              <div
                v-if="
                  dataform.target_services &&
                  dataform.target_services.length > 0
                "
                class="portmapping_border"
              >
                <div
                  v-for="(item, index) in dataform.target_services"
                  :key="index"
                  class="mb-4 overflow-hidden"
                >
                  <div class="portmapping_content">
                    <!-- 基本信息 start -->
                    <div class="block">
                      <div class="portmapping_content_left">
                        <div class="mr-2">
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
                        <div class="mr-2">
                          <!-- <el-form-item
                            class="mb-2"
                            label-width="100px"
                            :label="$t('form.composeDeployment')"
                          >
                            <el-popover
                              v-if="nowData.length > 12"
                              placement="top-start"
                              :title="$t('form.composeDeployment')"
                              popper-class="tablepopover"
                              width="200"
                              trigger="hover"
                              :content="nowData"
                            >
                              <span
                                slot="reference"
                                style="cursor: pointer"
                                class="ellipsis block w-32"
                                >{{
                                  nowData ? nowData : $t("form.notSelected")
                                }}</span
                              >
                            </el-popover>
                            <span v-else class="ellipsis block w-32">{{
                              nowData ? nowData : $t("form.notSelected")
                            }}</span>
                          </el-form-item> -->
                          <el-form-item
                            :label="$t('form.application') + ':'"
                            class="mb-2"
                            label-width="90px"
                          >
                            <el-select
                              v-if="!editpage"
                              v-model="item.service"
                              :placeholder="$t('form.pleaseSelectApplication')"
                              class="w-32"
                            >
                              <el-option
                                v-for="item in ApplicationList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                              />
                            </el-select>
                            <div v-else>
                              <el-popover
                                v-if="
                                  dataform.target_services[index].service_name
                                    .length > 12
                                "
                                placement="top-start"
                                :title="$t('form.application')"
                                popper-class="tablepopover"
                                width="200"
                                trigger="hover"
                                :content="
                                  dataform.target_services[index].service_name
                                "
                              >
                                <span
                                  slot="reference"
                                  class="block w-32 ellipsis"
                                  style="cursor: pointer"
                                  >{{
                                    dataform.target_services[index].service_name
                                  }}</span
                                >
                              </el-popover>
                              <span v-else class="block w-32 ellipsis">{{
                                dataform.target_services[index].service_name
                              }}</span>
                            </div>
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
                              v-if="!editpage"
                              v-model="
                                dataform.target_services[index].target_port
                              "
                              class="w-32"
                              :placeholder="$t('form.pleaseInputPort')"
                            />
                            <span v-else class="block w-32">{{
                              dataform.target_services[index].target_port
                            }}</span>
                          </el-form-item>
                        </div>

                        <div class="mx-5">
                          <el-form-item
                            label-width="95px"
                            :label="$t('form.protocol') + ':'"
                            :class="!editpage ? '' : 'mb-0'"
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
                              v-if="!editpage"
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
                            <span v-else class="block w-32">{{
                              dataform.target_services[index].protocol
                            }}</span>
                          </el-form-item>
                          <el-form-item :class="editpage ? 'mb-0' : ''">
                            <div class="arrow" />
                          </el-form-item>
                          <el-form-item
                            v-if="
                              dataform.target_services[index].protocol ==
                              'HTTPS'
                            "
                            label-width="95px"
                            :label="$t('form.certificate') + ':'"
                            :prop="'target_services.' + index + '.cert'"
                            :rules="[
                              {
                                required: true,
                                message: $t(
                                  'form.pleaseInputCertificateContent'
                                ),
                                trigger: 'blur',
                              },
                            ]"
                            style="margin-bottom: 0px"
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
                                class="w-32 flex"
                                style="white-space: nowrap; cursor: pointer"
                                >{{
                                  dataform.target_services[index].cert
                                    ? dataform.target_services[
                                        index
                                      ].cert.slice(0, 12) + "..."
                                    : ""
                                }}</span
                              >
                            </el-popover>
                            <span
                              v-if="
                                dataform.target_services[index].protocol ==
                                  'HTTPS' && !editpage
                              "
                            >
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
                            </span>

                            <!-- <el-input v-model="item.cert"
                                      class="w-28"
                                      v-if="dataform.target_services[index].protocol =='HTTPS'&&!editpage"
                                      placeholder="请输入内容"></el-input>
                            <span v-else-if="dataform.target_services[index].protocol =='HTTPS'&& editpage"
                                  class="block w-32">{{item.cert? item.cert.slice(0,40):"" }}</span> -->
                          </el-form-item>

                          <el-form-item v-if="item.status" class="mb-0">
                            <div class="text-center">
                              <span
                                v-if="item.status.code == 0"
                                class="text-blue-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 1"
                                class="text-yellow-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 2"
                                class="text-green-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 50"
                                class="text-red-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 51"
                                class="text-red-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 52"
                                class="text-red-500"
                                >{{ item.status.desc.cn }}</span
                              >
                              <span
                                v-if="item.status.code == 90"
                                class="text-red-300"
                                >{{ item.status.desc.cn }}</span
                              >
                            </div>
                          </el-form-item>
                        </div>
                        <div class="ml-5 mr-2">
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
                        <div class="ml-2 mr-2 w-68">
                          <el-form-item
                            class="mb-2"
                            label-width="72px"
                            :label="$t('form.gateway') + ':'"
                          >
                            <el-select
                              v-if="!editpage"
                              v-model="item.svc_node"
                              class="w-52"
                              clearable
                              :placeholder="$t('form.pleaseSelectGateway')"
                            >
                              <el-option
                                v-for="(item, index) in serviceGatewaysList"
                                :key="index"
                                :label="item.name"
                                :value="item.service_gateway_id"
                              />
                            </el-select>
                            <span
                              v-else
                              class="block w-72"
                              style="line-height: 1 !important"
                            >
                              <span class="block">
                                {{
      item.svc_node_name || item.old_svc_node || item.target_svc_node_name ||
      item.target_svc_node || '
                                ' }}</span
                              >
                              <small v-if="item.target_svc_node">
                                {{ $t("form.userSpecified") }}
                              </small>
                              <small v-else>
                                {{ $t("form.systemAssigned") }}
                              </small>
                            </span>
                          </el-form-item>
                          <el-form-item
                            class="mb-0"
                            label-width="72px"
                            :label="$t('form.port') + ':'"
                          >
                            <el-input
                              v-if="!editpage"
                              v-model="item.svc_port"
                              class="w-52"
                              clearable
                              :placeholder="$t('form.externalPort')"
                            />
                            <span
                              v-else
                              class="block w-72"
                              style="line-height: 1 !important"
                            >
                              <span class="block"> {{ item.svc_port }}</span>
                              <small v-if="item.target_svc_port">
                                {{ $t("form.userSpecified") }}
                              </small>
                              <small v-else>
                                {{ $t("form.systemAssigned") }}
                              </small>
                            </span>
                          </el-form-item>

                          <span
                            v-if="item.svc_ip && editpage"
                            class="block pt-1.5"
                            >{{ $t("form.accessibleIP") }}:{{
                              item.svc_ip
                            }}</span
                          >
                        </div>
                        <div
                          v-if="!editpage"
                          class="portmapping_content_delete"
                        >
                          <i
                            class="el-icon-delete"
                            @click="clickdelete(index)"
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <el-button
                  v-if="!editpage"
                  class="mt-2"
                  size="mini"
                  type="primary"
                  @click="pushmap"
                  >{{ $t("form.addMapping") }}</el-button
                >
              </div>
              <el-button
                v-if="
                  !editpage &&
                  !(
                    dataform.target_services &&
                    dataform.target_services.length > 0
                  )
                "
                class=""
                size="mini"
                type="primary"
                @click="pushmap"
                >{{ $t("form.addMapping") }}</el-button
              >
              <!-- 基本信息 end -->
            </el-form-item>
          </div>
        </div>
      </el-card>
    </el-form>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  edges,
  getServicePortsDetail,
  getServiceGateways,
  editServicePorts,
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
      editpage: true,
      nowData: "", // 当前选择的节点
      nodes_list: [],
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
      dataform: {},
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
            message: this.$t("message.pleaseSelect"),
            trigger: "change",
          },
        ],
      },
      activeName: "first",
      // 容器部署数据
      containerDeploysList: [],
      // 应用部署
      applicationDeployList: [],
      // 部署下容器
      containerList: [],
      // 部署下应用
      ApplicationList: [],
    };
  },

  async created() {},
  mounted() {
    this.getServiceGateways();
    this.init();
    if (this.$route.params.type == "edge") {
      this.getedges();
    } else if (this.$route.params.type == "container") {
      this.getContainerDeploys();
      this.getContainers(this.$route.params.deployment);
    } else if (this.$route.params.type == "compose") {
      this.getApplicationDeploy();
      this.getApplications(this.$route.params.deployment);
    }
  },
  computed: {
    ...mapGetters(["userInfo"]),
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
    cancelEdit() {
      // 取消编辑
      this.editpage = true;
      this.$refs["form"].resetFields();
      this.init();
    },
    saveEdit(formName) {
      // 这里是点击保存对数据的处理
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.btnLoading = true;
          var data = {
            deployment: this.dataform.deployment,
            service_port_id: this.dataform.service_port_id,
            name: this.dataform.name,
            description: this.dataform.description,
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
          editServicePorts(this.dataform.service_port_id, data)
            .then((res) => {
              this.btnLoading = false;
              this.$notify({
                title: this.$t("message.modifySuccess"),
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
    async getApplicationDeploy() {
      const list = await getApplicationDeploys();
      this.applicationDeployList = list.deployments;
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
    async getServiceGateways() {
      const list = await getServiceGateways({});
      this.serviceGatewaysList = list.service_gateways;
    },
    async init() {
      const list = await getServicePortsDetail(this.$route.params.id);
      list.service_port.target_services.forEach((el) => {
        el.old_svc_node = el.svc_node
          ? JSON.parse(JSON.stringify(el.svc_node))
          : "";
        el.old_svc_port = el.svc_port
          ? JSON.parse(JSON.stringify(el.svc_port))
          : "";
        el.svc_node = el.svc_node
          ? el.svc_node
          : el.target_svc_node
          ? JSON.parse(JSON.stringify(el.target_svc_node))
          : "";
        el.svc_port = el.svc_port
          ? el.svc_port
          : el.target_svc_port
          ? JSON.parse(JSON.stringify(el.target_svc_port))
          : "";
      });
      this.nowData = list.service_port.deployment_name;
      this.dataform = list.service_port;
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

.edit {
  width: 100%;
  text-align: right;
}
</style>
