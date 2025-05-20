<template>
  <div id="app">
    <router-view :key="$route.fullPath + Math.random()" />
  </div>
</template>

<style>
#app {
  background-color: #d0ccc6;
}
html,
body {
  margin: 0;
  padding: 0;
  background-color: #d0ccc6;
}

.el-card,
.el-card__body,
.el-dialog,
.help-center-container,
.el-collapse,
.el-collapse-item,
.el-collapse-item__header,
.el-collapse-item__content,
.el-table {
  background-color: #f3f0e9 !important;
}

.el-table .el-table__header-wrapper {
  background-color: #f3f0e9;
}

.el-table .el-table__header-wrapper .el-table__header {
  background-color: #f3f0e9;
}
thead,
tr,
th,
td,
.cell,
/* .el-textarea,
.el-textarea__inner, */
.el-form {
  background-color: #f3f0e9 !important;
}
</style>
