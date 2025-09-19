import request from "@/utils/request";
const billApi = process.env.VUE_APP_BASE_API + "api/bill/v1";
//月账单概览查询
export function getConsumes(params) {
    return request({
        url: billApi + "/consumes/overview",
        method: "get",
        params,
    });
}
//账单流水
export function getConsumesDetail(params) {
    return request({
        url: billApi + "/consumes/detail",
        method: "get",
        params,
    });
}
//明细账单
export function getConsumesReport(params) {
    return request({
        url: billApi + "/consumes/report",
        method: "get",
        params,
    });
}
//月账单概览消费趋势
export function  getConsumeTrend(params) {
  return request({
    url: billApi + "/consumes/trend",
    method: "get",
    params,
  });
}