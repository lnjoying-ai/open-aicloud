import request from "@/utils/request";

const billApi = process.env.VUE_APP_BASE_API + "api/bill/v1";

export function getOrders (params) {
  return request({
    url: billApi + "/orders",
    method: "get",
    params,
  });
}
export function addOrders (data) {
  return request({
    url: billApi + "/orders",
    method: "POST",
    data,
  });
}
export function detailOrders (order_id) {
  return request({
    url: billApi + "/orders/" + order_id,
    method: "get",
  });
}
export function delOrders (order_id) {
  return request({
    url: billApi + "/orders/" + order_id,
    method: "DELETE",
  });
}
export function delOrdersItem (order_id, order_item_id) {
  return request({
    url: billApi + "/orders/" + order_id + '/' + order_item_id,
    method: "DELETE",
  });
}
export function stopOrders (order_id) {
  return request({
    url: billApi + "/orders/" + order_id + '?action=finish',
    method: "POST",
  });
}


