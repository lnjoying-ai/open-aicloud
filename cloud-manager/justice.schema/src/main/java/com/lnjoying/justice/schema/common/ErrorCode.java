package com.lnjoying.justice.schema.common;

public enum ErrorCode
{
    /**********
     * COMMON *
     **********/
    SUCCESS(0, "Success"),
    /** Bad username */
    InvalidMetadataFieldName(10, "Metadata field name contains invalid characters."),
    INVALID_FIELD(11, "field  contains invalid characters."),

    /*********
     * USERS *
     *********/

    /** Bad username */
    INVALID_USERNAME_OR_PASSWORD(1101, "invalid user or password"),
    InvalidUsername(1111, "Username invalid."),

    /** Bad password */
    InvalidPassword(1112, "Password did not match."),

    /** Username already used */
    DuplicateUser(1113, "the info for user already in use."),

    /** Invalid authority reference */
    User_Params_Error(1114, "user params error"),
    UpdateUserError(1115, "failed to update user"),
    UNCHANGEDEmail(1116, "the input email is not changed"),
    EmailOccupied(1117, "the email is occupied by other"),
    DuplicatePhone(1118, "the input cell phone num is not changed"),
    PhoneOccupied(1119, "the cell phone num is occupied by other"),
    InvalidOldPasswd(1120, "invalid old password"),
    Invalid_validateCode(1121, "invalid validate code"),
    User_Not_Exist(1122, "user is not exist"),
    VER_Params_Error(1123, "verification params error"),
    User_Not_Grant(1124, "the user is not granted for the obj"),
    KeyOccupied(1125, "the key is occupied by other"),
    NewPassowrdError(1126, "the new password is invalid."),
    /** Authority name already used */
    DuplicateAuthority(1127, "Authority name already in use."),
    InvalidAuthority(1128, "Authority failed"),
    InvalidReq(1129, "url not exist, invalid request"),
    MODIFICATION_OF_KIND_IS_NOT_ALLOWED(1130, "modification of kind is not allowed"),


    /** No user logged in for action that requires authorization */
    CreateUserError(1131, "failed to create user "),
    User_OVER_DUE(1132, "user overdue"),
    RM_User_ERROR(1133, "remove user error."),
    USER_NOT_ACTIVE(1134, "user have not been active"),
    USER_IS_DISABLED(1135, "User is disabled"),
    USER_IS_LOCKED(1136, "User is locked"),
    NotLoggedIn(1137, "You must provide credentials to perform this action."),

    BP_NOT_EXIST(1151, "BP not exist"),
    BP_KEY_Occupied(1152,"create bp error, bp param have been occupied"),
    BP_NAME_INVALID(1153, "bp name is invalid"),
    BP_LIC_INVALID(1154, "bp license is invalid"),
    BP_USER_NEED_BP_ID(1155, "bp user must exist bp id"),
    USER_ID_NOT_FOUND(1160, "User authentication is invalid"),
    USER_NAME_NOT_FOUND(1161, "User authentication is invalid"),
    BP_ID_NOT_FOUND(1162, "bp user must exist bp id"),
    BP_NAME_NOT_FOUND(1163, "bp user must exist bp name"),
    AUTHORITIES_NOT_FOUND(1164, "authorities must exist"),
    USER_KIND_NOT_FOUND(1165, "user kind must exist"),
    DELETING_SELF_IS_NOT_SUPPORTED(1166, "deleting self is not support"),
    BP_MASTER_USER_EXIST(1167, "BP master user exist"),
    BP_ID_INCORRECT(1168, "bp id incorrect"),
    WX_LOGIN_VALIDATION_EXCEPTION(1170, "wx login validation exception"),
    GEN_USER_NAME_ERROR(1171, "gen user name error"),
    WX_REPORT_INCORRECT_USER_INFO(1172, "wx report incorrect user info"),
    PHONE_NOT_EXIST(1180, "phone is not exist"),
    EMAIL_NOT_EXIST(1181, "email is not exist"),
    OPERATION_TEMPORARILY_LOCKED(1182,"The operation is temporarily locked due to multiple input errors, please try again later"),
    INVITATION_LINK_INVALID(1183, "invitation link invalid"),
    UNSUPPORT_SELF_REGISTRATION(1184, "unsupport registration"),
    INVITATION_CODE_INVALID(1185, "invitation code invalid"),

    IAM_ILLEGAL_OPERATION(3000, "illegal Operation"),
    IAM_PROJECT_NAME_EXIST(3001, "project name exist"),
    IAM_PROJECT_NAME_NOT_EXIST(3002, "project name not exist"),
    IAM_PARENT_PROJECT_NOT_EXIST(3003, "parent project not exist"),
    IAM_PROJECT_NOT_EXIST(3004, "project not exist"),
    IAM_PROJECT_CREATE_FAILED(3005, "create project failed"),
    IAM_PROJECT_HAS_ASSOCIATED_USER(3006, "project has associated users, please delete all users"),
    IAM_PROJECT_HAS_ASSOCIATED_GROUP(3007, "policy has associated groups, please delete all groups"),
    IAM_PROJECT_HAS_ASSOCIATED_ROLE(3008, "policy has associated roles, please delete all roles"),
    IAM_POLICY_NAME_EXIST(3011, "policy name exist"),
    IAM_POLICY_NAME_NOT_EXIST(3012, "policy name not exist"),
    IAM_POLICY_NOT_EXIST(3013, "policy not exist"),
    IAM_POLICY_HAS_ASSOCIATED_RESOURCES(3015, "policy has associated resources, please delete all versions"),
    IAM_POLICY_VERSION_DEFAULT_NOT_SET_ERROR(3016, "If it is not the last one, you need to set the default version before deleting the default version."),
    IAM_GEN_NAME_ERROR(3017, "gen  name error"),
    IAM_REGO_DOC_INCORRECT(3018, "rego doc incorrect format"),
    IAM_AUTHZ_MISSING_TARGET(3021, "Missing authz target"),
    IAM_AUTHZ_SOME_POLICY_IDS_NOT_EXIST(3022, "some policy ids do not exist"),
    IAM_AUTHZ_SOME_ROLE_IDS_NOT_EXIST(3023, "some role ids do not exist"),
    IAM_AUTHZ_DETACH_ERROR(3024, "detach failed"),
    IAM_SERVICE_NAME_EXIST(3031, "service name exist"),
    IAM_SERVICE_IAM_CODE_EXIST(3032, "service iam code exist"),
    IAM_SERVICE_NAME_NOT_EXIST(3033, "service name not exist"),
    IAM_SERVICE_NOT_EXIST(3034, "service not exist"),
    IAM_ROLE_NAME_EXIST(3041, "role name exist"),
    IAM_ROLE_NAME_NOT_EXIST(3042, "role name not exist"),
    IAM_ROLE_NOT_EXIST(3043, "role not exist"),
    IAM_ROLE_NOT_ALLOWED_ACTION(3044, "role does not allow action"),
    IAM_ROLE_HAS_ASSOCIATED_RESOURCES(3045, "role has associated policies or users, please delete first"),
    IAM_AUTHZ_ATTACH_ERROR(3046, "attach failed"),

