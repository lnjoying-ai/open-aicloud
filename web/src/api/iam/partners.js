import request from "@/utils/request";

const iamApi = process.env.VUE_APP_BASE_API + "api/iam/v1";

export function getBps(params) {
    return request({
        url: iamApi + "/bps",
        method: "get",
        params,
    });
}

export function addBps(data) {
    return request({
        url: iamApi + "/bps",
        method: "post",
        data,
    });
}

export function editBps(params, data) {
    return request({
        url: iamApi + "/bps/" + params,
        method: "put",
        data,
    });
}
export function delBps(params) {
    return request({
        url: iamApi + "/bps/" + params,
        method: "delete",
    });
}
export function bpsUniqueness(data) {
    return request({
        url: iamApi + "/bps/" + "uniqueness",
        method: "PUT",
        data,
    });
}