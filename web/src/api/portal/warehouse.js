import request from "@/utils/request";

const mcsApi = process.env.VUE_APP_BASE_API + "api/mcs/v1";

// 查询模型组织列表
export function anonymousOrgs(params) {
  return request({
    url: mcsApi + "/anonymous/orgs",
    method: "get",
    params,
  });
}

// 23.1.3.查询模型分类列表
export function anonymousCategories(params) {
  return request({
    url: mcsApi + "/anonymous/categories",
    method: "get",
    params,
  });
}

// 查询模型列表
export function anonymousModels(params) {
  return request({
    url: mcsApi + "/anonymous/models",
    method: "get",
    params,
  });
}

// 23.1.5.查看指定模型
export function anonymousModelsDetail(model_id) {
  return request({
    url: mcsApi + "/anonymous/models/" + model_id,
    method: "get",
  });
}
