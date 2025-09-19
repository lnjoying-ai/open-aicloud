<template>
  <div class="login-container">
    <div class="inline-block languageChange">
      <el-dropdown class="pl-4 pr-4" @command="changeLanguage">
        <span class="el-dropdown-link">
          <span>{{ $t("domain.language") }}</span>
          <i class="el-icon-arrow-down el-icon--right" />
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="en">English</el-dropdown-item>
          <el-dropdown-item command="zh-CN">中文</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <div class="login_bg">
      <router-link to="/" class="logo">
        <img src="@/assets/images/public/logo.svg" alt="logo" />
      </router-link>
      <div class="welcomecontent">
        <h3 class="title">91GPU.cloud</h3>
        <h3 class="smallTitle">
          {{ $t("login.buildGlobalSmartComputingInfrastructure") }}
        </h3>
        <h3 class="smallTitle">
          {{ $t("login.accelerateGlobalDigitalRevolution") }}
        </h3>
      </div>
    </div>
    <div class="login_input">
      <div class="login_center">
        <div class="form-wrapper">
          <div class="wrapper">
            <h2 v-if="tabKey == 1">{{ $t("login.login") }}</h2>
            <h2 v-if="tabKey == 4">{{ $t("login.findPassword") }}</h2>
            <el-form
              ref="loginFormref"
              :model="loginForm"
              :rules="loginRules"
              class="login-form"
              auto-complete="on"
              label-position="left"
            >
              <!-- 登录 -->
              <div v-if="tabKey == 1">
                <el-tabs
                  v-model="activelogin"
                  style="
                    font-size: 16px;
                    color: rgb(50, 69, 88);
                    font-weight: 500;
                  "
                >
                  <el-tab-pane
                    :label="$t('login.passwordLogin')"
                    name="account"
                  >
                    <div v-if="activelogin == 'account'">
                      <el-form-item prop="username" class="mt-5">
                        <el-input
                          ref="username"
                          v-model="loginForm.username"
                          :placeholder="$t('login.usernamePhoneEmail')"
                          name="username"
                          type="text"
                          tabindex="1"
                          auto-complete="on"
                          @blur="blurusername"
                        />
                      </el-form-item>
                      <el-form-item prop="password">
                        <el-input
                          :key="passwordType"
                          ref="password"
                          v-model="loginForm.password"
                          :type="passwordType"
                          :placeholder="$t('login.password')"
                          name="password"
                          tabindex="2"
                          auto-complete="on"
                          @keyup.enter.native="handleLogin"
                        />
                        <span class="show-pwd" @click="showPwd">
                          <svg-icon
                            :icon-class="
                              passwordType === 'password' ? 'eye' : 'eye-open'
                            "
                          />
                        </span>
                      </el-form-item>
                      <div
                        v-if="identifyCodeShow"
                        style="display: flex; justify-content: space-between"
                      >
                        <el-form-item
                          style="background: white"
                          class="inputCode"
                          prop="identifyCode"
                        >
                          <el-input
                            v-model="loginForm.identifyCode"
                            type="text"
                            :placeholder="$t('login.verificationCode')"
                            autocomplete="off"
                            style="width: 100%"
                          />
                          <span class="show-code">
                            <img
                              v-if="!codeiscorrect"
                              class="mr-3"
                              src="@/assets/jdglicon/coderror.png"
                              alt=""
                            />
                            <img
                              v-if="codeiscorrect"
                              class="mr-3"
                              src="@/assets/jdglicon/codesuccess.png"
                              alt=""
                            />
                          </span>
                        </el-form-item>
                        <div class="code" @click="refreshCode">
                          <s-identify :identify-code="identifyCode" />
                        </div>
                      </div>
                      <el-button
                        :loading="loading"
                        type="primary"
                        style="width: 100%"
                        @click.native.prevent="handleLogin"
                        >{{ $t("login.login") }}</el-button
                      >
                      <div
                        class="mt-6"
                        style="
                          display: flex;
                          justify-content: center;
                          align-items: center;
                          line-height: 24px;
                        "
                      >
                        <div v-if="tabKey == 1">
                          <span class="goReg" @click="goRegister">{{
                            $t("login.registerNow")
                          }}</span>
                        </div>
                        <span class="line" />
                        <div>
                          <span class="goReg" @click="tabChange(4)">{{
                            $t("login.forgetPassword")
                          }}</span>
                        </div>
                      </div>
                    </div>
                  </el-tab-pane>
                  <el-tab-pane
                    :disabled="true"
                    :label="$t('login.smsLogin')"
                    name="message"
                  >
                    <div v-if="activelogin == 'message'">
                      <el-form-item prop="phone_num" class="mt-5">
                        <el-input
                          ref="phone_num"
                          v-model="loginForm.phone_num"
                          :placeholder="$t('login.phone')"
                          name="phone_num"
                          type="text"
                          tabindex="1"
                          auto-complete="on"
                        >
                          <template slot="prepend">+86</template>
                        </el-input>
                      </el-form-item>
                      <div style="display: flex">
                        <el-form-item prop="verification_code" style="flex: 1">
                          <el-input
                            ref="verification_code"
                            v-model="loginForm.verification_code"
                            :placeholder="$t('login.verificationCode')"
                            name="verification_code"
                            type="text"
                            tabindex="1"
                            auto-complete="on"
                          />
                        </el-form-item>
                        <el-button
                          v-show="regShow"
                          type="primary"
                          size="small"
                          class="sendCode"
                          @click="sendSms('phone_num')"
                          >{{ $t("login.getVerificationCode") }}
                        </el-button>
                        <el-button
                          v-show="!regShow"
                          disabled
                          type="info"
                          class="sendCode"
                          >{{ regCount }} s</el-button
                        >
                      </div>
                      <el-button
                        :loading="loading"
                        type="primary"
                        style="width: 100%"
                        @click.native.prevent="handleLogin"
                        >{{ $t("login.login") }}</el-button
                      >
                      <div
                        class="mt-6"
                        style="
                          display: flex;
                          justify-content: center;
                          align-items: center;
                          line-height: 24px;
                        "
                      >
                        <div v-if="tabKey == 1">
                          <span class="goReg" @click="goRegister">{{
                            $t("login.registerNow")
                          }}</span>
                        </div>
                        <span class="line" />
                        <div>
                          <span class="goReg" @click="tabChange(4)">{{
                            $t("login.forgetPassword")
                          }}</span>
                        </div>
                      </div>
                    </div>
                  </el-tab-pane>
                </el-tabs>
              </div>
            </el-form>
            <el-form
              ref="loginForm"
              :model="loginForm"
              :rules="forgetRules"
              class="login-form"
              auto-complete="on"
              label-position="left"
            >
              <!-- 忘记密码 -->
              <div v-if="tabKey == 4">
                <el-tabs
                  id="index"
                  v-model="activeName"
                  style="
                    font-size: 16px;
                    color: rgb(50, 69, 88);
                    font-weight: 500;
                  "
                  @tab-click="handleClick"
                >
                  <el-tab-pane :label="$t('login.throughPhone')" name="first">
                    <div v-if="this.activeName == 'first'">
                      <el-form-item prop="phone">
                        <el-input
                          v-model="loginForm.phone"
                          :placeholder="$t('login.phone')"
                        >
                          <template slot="prepend">+86</template>
                        </el-input>
                      </el-form-item>
                      <div style="display: flex">
                        <el-form-item prop="verification_code" style="flex: 1">
                          <el-input
                            ref="verification_code"
                            v-model="loginForm.verification_code"
                            :placeholder="$t('login.verificationCode')"
                            name="verification_code"
                            type="text"
                            tabindex="1"
                            autocomplete="off"
                          />
                        </el-form-item>
                        <el-button
                          v-show="regShow"
                          size="small"
                          type="primary"
                          class="sendCode"
                          @click="sendSms('phone')"
                          >{{ $t("login.getVerificationCode") }}
                        </el-button>
                        <el-button
                          v-show="!regShow"
                          disabled
                          type="info"
                          size="small"
                          class="sendCode"
                          >{{ regCount }} s</el-button
                        >
                      </div>
                      <el-form-item prop="new_password">
                        <el-popover
                          v-model="show_check"
                          popper-class="tablepopover"
                          placement="top"
                          width="400"
                          trigger="manual"
                        >
                          <div>
                            <div
                              class="flex"
                              :class="
                                number_pd ? 'active_color' : 'notactive_color'
                              "
                            >
                              <p class="mr-2">✔</p>
                              <p>{{ $t("login.lengthAtLeast6") }}</p>
                            </div>
                            <div
                              class="mt-2 flex"
                              :class="
                                contain_pd ? 'active_color' : 'notactive_color'
                              "
                            >
                              <p class="mr-2">✔</p>
                              <p>
                                {{ $t("login.includeNumbersLettersSymbols") }}
                              </p>
                            </div>
                          </div>
                          <div slot="reference">
                            <el-input
                              v-model="loginForm.new_password"
                              show-password
                              :placeholder="$t('login.newPassword')"
                              autocomplete="off"
                              @focus="show_check = true"
                              @blur="show_check = false"
                            />
                          </div>
                        </el-popover>
                      </el-form-item>
                      <el-button
                        :loading="loading"
                        type="primary"
                        style="width: 100%"
                        @click.native.prevent="handleChangePwd"
                        >{{ $t("login.modifyPassword") }}</el-button
                      >
                      <p class="mt-6" style="font-size: 14px">
                        {{ $t("login.haveAccount") }}
                        <span class="goReg" @click="tabChange(1)">{{
                          $t("login.nowLogin")
                        }}</span>
                      </p>
                    </div>
                  </el-tab-pane>
                  <el-tab-pane :label="$t('login.throughEmail')" name="second">
                    <div v-if="this.activeName == 'second'">
                      <el-form-item prop="email">
                        <el-input
                          v-model="loginForm.email"
                          :placeholder="$t('login.email')"
                        >
                          <template slot="prepend"
                            ><i class="el-icon-message"
                          /></template>
                        </el-input>
                      </el-form-item>
                      <div style="display: flex">
                        <el-form-item prop="verification_code" style="flex: 1">
                          <el-input
                            ref="verification_code"
                            v-model="loginForm.verification_code"
                            :placeholder="$t('login.verificationCode')"
                            name="verification_code"
                            type="text"
                            tabindex="1"
                            autocomplete="off"
                          />
                        </el-form-item>
                        <el-button
                          v-show="regShow"
                          size="small"
                          type="primary"
                          class="sendCode"
                          @click="sendEmailSms('email')"
                          >{{ $t("login.getVerificationCode") }}
                        </el-button>
                        <el-button
                          v-show="!regShow"
                          disabled
                          type="info"
                          size="small"
                          class="sendCode"
                          >{{ regCount }} s</el-button
                        >
                      </div>
                      <el-form-item prop="new_password">
                        <el-popover
                          v-model="show_check"
                          popper-class="tablepopover"
                          placement="top"
                          width="400"
                          trigger="manual"
                        >
                          <div>
                            <div
                              class="flex"
                              :class="
                                number_pd ? 'active_color' : 'notactive_color'
                              "
                            >
                              <p class="mr-2">✔</p>
                              <p>{{ $t("login.lengthAtLeast6") }}</p>
                            </div>
                            <div
                              class="mt-2 flex"
                              :class="
                                contain_pd ? 'active_color' : 'notactive_color'
                              "
                            >
                              <p class="mr-2">✔</p>
                              <p>
                                {{ $t("login.includeNumbersLettersSymbols") }}
                              </p>
                            </div>
                          </div>
                          <div slot="reference">
                            <el-input
                              v-model="loginForm.new_password"
                              show-password
                              :placeholder="$t('login.newPassword')"
                              autocomplete="off"
                              @focus="show_check = true"
                              @blur="show_check = false"
                            />
                          </div>
                        </el-popover>
                      </el-form-item>
                      <el-button
                        :loading="loading"
                        type="primary"
                        style="width: 100%"
                        @click.native.prevent="handleChangePwd"
                        >{{ $t("login.modifyPassword") }}</el-button
                      >
                      <p class="mt-6" style="font-size: 14px">
                        {{ $t("login.haveAccount")
                        }}<span class="goReg" @click="tabChange(1)">{{
                          $t("login.nowLogin")
                        }}</span>
                      </p>
                    </div>
                  </el-tab-pane>
                </el-tabs>
              </div>
            </el-form>
          </div>
        </div>
      </div>
      <div class="login_end">
        <div class="copyright" style="padding-left: 24px">
          Copyright © 2024 秒如科技 LNJOYING
        </div>
        <div class="contact" style="padding-right: 21px">
          {{ $t("other.contactUs") }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getsms,
  getEmail,
  registerSms,
  register,
  retPassword,
} from "@/api/user/mainApi";
import Cookies from "js-cookie";
import SIdentify from "@/components/indentify/index.vue";
const TIME_COUNT = 60; // 更改倒计时时间
let that;
export default {
  name: "Login",
  components: {
    SIdentify,
  },
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("message.passwordFormatError")));
      } else if (
        !/(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+\-=}{:";<>?,./]).{6,18}$/.test(
          value
        )
      ) {
        callback(new Error(this.$t("message.passwordFormatError")));
      } else {
        callback();
      }
    };
    const validateidentifyCode = (rule, value, callback) => {
      if (!this.codeiscorrect) {
        callback(
          new Error(this.$t("message.pleaseInputCorrectVerificationCode"))
        );
      } else {
        callback();
      }
    };
    const checkEmail = (rule, value, callback) => {
      const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
      if (!value) {
        return callback(new Error(this.$t("message.emailCannotBeEmpty")));
      }
      setTimeout(() => {
        if (mailReg.test(value)) {
          callback();
        } else {
          callback(new Error(this.$t("message.pleaseInputCorrectEmailFormat")));
        }
      }, 100);
    };
    var checkPhone = (rule, value, callback) => {
      const phoneReg = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
      if (!value) {
        return callback(new Error(this.$t("message.phoneNumberCannotBeEmpty")));
      }
      setTimeout(() => {
        // Number.isInteger是es6验证数字是否为整数的方法,但是我实际用的时候输入的数字总是识别成字符串
        // 所以我就在前面加了一个+实现隐式转换

        if (!Number.isInteger(+value)) {
          callback(new Error(this.$t("message.pleaseInputDigitalValue")));
        } else {
          if (phoneReg.test(value)) {
            callback();
          } else {
            callback(new Error(this.$t("message.phoneNumberFormatError")));
          }
        }
      }, 100);
    };
    return {
      activeName: "first",
      identifyCodeShow: false, // 验证码显示
      // 登录默认使用账号密码登录
      activelogin: "account",
      loginForm: {
        username: "",
        name: "",
        password: "",
        phone: "",
        verification_code: "",
        phone_num: "",
        new_password: "",
        email: "",
        identifyCode: "",
      },
      forgetRules: {
        email: [{ required: true, trigger: "blur", validator: checkEmail }],
        phone: [{ required: true, trigger: "blur", validator: checkPhone }],
        verification_code: [
          {
            required: true,
            trigger: "blur",
            message: this.$t("message.pleaseInputVerificationCode"),
          },
        ],
        new_password: [
          { required: true, trigger: "blur", validator: validatePassword },
        ],
      },
      // 登录的校验规则
      loginRules: {
        username: [
          {
            required: true,
            trigger: ["blur", "change"],
            message: this.$t("message.pleaseInputUsernamePhoneEmail"),
          },
        ],
        password: [
          {
            required: true,
            trigger: "blur",
            message: this.$t("message.pleaseInputPassword"),
          },
        ],
        identifyCode: [
          {
            required: true,
            trigger: "blur",
            message: this.$t("message.pleaseInputVerificationCode"),
          },
          { required: true, trigger: "blur", validator: validateidentifyCode },
        ],
        phone_num: [{ required: true, trigger: "blur", validator: checkPhone }],
        verification_code: [
          {
            required: true,
            trigger: "blur",
            message: this.$t("message.pleaseInputVerificationCode"),
          },
        ],
      },
      loading: false,
      passwordType: "password",
      redirect: undefined,
      tabKey: 1,
      regShow: true,
      regCount: 60,
      timer: null,
      identifyCode: "",
      identifyCodeShow: false, // 验证码显示
      // 用于判断输入的验证码是否正确
      codeiscorrect: false,
      identifyindex: 0, // 计算输错index
      identifyUser: [], // 缓存账号的数组
      imageList: [
        {
          url: require("../../assets/images/login/login-bg.jpg"),
        },
      ],
      show_check: false,
      number_pd: false,
      contain_pd: false,
    };
  },

  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true,
    },
    identifyCodeShow: function (newVal, oldVal) {
      if (!newVal) {
        this.loginForm.identifyCode = "";
      }
    },
    // 对账户输入三次错误的验证码处理
    "loginForm.identifyCode": function (newVal, oldVal) {
      if (newVal) {
        if (newVal.length == 4 && newVal == this.identifyCode) {
          this.codeiscorrect = true;
        }
        if (newVal.length == 4 && newVal != this.identifyCode) {
          this.loginForm.identifyCode = "";
          this.codeiscorrect = false;
          this.refreshCode();
        }
        if (newVal.length > 4) {
          this.loginForm.identifyCode = newVal.slice(0, 4);
        }
        if (newVal.length < 4) {
          this.codeiscorrect = false;
        }
      }
    },
    "loginForm.new_password": function (newVal, oldVal) {
      if (newVal.length >= 6 && newVal.length <= 18) {
        this.number_pd = true;
      } else {
        this.number_pd = false;
      }
      if (
        /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[~!@#$%^&*()_+\-={}:";<>?,./])(?!.*[\u4e00-\u9fa5])[^\u4e00-\u9fa5]+$/.test(
          newVal
        )
      ) {
        this.contain_pd = true;
      } else {
        this.contain_pd = false;
      }
    },
  },
  created() {
    that = this;
  },
  methods: {
    refreshCode() {
      this.identifyCode = "";
      this.loginForm.identifyCode = "";
      // 4位验证码可以根据自己需要来定义验证码位数
      this.makeCode(this.identifyCode, 4);
    },
    // 随机生成验证码字符串
    makeCode(data, len) {
      var random = new Array(
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z"
      ); // 随机数
      for (var i = 0; i < len; i++) {
        // 循环操作
        var index = Math.floor(Math.random() * 36); // 取得随机数的索引（0~35）
        this.identifyCode += random[index]; // 根据索引取得随机数加到code上
      }
    },
    handleClick() {
      this.resetForm();
    },
    async sendSmsCode() {
      if (!this.timer) {
        const phoneReg = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
        if (this.loginForm.phone && phoneReg.test(this.loginForm.phone)) {
          this.regCount = TIME_COUNT;
          this.regShow = false;
          await registerSms(this.loginForm.phone);
        } else {
          this.regCount = this.$t("login.getVerificationCode");
          clearInterval(this.timer); // 清除定时器
          this.timer = null;
          this.$notify({
            title: this.$t("message.pleaseInputCorrectPhoneNumber"),
            type: "info",
            duration: 2500,
          });
        }
        this.timer = setInterval(() => {
          if (this.regCount > 0 && this.regCount <= TIME_COUNT) {
            this.regCount--;
          } else {
            this.regShow = true;
            clearInterval(this.timer); // 清除定时器
            this.timer = null;
          }
        }, 1000);
      }
    },
    async sendEmailSms(name) {
      if (!this.timer) {
        const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
        if (this.loginForm[name] && mailReg.test(this.loginForm[name])) {
          this.regCount = TIME_COUNT;
          this.regShow = false;
          await getEmail(this.loginForm[name]);
        } else {
          this.regCount = this.$t("login.getVerificationCode");
          clearInterval(this.timer); // 清除定时器
          this.timer = null;
          this.$notify({
            title: this.$t("message.pleaseInputCorrectEmailAddress"),
            type: "info",
            duration: 2500,
          });
        }
        this.timer = setInterval(() => {
          if (this.regCount > 0 && this.regCount <= TIME_COUNT) {
            this.regCount--;
          } else {
            this.regShow = true;
            clearInterval(this.timer); // 清除定时器
            this.timer = null;
          }
        }, 1000);
      }
    },
    async sendSms(name) {
      if (!this.timer) {
        const phoneReg = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
        if (this.loginForm[name] && phoneReg.test(this.loginForm[name])) {
          this.regCount = TIME_COUNT;
          this.regShow = false;
          await getsms(this.loginForm[name]);
        } else {
          this.regCount = this.$t("login.getVerificationCode");
          clearInterval(this.timer); // 清除定时器
          this.timer = null;
          this.$notify({
            title: this.$t("message.pleaseInputCorrectPhoneNumber"),
            type: "info",
            duration: 2500,
          });
        }
        this.timer = setInterval(() => {
          if (this.regCount > 0 && this.regCount <= TIME_COUNT) {
            this.regCount--;
          } else {
            this.regShow = true;
            clearInterval(this.timer); // 清除定时器
            this.timer = null;
          }
        }, 1000);
      }
    },
    tabChange(e) {
      this.tabKey = e;
      this.resetForm();
    },

    // 忘记密码
    handleChangePwd() {
      for (var key in this.loginForm) {
        if (this.loginForm[key] === undefined || this.loginForm[key] === "") {
          delete this.loginForm[key];
        }
      }
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          for (var key in this.loginForm) {
            if (
              this.loginForm[key] === undefined ||
              this.loginForm[key] === ""
            ) {
              delete this.loginForm[key];
            }
          }
          const params = Object.assign({}, this.loginForm);
          retPassword(params)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.modifyPasswordSuccess"),
                type: "success",
                duration: 2500,
              });
              this.tabKey = 1;
              this.resetForm();
            })
            .catch(() => {
              this.loading = false;
            });
        } else {
          return false;
        }
      });
    },
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    },
    // 失去用户名的时候触发
    blurusername(e) {
      var data = Cookies.get("input_frequency")
        ? JSON.parse(Cookies.get("input_frequency"))
        : "";
      if (data) {
        this.identifyCodeShow = false;
        data.filter((res) => {
          if (res.username == e.target.value && res.identifyindex >= 2) {
            this.identifyCode = "";
            this.makeCode(this.identifyCode, 4);
            this.identifyCodeShow = true;
          }
        });
      }
    },
    goPage() {
      // console.log('login this.router')
      // console.log(this.router)
      if (this.redirect) {
        // console.log('loginRedirect')
        this.$router.push({ path: this.redirect });
      } else {
        // console.log('loginIndex')

        this.$router.push({ path: "/index" });
      }
    },
    handleLogin() {
      this.$refs.loginFormref.validate((valid) => {
        if (valid) {
          this.loading = true;
          this.$store
            .dispatch("user/login", this.loginForm)
            .then((res) => {
              this.goPage();

              this.loading = false;
              Cookies.remove("input_frequency");
              this.loginForm.identifyCode = "";
            })
            .catch(() => {
              this.loading = false;
              var input_frequency = [];
              this.loginForm.identifyCode
                ? (this.loginForm.identifyCode = "")
                : "";
              var data = Cookies.get("input_frequency")
                ? JSON.parse(Cookies.get("input_frequency"))
                : "";
              if (data) {
                data.filter((res) => {
                  if (res.username == this.loginForm.username) {
                    res.identifyindex += 1;
                    if (res.identifyindex >= 2) {
                      this.refreshCode();
                      this.identifyCodeShow = true;
                    }
                  } else {
                    data.push({
                      username: this.loginForm.username,
                      identifyindex: this.identifyindex,
                    });
                  }
                });
                Cookies.set("input_frequency", data);
              } else {
                this.identifyCodeShow ? (this.identifyCodeShow = false) : "";
                input_frequency.push({
                  username: this.loginForm.username,
                  identifyindex: this.identifyindex,
                });
                Cookies.set("input_frequency", input_frequency);
              }
            });
        } else {
          return false;
        }
      });
    },

    resetForm() {
      this.loading = false;
      this.$nextTick(() => {
        if (this.$refs["loginForm"] !== undefined) {
          this.$refs["loginForm"].resetFields();
        }
      });
      this.loginForm = {
        username: "",
        name: "",
        password: "",
        phone: "",
        verification_code: "",
        phone_num: "",
        new_password: "",
        email: "",
      };
      if (this.timer) {
        this.regShow = true;
        clearInterval(this.timer); // 清除定时器
        this.timer = null;
      }
    },
    goRegister() {
      if (this.redirect) {
        this.$router.push({
          path: "/register",
          query: { redirect: this.redirect },
        });
      } else {
        this.$router.push({ path: "/register" });
      }
    },
    changeLanguage(lang) {
      this.$i18n.locale = lang;
      localStorage.setItem("locale", lang);
      this.$router.go(0);
    },
  },
};
</script>

