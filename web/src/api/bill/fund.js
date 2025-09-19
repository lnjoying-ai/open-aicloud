import request from "@/utils/request";

const billApi = process.env.VUE_APP_BASE_API + "api/bill/v1";

export function getFunds(params) {
    return request({
        url: billApi + "/fundflows",
        method: "get",
        params,
    });
}

