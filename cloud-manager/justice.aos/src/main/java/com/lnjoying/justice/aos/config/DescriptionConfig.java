package com.lnjoying.justice.aos.config;

public class DescriptionConfig
{
    private DescriptionConfig() {}

    //service
    public static final String ADD_SERVICE_MSG        = "#incomplete# The API can receive the add service request.";
    public static final String UPDATE_SERVICE_MSG     = "#incomplete# The API can receive the update service request.";
    public static final String DELETE_SERVICE_MSG     = "The API can receive the delete service request.";
    public static final String ACTION_SERVICE_MSG     = "The API can receive the action service request. Action can be" +
            " start, stop and restart.";
    public static final String SERVICE_INFO_MSG       = "The API can receive the get service info request.";
    public static final String SERVICE_LIST_MSG       = "The API can receive the get service list request.";
    public static final String SERVICE_INST_LIST_MSG  = "The API can receive get inst list by serviceId request.";

    //stack
    public static final String ADD_STACK_MSG          = "The API can receive the add stack request.";
    public static final String UPDATE_STACK_MSG       = "The API can receive the update stack request.";
    public static final String DELETE_STACK_MSG       = "The API can receive the delete stack request.";
    public static final String ACTION_STACK_MSG       = "The API can receive the action stack request. Action can be" +
            " start, stop and restart.";
    public static final String STACK_INFO_MSG         = "The API can receive the get stack info request.";
    public static final String STACK_COMPOSE_MSG      = "The API can receive the get stack compose config request.";
    public static final String DOWNLOAD_COMPOSE_MSG   = "The API can receive the download compose request.";
    public static final String STACK_LIST_MSG = "The API can receive the get stack list request.";
    public static final String STACK_SERVICE_LIST_MSG = "The API can receive get service list by stackId request.";

    //template
    public static final String ADD_TEMPLATE_MSG       = "The API can receive the add template request.";
    public static final String UPDATE_TEMPLATE_MSG    = "The API can receive the update template request.";

    public static final String TEMPLATE_INFO_MSG      = "The API can receive the get template info request.";
    public static final String DOWNLOAD_TEMPLATE_MSG  = "The API can receive the download template request.";
    public static final String TEMPLATE_LIST_MSG      = "The API can receive the get template list request.";

    public static final String DELETE_TEMPLATE_BY_ID_MSG      = "The API can receive the delete template by templateId request.";
    public static final String DELETE_TEMPLATE_BY_ID_NAME_MSG = "The API can receive the delete template by templateId and name request.";
}