    /****************
     * ACTION
     ***************/
    BAD_REQUST(1200, "bad request"),
    ACTION_NOT_SUPPORT(1201, "action is not supported"),


    /*********
     * Project*
     *********/
    Project_Not_Exist(1260, "Project not exist"),
    Project_Parmas_Error(1261, "project params error"),

    /*********
     * Dev*
     *********/
    DEV_NOT_EXIST(1360, "dev not exist"),
    DEV_LOGIN_RECORD_ERROR(1361, "dev can not login"),
    DEV_NOT_REG(1362, "dev have not been reg"),
    DEV_HAVE_NOT_CHECKPASS(1363, "dev have not been check passed"),
    DEV_ABNORMAL(1364, "dev abnormal"),
    DEV_REG_TOKEN_EMPTY(1365, "dev reg token is empty"),
    DEV_NODEID_OCCUPIED(1366, "nodeId have been occupied"),
    DEV_REG_TOKEN_INVALID(1367, "dev reg token is invalid"),
    DEV_DROPPED(1368, "dev have been dropped"),
    NODE_NOT_EXIST(1369, "node not exist"),
    PERMISSION_DENIED(1370, "permission denied"),
    VERSION_INFO_EMPTY(1371, "version info empty"),
    DEV_CANNOT_UPGRADE(1372, "node cannot be upgraded"),
    DEV_UPGRADE_FAILED(1373, "node upgraded failed"),
    DEV_UPGRADE_CANNOT_CONFIRM(1374, "cannot confirm"),
    DEV_UPGRADING(1375, "node upgrading"),
    MISS_VERSION_INFO(1376, "miss version info"),
    DEV_INFO_EMPTY(1377, "dev info is empty"),
    DEV_UUID_OCCUPIED(1378, "uuid have been occupied"),


    /*********
     * sms*
     *********/
    SMSError(1501, "failed to send sms"),
    EMAILError(1502, "failed to send sms"),


    /**Region**/
    REGION_REMOVED(1551, "region has been removed"),
    REGION_NOT_EXIST(1552, "region not exist"),
    REGION_EMPTY(1553, "region is empty"),
    REGION_IN_USE(1554, "region in use"),
    REGIONID_OCCUPIED(1555, "regionId have been occupied"),
    REGION_NO_ONLINE_GW(1556, "no binding online gateway in the region"),

    /***
     * Site Error
     */
    SITE_NOT_EXIST(1651, "site not exist"),
    SITE_REMOVED(1652, "site has been removed"),
    SITE_IN_USE(1653, "site in use"),
    SITEID_OCCUPIED(1654, "siteId have been occupied"),

    /***
     * cluster Error
     */
    CLUSTER_DEPLOY_PLAN_NULL(1700, "cluster deploy plan is null"),
    CLUSTER_K8S_VERSION_EMPTY(1701, "k8s version is null"),
    CLUSTER_K8S_ADDON_TEMPLATE_INDEX_EMPTY(1702, "k8s addon template index is null"),
    CLUSTER_K8S_ADDON_TEMPLATE_EMPTY(1703, "k8s addon template is null"),
    CLUSTER_K8S_JKE_ENGINE_NULL(1704, "k8s joy kube engine param null"),
    CLUSTER_K8S_DUPLICATE(1705, "k8s key param is duplicate"),
    CLUSTER_K8S_REMOVED(1706, "cluster has been removed"),
    CLUSTER_K8S_LOG_EMPTY(1707, "log empty"),
    CLUSTER_K8S_PORT_ERROR(1708, "port error"),
    CLUSTER_K8S_CERT_ERROR(1709, "cert error"),
    CLUSTER_K8S_IMAGE_NOT_CFG(1710, "image not found"),
    CLUSTER_K8S_CERT_EMPTY(1711, "cert is empty"),
    CLUSTER_K8S_LOG_CONTAIN_ERROR(1712, "log has error info"),
    CLUSTER_K8S_UNKOWN_DNS_PROVIDER(1713, "unknown dns provider"),
    CLUSTER_K8S_UNKOWN_NETWORK(1714, "unknown network plugin"),
    CLUSTER_K8S_CONTROLLER_EMPTY(1715, "k8s controller planes are empty"),
    CLUSTER_K8S_NOT_GRANTED(1716, "cluster has not been granted"),
    CLUSTER_K8S_PLAN_ABNORMAL(1717, "build plan abormal"),
    CLUSTER_K8S_BP_NOT_MATCH(1718, "bp not match"),
    CLUSTER_DEPLOY_FAILED(1719, "cluster deploy failed"),
    CLUSTER_K8S_NOT_EXIST(1720, "cluster k8s not exist"),
    KUBECONFIG_TEMPLATE_FORMAT_FAILED(1721, "kubeconfig template format failed"),
    CLUSTER_UDEPLOY_FAILED(1722, "cluster uDeploy failed"),
    CLUSTER_K8S_TEMPLATE_NAME_DUP(1730, "cluster k8s template name duplicated"),
    CLUSTER_K8S_TEMPLATE_NOT_EXIST(1731, " cluster k8s template  not exist"),
    CLUSTER_K8S_TEMPLATE_ASSOCIATED_RESOURCES(1732, "There are associated resources"),
    CLUSTER_K8S_TEMPLATE_VERSIONS_DUP(1733, "cluster k8s template versions duplicated"),
    CLUSTER_K8S_TEMPLATE_NAME_EXIST(1734, "cluster k8s template name exist"),
    CLUSTER_K8S_TEMPLATE_VERSION_NOT_EXIST(1735, " cluster k8s template version not exist"),
    CLUSTER_K8S_TEMPLATE_VERSION_EXIST(1736, " cluster k8s template version exist"),
    CLUSTER_K8S_UNSUPPORTED_ACTION(1737, " cluster k8s unsupported action"),
    AUTH_CLUSTER_FAILED(1738, "auth cluster failed"),
    CLUSTER_SERVER_MISSING_CERTIFICATE(1780, "cluster server missing certificate"),
    GEN_CLUSTER_SERVER_CA_CERTS_ERROR(1781, "gen cluster server ca certs error"),
    RPC_AUTH_CLUSTER_FAILED(1782, "rpc auth cluster failed"),
    CLUSTER_K8S_CERTIFICATE_INCORRECT(1783, "cluster k8s certificate incorrect"),
    CLUSTER_K8S_TEMPLATE_VERSION_DISABLED(1784, " cluster k8s template version disabled"),
    CLUSTER_K8S_CANNOT_OPEN_TERMINAL(1785, "cluster can not open terminal"),
    CLUSTER_K8S_INACTIVE(1786, "cluster inactive"),
    CLUSTER_K8S_WORKER_OFFLINE(1787, "cluster worker offline"),

