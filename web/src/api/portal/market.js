import request from "@/utils/request";

const cmpApi = process.env.VUE_APP_BASE_API + "api/cmp/v1";

// 获取多云列表接口
export function get_market_clouds(params) {
  return request({
    url: cmpApi + "/market/anonymous/clouds/basic-info",
    method: "get",
    params,
  });
}

// 获取显卡列表接口
export function get_market_gpu(params) {
  return request({
    url: cmpApi + "/market/anonymous/resources/gpu/names",
    method: "get",
    params,
  });
}

// 获取规格列表接口
export function get_market_flavors(params) {
  return request({
    url: cmpApi + "/market/anonymous/flavors",
    method: "get",
    params,
  });
}