<style lang="scss" scoped>
.active_color {
  color: rgb(102, 177, 255);
  line-height: 20px;
}

.notactive_color {
  color: rgb(147, 158, 169);
  line-height: 20px;
}

.line {
  width: 1px;
  height: 14px;
  display: inline-block;
  background: #e4ebf1;
  margin-left: 16px;
  margin-right: 16px;
}

// logo位置
.logo {
  display: inline-block;
  position: relative;
  top: 60px;
  left: calc(50% - 120px);
  width: 160px;

  img {
    width: 100%;
  }
}

::v-deep .el-tabs__nav-wrap::after {
  height: 0px;
}

.sendCode {
  height: 38px;
  margin-left: 10px;
  border-radius: 5px;
}

.welcomecontent {
  width: calc(50% + 120px);
  position: relative;
  top: 30%;
  left: calc(50% - 120px);

  .title {
    color: #fff;
    font-size: 34px;
    margin-bottom: 10px;
  }

  .login-form {
    width: 100%;
    padding: 20px 80px 40px;
    overflow: hidden;
    background-color: #fff;
    border-radius: 0px 23px 23px 0px;
    height: 640px;
    min-height: 640px;
    width: 640px;
  }

  .title-tab {
    display: flex;
    align-items: center;
    margin-bottom: 30px;
    justify-content: space-around;

    div {
      color: #333;
      font-size: 14px;
      border-bottom: 2px solid #fff;
      padding: 12px 28px 5px 28px;
      cursor: pointer;

      &.active {
        color: #1890ff;
        border-bottom: 2px solid #1890ff;
      }
    }
  }

  .registerTitle {
    // color: #333;
    // font-size: 22px;
    padding: 5px 0;
    margin-bottom: 15px;
    text-align: center;
    margin-top: 25px;
    font-size: 20px;
    color: #749acb;
  }

  .sendCode {
    height: 38px;
    margin-left: 10px;
    border-radius: 5px;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    font-size: 34px;
    margin-bottom: 10px;
  }

  .smallTitle {
    color: #fff;
    font-size: 20px;
  }
}

