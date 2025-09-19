package com.lnjoying.justice.aos.util;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.lnjoying.justice.aos.db.model.TblStackTemplateInfo;
import com.lnjoying.justice.aos.domain.dto.req.AddTemplateReq;
import com.lnjoying.justice.aos.domain.dto.req.UpdateTemplateReq;
import com.lnjoying.justice.aos.facade.TemplateServiceFacade;
import com.lnjoying.justice.commonweb.util.ApplicationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/7 14:35
 */
@Slf4j
public  class TtyStackUtils
{
    public static final String FRP_SERVER_ADDR_KEY = "frp_server_addr";

    public static final String TTY_STACK_TEMPLATE = "tty-system-default-stack";

    public static final String NODE_ID_KEY = "node_id";

    public static final String FRP_ADDR_KEY = "frp_addr";

    public static final String FRP_TTY_SSH_HOST = "ssh_host";

    public static final String DEFAULT_FRP_TTY_SSH_HOST = "172.17.0.1";

    private static TtyStackUtils DEFAULT = new TtyStackUtils();

    private static final String TTY_STACK_TEMPLATE_FILE_PATH = "tty_stack.yaml";

    private static final String ADMIN_USER_ID = "39937079-99fe-4cd8-881f-04ca8c4fe09d";

    public static TtyStackUtils getInstance()
    {
        return DEFAULT;
    }

    public void checkTtyStackTemplate(TemplateServiceFacade templateServiceFacade, ConfigService configService)
    {
        // If not in the db, create it from the template file
        List<TblStackTemplateInfo> templateList = templateServiceFacade.selectBasicInfoByName(TTY_STACK_TEMPLATE);
        if (!CollectionUtils.isEmpty(templateList))
        {
            return;
        }

        try
        {
            String frptmpl = configService.getConfig("com.justice.aos.config.ttytmpl", "aos", 30000);
            //List<String> res = YamlConfigUtils.readMultiYamlConfig(TTY_STACK_TEMPLATE_FILE_PATH);
            List<String> res = YamlConfigUtils.readMultiYamlConfigFromString(frptmpl);
            if (!CollectionUtils.isEmpty(res) && res.size() == 2)
            {
                String dockerCompose = res.get(0);
                String justiceCompose = res.get(1);
                AddTemplateReq addTemplateReq = buildAddTemplateReq(dockerCompose, justiceCompose);
                templateServiceFacade.addTemplate(addTemplateReq, null, null, ADMIN_USER_ID);
            }
            else
            {
                log.error("frpc template parse failed:{}");
            }
        }
        catch (NacosException e)
        {
            log.error("get frptmpl from nacos error: {}", e);
        }

    }

    public void updateTtyStackTemplate(TemplateServiceFacade templateServiceFacade,String newContent)
    {
        // If in the db, update it from the template file
        List<TblStackTemplateInfo> templateList = ((TemplateServiceFacade)ApplicationUtils.getBean("templateServiceFacade")).selectBasicInfoByName(TTY_STACK_TEMPLATE);
        if (CollectionUtils.isEmpty(templateList) || StringUtils.isBlank(newContent))
        {
            return;
        }

        List<String> res = YamlConfigUtils.readMultiYamlConfigFromString(newContent);
        if (!CollectionUtils.isEmpty(res) && res.size() == 2)
        {
            String dockerCompose = res.get(0);
            String justiceCompose = res.get(1);
            UpdateTemplateReq updateTemplateReq = buildUpdateTemplateReq(dockerCompose, justiceCompose);
            templateServiceFacade.updateTemplate(templateList.get(0).getTemplateId(), updateTemplateReq);
        }
        else
        {
            log.error("frpc template parse failed:{}");
        }


    }

    private UpdateTemplateReq buildUpdateTemplateReq(String dockerCompose, String justiceCompose)
    {
        UpdateTemplateReq updateTemplateReq = new UpdateTemplateReq();
        updateTemplateReq.setStack_compose(dockerCompose);
        updateTemplateReq.setJustice_compose(justiceCompose);
        return updateTemplateReq;
    }

    private AddTemplateReq buildAddTemplateReq(String dockerCompose, String justiceCompose)
    {
        AddTemplateReq addTemplateReq = new AddTemplateReq();
        addTemplateReq.setName(TTY_STACK_TEMPLATE);
        addTemplateReq.setVersion("v1.41");
        addTemplateReq.setDescription("frpc and wetty");
        addTemplateReq.setLogo_url("");
        addTemplateReq.setVendor("system");
        addTemplateReq.setAos_type("docker-compose");
        addTemplateReq.setContent_type("yaml");
        addTemplateReq.setStack_compose(dockerCompose);
        addTemplateReq.setJustice_compose(justiceCompose);
        return addTemplateReq;
    }
}