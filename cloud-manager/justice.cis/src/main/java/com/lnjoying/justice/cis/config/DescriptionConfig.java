package com.lnjoying.justice.cis.config;

public class DescriptionConfig
{
    private DescriptionConfig() {}

    public static final String GET_CONTAIN_SPEC_INFOS_MSG = "The API can receive the get container spec infos request.";

    public static final String CREATE_CONTAIN_INST_MSG    = "The API can receive the create container instance request.";

    public static final String GET_CONTAIN_INST_INFOS_MSG = "The API can receive the get container instance infos request.";

    public static final String DEL_CONTAIN_INST_MSG       = "The API can receive the delete container instance request.";

    public static final String GET_CONTAIN_INST_INFO      = "#incomplete# The API can receive the get container instance info request.";

    public static final String CONTAIN_INST_ACTION_MSG    = "The API can receive the container instance action request." +
            " Action can be start, stop, restart, execute and log. When action is execute, cmd req need to be provided." +
            " When action is log, flow and lines need to be provided. Default value of flow is true, Default value of" +
            " lines is 500.";

    public static final String GET_CONTAINER_INST_STATS   = "#incomplete# The API can receive the get container instance" +
            " stats request.";

    public static final String REMOTE_COMMAND_EXECUTE     = "The API can receive the remote command execute request.";

    public static final String GET_CONTAIN_INST_OPER_LOGS = "The API can receive the remote command execute request.";

    public static final String GET_CONTAIN_INST_RUN_LOGS  = "The API can receive the remote command execute request.";
}