    // --- k3s
    CLUSTER_K3S_VERSION_EMPTY(1801, "k3s version is null"),
    CLUSTER_K3S_ADDON_TEMPLATE_INDEX_EMPTY(1802, "k3s addon template index is null"),
    CLUSTER_K3S_ADDON_TEMPLATE_EMPTY(1803, "k3s addon template is null"),
    CLUSTER_K3S_JKE_ENGINE_NULL(1804, "k3s joy kube engine param null"),
    CLUSTER_K3S_DUPLICATE(1805, "k3s key param is duplicate"),
    CLUSTER_K3S_REMOVED(1806, "cluster has been removed"),
    CLUSTER_K3S_LOG_EMPTY(1807, "log empty"),
    CLUSTER_K3S_PORT_ERROR(1808, "port error"),
    CLUSTER_K3S_CERT_ERROR(1809, "cert error"),
    CLUSTER_K3S_IMAGE_NOT_CFG(1810, "image not found"),
    CLUSTER_K3S_CERT_EMPTY(1811, "cert is empty"),
    CLUSTER_K3S_LOG_CONTAIN_ERROR(1812, "log has error info"),
    CLUSTER_K3S_UNKNOWN_DNS_PROVIDER(1813, "unknown dns provider"),
    CLUSTER_K3S_UNKNOWN_NETWORK(1814, "unknown network plugin"),
    CLUSTER_K3S_CONTROLLER_EMPTY(1815, "k3s controller planes are empty"),
    CLUSTER_K3S_NOT_GRANTED(1816, "cluster has not been granted"),
    CLUSTER_K3S_PLAN_ABNORMAL(1817, "build plan abormal"),
    CLUSTER_K3S_BP_NOT_MATCH(1818, "bp not match"),
    CLUSTER_K3S_NOT_EXIST(1820, "cluster k3s not exist"),
    CLUSTER_K3S_TEMPLATE_NAME_DUP(1830, "cluster k3s template name duplicated"),
    CLUSTER_K3S_TEMPLATE_NOT_EXIST(1831, " cluster k3s template  not exist"),
    CLUSTER_K3S_TEMPLATE_ASSOCIATED_RESOURCES(1832, "There are associated resources"),
    CLUSTER_K3S_TEMPLATE_VERSIONS_DUP(1833, "cluster k3s template versions duplicated"),
    CLUSTER_K3S_TEMPLATE_NAME_EXIST(1834, "cluster k3s template name exist"),
    CLUSTER_K3S_TEMPLATE_VERSION_NOT_EXIST(1835, " cluster k3s template version not exist"),
    CLUSTER_K3S_TEMPLATE_VERSION_EXIST(1836, " cluster k3s template version exist"),
    CLUSTER_K3S_UNSUPPORTED_ACTION(1837, " cluster k3s unsupported action"),
    CLUSTER_K3S_CERTIFICATE_INCORRECT(1883, "cluster k3s certificate incorrect"),
    CLUSTER_K3S_TEMPLATE_VERSION_DISABLED(1884, " cluster k3s template version disabled"),
    CLUSTER_K3S_CANNOT_OPEN_TERMINAL(1885, "cluster k3s can not open terminal"),
    CLUSTER_K3S_INACTIVE(1886, "cluster k3s inactive"),
    CLUSTER_K3S_WORKER_OFFLINE(1887, "cluster k3s worker offline"),

    CLUSTER_MONITOR_FAILED(1898, "cluster monitor failed"),
    CLUSTER_IMPORT_CMDS_FAILED(1899, "generate cluster import cmds failed"),


    /**
     * Aos
     */
    TEMPLATE_NOT_EXIST(1901, "template not exist"),
    TEMPLATE_DROPPED(1902,  "template have been dropped"),
    TEMPLATE_DUP(1903,  "template name:version duplicated"),
    TEMPLATE_PARSE_FAILED(1904, "template parse failed"),
    STACK_NOT_EXIST(1951, "stack  not exist"),
    STACK_DROPPED(1952, "stack have been dropped"),
    STACK_DUP(1953,  "stack name duplicated"),
    STACK_COMPOSE_PARSE_FAILED(1954,  "stack compose parse failed"),

