<template>
    <div id="index">
        <template class="wei-table">
            <wei-table ref="table"
                       :tableDataRequest="tableDataRequest"
                       :tableColumns="tableColumns"
                       :tableOperates="tableOperates"
                       :tableSearch="tableSearch"></wei-table>
        </template>
        <template>
            <el-dialog title="操作详情"
                       :visible.sync="dialogDetail">
                <detail :formData="formData"></detail>
            </el-dialog>
        </template>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Complex.vue'),
            'detail': () => import('./Detail.vue')
        },
        data() {
            let that = this;
            return {
                tableDataRequest: {
                    url: that.$global.URL.system.userLog.getPageList,
                    data: {}
                },
                tableColumns: [
                    {label: '用户名', prop: 'username', showOverflowTooltip: true, fixed: 'left'},
                    {label: '请求路径', prop: 'url', showOverflowTooltip: true},
                    {
                        label: '类型', type: 'tag',
                        element({type}) {
                            let result = [
                                null,
                                {content: '查询'},
                                {content: '新增', type: 'success'},
                                {content: '修改', type: 'warning'},
                                {content: '删除', type: 'danger'},
                            ];
                            return result[type] || {
                                content: `未知类型,type:${type}`,
                                type: 'danger'
                            }
                        }
                    },
                    {label: '参数', prop: 'param', showOverflowTooltip: true},
                    {label: '操作', prop: 'description'},
                    {label: 'ip地址', prop: 'ipAddress'},
                    {label: '浏览器', prop: 'borderName'},
                    {label: '操作系统', prop: 'osName'},
                    {
                        label: '创建时间',
                        prop: 'createTime',
                        width: 120,
                        sortable: 'custom',
                        type: 'icon',
                        element(row) {
                            return {
                                leftIcon: 'el-icon-time',
                                content: row['createTime']
                            };
                        }
                    }
                ],
                //表格对应每一行按钮
                tableOperates: {
                    //按钮数组
                    buttons:
                        [
                            {
                                name: '查看', type: 'primary', show: true,
                                handleClick(row) {
                                    that.formData = row;
                                    that.dialogDetail = true;
                                }
                            }
                        ]
                },
                //表格顶部搜索
                tableSearch: [
                    {type: 'input', prop: 'username', placeholder: '用户名'},
                    {
                        type: 'select', prop: 'type', placeholder: '类型',
                        options: [
                            {label: '查询', value: 1},
                            {label: '新增', value: 2},
                            {label: '修改', value: 3},
                            {label: '删除', value: 4}
                        ]
                    },
                    {type: 'input', prop: 'description', placeholder: '操作'},
                    {type: 'input', prop: 'ipAddress', placeholder: 'ip地址'},
                    {type: 'input', prop: 'borderName', placeholder: '浏览器'},
                    {type: 'input', prop: 'osName', placeholder: '操作系统'},
                    {type: 'dateTimePicker', prop: 'startTime', placeholder: '起始时间'},
                    {type: 'dateTimePicker', prop: 'endTime', placeholder: '截止时间'}
                ],
                //表单数据
                formData: {},
                //详情弹窗
                dialogDetail: false,
            }
        }
    }
</script>