.show-pwd {
  position: absolute;
  right: 10px;
  top: 3px;
  font-size: 16px;
  color: #889aa4;
  cursor: pointer;
  user-select: none;
}

.show-code {
  position: absolute;
  right: 3px;
  top: 12px;
}

.login-container {
  position: relative;
  min-height: 700px;
  width: 100%;
  height: 100%;
  display: flex;
  background: rgb(245, 247, 250);
  .languageChange {
    position: absolute;
    top: 10px;
    right: 10px;
    z-index: 9;
  }
  .login_bg {
    width: 30%;
    background-image: url("~@/assets/welcome.jpg");
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
  }

  .login_input {
    width: 70%;

    .logo_title {
      height: 50px;
      display: flex;
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;
      -webkit-box-pack: justify;
      -ms-flex-pack: justify;
      justify-content: space-between;
      padding: 20px 16px 0px 20px;
    }

    .login_center {
      height: calc(100% - 112px);
      min-height: 564px;
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;
      -webkit-box-pack: center;
      -ms-flex-pack: center;
      justify-content: center;

      .form-wrapper {
        position: relative;
        color: #324558;
        width: 440px;
        height: 564px;
        background: #fff;
        -webkit-box-shadow: 0px 5px 15px rgba(3, 5, 7, 0.08);
        box-shadow: 0px 5px 15px rgba(3, 5, 7, 0.08);
        border-radius: 6px;

        .wrapper {
          padding: 60px 32px 0px 22px;

          h2 {
            font-family: PingFang SC;
            font-style: normal;
            font-weight: 500;
            font-size: 24px;
            line-height: 30px;
            color: #09121a;
            margin-bottom: 32px;
          }

          .goReg {
            font-size: 14px;
            cursor: pointer;
            color: #1890ff;
          }
        }
      }
    }
  }
}

