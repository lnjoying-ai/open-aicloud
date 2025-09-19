import request from "@/utils/request";
const billApi = process.env.VUE_APP_BASE_API + "api/bill/v1";

export function getCategories(params) {
  return request({
    url: billApi + "/categories",
    method: "get",
    params,
  });
}

export function addCategories(data) {
  return request({
    url: billApi + "/categories",
    method: "post",
    data,
  });
}
export function editCategories(category_id, data) {
  return request({
    url: billApi + "/categories/" + category_id,
    method: "put",
    data,
  });
}
export function delCategories(category_id) {
  return request({
    url: billApi + "/categories/" + category_id,
    method: "DELETE",
  });
}
export function detailCategories(category_id) {
  return request({
    url: billApi + "/categories/" + category_id,
    method: "get",
  });
}
