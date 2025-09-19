
const state = {
    transmissionUserInfo: JSON.parse(localStorage.getItem('transmissionUserInfo')), //用户信息
    transmissionInfo: JSON.parse(localStorage.getItem('transmissionInfo')), //传输信息
    transferDialogStatus: false, //传输弹窗状态
    windowstatus: false, //window电脑
    macstatus: false, //mac电脑
}

const mutations = {
    userInfo(state, data) {
        state.userInfo = data
    },
}

const actions = {

}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
