package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.controller.BaseErrorController;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RestSchema(schemaId = "error")
@Controller
@RequestMapping("/error")
public class ErrorController extends BaseErrorController
{
}
