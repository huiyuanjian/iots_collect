<template>
    <div class="warp-main">
        <Card>
            <h4 class="title-h4"  slot="title">
                <Icon type="ios-paper-outline"></Icon> 日志管理
            </h4>
            <Row style="margin-bottom: 10px;">
                <Col span="20">
                    <Input  placeholder="搜索日志名称" style="width: 200px" />
                    <Input  placeholder="IP" style="width: 200px" />
                    <Select  style="width:120px;text-align:left" placeholder="请选择日志级别" v-model="search_status">
                        <Option v-for="item in search_status_list" :value="item.value" :key="item.value">{{ item.label }}</Option>
                    </Select>
                    <Button type="primary" size="large">查询</Button>
                    <Button type="default" size="large">清空</Button>
                </Col>
                <!-- <Col  span="4" style="text-align: right">
                    <Button type="primary" size="large"><Icon type="ios-add"></Icon> 新增</Button>
                </Col> -->
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

import {logData, logTit} from '../datas/tableData.js';
export default {
    name: '',
    data () {
        return {
            InternetTit: logTit,
            InternetData: logData,
            pageIndex: 1,
            totalItemCount: 0,
            pageSize: 2,
            search_status: '',
            search_status_list: [
                { value: 0, label:'ERROR'},
                { value: 1, label:'DEBUG'},
                {value: 2, label:'INFO'},
                {value: 3, label:'WARNING'}
            ],
        };
    },
    mounted () {
        this.totalItemCount =   this.InternetData.length
    }
};
</script>
