import request from "@/utils/request";
const billApi = process.env.VUE_APP_BASE_API + "api/bill/v1";


// 获取支付通道的接口
export function getchannels(params) {
    return request({
        url: billApi + "/pay/channels",
        method: "get",
        params,
    });
}
// 充值金额接口
export function postrecharge(data) {
    return request({
        url: billApi + "/pay/orders/actions/charge",
        method: "POST",
        data,
    });
}

// 取消订单接口
export function postcancelpayment(data) {
    return request({
        url: billApi + "/pay/orders/actions/cancel",
        method: "POST",
        data,
    });
}

// 查看是否充值成功
export function getCompletepayment(params) {
    return request({
        url: billApi + `/pay/orders/${params.order_id}`,
        method: "get",
    });
}

// 获取充值记录的接口
export function getchargerecords(params) {
    return request({
        url: billApi + "/accounts/charge-records",
        method: "get",
        params,
    });
}