local ngx = ngx
local ngx_var = ngx.var
local ngx_req = ngx.req
local ngx_exit = ngx.exit
local ngx_http_unauthorized = ngx.HTTP_UNAUTHORIZED
local ngx_header = ngx.header


local auth_header = ngx_req.get_headers()["Authorization"]


local expected_user = os.getenv("ADMIN_USER")
local expected_password = os.getenv("ADMIN_PASSWORD")


local function check_credentials(user, password)
    return user == expected_user and password == expected_password
end


local _, _, user_password = string.find(auth_header or "", "Basic%s+(.*)")
if user_password then

    local decoded = ngx.decode_base64(user_password)
    if decoded then

        local pos = string.find(decoded, ":")
        if pos then
            local user = string.sub(decoded, 1, pos - 1)
            local password = string.sub(decoded, pos + 1)


            if check_credentials(user, password) then
                return
            end
        end
    end
end


ngx_header["WWW-Authenticate"] = 'Basic realm="Restricted Access"'
ngx_exit(ngx_http_unauthorized)
