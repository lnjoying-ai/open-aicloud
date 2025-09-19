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
      <div class="logo_title">
        <div />
        <div />
      </div>
      <div class="login_center">
        <div class="form-wrapper">
          <div v-if="$route.query.code != 1183" class="wrapper">
            <h2 v-if="!isinvite">{{ $t("login.register") }}</h2>
            <h2 v-if="isinvite">{{ $t("login.inviteRegister") }}</h2>
            <el-form
              ref="loginForm"
              :model="loginForm"
              :rules="loginRules"
              class="login-form"
              auto-complete="on"
              label-position="left"
            >
              <el-form-item prop="name">
                <el-input
                  v-model="loginForm.name"
                  :placeholder="$t('login.username')"
                />
              </el-form-item>
              <el-form-item prop="email">
                <el-input
                  v-model="loginForm.email"
                  :placeholder="$t('login.email')"
                />
              </el-form-item>
              <el-form-item prop="password">
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
                      :class="number_pd ? 'active_color' : 'notactive_color'"
                    >
                      <p class="mr-2">✔</p>
                      <p>{{ $t("login.lengthAtLeast6") }}</p>
                    </div>
                    <div
                      class="mt-2 flex"
                      :class="contain_pd ? 'active_color' : 'notactive_color'"
                    >
                      <p class="mr-2">✔</p>
                      <p>
                        {{ $t("login.includeNumbersLettersSymbols") }}
                      </p>
                    </div>
                  </div>
                  <div slot="reference">
                    <el-input
                      v-model="loginForm.password"
                      show-password
                      :placeholder="$t('login.newPassword')"
                      autocomplete="off"
                      @focus="show_check = true"
                      @blur="show_check = false"
                    />
                  </div>
                </el-popover>
              </el-form-item>
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
                    auto-complete="on"
                  />
                </el-form-item>
                <el-button
                  v-show="regShow"
                  size="small"
                  type="primary"
                  class="sendCode"
                  @click="sendSmsCode"
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
              <el-button
                :loading="loading"
                type="primary"
                style="width: 100%"
                @click.native.prevent="handleRegister"
                >{{ $t("login.register") }}</el-button
              >
              <el-form-item
                prop="checkTerms"
                style="flex: 1"
                class="checkTermsStyle mt-3 mb-0"
              >
                <el-checkbox v-model="loginForm.checkTerms" class="w-full">
                  <span class="text-gray-500">
                    {{ $t("login.iAgreeToThe") }}
                    <router-link
                      to="/Terms-of-Service?info=service-agreement"
                      class="goReg"
                      target="_blank"
                      >{{ $t("login.serviceAgreement") }}</router-link
                    >
                    {{ $t("login.and") }}
                    <router-link
                      to="/Terms-of-Service?info=privacy_policy"
                      class="goReg"
                      target="_blank"
                      >{{ $t("login.privacyPolicy") }}</router-link
                    >
                  </span>
                </el-checkbox>
              </el-form-item>

              <p class="mt-4" style="font-size: 14px">
                {{ $t("login.haveAccount")
                }}<span class="goReg" @click="tologin">{{
                  $t("login.nowLogin")
                }}</span>
              </p>
            </el-form>
          </div>
          <div v-else class="invalid">
            <p>{{ $t("login.inviteLinkInvalid") }}</p>
            <div>
              <div>
                <el-link type="primary" @click="returnwelcome">{{
                  $t("domain.back")
                }}</el-link>
              </div>
            </div>
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
const TIME_COUNT = 60; // 更改倒计时时间
let that;
export default {
  name: "Login",
  components: {},
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
    const checkEmail = (rule, value, callback) => {
      const mailReg = /^[\.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
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
    var checkTerms = (rule, value, callback) => {
      if (!value) {
        return callback(
          new Error(this.$t("message.pleaseAgreeToTheServiceAgreement"))
        );
      } else {
        callback();
      }
    };

    return {
      loginForm: {
        name: "",
        password: "",
        phone: "",
        verification_code: "",
        email: "",
        checkTerms: false,
      },
      // 判断用户是否是通过邀请链接进入
      isinvite: false,
      loginRules: {
        name: [
          {
            required: true,
            trigger: "blur",
            message: this.$t("message.pleaseInputUsername"),
          },
        ], // validator: checkEmail
        email: [{ required: true, trigger: "blur", validator: checkEmail }],
        password: [
          { required: true, trigger: "blur", validator: validatePassword },
        ],
        phone: [{ required: true, trigger: "blur", validator: checkPhone }],
        verification_code: [
          {
            required: true,
            trigger: "blur",
            message: this.$t("message.pleaseInputVerificationCode"),
          },
        ],
        checkTerms: [
          {
            required: true,
            trigger: "change",
            validator: checkTerms,
          },
        ],
      },
      loading: false,
      passwordType: "password",
      redirect: undefined,
      regShow: true,
      regCount: 60,
      timer: null,
      identifyindex: 0, // 计算输错index
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
    "loginForm.password": function (newVal, oldVal) {
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
  mounted() {
    if (this.$route.query.invite) {
      this.isinvite = true;
    } else {
      this.isinvite = false;
    }
  },
  methods: {
    // 返回到门户页面
    returnwelcome() {
      this.$router.push("/");
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
    // 注册
    handleRegister() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;

          var data = {
            name: this.loginForm.name,
            password: this.loginForm.password,
            verification_code: this.loginForm.verification_code,
            contact_info: {
              email: this.loginForm.email,
              phone: this.loginForm.phone,
            },
          };
          // for (var key in data) {
          //   if (
          //     this.loginForm[key] === undefined ||
          //     this.loginForm[key] === ""
          //   ) {
          //     delete this.loginForm[key];
          //   }
          // }
          // const params = Object.assign({}, data);

          register(data)
            .then((res) => {
              this.loading = false;
              if (res) {
                this.$notify({
                  title: this.$t("message.registerSuccess"),
                  type: "success",
                  duration: 2500,
                });
                this.resetForm();
                this.tologin();
              }
            })
            .catch(() => {
              this.loading = false;
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
        name: "",
        password: "",
        phone: "",
        verification_code: "",
        email: "",
      };
      if (this.timer) {
        this.regShow = true;
        clearInterval(this.timer); // 清除定时器
        this.timer = null;
      }
    },
    tologin() {
      // redirect
      if (this.redirect) {
        this.$router.push({
          path: "/login",
          query: { redirect: this.redirect },
        });
      } else {
        this.$router.push({ path: "/login" });
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

.login-form {
  .checkTermsStyle {
    ::v-deep .el-form-item__content {
      line-height: 20px;
    }
    ::v-deep .el-checkbox__label {
      width: 100%;
      display: inline-grid !important;
      white-space: pre-line !important;
      word-wrap: break-word !important;
      overflow: hidden !important;
    }
  }
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

  .el-input input {
    height: 38px;
  }

  .el-form-item {
    border: 1px solid rgba(0, 0, 0, 0.05);
    background: rgba(0, 0, 0, 0.05);
    border-radius: 5px;
    color: #454545;
  }
}

.invalid {
  text-align: center;
  height: 564px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  p {
    font-size: 20px;
    margin-bottom: 10px;
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
        position: none;
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