    STACK_SERVICE_NOT_EXIST(2001, "stack service not exist"),
    STACK_SERVICE_DROPPED(2002, "stack service have been dropped"),
    STACK_STATUS_PARAM_ERROR(2003, "stack status param error"),
    TEMPLATE_FORMAT_FAILED(2010, "template format failed"),
    PROCESS_BATCH_RPT_STACK_ERROR(2011, "process batch rpt stack error"),
    TTY_STACK_TEMPLATE_NOT_FOUND(2012, "tty stack template not found"),
    TTY_STACK_TEMPLATE_DUP(2013, "tty stack template duplicated"),
    TTY_STACK_DEPLOY_STATUS_UNKNOWN(2014, "tty stack deploy status unknown"),
    TTY_STACK_DEPLOY_STATUS_EXCEPTION(2015, "tty stack deploy status exception"),
    NAME_IS_ALREADY_USED_BY_ANOTHER_USER(2016, "name is already used by another user"),
    HELM_REPO_NAME_EXIST(2020, "repo name exist"),
    HELM_REPO_NAME_NOT_EXIST(2021, "repo name not exist"),
    AUTH_METHOD_NOT_CORRECT(2022, "auth method not correct"),
    HELM_STACK_NAME_EXIST(2023, "helm stack name exist"),
    HELM_CHART_INSTALL_MISSING_CLUSTER_INFO(2024, "helm chart install missing cluster info"),
    HELM_APP_INFO_NOT_EXIST(2025, "helm app info not exist"),
    //HELM_STACK_NAME_EXIST(2026, "helm stack name exist"),
    HELM_STACK_NOT_EXIST(2026, "helm stack not exist"),

    HELM_REPO_ACCESS_INCORRECT(2027, "helm repo access incorrect"),
    HELM_REPO_NOT_EXIST(2028, "repo not exist"),
    HELM_REPO_ACCESS_NOT_ALLOWED(2029, "helm repo access not allowed"),
    PARSE_CHART_YAML_FAIL(2030, "parse chart yaml fail"),
    HELM_STACK_ACTION_TYPE_ERROR(2031, "helm stack action type error"),
    HELM_INSTALL_LACK_KUBE_CONFIG(2032, "lack cluster kubeConfig file"),
    HELM_COMMAND_EXECUTION_ERROR(2033, "helm command execution error"),
    TEMPLATE_CREATE_FAILED(2040,  "template create failed"),

    /**
     * CIS Error
     */
    INST_DROPPED(2101, "inst has been dropped"),
    IMAGE_EMPTY(2102, "image is empty"),
    INST_DEPLOY_STATUS_ERROR(2120, "inst deploy status error"),
    INST_SPEC_NOT_EXIST(2121, "inst spec not exist"),
    INST_SPEC_DROPPED(2122, "inst spec has been dropped"),
    INST_DUP(2123,  "inst name duplicated"),
    ASSOCIATED_RESOURCES_IN_THE_INST_SPEC(2124, "There are associated resources in the inst spec"),
    INST_SPEC_DELETING_ERROR(2125, "error deleting inst spec"),
    INST_CANNOT_REMOTE_EXECUTE(2126, "inst cannot remote execute"),
    PARSE_CFG_ERROR(2130, "parse cfg error"),
    CONFIG_PARAM_ERROR(2131, "config param error"),
    CONFIG_CREATE_ERROR(2132, "config create error"),

    /**
     * ims
     */
    REGISTRY_EXIST(2200, "registry exist"),
    REGISTRY_DUP(2201, "registry  duplicated"),
    REGISTRY_NOT_EXIST(2202, "registry  not exist"),
    REGISTRY_DROPPED(2203, "registry has been dropped"),
    REGISTRY_DISABLE(2204, "registry disabled"),
    REGISTRY_UNAVAILABLE(2205, "registry unavailable"),
    NO_AVAILABLE_REGISTRY(2206, "There is no registry available"),
    REGION_ID_ERROR(2250, "wrong region id"),
    REGISTRY_REGION_INSERT_FAILED(2251, "create registry region fail"),
    REGISTRY_PASSWORD_ERROR(2252, "incorrect password"),
    MODIFY_REGISTRY_PASSWORD_FAILED(2253, "failed to modify registry password"),
    REGISTRY_USER_EXIST(2254, "registry user already exists"),
    REGISTRY_USER_NOT_EXIST(2255, "registry user does not exist"),
    ADD_REGISTRY_USER_ERROR(2256, "add registry user fail"),
    REGISTRY_PROJECT_EXIST(2257, "registry project already exists"),
    FAILED_TO_ASSOCIATE_PROJECT_AND_USER(2258, "failed to associate project and user"),
    THE_USER_HAS_CREATED_A_REGISTRY_USER(2259, "Each user can only create one registry user, and the current user has already created a registry user"),
    FAILED_TO_CREATE_REGISTRY_PROJECT(2260, "failed to create registry project"),
    THE_PROJECT_ALREADY_EXISTS_IN_THE_REGISTRY(2261, "The project already exists in the registry"),
    REGISTRY_PROJECT_NOT_EXIST(2262, "registry project does not exist"),
    REGISTRY_USER_DROPPED(2263, "registry user has been dropped"),
    REGISTRY_PROJECT_DROPPED(2264, "registry project has been dropped"),
    REGISTRY_REPO_NOT_EXIST(2265, "registry repo does not exist"),
    MODIFY_REGISTRY_REPO_FAILED(2266, "failed to modify registry repo"),
    REGISTRY_REQUEST_HTTP_CLIENT_ERROR(2267, "registry request http client error"),
    REGISTRY_REPO_TAG_NOT_EXIST(2268, "registry repo tag does not exist"),
    REGISTRY_3RD_DUP(2269, "third party registry duplicated"),
    REGISTRY_3RD_NOT_EXIST(2270, "third party registry  not exist"),
    REGISTRY_3RD_EXIST(2271, "same name registry exist"),
    REGISTRY_3RD_DROPPED(2272, "third party registry has been dropped"),
    FAILED_TO_UPDATE_PRE_DOWNLOAD(2273, "failed to update pre download record"),
    REGION_REGISTRY_NOT_FOUND(2274, "region registry not found"),
    GET_REPO_LIST_ERROR(2275, "get repo list error"),
    ONLY_ONE_REGISTRY_USER(2276, "A user can only have one registry user"),
    UPDATE_REGISTRY_USER_PASSWORD_FAIL(2277, "update registry user password fail"),
    BP_VISIBLE_PROJECT_NEED_TO_HAVE_BPID(2278, "bp visible project need to have bpid"),
    MSG_PROCESSING_ERROR(2280, "msg processing failed"),
    REGISTRY_COMMAND_TYPE_ERROR(2281, "registry command type error"),
    ASSOCIATED_RESOURCES_IN_THE_REGISTRY(2282, "There are associated resources in the registry"),
    ASSOCIATED_RESOURCES_IN_THE_PROJECT(2283, "There are associated resources in the project"),
    TASK_EXECUTE_EXCEPTION(2290, "task execute exception"),
    REGISTRY_HTTP_REQUEST_EXCEPTION(2291, "registry http request exception"),
    INCORRECT_USER_ID(2292, "incorrect user id"),
    USER_REGISTRY_PASSWORD_HAS_NOT_BEEN_SET(2293, "user registry password has not been set"),


