<template>
  <div class="helpDetailPage scrollStyle-0">

    <div class="helpDetailMain " v-if="documentDetail.createTimestamp">
      <h2 class="title pt-6">{{ documentDetail.title }}</h2>
      <span class="text-gray-500 block mt-6 mb-10 font-bold text-center text-sm">{{
      formatDate(documentDetail.createTimestamp) }}</span>
      <div v-html="documentDetail.content" class=" textData editor-content-view">
      </div>
    </div>
  </div>
</template>
<script>
import '@wangeditor/editor/dist/css/style.css' // 引入 css

export default {
  name: "helpCenter",
  components: {
  },
  props: {
    documentDetail: {
      type: Object,
      default: {}
    }
  },
  data () {
    return {

    };
  },
  methods: {
    //时间戳转换时间
    formatDate (date) {
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

  },
}
</script>

<style lang="stylus" scpoed>
.helpDetailPage{
 height 100%;
 overflow:auto;

 .helpDetailMain{
  height: 100%;
  max-width:1000px;
   margin:0 auto;
   padding:0 20px
  .title{
    text-align: center;
    font-size: 32px;
    color: #333;
    margin: 0;
  }

    .textData{
        font-size: 14px;
        font-weight: 400;
        line-height: 1.5;
        color: #333;
        padding-bottom:60px;


        h1{
          font-size:32px;
          font-weight: 700;
        }
        h2{
          font-size:21px;
          font-weight: 700;
        }
        h3{
          font-size:16px;
          font-weight: 700;
        }
        h4{
          font-size:14px;
          font-weight: 700;
        }
        h5{
          font-size:12px;
          font-weight: 700;
        }
        blockquote, dl, dd, h1, h2, h3, h4, h5, h6, hr, figure, p, pre{
          margin:revert;
        }
      ol, ul{
        list-style: revert;
        padding:revert;
        margin:revert;

      }
      a{
        color: -webkit-link;
        cursor: pointer;
        text-decoration: underline;
      }

      li {
        white-space: pre-wrap; /* 保留空格 */
      }
      p{
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
       td,
       th {
        border: 1px solid #ccc;
        min-width: 50px;
        height: 20px;
      }
       th {
        background-color: #f1f1f1;
      }

       ul,
       ol {
        padding-left: 20px;
      }

       input[type="checkbox"] {
        margin-right: 5px;
      }
 }

 }
}


</style>
