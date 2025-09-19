<template>
  <div class="helpAddPage">
    <div class="helpAddMain">

      <el-form :model="addDocumentForm" size="mini" label-width="40px" style="width: 100%" ref="addDocumentFormRef"
        :rules="rules" :hide-required-asterisk="true">
        <el-form-item label="标题" prop="title">
          <el-input v-model="addDocumentForm.title" placeholder="请输入标题" clearable style="width:232px" />
        </el-form-item>


        <el-form-item label="内容" prop="content">
          <div style="border: 1px solid #ccc;" class="textData">
            <Toolbar style="border-bottom: 1px solid #ccc" :editor="editor" :defaultConfig="toolbarConfig" :mode="mode"
              ref="ToolbarRef" />
            <Editor style="height: 500px; overflow-y: hidden;" v-model="html" :defaultConfig="editorConfig" :mode="mode"
              @onChange="onChange" @onCreated="onCreated" />
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('addDocumentFormRef')">保存</el-button>
          <el-button @click="cancelForm('addDocumentFormRef')">取消</el-button>
        </el-form-item>
      </el-form>

    </div>
  </div>
</template>
<script>
import '@wangeditor/editor/dist/css/style.css' // 引入 css

import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { documentAdd, documentUpdate } from "@/api/transfer/mainApi";
import { DomEditor, IEditorConfig } from "@wangeditor/editor"
export default {
  name: "helpAddPage",
  components: {
    Editor,
    Toolbar
  },
  props: {
    parentId: {
      type: String,
      default: ''
    },
    documentDetail: {
      type: Object,
      default: () => {
        return {}
      }
    },
    isDocumentAdd: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      addDocumentForm: {
        parentId: this.parentId,
        title: "",
        content: "",
        disabled: 1,//0启用 1停用
      },
      rules: {
        title: [
          { required: true, message: "请输入文档名称", trigger: "blur" },
        ],
        content: [
          { required: true, message: "请输入文档内容", trigger: "change" },
        ],

      },
      html: '',
      editor: null,
      toolbarConfig: {

        toolbarKeys: [
          "clearStyle",
          "headerSelect",

          "fontSize",
          "lineHeight",
          {
            "key": "group-justify",
            "title": "对齐",
            "iconSvg": "<svg viewBox=\"0 0 1024 1024\"><path d=\"M768 793.6v102.4H51.2v-102.4h716.8z m204.8-230.4v102.4H51.2v-102.4h921.6z m-204.8-230.4v102.4H51.2v-102.4h716.8zM972.8 102.4v102.4H51.2V102.4h921.6z\"></path></svg>",
            "menuKeys": [
              "justifyLeft",
              "justifyRight",
              "justifyCenter",
              "justifyJustify"
            ]
          },
          {
            "key": "group-indent",
            "title": "缩进",
            "iconSvg": "<svg viewBox=\"0 0 1024 1024\"><path d=\"M0 64h1024v128H0z m384 192h640v128H384z m0 192h640v128H384z m0 192h640v128H384zM0 832h1024v128H0z m0-128V320l256 192z\"></path></svg>",
            "menuKeys": [
              "indent",
              "delIndent"
            ]
          },
          "|",
          "bold",
          "underline",
          "italic",
          "through",

          "sup",
          "sub",


          "color",
          "bgColor",
          "|",


          "bulletedList",
          "numberedList",
          "todo",


          "blockquote",
          "insertLink",
          "uploadImage",


          "insertTable",
          "code",
          "codeBlock",
          "divider",
          "|",
          "undo",
          "redo",

          "fullScreen"
        ]
      },
      editorConfig: {
        placeholder: '请输入内容',
        MENU_CONF: {
          uploadImage: {
            base64LimitSize: 1024 * 1024 * 2,
          }
        }
      },
      mode: 'default'

    }
  },
  beforeDestroy () {
    const editor = this.editor
    if (editor == null) return
    editor.destroy() // 组件销毁时，及时销毁编辑器
  },
  mounted () {



  },
  methods: {
    onCreated (editor) {//编辑器创建完成时触发
      if (!this.isDocumentAdd) {
        this.addDocumentForm.title = this.documentDetail.title;
        this.addDocumentForm.content = this.documentDetail.content;
        this.html = this.documentDetail.content;
      }
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错


    },
    onChange (editor) {//编辑器内容改变时触发
      this.addDocumentForm.content = editor.getHtml();
      this.$refs['addDocumentFormRef'].validateField('content');
      const toolbar = DomEditor.getToolbar(editor);
      const curToolbarConfig = toolbar.getConfig();
      console.log(curToolbarConfig);
      console.log(curToolbarConfig.toolbarKeys);
      IEditorConfig
    },
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.isDocumentAdd) {//新增
            documentAdd(this.addDocumentForm).then(res => {
              if (res.status === 0) {
                this.$message.success("新增成功");
                this.$emit("addDocumentSuccess", res.obj);
              }
            });
          } else {//编辑
            var data = {
              id: this.documentDetail.id,
              parentId: this.parentId,
              title: this.addDocumentForm.title,
              content: this.addDocumentForm.content,
              disabled: this.addDocumentForm.disabled
            }
            documentUpdate(data).then(res => {
              if (res.status === 0) {
                this.$message.success("编辑成功");
                this.$emit("addDocumentSuccess", this.documentDetail.id);
              }
            });

          }

        } else {
          return false;
        }
      });
    },
    cancelForm (formName) {
      // this.addDocumentForm = {
      //   parentId: this.parentId,
      //   title: "",
      //   content: "",
      //   disabled: 0
      // }
      // this.$refs[formName].resetFields();
      // this.editor.clear()
      this.$refs[formName].resetFields();
      this.$emit("addDocumentDialogCancel");
    },

  }
}


</script>

<style lang="stylus" scpoed>
.helpAddPage{
 height 100%;
 overflow:auto;
 .helpAddMain{
  height: 100%;
  max-width:1000px;
   margin:0 auto;
   padding:40px 20px
   .textData{
        font-size: 14px;
        font-weight: 400;
        line-height: 1.5;
        color: #333;
        z-index:99;

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
      p,
      li {
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
      .w-e-select-list{
        ul{
          padding-left: 0;
          li{
            list-style: none;
          }
        }
      }
    }

 }
}


</style>