    /*********
     * plugin*
     *********/
    NO_PLUGIN(2600, "no plugin for the software"),
    PLUGIN_PARAM_ERROR(2601,"plugin param error"),



    /****content ****/
    CONTENT_NOT_EXIST(3300, "content not exist"),
    CONTENT_UNKOWN_ERROR(3301, "get content list error"),
    CONTENT_UNSUPPORT_ERROR(3302, "unsupport content type"),

    /******count error*************/
    STATISTIC_ERRROR(4800, "statistic error"),

    /******cluster agent error*************/
    WORKER_NOT_EXIST(5001, "worker not exist"),
    CLUSTER_NOT_EXIST(5002, "cluster not exist"),
    CLUSTER_DROPPED(5003, "cluster dropped"),
    WORKER_HAVE_NOT_CHECKPASS(5004, "worker have not been check passed"),
    WORKER_REG_TOKEN_INVALID(5053, "worker reg token is invalid"),

    /******service manager error*************/
    SERVICE_GATEWAY_NOT_EXIST(6001, "service gateway not exist"),
    SERVICE_PORT_NOT_EXIST(6002, "service port not exist"),
    SERVICE_GATEWAY_PORT_RANGE_ERROR(6003, "port ranges error"),
    /******service gateway error*************/
    SERVICE_GATEWAY_PORT_CONFLICT(6101, "service gateway port conflict"),

    SUBMIT_MSG_ERROR(8001, "submit msg error"),

    /*********
     * sys*
     *********/
    PUBLISH_CONFIG_ERROR(8800, "publish config error"),
    GET_CONFIG_ERROR(8801, "get config error"),
    NACOS_OPERATION_ERROR(8802, "config operation error"),
    OPERATION_NOT_ALLOWED(8803, "operation not allow"),
    DELETE_CONFIG_ERROR(8804, "delete config error"),
    OVER_LICENSE_NODE_NUM(8805, "over license define node num"),
    LICENSE_ABNORMAL(8806, "license abnormal"),
    LICENSE_PARAMS_ERROR(8807, "license params error"),

    LICENSE_NOT_ACTIVE(8808, "license params error"),

    OVER_LICENSE_CORE_NUM(8809, "over license define core num"),



    /*********
     * OTHER *
     *********/
    PARAM_ERROR(9000, "param error."),
    /** No user logged in for action that requires authorization */
    SystemError(9999, "System error."),
    /** Error with no explanation */
    Unknown(9998, "Unknown error."),
    Redirect(9997,"redirect"),
    UNKNOW_SERVICE(9996, "Unknown service."),
    SQL_ERROR(9100, "data exception"),
    ILLEGAL_ARGUMENT(9200, "illegal argument"),
    PARAMETER_IS_NOT_VALID_FOR_OPERATION(9201, "Parameter is not valid for operation"),
    ENCODE_FAILED(9300, "encode failed"),
    DECODE_FAILED(9301, "decode failed"),
    DUMP_YAML_FILE_FAILED(9302, "dump yaml file failed"),
    LOAD_YAML_FILE_FAILED(9303, "load yaml file failed"),
    DATA_DUP(9304, "data  duplicated"),