.login_end {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: end;
  -ms-flex-align: end;
  align-items: end;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
  height: 56px;
  padding-bottom: 35px;
  text-align: center;
  font-weight: normal;
  font-size: 12px;
  line-height: 14px;
  text-align: center;
  color: #4c5e70;

  .svg-container {
    padding: 6px 15px 6px 15px;
    color: #889aa4;
    vertical-align: middle;
    width: 40px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: #333;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .imgurl {
    border-radius: 25px 0px 0px 25px;
    height: 640px;
    width: 640px;
    min-height: 640px;
  }

  .flexBox {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

@media (max-width: 768px) {
  .login-container {
    width: 100vw;
    min-height: 100%;
    height: auto;
    overflow: hidden;
    display: block;
    .languageChange {
      .el-dropdown {
        color: #fff;
      }
    }
    .login_bg {
      width: 100%;
      position: relative;

      .logo {
        width: 120px;
        position: relative;
        top: 15px;
        left: 15px;
      }

      .welcomecontent {
        top: 0;
        left: 0;
        width: 100%;
        text-align: center;
        padding: 20px 0;

        .title {
          font-size: 24px;
        }

        .smallTitle {
          font-size: 16px;
        }
      }
    }

    .login_input {
      width: 100%;

      .logo_title {
        display: none;
      }

      .login_center {
        height: auto;
        min-height: auto;

        .form-wrapper {
          width: 100%;
          height: auto;

          .wrapper {
            padding: 50px 30px;

            .inputCode {
              width: calc(100% - 160px);
            }
          }
        }
      }

      .login_end {
        padding-top: 50px;
      }
    }
  }
}
</style>
