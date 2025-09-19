<template>
  <div
    v-if="
      !item.hidden &&
      item.meta &&
      item.meta.group &&
      $store.state.settings.routerGroup == item.meta.group
    "
  >
    <template v-if="hasOneShowingChild(item.children, item)">
      <div>
        <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
          <el-menu-item
            :index="resolvePath(onlyOneChild.path)"
            :class="{ 'submenu-title-noDropdown': !isNest }"
          >
            <item
              :icon="onlyOneChild.meta.icon || (item.meta && item.meta.icon)"
              :title="$t('router.' + onlyOneChild.meta.languageTitle)"
            />
          </el-menu-item>
        </app-link>
      </div>
    </template>

    <div v-else-if="item.meta.isExpand">
      <el-submenu
        ref="subMenu"
        :index="resolvePath(item.path)"
        popper-append-to-body
      >
        <template slot="title">
          <item
            v-if="item.meta"
            :icon="item.meta && item.meta.icon"
            :title="$t('router.' + item.meta.languageTitle)"
          />
        </template>

        <sidebar-item
          v-for="child in item.children"
          :key="child.path"
          :is-nest="true"
          :item="child"
          :base-path="resolvePath(child.path)"
          class="nest-menu"
        />
      </el-submenu>
    </div>
    <div
      v-else
      ref="subMenu"
      :index="resolvePath(item.path)"
      popper-append-to-body
    >
      <div
        v-if="
          item.name == 'relational' ||
          item.name == 'infrastructure' ||
          item.name == 'containerApplicationService' ||
          item.name == 'cloudy' ||
          item.name == 'nonrelational' ||
          item.name == 'statistic' ||
          item.name == 'MonitoringComponents' ||
          item.name == 'servicegovernance'
        "
        class="nest-menu-title"
      >
        <div :class="{ border: item.name == 'cloudy' }" />
        <span
          style="
            font-size: 14px;
            margin: 10px 0px 0px 21px;
            display: inline-block;
          "
          >{{ $t("router." + item.meta.languageTitle) }}</span
        >
      </div>
      <div v-else class="titles">
        <span>
          {{ $t("router." + item.meta.languageTitle) }}
        </span>
      </div>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
      />
    </div>
  </div>
</template>

<script>
import path from "path";
import { isExternal } from "@/utils/validate";
import Item from "./Item";
import AppLink from "./Link";
import FixiOSBug from "./FixiOSBug";
import { mapGetters } from "vuex";

export default {
  name: "SidebarItem",
  components: { Item, AppLink },
  mixins: [FixiOSBug],
  props: {
    // route object
    item: {
      type: Object,
      required: true,
    },
    isNest: {
      type: Boolean,
      default: false,
    },
    basePath: {
      type: String,
      default: "",
    },
  },
  data() {
    this.onlyOneChild = null;
    return {};
  },

  computed: {
    ...mapGetters(["kind"]),
  },
  created() {},
  mounted() {},
  methods: {
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter((item) => {
        if (item.hidden) {
          return false;
        } else {
          this.onlyOneChild = item;
          return true;
        }
      });

      if (showingChildren.length === 0) {
        this.onlyOneChild = { ...parent, path: "", noShowingChildren: true };
        return true;
      }

      return false;
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath;
      }
      if (isExternal(this.basePath)) {
        return this.basePath;
      }
      return path.resolve(this.basePath, routePath);
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-menu {
  border-right-width: 0;
}

.menu-title {
  font-size: 16px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
  background-color: #fff;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  width: 200px;
  margin: 10px 0px 0px 21px;
}

.nest-menu-title {
  font-size: 16px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
  background-color: #fff;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  width: 200px;
  display: inline-block;
}

.title {
  font-size: 18px;
  color: rgba(0, 0, 0, 0.85);
  margin: 10px 0px 17px 0px;
  font-weight: 600;
  background-color: #fff;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  // width: 200px;
}

.titles {
  font-size: 17px;
  color: rgba(0, 0, 0, 0.85);
  margin: 10px 0px 15px 21px;
  background-color: #fff;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  font-weight: 600;
}

.border {
  padding-bottom: 10px;
  border-bottom: 0px dashed #cecece !important;
  border-top: 1px dashed #cecece !important;
  border-left: 0px dashed #cecece !important;
  border-right: 0px dashed #cecece !important;
}
</style>
