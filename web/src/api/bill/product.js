import request from "@/utils/request";

const billApi = process.env.VUE_APP_BASE_API + "api/bill/v1";

export function getProducts(params) {
  return request({
    url: billApi + "/products",
    method: "get",
    params,
  });
}
export function addProduct(data) {
  return request({
    url: billApi + "/products",
    method: "post",
    data,
  });
}
export function editProduct(product_id, data) {
  return request({
    url: billApi + "/products/" + product_id,
    method: "put",
    data,
  });
}

export function delProduct(product_id) {
  return request({
    url: billApi + "/products/" + product_id,
    method: "DELETE",
  });
}
export function detailProduct(product_id) {
  return request({
    url: billApi + "/products/" + product_id,
    method: "get",
  });
}
export function getSpecs(category_id) {
  return request({
    url: billApi + "/products/specs/" + category_id,
    method: "get",
  });
}
