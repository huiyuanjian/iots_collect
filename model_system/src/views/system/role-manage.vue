<template>
    <div class="warp-main">
        <Card>
            <h4 class="title-h4"  slot="title">
                <Icon type="ios-paper-outline"></Icon> 角色管理
            </h4>
            <Row style="margin-bottom: 10px;">
                <Col span="20">
                    <Input placeholder="搜索角色名称" style="width: 200px" />
                    <!-- <Input  icon="search" placeholder="IP" style="width: 200px" /> -->
                    <Select  style="width:130px;text-align:left" placeholder="请选择状态..." v-model="search_status">
                        <Option v-for="item in search_status_list" :value="item.value" :key="item.value">{{ item.label }}</Option>
                    </Select>
                    <Button type="primary" size="large">查询</Button>
                    <Button type="default" size="large">清空</Button>
                </Col>
                <Col  span="4" style="text-align: right">
                    <Button type="primary" size="large" @click="$router.push({name: 'role-add'})"><Icon type="ios-add"></Icon> 新增</Button>
                </Col>
            </Row>
            <Row :gutter="10">
                <Col span="24">
                    <Table border :columns="InternetTit" :data="InternetData"></Table>
                    <Page class="page-position" :current="pageIndex" :total="totalItemCount" show-total :page-size='pageSize'></Page>
                </Col>
            </Row>
        </Card>
    </div>
</template>

<script>

import {roleData, roleTit} from '../datas/tableData.js';
export default {
    name: '',
    data () {
        return {
            InternetTit: [{
                title: '序号',
                type: 'index',
                width: 80,
                align: 'center'
                },
                {
                    title: '角色名称',
                    key: 'name'
                },
                {
                    title: '备注信息',
                    key: 'info'
                },
                {
                    title: '创建时间',
                    key: 'time'
                },
                {
                    title: '状态',
                    key: 'status'
                },
                {
                    title: '操作',
                    key: 'Operation',
                    width: 150,
                    align: 'center',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                type: 'primary',
                                size: 'small'
                                },
                                style: {
                                marginRight: '5px'
                                },
                                on: {
                                click: () => {
                                    this.$router.push({
                                        name: 'role-upd',
                                        query: {
                                            type: 'upd'
                                        }
                                    })
                                }
                            }
                            }, '修改'),
                            h('Button', {
                                props: {
                                type: 'error',
                                size: 'small'
                                },
                                on: {
                                    click: () => {
                                        this.remove(params.index)
                                    }
                                }
                                }, '删除 ')
                            ]);
                        }
                    }
                ],
            InternetData: roleData,
            pageIndex: 1,
            totalItemCount: 0,
            pageSize: 2,
            search_status: '',
            search_status_list: [
                // { value: 0, label:'请选择'},
              { value: 1, label:'启用'},
                {value: 2, label:'禁用'}
            ],
        };
    },
    mounted () {
        this.totalItemCount =   this.InternetData.length
    }
};
</script>
