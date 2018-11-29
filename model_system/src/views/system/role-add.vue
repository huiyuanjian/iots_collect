<template>
    <div class="warp-main">
        <Card>
            <p slot="title">
                <Icon type="person"></Icon>
                {{$route.query.type === 'upd'? '修改角色' : '添加角色'}}
            </p>
            <div>
                <Row style="margin:20px 0 30px">
                    <Col span="22" offset="2">
                        <Steps :current="currentStep" :status="status">
                            <Step v-for="item in stepList" :title="item.title" :key="item.title"></Step>
                        </Steps>
                    </Col>
                </Row>
                <Row v-if="currentStep === 0">
                    <Col span="12" offset="6">
                        <Form 
                            ref="userForm"
                            :model="userForm" 
                            :label-width="100" 
                            label-position="right"
                            :rules="inforValidate"
                        >
                            <FormItem label="角色名称：" prop="name">
                                <div style="display:inline-block;width:300px;">
                                    <Input placeholder="请输入角色名称" v-model="userForm.name" ></Input>
                                </div>
                            </FormItem>
                            <FormItem label="备注信息：">
                                <div style="display:inline-block;width:300px;">
                                    <Input  type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="请输入备注信息"></Input>
                                    <!-- <Textarea placeholder="请输入备注信息 " v-model="userForm.name" ></Textarea> -->
                                </div>
                            </FormItem>
                            <FormItem label="角色状态：">
                                <div style="display:inline-block;width:300px;">
                                    <i-switch size="large" v-model="switch1"> 
                                        <span slot="open">启用</span>
                                        <span slot="close">禁用</span>
                                    </i-switch>
                                </div>
                            </FormItem>
                            <!-- <FormItem label="角色状态：">
                                <Button type="dashed" icon="plus-round" @click="editPasswordModal = true">选择菜单权限</Button>
                            </FormItem> -->
                            <div v-if="$route.query.type==='upd'">
                                <FormItem label="创建者：">
                                    <div style="display:inline-block;width:300px;">
                                        Admin
                                    </div>
                                </FormItem>
                                    <FormItem label="创建时间：">
                                    <div style="display:inline-block;width:300px;">
                                        2018-05-19 14:00:32
                                    </div>
                                </FormItem>
                                    <FormItem label="修改者：">
                                    <div style="display:inline-block;width:300px;">
                                        Admin
                                    </div>
                                </FormItem>
                                    <FormItem label="修改时间：">
                                    <div style="display:inline-block;width:300px;">
                                        2018-05-19 14:00:32
                                    </div>
                                </FormItem>
                            </div>
                             <FormItem class="btn-box">
                                <Button type="primary" @click="saveEdit('formValidate')" style="margin-left: 8px">下一步</Button>
                            </FormItem>
                        </Form>
                    </Col>
                </Row>
                <Row v-if="currentStep === 1">
                    <Col span="12" offset="6">
                        <Card>
                        <Tree :data="searchList" show-checkbox></Tree>
                        </Card>
                        <div class="btn-box">
                            <Button type="primary" @click="currentStep = currentStep - 1" style="margin-right: 8px">上一步</Button>
                            <Button type="primary" @click="currentStep = currentStep + 1" style="margin-left: 8px">下一步</Button>
                        </div>
                    </Col>
                </Row>
                <Row v-if="currentStep === 2">
                    <Col span="12" offset="6">
                         <!-- <Input icon="search" placeholder="请输入IOServer名称" style="width: 200px;margin-bottom: 20px" /> -->

                        <Card style="padding-bottom: 50px;">
                            <span style="display: block;margin-bottom: 10px;display:flex;justify-content: space-between;align-items: center">已选管辖范围 
                                <Button type="dashed" @click="editPasswordModal = true" icon="plus-round"  style="margin-right: 8px">选择管辖范围</Button>
                            </span>
                            <Table  border :columns="InternetTit" :data="InternetData"></Table>
                            <Page class="page-position" :current="1" :total="totalItemCount" show-total :page-size='pageSize'></Page>
                       </Card>
                        <div class="btn-box">
                            <Button type="primary" @click="currentStep = currentStep - 1" style="margin-right: 8px">上一步</Button>
                            <Button type="primary" @click="currentStep = currentStep + 1" style="margin-left: 8px">下一步</Button>
                        </div>
                    </Col>
                </Row>
                <Row v-if="currentStep === 3">
                    <Col span="12" offset="6">
                        <Card>
                            <span v-for="(item, i) in checkedPList" :key="i">
                                <Tag type="dot" color="blue" closable @on-close="handleClose('checkedList', item.id, 'id')">
                                    {{item.title}}
                                </Tag>
                            </span>
                            <Button type="dashed" @click="xzPer" icon="plus-round"  style="margin-right: 8px">选择人员</Button>
                        </Card>
                        <div class="btn-box">
                            <Button type="primary" @click="currentStep = currentStep - 1" style="margin-right: 8px">上一步</Button>
                            <Button type="primary" @click="$router.push({name:'system-role-manage'})" style="margin-left: 8px">添加</Button>
                        </div>
                    </Col>
                </Row>
            </div>
        </Card>
        <Modal v-model="editPasswordModal" :closable='false' :mask-closable=false :width="800">
            <h3 slot="header" style="color:#2D8CF0">管辖范围</h3>
            <div style="overflow:hidden;">
                <Table  border :columns="InternetTitBX" :data="InternetData"></Table>
                <Page class="page-position" :current="1" :total="totalItemCount" show-total :page-size='pageSize'></Page>
            </div>
            <div slot="footer">
                <Button type="text" @click="editPasswordModal = false">取消</Button>
                <Button type="primary" :loading="savePassLoading" @click="editPasswordModal = false">选择</Button>
            </div>
        </Modal>
         <Modal v-model="modalSeting" :closable='false' :mask-closable=false :width="600">
            <h3 slot="header" style="color:#2D8CF0">选择人员</h3>
            <Row>
                <Col span="10"  offset="1">
                    <div class="border-right">
                         <Input v-model="search_demp_name" icon="search" placeholder="搜索人员姓名" style="width: 100%;margin-bottom: 10px;"></Input>
                        <div class="tree-box" style="min-height: 300px;">
                            <CheckboxGroup v-model="checkedObj" @on-change="pushList">
                                <Checkbox :label="item.id" v-for="(item, i) in personList" :key="i" style="display:block;line-height: 30px;">
                                    <span>{{item.title}}</span>
                                </Checkbox>
                            </CheckboxGroup>
                        </div>
                    </div>
                </Col> 
                <Col span="10"  offset="2">
                    <div class="checked-box">
                         <p v-for="(item, i) in checkedList" :key="i">
                            <Tag type="dot" color="blue" closable @on-close="handleClose('checkedList', item.id, 'id')">
                                {{item.title}}
                            </Tag>
                        </p>
                    </div>
                </Col>
            </Row>
            <div slot="footer">
                <Button type="text" @click="modalSeting = false">取消</Button>
                <Button type="primary"  @click="savePer">选择</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
