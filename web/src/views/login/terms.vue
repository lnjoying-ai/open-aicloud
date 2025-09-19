<template>
  <div class="all_content overflow-auto h-full scrollStyle">
    <div class="termsPage_index">
      <div class="termsBanner w-full inline-block">
        <div class="mx-auto termsContent">
          <h2 class="font-bold text-5xl">服务条款</h2>
          <p class="text-2xl text-gray-500 mt-8">91GPU服务条款</p>
        </div>
      </div>
    </div>
    <div class="clause_content">
      <div class="clause_catalogue">
        <h5 class="mb-6">条款</h5>
        <ul>
          <li v-for="(item) in Object.keys(documentDetail)" :key="item" class="cursor-pointer"
            :class="active == item ? 'active_border' : ''" @click="select_catalogue(item)">
            <div class="catalogue_sideedge">
              <p :class="active == item ? 'catalogue_active' : 'catalogue'">
                {{ documentDetail[item].name }}
              </p>
              <i style="color: rgb(224, 224, 224)" v-if="active == item" class="el-icon-arrow-right mr-3"></i>
            </div>
          </li>

        </ul>
      </div>
      <div class="clause_main">
        <div class="termsDetailMain" v-if="documentDetail[active]">
          <h2 class="title">{{ documentDetail[active].name }}</h2>
          <span class="text-gray-500 block mt-6 mb-10 font-bold text-center text-sm">生效日期:{{ documentDetail[active].time
            }}</span>
          <div v-html="documentDetail[active].content" class="textData editor-content-view"></div>
        </div>
      </div>
    </div>
    <el-backtop target=".all_content">
      <div>
        <i class="el-icon-download transform rotate-180"></i>
      </div>
    </el-backtop>

  </div>
</template>

<script>
import { getTermsJson } from "@/api/mainApi";
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
export default {
  data () {
    return {
      documentDetail: [],
      active: "",
    };
  },
  mounted () {
    // 当前 url query
    if (this.$route.query.info) {
      this.active = this.$route.query.info;
    }
    this.init();
  },
  methods: {
    async init () {
      const list = await getTermsJson();
      this.documentDetail = list.data;
      this.active ? '' : this.active = Object.keys(this.documentDetail)[0];
    },
    select_catalogue (id) {
      this.active = id;
    },
  },
};
</script>
<style lang="stylus" scpoed>
.catalogue_sideedge {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.catalogue_active {
  display: block;
  padding-top: 0.575em;
  padding-left: 1.25rem;
  padding-bottom: 0.575em;
  color: rgb(59, 130, 246);
}

.active_border {
  border-color: rgb(224, 224, 224)!important;
}

.catalogue {
  color: #616076;
  display: block;
  padding-top: 0.575em;
  padding-left: 1.25rem;
  padding-bottom: 0.575em;
}


.all_content {
  min-width: 1200px;
  .clause_content {
    display: flex;
    width: 1200px;
    margin: 3.125rem auto;

    .clause_catalogue {
      width: 25%;
      padding-left: 1.25rem;
      padding-right: 1.25rem;

      h5 {
        color: #a1a6ab;
        font-size: 14px;
        line-height: 1;
        text-align: left;
      }

      ul {
        line-height: 1.25;
        color: #68747f;

        li {
           border: 1px solid #fff;
          &:hover {
            .catalogue_sideedge{
              .catalogue{
                color: rgb(59, 130, 246);
              }
            }

          }
        }
      }
    }

    .clause_main {
      width: 75%;
      padding-left: 1.25rem;
      padding-right: 1.25rem;
    }
  }

  .termsPage_index {
    background-color: #f4f7fb;

    .termsBanner {
      height: 250px;
      padding-top: 50px;
      .termsContent{
        width: 1200px;
      }
    }
  }
}

.termsDetailMain {
  height: 100%;
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;

  .title {
    text-align: center;
    font-size: 2rem;
    color: #333;
     margin: 0;
  }



  .textData {
    font-size: 14px;
    font-weight: 400;
    line-height: 1.5;
    color: #333;
    padding-bottom: 60px;

    h1 {
      font-size: 32px;
      font-weight: 700;
    }

    h2 {
      font-size: 21px;
      font-weight: 700;
    }

    h3 {
      font-size: 16px;
      font-weight: 700;
    }

    h4 {
      font-size: 14px;
      font-weight: 700;
    }

    h5 {
      font-size: 12px;
      font-weight: 700;
    }

    blockquote, dl, dd, h1, h2, h3, h4, h5, h6, hr, figure, p, pre {
      margin: revert;
    }

    ol, ul {
      list-style: revert;
      padding: revert;
      margin: revert;
    }

    a {
      color: -webkit-link;
      cursor: pointer;
      text-decoration: underline;
    }

    li {
      white-space: pre-wrap; /* 保留空格 */
    }

    p {
      white-space: pre-wrap; /* 保留空格 */
    }

    blockquote {
      border-left: 8px solid #d0e5f2;
      padding: 10px 10px;
      margin: 10px 0;
      background-color: #f1f1f1;
    }

    code {
      font-family: monospace;
      background-color: #eee;
      padding: 3px;
      border-radius: 3px;
    }

    pre>code {
      display: block;
      padding: 10px;
    }

    table {
      border-collapse: collapse;
    }

    td, th {
      border: 1px solid #ccc;
      min-width: 50px;
      height: 20px;
    }

    th {
      background-color: #f1f1f1;
    }

    ul, ol {
      padding-left: 20px;
    }

    input[type='checkbox'] {
      margin-right: 5px;
    }
  }
}
@media (max-width: 768px) {
  .all_content{
    width: 100%;
    min-width: 100%;
    .termsPage_index{
      text-align: center;
      .termsBanner{
        height: 170px;
        .termsContent{
          width: 100%;
          h2{
            font-size: 2rem!important;
          }
          p{
            font-size: 1rem!important;
            margin-top:1.25rem!important;
          }
        }
      }
    }
    .clause_content{
      display: inline-block;
      margin-top: 1.5rem;
      width: 100%;
      .clause_catalogue{
        padding: 0 1.25rem;
        h5{
          margin-bottom: 0.5rem!important;
        }
        width: 100%;
        ul{
          display: flex;
          overflow-x: auto;
          overflow-y: hidden;
          width: auto;
          white-space: nowrap;
          margin-bottom: 2rem;
          li{
            height: 30px;
            margin-right: 5px;
            .catalogue_sideedge{
              .catalogue_active{
                padding: 5px 10px;
              }
              .catalogue{
                padding: 5px 10px;
              }
              i{
                display: none;
              }
            }
          }
        }
      }
      .clause_main{
        width: 100%;
        padding: 0 1.25rem;
        .termsDetailMain{
          padding: 0;
        }
      }
    }
  }

}
</style>