    /*********
     * cmp*
     *********/
    //compute
    CLOUD_NOT_EXIST(12001, "cloud not exists"),
    CLOUD_AGENT_DROPPED(12002, "cloud agent dropped"),
    PROHIBIT_UPDATE_CLOUD(12003, "prohibit update cloud"),
    FORBIDDEN_ACTION(12004, "forbidden action"),
    CLOUD_MAINTAINING(12005, "cloud is maintaining"),
    CLOUD_OFF_SHELF(12006, "cloud is off shelf"),
    //compute
    BAREMETAL_DEVICE_NOT_EXIST(12200, "baremetal device not exists"),
    PCI_DEVICE_GROUP_NOT_EXIST(12208, "pci device group does not exists"),
    PCI_DEVICE_NOT_EXIST(12209, "pci device does not exists"),
    FLAVOR_IS_USING(12210, "falvor is using"),
    VM_RUNNING_ON_HYPERVISOR_NODE(12211,"vms are running on the node"),
    BAREMETAL_INSTANCE_EXISTED(12220, "baremetal instance existed"),
    BAREMETAL_INSTANCE_NOT_EXIST(12221, "baremetal instance not exists"),
    BAREMETAL_INSTANCE_NETWORK_ERROR(12222, "baremetal instance get network info error"),
    PUBKEY_NOT_EXIST(12223, "pubkey not exists"),
    BAREMETAL_INSTANCE_IS_RUNNING(12224,"baremtal instance is running with the baremetal_device_id"),
    FLAVOR_NOT_EXIST(12225, "flavor not exists"),
    VM_INSTANCE_NOT_EXIST(12226, "vm instance not exists"),
    VM_SNAP_NOT_EXIST(12227, "vm snap not exists"),
    HYPERVISOR_NODE_NOT_EXIST( 12228, "hypervisor node not exists"),
    VM_HAS_SNAPS(12229, "remove the snaps first"),
    VM_SNAP_SWITCHING(12230, "vm snap is switching,please wait"),
    INSTANCE_GROUP_NOT_EMPTY(12231, "instance group is not empty"),
    VM_INSTANCE_HAS_GROUP(12232, "the groupId of the vm instance is not null"),
    INSTANCE_GROUP_ID_NOT_CORRECT(12233, "the groupId of the vm instance is not correct"),
    INSTANCE_GROUP_NOT_EXIST(12234, "instance group does not exist"),
    INSTANCE_GROUP_CREATE_FAILED(12235, "instance group create failed"),
    VM_INSTANCE_IS_MIGRATING(12241, "vm instance is migrating,please wait"),
    VM_SNAP_IS_UPDATING(12242, "vm snap is updating,please wait"),
    VOLUME_IS_UPDATING(12243, "volume is updating,please wait"),
    PCI_DEVICE_NOT_DETACHED(12900, "pci device is not detached"),
    PCI_DEVICE_NOT_ATTACHED(12901, "pci device is not attached"),
    PCI_DEVICE_GROUP_ALREADY_ATTACHED(12902, "pci device group already attached by the other vm"),
    VM_STATUS_ERROR(12903, "vm status error"),
    VM_INSTANCE_NOT_ALLOW_UPDATE(12904, "vm instance cannot be modified"),
    NODE_RESOURCE_NOT_ENOUGH(12906, "node resource not enough"),
    FLAVOR_ROOT_DISK_SIZE_NOT_SAME(12909, "the size of root disk is not same with the flavor"),
    FLAVOR_GPU_NAME_NOT_SAME(12910, "the name of gpu is not same with the flavor"),
    VM_PCI_DEVICE_POWER_OFF(12911, "vm with pci device should be power off"),
    //network
    VPC_NOT_EXIST(12300, "vpc not exist"),
    SUBNET_NOT_EXIST(12301, "subnet not exist"),
    AGENT_SERVICE_ERR(12302, "backend service error"),
    VPC_HAS_SUBNETS(12303,"remove the subnets first"),
    SUBNET_HAS_PORTS(12304,"remove the instances first"),
    UPDATE_DATABASE_ERR(12305,"update database error"),
    EIP_NOT_EXISTS(12306,"eip not exists"),
    EIP_MAP_NOT_EXISTS(12307, "eipMap not exists"),
    PORT_MAP_NOT_EXISTS(12308, "portMap not exists"),
    EIP_NOT_ENOUGH(12309, "eip not enough"),
    EIP_MAP_ALREADY_EXISTS(12310, "eipMap already exists"),
    SUBNET_CIDR_EXISTS(12311, "cidr already exists"),
    SECURITY_GROUP_NOT_EXISTS(12312, "security group not exists"),
    SECURITY_GROUP_HAS_RULES(12313, "remove the rules first"),
    SECURITY_GROUP_RULE_NOT_EXISTS(12314, "security group rule not exists"),
    DEFAULT_SECURITY_GROUP_NOT_REMOVE(12315,"default security group can't remove"),
    DEFAULT_SECURITY_GROUP_NOT_UPDATE(12316, "default security group can't update"),
    INSTANCE_USED_BY_NAT(12317, "remove the nat gateway first"),
    SECURITY_GROUP_USED_BY_RULE(12318,"security group is used by other rules"),
    EIP_POOL_NOT_EXISTS(12319, "eip pool not exists"),
    CIDR_OVERLAP(12320, "cidr overlap"),
    NETWORK_ADDRESS_ERROR(12321, "network address error"),
    PORT_HAS_EIP(12322, "detach the eip first"),
    PORT_EIP_NOT_EXIST(12323, "eip is not attached"),
    PORT_NOT_CREATED(12324, "port is not created"),
    EIP_POOL_VPC_RELATION_NOT_EXISTS(12400, "the relationship between the eip pool and the vpc not exists" ),
    EIP_POOL_VPC_RELATION_ALREADY_EXISTS(12401, "the relationship between the eip pool and the vpc already exists"),
    EIP_POOL_HAS_EIPS(12402, "remove the eips first"),
    OBJECT_IS_DELETING(12403, "object is deleting,can't update it"),
    STATIC_IP_INVALID(12404, "static ip is invalid"),
    STATIC_IP_IS_OCCUPIED(12405, "ip address is occupied"),
    INSTANCE_USED_BY_EIP(12406, "detach the eip first"),
    EIP_CHECK_ERR(12407, "nat gateway check error"),
    EIP_MAP_IS_UNMAPPING(12408, "nat gateway is removing"),
    EIP_ALREADY_ATTACHED(12409, "eip already attached"),
    SECURITY_GROUP_IS_BOUND(12410,"security group is bound"),
    SG_INSTANCE_EXISTS(12411, "the instance has been bound to the security group"),
    BACKEND_NOT_EXIST(12950,"backend not exist"),
    FRONTEND_NOT_EXIST(12951,"frontend not exist"),
    LOADBALANCER_NOT_EXIST(12952,"loadbalancer not exist"),

    //image
    IMAGE_NOT_EXIST(12500, "image does not exists"),
    IMAGE_IS_USING(12501,"image is using"),
    SHARE_NOT_EXIST(12502, "share does not exists"),
    IMAGE_SYSTEM_DEFAULT(12503, "default image,cannot be deleted"),
    IMAGE_NAME_EXIST(12504, "image name already exists"),
    /**
     * volume
     */
    VOLUME_NOT_EXIST(12700, "volume does not exists"),
    VOLUME_IS_USING(12701, "volume is using"),
    VOLUME_SNAP_NOT_EXIST(12702,"volume snap does not exists"),
    VOLUME_NOT_DETACHED(12703, "volume is not detached"),
    VOLUME_NOT_ATTACHED(12704, "volume is not attached"),
    INSTANCE_SHOULD_BE_POWER_OFF(12705, "vm instance should be shut down"),
    VOLUME_CANNOT_DETACHED_OR_ATTACHED(12706, "vm instance has vmSnaps,can't attach or detach more volumes"),
    VOLUME_HAS_SNAPS(12707,"remove the volumeSnaps first"),
    VOLUME_ALREADY_ATTACHED(12708, "volume is already attached"),
    VOLUME_COUNT_EXCEED(12709, "volume count exceed"),
    VOLUME_SIZE_TOO_LARGE(12710, "volume size too large"),
    VOLUME_ATTACHED_TOO_MANY(12711, "volume attached too many"),
    VOLUME_IS_EXPORTING(12712, "volume is exporting,please wait"),
    STORAGE_POOL_NOT_EXIST(12720, "storage pool does not exists"),
    VOLUME_NEVER_USED(12721, "volume has never been used before"),
    OPERATION_RETRY(12905, "please retry"),
    INSTANCE_NOT_POWER_ON(12907,"vm instance is not powered on"),
    INSTANCE_NOT_POWER_OFF(12908,"vm instance is not powered off"),
    /**
     * openstack
     */
    VOLUME_BACKUP_NOT_EXIST(12801,"volume backup does not exists"),
    FLOATINGIP_NOT_EXISTS(12802,"floating ip not exists"),
    PORTFORWARDING_NOT_EXISTS(12803,"port forwarding not exists"),
    KEYPAIR_NOT_EXIST(12804, "key pair not exists"),
    NETWORK_NOT_EXIST(12805, "network not exist"),
    PORT_NOT_EXIST(12806, "port not exist"),
    ROUTER_NOT_EXIST(12807, "router not exist"),
    INSTANCE_NOT_EXIST(12808, "instance not exist"),
    /**
     * easystack
     */
    FIREWALL_NOT_EXIST(12811,"firewall does not exists"),
    FIREWALL_POLICY_NOT_EXIST(12812,"firewall policy does not exists"),
    FIREWALL_RULE_NOT_EXIST(12813,"firewall rule does not exists"),