export default {
    name: 'ownspace_index',
    data () {
        const validePhone = (rule, value, callback) => {
            var re = /^1[0-9]{10}$/;
            if (!re.test(value)) {
                callback(new Error('请输入正确格式的手机号'));
            } else {
                callback();
            }
        };
        const valideRePassword = (rule, value, callback) => {
            if (value !== this.editPasswordForm.newPass) {
                callback(new Error('两次输入密码不一致'));
            } else {
                callback();
            }
        };
        return {
            modalSeting: false,
            checkedObj: [],
            checkedList: [],
            checkedPList: [],
            InternetTit1: [
                {
                    type: 'selection',
                    width: 60,
                    align: 'center'
                },
                 {
                    title: '人员id',
                    key: 'id'
                },
                {
                    title: '人员名称',
                    key: 'name'
                },
                {
                    title: '手机号',
                    key: 'phone'
                }
            ],
            InternetData1: [
                {
                    id: '11111',
                    name: '谢霆锋',
                    phone: '1305222222'
                }
            ],
            InternetTitBX: [
                {
                    type: 'selection',
                    width: 60,
                    align: 'center'
                },
                {
                    title: 'IOServer名称',
                    key: 'name'
                },
                {
                    title: '描述',
                    key: 'ms'
                },
                {
                    title: '分组',
                    key: 'address'
                },
                {
                    title: 'IP',
                    key: 'ip'
                }
            ],
            InternetTit: [
                {
                    title: 'IOServer名称',
                    key: 'name'
                },
                {
                    title: '描述',
                    key: 'ms'
                },
                {
                    title: '分组',
                    key: 'address'
                },
                {
                    title: 'IP',
                    key: 'ip'
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
            InternetData: [
                {
                    name: 'IOServer 1',
                    ms: '短暂描述',
                    address: '分组1',
                    ip: '192.168.1.1:8080'
                }
            ],
            pageIndex: 1,
            totalItemCount:1,
            pageSize: 2,
            stepList: [],
            currentStep: 0,
            status: 'wait',
            switch1: true,
            personList: [{
                id: 1,
                title: '谢霆锋'
            },{
                id: 2,
                title: '李子璇'
            },{
                id: 3,
                title: '周杰伦'
            }],
            searchList: [{
                title: '系统管理',
                expand: true,
                children: [
                    {
                        title: '管理员管理',
                        expand: true,
                        children: [
                            {
                                title: '查看'
                            },
                            {
                                title: '新增'
                            },
                            {
                                title: '修改'
                            },
                            {
                                title: '删除'
                            }
                        ]
                    },
                    {
                        title: '角色管理',
                        expand: true,
                        children: [
                            {
                                title: '查看'
                            },
                            {
                                title: '新增'
                            },
                            {
                                title: '修改'
                            },
                            {
                                title: '删除'
                            }
                        ]
                    }
                ]
            }],
            userForm: {
                name: '',
                cellphone: '',
                company: '',
                department: ''
            },
            uid: '', // 登录用户的userId
            securityCode: '', // 验证码
            phoneHasChanged: false, // 是否编辑了手机
            save_loading: false,
            identifyError: '', // 验证码错误
            editPasswordModal: false, // 修改密码模态框显示
            savePassLoading: false,
            oldPassError: '',
            identifyCodeRight: false, // 验证码是否正确
            hasGetIdentifyCode: false, // 是否点了获取验证码
            canGetIdentifyCode: false, // 是否可点获取验证码
            checkIdentifyCodeLoading: false,
            inforValidate: {
                name: [
                    { required: true, message: '请输入名称', trigger: 'blur' }
                ],
                cellphone: [
                    { required: true, message: '请输入手机号码' },
                    { validator: validePhone }
                ]
            },
            editPasswordForm: {
                oldPass: '',
                newPass: '',
                rePass: ''
            },
            passwordValidate: {
                oldPass: [
                    { required: true, message: '请输入原密码', trigger: 'blur' }
                ],
                newPass: [
                    { required: true, message: '请输入新密码', trigger: 'blur' },
                    { min: 6, message: '请至少输入6个字符', trigger: 'blur' },
                    { max: 32, message: '最多输入32个字符', trigger: 'blur' }
                ],
                rePass: [
                    { required: true, message: '请再次输入新密码', trigger: 'blur' },
                    { validator: valideRePassword, trigger: 'blur' }
                ]
            },
            inputCodeVisible: false, // 显示填写验证码box
            initPhone: '',
            gettingIdentifyCodeBtnContent: '获取验证码' // “获取验证码”按钮的文字
        };
    },
    methods: {
        xzPer () {
            this.checkedList = this.checkedPList
            this.checkedObj = this.checkedPList.map(one => {
                return one.id
            })
            this.modalSeting = true
        },
        savePer () {
            this.checkedPList = this.checkedList
            this.modalSeting = false
        },
        pushList (data) {
            let d = data.join(',')
            this.checkedList = [] 
            this.personList.map(one => {
                if (d.indexOf(one.id) >= 0) {
                    this.checkedList.push({
                        id: one.id,
                        title: one.title
                    })
                }
            })
        },
        handleClose (listName, id, colName) {
            this.checkedObj.map((one, i) => {
                if (one === id) {
                    this.checkedObj.splice(i, 1);
                }
            })
            this[listName].map((one, i) => {
                if (one[colName] === id) {
                    this[listName].splice(i, 1);
                }
            })
        },
        getIdentifyCode () {
            this.hasGetIdentifyCode = true;
            this.$refs['userForm'].validate((valid) => {
                if (valid) {
                    this.canGetIdentifyCode = true;
                    let timeLast = 60;
                    let timer = setInterval(() => {
                        if (timeLast >= 0) {
                            this.gettingIdentifyCodeBtnContent = timeLast + '秒后重试';
                            timeLast -= 1;
                        } else {
                            clearInterval(timer);
                            this.gettingIdentifyCodeBtnContent = '获取验证码';
                            this.canGetIdentifyCode = false;
                        }
                    }, 1000);
                    this.inputCodeVisible = true;
                    // you can write ajax request here
                }
            });
        },
        showEditPassword () {
            this.editPasswordModal = true;
        },
        cancelEditUserInfor () {
            this.$store.commit('removeTag', 'ownspace_index');
            localStorage.pageOpenedList = JSON.stringify(this.$store.state.app.pageOpenedList);
            let lastPageName = '';
            if (this.$store.state.app.pageOpenedList.length > 1) {
                lastPageName = this.$store.state.app.pageOpenedList[1].name;
            } else {
                lastPageName = this.$store.state.app.pageOpenedList[0].name;
            }
            this.$router.push({
                name: lastPageName
            });
        },
        saveEdit () {
            this.$refs['userForm'].validate((valid) => {
                if (valid) {
                   this.saveInfoAjax();
                }
            });
        },
        cancelEditPass () {
            this.editPasswordModal = false;
        },
        saveEditPass () {
            this.$refs['editPasswordForm'].validate((valid) => {
                if (valid) {
                    this.savePassLoading = true;
                    // you can write ajax request here
                }
            });
        },
        init () {
            // this.userForm.name = 'Lison';
            // this.userForm.cellphone = '17712345678';
            // this.initPhone = '17712345678';
            // this.userForm.company = 'TalkingData';
            // this.userForm.department = '可视化部门';
        },
        cancelInputCodeBox () {
            this.inputCodeVisible = false;
            this.userForm.cellphone = this.initPhone;
        },
        submitCode () {
            let vm = this;
            vm.checkIdentifyCodeLoading = true;
            if (this.securityCode.length === 0) {
                this.$Message.error('请填写短信验证码');
            } else {
                setTimeout(() => {
                    this.$Message.success('验证码正确');
                    this.inputCodeVisible = false;
                    this.checkIdentifyCodeLoading = false;
                }, 1000);
            }
        },
        hasChangePhone () {
            this.phoneHasChanged = true;
            this.hasGetIdentifyCode = false;
            this.identifyCodeRight = false;
        },
        saveInfoAjax () {
            this.currentStep += 1
        },
        isHaveID (list,id,idName) {
            let flag = false
            this[list].map(one => {
                if(one[idName] === id) {
                    flag = true
                }
            })
            return flag
        },
        clearIDGetList (list,id,idName){
            let arr = []
            this[list].map(one => {
                if(one[idName] !== id) {
                    arr.push(one)
                }
            })
            return arr
        }
    },
    mounted () {
        this.init();
        this.stepList = [
            {
                title: '填写基础信息',
            },
            {
                title: '请选择角色状态菜单',
            },
            {
                title: '请选择管辖范围',
            },
            {
                title: '请选择人员',
            }
        ]
    }
};
</script>
