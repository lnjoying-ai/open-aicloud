-- add basic auth info
-- local allowedOrigin = "http://192.168.1.250:13000"
local origin = ngx.var.http_origin
ngx.log(ngx.INFO, "http origin:", origin)
local basic_auth_credentials = ngx.shared.env:get("basic_auth_credentials")
if basic_auth_credentials then
	ngx.log(ngx.INFO, "basic auth ok")
	 ngx.req.set_header("Authorization", basic_auth_credentials)
	 --ngx.req.set_header("Origin", allowedOrigin)
end