    NFS_NOT_EXIST(13000, "nfs does not exist"),
    /**
     * cmp
     */
    NO_AVAILABLE_EIP(13001,"no available eip"),
    EXPIRE_TIME_CONFLICT(13002,"expire time conflict"),
    RESOURCE_IS_CREATING(13003,"resource is cerating, please wait"),

    /*********
     * bill*
     *********/
    BILL_PARENT_CATEGORY_NOT_EXIST(24001, "bill parent category not exist"),
    BILL_CATEGORY_NOT_EXIST(24002, "category not exist"),
    BILL_PRODUCT_NOT_EXIST(24003, "product not exist"),
    BILL_DISCOUNT_NOT_EXIST(24004, "discount not exist"),
    BILL_BP_ACCOUNT_NOT_EXIST(24005, "bp account not exist"),
    BILL_CATEGORY_NOT_BELONG_CLOUD(24006, "category that does not belong to the specified cloud"),
    BILL_PRODUCT_NOT_PRICED(24007, "product not yet priced"),
    BILL_DISCOUNT_VALUE_INCORRECT(24008, "Discount value needs to be between 0 and 1 when Discount Type is Percentage"),
    BILL_PRODUCT_PARAM_WRONG(24009, "the correct cloud id and specification id are required"),
    BILL_ClOUD_INCORRECT(24010, "cloud does not exist"),
    BILL_BP_ACCOUNT_OVERDUE(24011, "bp account overdue"),
    BILL_CATEGORY_EXIST(24012, "category  exist"),
    BILL_PREPAID_PARAMS_NEED(24013, "Period and period Unit parameters are required for prepaid"),
    BILL_RECHARGE_AMOUNT_CANNOT_BE_LESS_THAN_0(24014, "recharge amount cannot be less than 0"),
    BILL_DELETE_PRODUCT_FIRST(24015, "delete product first"),
    BILL_PRODUCT_UNDER_BILLING(24016, "the product is still under billing and cannot be deleted."),
    BILL_PRODUCT_UNDER_BILLING_DISALLOW_UPDATE(24017, "the product is still under billing and cannot be update."),
    BILL_BP_ACCOUNT_BALANCE_INSUFFICIENT(24018, "User account balance is insufficient, please charge"),
    BILL_UNKNOWN_CHARGE_TYPE(24019, "Unknown charge type,optional values are: prepaid, postpaid"),
    BILL_INSTANCE_NOT_EXIST(24020, "the billing instance does not exist"),
    BILL_CATEGORY_NAME_EXIST(24021, "category name already exists"),
    BILL_PRODUCT_DISK_PARAMS_ERROR(24022, "product disk params error"),
    BILL_ORDER_NOT_EXIST(24023, "order not exist"),
    BILL_ORDER_ITEM_NOT_EXIST(24024, "order item not exist"),
    BILL_INSTANCE_PARAMS_EMPTY_NOT_ALLOWED(24025, "instance parameter cannot be empty"),
    BILL_ORDER_ACTION_TYPE_ERROR(24026, "bill order action type error"),
    BILL_SPEC_PARAMS_EMPTY_NOT_ALLOWED(24027, "spec parameter cannot be empty"),
    BILL_PRODUCT_INFO_INCORRECT(24028, "product info incorrect"),
    BILL_PAY_ORDER_DUP(24029,  "bill pay order duplicated"),
    BILL_CREATE_PAY_ORDER_ERROR(24030,  "bill create pay order error"),
    BILL_PAY_ORDER_NOT_EXIST(24031,  "bill  pay order not exist"),
    BILL_PAY_ORDER_ALREADY_SUCCESS(24032,  "bill pay order already success"),
    BILL_PAY_ORDER_STATUS_ERROR(24033,  "bill pay order status error"),
    BILL_PAY_ORDER_EXPIRED(24034,  "bill pay order expired"),
    BILL_PAY_CHANNEL_NOT_EXIST(24035,  "bill pay channel not exist"),
    BILL_UPDATE_PAY_ORDER_ERROR(24036,  "bill pay order update error"),
    BILL_CHANNEL_NOT_FOUND(24040,  "pay channel not found"),
    BILL_ACCOUNT_BALANCE_NOT_ENOUGH(24041,  "account balance not enough"),
    BILL_ACCOUNT_FREEZE_AMOUNT_NOT_ENOUGH(24042,  "account freeze amount not enough"),
    BILL_ACCOUNT_FREEZE_AMOUNT_FAILED(24043,  "fail when freezing account"),
    BILL_ORDER_EXPIRED(24044, "order expired" ),
    BILL_PAY_PUBLIC_REMIT_CODE_GEN_ERROR(24045,  "public remit code gen error"),
    BILL_RPC_PARAMS_INSTANCE_NOT_EMPTY(24046,  "rpc params instance not empty"),
    BILL_PAY_PUBLIC_REMIT_CODE_NOT_EXIST(24047,  "public remit code not exist"),
    BILL_PAY_PUBLIC_REMIT_CODE_ALREADY_CHARGE(24048,  "public remit code already charged"),
    BILL_PRODUCT_EIP_PARAMS_ERROR(24049, "product eip params error"),


