import request from "@/utils/request";
const billApi = process.env.VUE_APP_BASE_API + "api/bill/v1";

export function getAccounts(params) {
  return request({
    url: billApi + "/accounts",
    method: "get",
    params,
  });
}
//账户充值
export function addAccounts(bp_id, data) {
  return request({
    url: billApi + "/accounts/" + bp_id + "/charge",
    method: "put",
    data,
  });
}
//查看账户余额
export function detailAccounts(bp_id) {
  return request({
    url: billApi + "/accounts/" + bp_id,
    method: "get",
  });
}
