import request from "@/utils/request";

const iamApi = process.env.VUE_APP_BASE_API + "api/iam/v1";

export function getPolicie(params) {
  return request({
    url: iamApi + "/policies",
    method: "get",
    params,
  });
}
export function entitiesPolicie(id) {
  return request({
    url: iamApi + "/policies/" + id + "/entities",
    method: "get",
  });
}
export function addPolicie(data) {
  return request({
    url: iamApi + "/policies",
    method: "post",
    data,
  });
}
export function editPolicie(id, data) {
  return request({
    url: iamApi + "/policies/" + id,
    method: "put",
    data,
  });
}
export function delPolicie(id) {
  return request({
    url: iamApi + "/policies/" + id,
    method: "delete",
  });
}
export function detailPolicie(id) {
  return request({
    url: iamApi + "/policies/" + id,
    method: "get",
  });
}
export function getPolicieVersion(policy_id, params) {
  return request({
    url: iamApi + "/policies/" + policy_id + "/versions",
    method: "get",
    params,
  });
}
export function detailPolicieVersion(policy_id, id) {
  return request({
    url: iamApi + "/policies/" + policy_id + "/versions/" + id,
    method: "get",
  });
}
export function addPolicieVersion(policy_id, data) {
  return request({
    url: iamApi + "/policies/" + policy_id + "/versions",
    method: "post",
    data,
  });
}
export function setPolicieVersion(policy_id, id, data) {
  return request({
    url: iamApi + "/policies/" + policy_id + "/versions/" + id,
    method: "patch",
    data,
  });
}
export function delPolicieVersion(policy_id, id) {
  return request({
    url: iamApi + "/policies/" + policy_id + "/versions/" + id,
    method: "delete",
  });
}