    /*********
     * omc *
     *********/
    OMC_ALARM_ADD_FAILED(25010, "alarm add failed"),
    OMC_ALARM_DELETE_FAILED(25011, "alarm delete failed"),
    OMC_ALARM_CREATE_NOT_ALLOWED(25012, "alarm create illegal"),
    OMC_RECEIVER_NOT_EXIST(25020, "receiver not exist"),
    OMC_RULE_CANNOT_BE_EMPTY(25021, "rule cannot not empty"),
    OMC_ALARM_NOT_EXIST(25022, "alarm not exist"),
    PROMETHEUS_RELOAD_ERROR(25050, "prometheus reload error"),
    PROMETHEUS_RELOAD_FAILED(25051, "prometheus reload failed"),
    PROMETHEUS_RULE_CREATE_FAILED(25052, "rule add failed"),
    PROMETHEUS_CONFIG_UPDATE_FAILED(25053, "prometheus config update failed"),


    /*********
     * oss *
     *********/
    OSS_SETTLE_RULE_NOT_EXIST(26001, "oss settle rule not exist"),
    OSS_ORDER_ALREADY_SETTLED(26012, "a certain accounting period  already settled"),
    OSS_SETTLE_NOT_EXIST(26013, "oss settle not exist"),
    OSS_SETTLED_NOT_ALLOWED_UPDATE(26014, "already settled, cannot update"),
    OSS_SETTLE_RULE_STATUS_ERROR(26015, "settle rule status error"),
    OSS_POOL_HAS_SETTLE_RULE(26016, "a settle rule has been set for the resource pool"),
    OSS_POOL_ALREADY_SETTLE_UPDATE_DELETE_NOT_ALLOWED(26017, " the resource pool already settled not allowed update and delete"),
    OSS_PROVIDER_DISABLED_SETTLE_NOT_ALLOWED(26018, "provider disabled settle not allowed"),
    OSS_ACCOUNT_HAS_SETTLE_RULE(26019, "a settle rule has been set for the provider account"),
    OSS_PROVIDER_NOT_EXIST(26003, "provider not exist"),
    OSS_COMPUTE_RESOURCE_NOT_EXIST(26005, "computer resource not exist"),
    OSS_PROVIDER_ACCOUNT_NOT_EXIST(26006, "provider account not exist"),
    OSS_IAM_USER_DUPLICATED(26004, "iam user already used"),


    /*********
     * issue *
     *********/
    ISSUE_WORKFLOW_DELETE_NOT_ALLOWED(27101, "You can not delete a workflow active or still in use"),
    ISSUE_WORKFLOW_UPDATE_NOT_ALLOWED(27102, "You can not update a workflow active or still in use"),
    ISSUE_WORKFLOW_NOT_FOUND(27103, "Workflow not found"),
    ISSUE_WORKFLOW_CREATE_PARA_ERROR(27104, "Paras error while workflow creation"),
    ISSUE_WORKFLOW_UPDATE_PARA_ERROR(27105, "Paras error while workflow update"),
    ISSUE_WORKFLOW_TRANSITION_NOT_FOUND(27106, "Workflow transition not found"),
    ISSUE_WORKFLOW_STEP_ADD_NOT_ALLOWED(27107, "Workflow is still in use"),
    ISSUE_WORKFLOW_STEP_UPDATE_NOT_ALLOWED(27108, "Workflow is still in use"),
    ISSUE_WORKFLOW_STEP_NOT_FOUND(27109, "Workflow step not found"),
    ISSUE_WORKFLOW_STEP_DELETE_NOT_ALLOWED(27110, "Workflow is still in use"),
    ISSUE_WORKFLOW_TRANSITION_ADD_NOT_ALLOWED(27111, "Workflow is still in use"),
    ISSUE_WORKFLOW_TRANSITION_DUPLICATE(27112, "Workflow transition is duplicated"),
    ISSUE_WORKFLOW_TRANSITION_UPDATE_NOT_ALLOWED(27113, "Workflow is still in use or something else"),
    ISSUE_WORKFLOW_TRANSITION_DELETE_NOT_ALLOWED(27114, "Workflow is still in use"),

    ISSUE_TYPE_NOT_FOUND(27201, "Issue type not found"),
    ISSUE_TYPE_DELETE_NOT_ALLOWED(27202, "Issue type is associated to issues"),
    ISSUE_TYPE_CREATE_PARA_ERROR(27203, "Paras error while issue type creation"),
    ISSUE_PROJECT_NOT_FOUND(27301, "Project not found"),
    ISSUE_SEARCH_INVALID_QUERY(27401, "Invalid query"),
    ISSUE_NOT_FOUND(27402, "Issue not found"),
    ISSUE_CREATE_PARA_ERROR(27403, "Paras error while issue creation"),
    ISSUE_DELETE_NOT_ALLOWED(27404, "Issue delete is not permitted"),
    ISSUE_TRANSITION_NOT_ALLOWED(27405, "Issue transition is not permitted"),
    ISSUE_ATTACHMENT_NOT_FOUND(27406, "Issue attachment not found"),
    ISSUE_ATTACHMENT_DELETE_NOT_ALLOWED(27407, "Issue attachment delete not permitted"),

    //custom
    CUSTOM_ERROR(99999, "");


    /** Numeric code */
    private int code;

    /** Error message */
    private String message;

    private ErrorCode(int code, String message)
    {
        this.setCode(code);
        this.setMessage(message);
    }

    /**
     * Look up the enum based on error code.
     *
     * @param code
     * @return
     */
    public static ErrorCode fromCode(int code)
    {
        for (ErrorCode current : ErrorCode.values()) {
            if (current.getCode() == code) {
                return current;
            }
        }
        throw new RuntimeException("Invalid error code: " + code);
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setErrorCode(ErrorCode errorCode)
    {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
