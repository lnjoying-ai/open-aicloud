

<template>
  <div class="table_head">
    {{ tableTxt }}
  </div>
</template>

<script>
export default {
  props: {
    tableTxt: {
      typeof: String,
    },
  },
};
</script>

<style lang="scss" scoped>
.table_head {
  min-width: 1100px;
  height: 68px;
  background: #ffffff;
  font-size: 17px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: #333333;
  padding: 20px;
  box-sizing: border-box;
  text-align: left;
}
</style>