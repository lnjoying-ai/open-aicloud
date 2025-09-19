ngx.log(ngx.INFO, "Original URI args1: ", ngx.var.args)
--ngx.req.set_uri_args({kiosk=""})
--ngx.req.set_uri("/"..ngx.var.uri)
--ngx.var.args = ngx.var.args.."&kiosk"
--ngx.req.set_uri_args(ngx.var.args.."&kiosk=")
local args = ngx.req.get_uri_args()

-- 检查是否存在 kiosk 参数
if not args["kiosk"] then
    -- 如果不存在，则添加 kiosk 参数，不带值
    ngx.log(ngx.ERR, "no kiosk.....")
    --ngx.req.set_uri_args({kiosk=true})
    args.kiosk = true
    ngx.req.set_uri_args(args)
-- ngx.req.set_uri_args("var-bp=default&dashboard_id=a3bb6fb8-fa14-46db-84cc-3f4996286784&var-vm_user_id=ddd&var-user=39937079-99fe-4cd8-881f-04ca8c4fe09d&var-site=All&orgId=1&var-vendor=&var-node_id=All&var-nodename=All&var-interval=2m&var-total=2&var-device=All&var-maxmount=%2F&var-show_name=&var-sname=&from=1702271817569&to=1702275417569&kiosk")
end

--ngx.log(ngx.ERR, "Original URI args2: ", ngx.var.args)
