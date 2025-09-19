package com.lnjoying.justice.ecrm.config;

public class DescriptionConfig
{
    private DescriptionConfig() {}

    //division
    public static final String NATIONS_MSG          = "The API can receive the get nations request.";
    public static final String NATION_DIVISIONS_MSG = "The API can receive the get divisions by nation request.";
    public static final String DIVISIONS_MSG        = "The API can receive the get divisions by nation and adcode request.";

    //edge
    public static final String ADD_EDGE_NODE_MSG    = "The API can receive the add edge node request.";
    public static final String UPDATE_EDGE_NODE_MSG = "The API can receive the update edge node request.";
    public static final String DELETE_EDGE_NODE_MSG = "The API can receive delete add edge node request.";
    public static final String ACTION_EDGE_NODE_MSG = "The API can receive the action edge node request. Action can be" +
            " active, deactive, evacuate and reboot.";
    public static final String EDGE_NODE_INFO_MSG   = "The API can receive the get edge node info request.";
    public static final String EDGE_NODE_LIST_MSG   = "The API can receive the get edge node list request.";
    public static final String EDGE_NODE_IMAGES_MSG = "The API can receive the get edge node images request.";
    public static final String EDGE_QRCODES_MSG     = "The API can receive the get edge qrcodes request.";
    public static final String BIND_EDGE_MSG        = "The API can receive the bind edge request.";

    //gw
    public static final String ADD_GW_NODE_MSG      = "The API can receive the add gw node request.";
    public static final String UPDATE_GW_NODE_MSG   = "The API can receive the update gw node request.";
    public static final String DELETE_GW_NODE_MSG   = "The API can receive delete add gw node request.";
    public static final String ACTION_GW_NODE_MSG   = "The API can receive the action gw node request. Action can be" +
            " active, deactive, reboot and remove.";
    public static final String GW_NODE_LIST_MSG     = "The API can receive the get gw node list request.";

    //region
    public static final String ADD_REGION_MSG       = "The API can receive the add region request.";
    public static final String UPDATE_REGION_MSG    = "The API can receive the update region request.";
    public static final String DELETE_REGION_MSG    = "The API can receive the delete region request.";
    public static final String REGION_LIST_MSG      = "The API can receive the get region list request.";

    //site
    public static final String ADD_SITE_MSG         = "The API can receive the add site request.";
    public static final String UPDATE_SITE_MSG      = "The API can receive the update site request.";
    public static final String DELETE_SITE_MSG      = "The API can receive the delete site request.";
    public static final String SITE_LIST_MSG        = "The API can receive the get site list request.";

    //label option
    public static final String GET_LABEL_OPTION_MSG         = "The API can receive the get label option request.";
    public static final String GET_TAINT_OPTION_MSG         = "The API can receive the get taint option request.";

    //unique
    public static final String CHECK_UNIQUE_MSG         = "The API can receive the check unique request.";
}