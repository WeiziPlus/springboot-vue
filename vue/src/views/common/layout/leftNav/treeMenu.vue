<template>
    <div class="treeMenu">
        <div v-for="item in data.children" :key="item.name">
            <template v-if="null == item.children || 0 >= item.children.length">
                <el-menu-item :index="`/${parentPath}/${item.path}`">
                    <el-tooltip :content="item.title" placement="right">
                        <div>
                            <i :class="item['icon'] || 'el-icon-s-help'"></i>
                            <span>{{item.title}}</span>
                        </div>
                    </el-tooltip>
                </el-menu-item>
            </template>
            <template v-else>
                <el-submenu :index="`/${parentPath}/${item.path}`">
                    <template slot="title">
                        <el-tooltip :content="item.title" placement="right">
                            <div>
                                <i :class="item['icon'] || 'el-icon-s-help'"></i>
                                <span>{{item.title}}</span>
                            </div>
                        </el-tooltip>
                    </template>
                    <tree-menu :data="item" :parentPath="`${parentPath}/${item.path}`"></tree-menu>
                </el-submenu>
            </template>
        </div>
    </div>
</template>

<script>
    export default {
        name: "treeMenu",
        props: {
            data: {
                type: Object,
                default: () => {
                }
            },
            parentPath: {
                type: String,
                default: () => {

                }
            }
        }
    }
</script>