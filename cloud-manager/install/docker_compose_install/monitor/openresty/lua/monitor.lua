local ngx = ngx

local originalHost = ngx.var.host
local originaHttpHost = ngx.var.http_host
ngx.log(ngx.INFO, "host:", originalHost)
ngx.log(ngx.INFO, "http host:", originaHttpHost)


local function is_boolean(v) return v == true or v == false end
local function has_permission(cookie, args, user_agent)
	local httpc = http.new()
	local ngx = ngx
	local monitor_url = ngx.var.monitor_url
	local url = monitor_url.."?"..args
	ngx.log(ngx.INFO, "get permission url: ",url)
	local res,err = httpc:request_uri(url, {method = "GET", headers = { ["User-Agent"] = user_agent,
                                                                   ["Accept"] = "*/*",
                                                                   ["Cookie"] = cookie,
							   }})
	if not res then
        	ngx.exit(444)
        end
	ngx.log(ngx.INFO, res.body)
	local json = cjson.decode(res.body)
	ngx.log(ngx.INFO,"hasPermission: ",json.hasPermission)
	if ( nil == json )then
        	ngx.exit(444)
	end
	if json.hasPermission then return true end
	return false
end
local args = ngx.req.get_uri_args()
--ngx.log(ngx.INFO,"all args: ", args)
ngx.log(ngx.INFO,"uri_args: ",args["Access-Token"])
local uri = ngx.var.uri
local dashboard_id = ""
for w in string.gmatch(uri, ".*/d/(.*)/.*") do dashboard_id = w end

local headers = ngx.req.get_headers()
-- local vm_id = ngx.var.id
local request_args = ngx.req.get_uri_args()

local user_agent = headers["User-Agent"]
local token = headers["x-access-token"]
local cookie = headers["Cookie"]
ngx.log(ngx.INFO, "token:", token)
ngx.log(ngx.INFO, "cookie:", cookie)

local has_project = false
local has_compute = false
local has_vm = false
--local has_panel = false
local has_user = false
local has_node = false
local monitor_args = ""

for key,value in pairs(request_args) do
        if (value ~= nil and (not is_boolean(value)))  then
            if key == "var-project_name" then has_project = true end
            if key == "var-coumpute_node" then has_compute = true end
            if key == "var-vm_name" then has_vm = true end
            if key == "var-vm_instance_id" then has_vm = true end
            if key == "var-vm_user_id" then has_user = true end
            if key == "var-node" then has_node = true end
        --		if key == "viewPanel" then has_panel = true end
            monitor_args = monitor_args..key.."="..value.."&"
        end
end

monitor_args = monitor_args.."dashboard_id="..dashboard_id
-- monitor_args = string.sub(monitor_args, 0, -2)

--if not has_panel  then
--	ngx.exit(ngx.HTTP_FORBIDDEN)
--end

--if not (has_project or has_compute or has_vm or has_user or has_node) then
--	ngx.log(ngx.INFO, "not has")
--	ngx.exit(ngx.HTTP_FORBIDDEN)
--end

if not has_permission(cookie, monitor_args, user_agent) then
	ngx.log(ngx.ERR, "no  permission")
	ngx.exit(ngx.HTTP_FORBIDDEN)
end

--ngx.req.set_header("Host", originalHttpHost)
local admin_user_name = os.getenv("ADMIN_USER")
ngx.log(ngx.INFO, "admin_user_name:",  admin_user_name)
local admin_user_password = os.getenv("ADMIN_PASSWORD")
ngx.log(ngx.INFO, "admin_user_password:",  admin_user_password)

if admin_user_name and admin_user_password then
	local credentials = ngx.encode_base64(admin_user_name .. ":" .. admin_user_password)
        ngx.req.set_header("Authorization",  "Basic " .. credentials)
end


