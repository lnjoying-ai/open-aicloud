import i18n from "@/utils/i18n/index.js";

export default {
  getCodeStatus(code, errorInfo) {
    // if (code == 3000) {
    //   return (
    //     errorInfo.config.url + "\n" + codeMessageMap[JSON.stringify(code)] ||
    //     false
    //   );
    // } else {
    if (code == 9000 && errorInfo.data.message) {
      return errorInfo.data.message;
    } else {
      return codeMessageMap[JSON.stringify(code)] || false;
    }
    // }
  },
};

const codeMessageMap = {
  /** ********
   * COMMON *
   **********/
  0: i18n.t("codeStatus.0"), // 'Success'
  /** Bad username */
  10: i18n.t("codeStatus.10"), // 'Metadata field name contains invalid characters.'

  /** *******
   * USERS *
   *********/

  /** Bad username */
  1101: i18n.t("codeStatus.1101"), // 'Username invalid.'
  1111: i18n.t("codeStatus.1111"), // 'Username invalid.'

  /** Bad password */
  1112: i18n.t("codeStatus.1112"), // 'Password did not match.'

  /** Username already used */
  1113: i18n.t("codeStatus.1113"), // 'the info for user already in use.'

  /** Invalid authority reference */
  1114: i18n.t("codeStatus.1114"), // 'user params error'
  1115: i18n.t("codeStatus.1115"), // 'failed to update user'
  1116: i18n.t("codeStatus.1116"), // 'the input email is not changed'
  1117: i18n.t("codeStatus.1117"), // 'the email is occupied by other'
  1118: i18n.t("codeStatus.1118"), // 'the input cell phone num is not changed'
  1119: i18n.t("codeStatus.1119"), // 'the cell phone num is occupied by other'
  1120: i18n.t("codeStatus.1120"), // 'invalid old password'
  1121: i18n.t("codeStatus.1121"), // 'invalid validate code'
  1122: i18n.t("codeStatus.1122"), // 'user is not exist'
  1123: i18n.t("codeStatus.1123"), // 'verification params error'
  1124: i18n.t("codeStatus.1124"), // 'the user is not granted for the obj'
  1125: i18n.t("codeStatus.1125"), // 'the key is occupied by other'
  1126: i18n.t("codeStatus.1126"), // 'the new password is invalid.'
  /** Authority name already used */
  1127: i18n.t("codeStatus.1127"), // 'Authority name already in use.'
  1128: i18n.t("codeStatus.1128"), // 'Authority failed'
  1129: i18n.t("codeStatus.1129"), // 'url not exist, invalid request'
  1130: i18n.t("codeStatus.1130"), // 'modification of kind is not allowed'

  /** No user logged in for action that requires authorization */
  1131: i18n.t("codeStatus.1131"), // 'failed to create user '
  1132: i18n.t("codeStatus.1132"), // 'user overdue'
  1133: i18n.t("codeStatus.1133"), // 'remove user error.'
  1134: i18n.t("codeStatus.1134"), // 'user have not been active'
  1135: i18n.t("codeStatus.1135"), // 'User is disabled'
  1136: i18n.t("codeStatus.1136"), // 'User is locked'
  1137: i18n.t("codeStatus.1137"), // 'You must provide credentials to perform this action.'

  1151: i18n.t("codeStatus.1151"), // 'BP not exist'
  1152: i18n.t("codeStatus.1152"), // 'create bp error, bp param have been occupied'
  1153: i18n.t("codeStatus.1153"), // 'bp name is invalid'
  1154: i18n.t("codeStatus.1154"), // 'bp license is invalid'
  1155: i18n.t("codeStatus.1155"), // 'bp user must exist bp id'
  1160: i18n.t("codeStatus.1160"), // 'User authentication is invalid'
  1161: i18n.t("codeStatus.1161"), // 'User authentication is invalid'
  1162: i18n.t("codeStatus.1162"), // 'bp user must exist bp id'
  1163: i18n.t("codeStatus.1163"), // 'bp user must exist bp name'
  1164: i18n.t("codeStatus.1164"), // 'authorities must exist'
  1165: i18n.t("codeStatus.1165"), // 'user kind must exist'
  1166: i18n.t("codeStatus.1166"), // 'deleting self is not support'
  1167: i18n.t("codeStatus.1167"), // 'BP master user exist'
  1168: i18n.t("codeStatus.1168"), // 'bp id incorrect'
  1170: i18n.t("codeStatus.1170"), // 'wx login validation exception'
  1171: i18n.t("codeStatus.1171"), // 'gen user name error'
  1172: i18n.t("codeStatus.1172"), // 'wx report incorrect user info'
  1180: i18n.t("codeStatus.1180"), // 'phone is not exist'
  1181: i18n.t("codeStatus.1181"), // 'email is not exist'
  1182: i18n.t("codeStatus.1182"), // 'The operation is temporarily locked due to multiple input errors, please try again later'
  1183: i18n.t("codeStatus.1183"), // 'invitation link invalid'
  1184: i18n.t("codeStatus.1184"), // UNSUPPORT_SELF_REGISTRATION
  3000: i18n.t("codeStatus.3000"), // 'illegal Operation'
  3001: i18n.t("codeStatus.3001"), // 'project name exist'
  3002: i18n.t("codeStatus.3002"), // 'project name not exist'
  3003: i18n.t("codeStatus.3003"), // 'parent project not exist'
  3004: i18n.t("codeStatus.3004"), // 'project not exist'
  3005: i18n.t("codeStatus.3005"), // 'create project failed'
  3006: i18n.t("codeStatus.3006"), // 'project has associated users, please delete all users'
  3007: i18n.t("codeStatus.3007"), // 'policy has associated groups, please delete all groups'
  3008: i18n.t("codeStatus.3008"), // 'policy has associated roles, please delete all roles'
  3011: i18n.t("codeStatus.3011"), // 'policy name exist'
  3012: i18n.t("codeStatus.3012"), // 'policy name not exist'
  3013: i18n.t("codeStatus.3013"), // 'policy not exist'
  3015: i18n.t("codeStatus.3015"), // 'policy has associated resources, please delete all versions'
  3016: i18n.t("codeStatus.3016"), // 'If it is not the last one, you need to set the default version before deleting the default version.'
  3017: i18n.t("codeStatus.3017"), // 'gen  name error'
  3018: i18n.t("codeStatus.3018"), // 'rego doc incorrect format'
  3021: i18n.t("codeStatus.3021"), // 'Missing authz target'
  3022: i18n.t("codeStatus.3022"), // 'some policy ids do not exist'
  3023: i18n.t("codeStatus.3023"), // 'some role ids do not exist'
  3024: i18n.t("codeStatus.3024"), // 'detach failed'
  3031: i18n.t("codeStatus.3031"), // 'service name exist'
  3032: i18n.t("codeStatus.3032"), // 'service iam code exist'
  3033: i18n.t("codeStatus.3033"), // 'service name not exist'
  3034: i18n.t("codeStatus.3034"), // 'service not exist'
  3041: i18n.t("codeStatus.3041"), // 'role name exist'
  3042: i18n.t("codeStatus.3042"), // 'role name not exist'
  3043: i18n.t("codeStatus.3043"), // 'role not exist'
  3044: i18n.t("codeStatus.3044"), // 'role does not allow action'
  3045: i18n.t("codeStatus.3045"), // 'role has associated policies or users, please delete first'
  /** **************
   * ACTION
   ***************/
  1200: i18n.t("codeStatus.1200"), // 'bad request'
  1201: i18n.t("codeStatus.1201"), // 'action is not supported'

  /** *******
   * Project*
   *********/
  1260: i18n.t("codeStatus.1260"), // 'Project not exist'
  1261: i18n.t("codeStatus.1261"), // 'project params error'

  /** *******
   * Dev*
   *********/
  1360: i18n.t("codeStatus.1360"), // 'dev not exist'
  1361: i18n.t("codeStatus.1361"), // 'dev can not login'
  1362: i18n.t("codeStatus.1362"), // 'dev have not been reg'
  1363: i18n.t("codeStatus.1363"), // 'dev have not been check passed'
  1364: i18n.t("codeStatus.1364"), // 'dev abnormal'
  1365: i18n.t("codeStatus.1365"), // 'dev reg token is empty'
  1366: i18n.t("codeStatus.1366"), // 'nodeId have been occupied'
  1367: i18n.t("codeStatus.1367"), // 'dev reg token is invalid'
  1368: i18n.t("codeStatus.1368"), // 'dev have been dropped'
  1369: i18n.t("codeStatus.1369"), // 'node not exist'
  1370: i18n.t("codeStatus.1370"), // 'permission denied'
  1371: i18n.t("codeStatus.1371"), // 'version info empty'
  1372: i18n.t("codeStatus.1372"), // 'node cannot be upgraded'
  1373: i18n.t("codeStatus.1373"), // 'node upgraded failed'
  1374: i18n.t("codeStatus.1374"), // 'cannot confirm'
  1375: i18n.t("codeStatus.1375"), // 'node upgrading'
  1376: i18n.t("codeStatus.1376"), // 'miss version info'
  1377: i18n.t("codeStatus.1377"), // 'dev info is empty'
  1378: i18n.t("codeStatus.1378"), // 'uuid have been occupied'

  /** *******
   * sms*
   *********/
  1501: i18n.t("codeStatus.1501"), // 'failed to send sms'
  1502: i18n.t("codeStatus.1502"), // 'failed to send sms'

  /** Region**/
  1551: i18n.t("codeStatus.1551"), // 'region has been removed'
  1552: i18n.t("codeStatus.1552"), // 'region not exist'
  1553: i18n.t("codeStatus.1553"), // 'region is empty'
  1554: i18n.t("codeStatus.1554"), // 'region in use'
  1555: i18n.t("codeStatus.1555"), // 'regionId have been occupied'
  1556: i18n.t("codeStatus.1556"), // 'no binding online gateway in the region'

  /** *
   * Site Error
   */
  1651: i18n.t("codeStatus.1651"), // 'site not exist'
  1652: i18n.t("codeStatus.1652"), // 'site has been removed'
  1653: i18n.t("codeStatus.1653"), // 'site in use'
  1654: i18n.t("codeStatus.1654"), // 'siteId have been occupied'

  /** *
   * cluster Error
   */
  1700: i18n.t("codeStatus.1700"), // 'cluster deploy plan is null'
  1701: i18n.t("codeStatus.1701"), // 'k8s version is null'
  1702: i18n.t("codeStatus.1702"), // 'k8s addon template index is null'
  1703: i18n.t("codeStatus.1703"), // 'k8s addon template is null'
  1704: i18n.t("codeStatus.1704"), // 'k8s joy kube engine param null'
  1705: i18n.t("codeStatus.1705"), // 'k8s key param is duplicate'
  1706: i18n.t("codeStatus.1706"), // 'cluster has been removed'
  1707: i18n.t("codeStatus.1707"), // 'log empty'
  1708: i18n.t("codeStatus.1708"), // 'port error'
  1709: i18n.t("codeStatus.1709"), // 'cert error'
  1710: i18n.t("codeStatus.1710"), // 'image not found'
  1711: i18n.t("codeStatus.1711"), // 'cert is empty'
  1712: i18n.t("codeStatus.1712"), // 'log has error info'
  1713: i18n.t("codeStatus.1713"), // 'unknown dns provider'
  1714: i18n.t("codeStatus.1714"), // 'unknown network plugin'
  1715: i18n.t("codeStatus.1715"), // 'k8s controller planes are empty'
  1716: i18n.t("codeStatus.1716"), // 'cluster has not been granted'
  1717: i18n.t("codeStatus.1717"), // 'build plan abormal'
  1718: i18n.t("codeStatus.1718"), // 'bp not match'
  1719: i18n.t("codeStatus.1719"), // 'cluster deploy failed'
  1720: i18n.t("codeStatus.1720"), // 'cluster k8s not exist'
  1721: i18n.t("codeStatus.1721"), // 'kubeconfig template format failed'
  1722: i18n.t("codeStatus.1722"), // 'cluster uDeploy failed'
  1730: i18n.t("codeStatus.1730"), // 'cluster k8s template name duplicated'
  1731: i18n.t("codeStatus.1731"), // ' cluster k8s template  not exist'
  1732: i18n.t("codeStatus.1732"), // 'There are associated resources'
  1733: i18n.t("codeStatus.1733"), // 'cluster k8s template versions duplicated'
  1734: i18n.t("codeStatus.1734"), // 'cluster k8s template name exist'
  1735: i18n.t("codeStatus.1735"), // ' cluster k8s template version not exist'
  1736: i18n.t("codeStatus.1736"), // ' cluster k8s template version exist'
  1737: i18n.t("codeStatus.1737"), // ' cluster k8s unsupported action'
  1738: i18n.t("codeStatus.1738"), // 'auth cluster failed'
  1780: i18n.t("codeStatus.1780"), // 'cluster server missing certificate'
  1781: i18n.t("codeStatus.1781"), // 'gen cluster server ca certs error'
  1782: i18n.t("codeStatus.1782"), // 'rpc auth cluster failed'
  1783: i18n.t("codeStatus.1783"), // 'cluster k8s certificate incorrect'
  1784: i18n.t("codeStatus.1784"), // ' cluster k8s template version disabled'
  1785: i18n.t("codeStatus.1785"), // 'cluster can not open terminal'
  1786: i18n.t("codeStatus.1786"), // 'cluster inactive'
  1787: i18n.t("codeStatus.1787"), // 'cluster worker offline'

  // --- k3s
  1801: i18n.t("codeStatus.1801"), // 'k3s version is null'
  1802: i18n.t("codeStatus.1802"), // 'k3s addon template index is null'
  1803: i18n.t("codeStatus.1803"), // 'k3s addon template is null'
  1804: i18n.t("codeStatus.1804"), // 'k3s joy kube engine param null'
  1805: i18n.t("codeStatus.1805"), // 'k3s key param is duplicate'
  1806: i18n.t("codeStatus.1806"), // 'cluster has been removed'
  1807: i18n.t("codeStatus.1807"), // 'log empty'
  1808: i18n.t("codeStatus.1808"), // 'port error'
  1809: i18n.t("codeStatus.1809"), // 'cert error'
  1810: i18n.t("codeStatus.1810"), // 'image not found'
  1811: i18n.t("codeStatus.1811"), // 'cert is empty'
  1812: i18n.t("codeStatus.1812"), // 'log has error info'
  1813: i18n.t("codeStatus.1813"), // 'unknown dns provider'
  1814: i18n.t("codeStatus.1814"), // 'unknown network plugin'
  1815: i18n.t("codeStatus.1815"), // 'k3s controller planes are empty'
  1816: i18n.t("codeStatus.1816"), // 'cluster has not been granted'
  1817: i18n.t("codeStatus.1817"), // 'build plan abormal'
  1818: i18n.t("codeStatus.1818"), // 'bp not match'
  1820: i18n.t("codeStatus.1820"), // 'cluster k3s not exist'
  1830: i18n.t("codeStatus.1830"), // 'cluster k3s template name duplicated'
  1831: i18n.t("codeStatus.1831"), // ' cluster k3s template  not exist'
  1832: i18n.t("codeStatus.1832"), // 'There are associated resources'
  1833: i18n.t("codeStatus.1833"), // 'cluster k3s template versions duplicated'
  1834: i18n.t("codeStatus.1834"), // 'cluster k3s template name exist'
  1835: i18n.t("codeStatus.1835"), // ' cluster k3s template version not exist'
  1836: i18n.t("codeStatus.1836"), // ' cluster k3s template version exist'
  1837: i18n.t("codeStatus.1837"), // ' cluster k3s unsupported action'
  1883: i18n.t("codeStatus.1883"), // 'cluster k3s certificate incorrect'
  1884: i18n.t("codeStatus.1884"), // ' cluster k3s template version disabled'
  1885: i18n.t("codeStatus.1885"), // 'cluster k3s can not open terminal'
  1886: i18n.t("codeStatus.1886"), // 'cluster k3s inactive'
  1887: i18n.t("codeStatus.1887"), // 'cluster k3s worker offline'
  1898: i18n.t("codeStatus.1898"), // 'cluster monitor failed'
  1899: i18n.t("codeStatus.1899"), // 'generate cluster import cmds failed'

  /**
   * CIS Error
   */
  2101: i18n.t("codeStatus.2101"), // 'inst has been dropped'
  2102: i18n.t("codeStatus.2102"), // 'image is empty'
  2120: i18n.t("codeStatus.2120"), // 'inst deploy status error'
  2121: i18n.t("codeStatus.2121"), // 'inst spec not exist'
  2122: i18n.t("codeStatus.2122"), // 'inst spec has been dropped'
  2123: i18n.t("codeStatus.2123"), // 'inst name duplicated'
  2124: i18n.t("codeStatus.2124"), // 'There are associated resources in the inst spec'
  2125: i18n.t("codeStatus.2125"), // 'error deleting inst spec'
  2126: i18n.t("codeStatus.2126"), // 'inst cannot remote execute'
  2130: i18n.t("codeStatus.2130"), // 'parse cfg error'
  2131: i18n.t("codeStatus.2131"), // 'config param error'
  2132: i18n.t("codeStatus.2132"), // 'config create error'
  /**
   * Aos
   */
  1901: i18n.t("codeStatus.1901"), // 'template not exist'
  1902: i18n.t("codeStatus.1902"), // 'template have been dropped'
  1903: i18n.t("codeStatus.1903"), // 'template name:version duplicated'
  1904: i18n.t("codeStatus.1904"), // 'template parse failed'
  1951: i18n.t("codeStatus.1951"), // 'stack  not exist'
  1952: i18n.t("codeStatus.1952"), // 'stack have been dropped'
  1953: i18n.t("codeStatus.1953"), // 'stack name duplicated'
  1954: i18n.t("codeStatus.1954"), // 'stack compose parse failed'

  2001: i18n.t("codeStatus.2001"), // 'stack service not exist'
  2002: i18n.t("codeStatus.2002"), // 'stack service have been dropped'
  2003: i18n.t("codeStatus.2003"), // 'stack status param error'
  2010: i18n.t("codeStatus.2010"), // 'template format failed'
  2011: i18n.t("codeStatus.2011"), // 'process batch rpt stack error'
  2012: i18n.t("codeStatus.2012"), // 'tty stack template not found'
  2013: i18n.t("codeStatus.2013"), // 'tty stack template duplicated'
  2014: i18n.t("codeStatus.2014"), // 'tty stack deploy status unknown'
  2015: i18n.t("codeStatus.2015"), // 'tty stack deploy status exception'
  2016: i18n.t("codeStatus.2016"), // 'name is already used by another user'
  2020: i18n.t("codeStatus.2020"), // 'repo name exist'
  2021: i18n.t("codeStatus.2021"), // 'repo name not exist'
  2022: i18n.t("codeStatus.2022"), // 'auth method not correct'
  2023: i18n.t("codeStatus.2023"), // 'helm stack name exist'
  2024: i18n.t("codeStatus.2024"), // 'helm chart install missing cluster info'
  2025: i18n.t("codeStatus.2025"), // 'helm app info not exist'
  2026: i18n.t("codeStatus.2026"), // 'helm stack not exist'
  2027: i18n.t("codeStatus.2027"), // 'helm repo access incorrect'
  2028: i18n.t("codeStatus.2028"), // 'repo not exist'
  2029: i18n.t("codeStatus.2029"), // 'helm repo access not allowed'
  2030: i18n.t("codeStatus.2030"), // 'parse chart yaml fail'
  2031: i18n.t("codeStatus.2031"), // 'helm stack action type error'
  2032: i18n.t("codeStatus.2032"), // 'lack cluster kubeConfig file'
  2033: i18n.t("codeStatus.2033"), // 'helm command execution error'
  2040: i18n.t("codeStatus.2040"), // 'template create failed'

  /**
   * ims
   */
  2200: i18n.t("codeStatus.2200"), // 'registry exist'
  2201: i18n.t("codeStatus.2201"), // 'registry  duplicated'
  2202: i18n.t("codeStatus.2202"), // 'registry  not exist'
  2203: i18n.t("codeStatus.2203"), // 'registry has been dropped'
  2204: i18n.t("codeStatus.2204"), // 'registry disabled'
  2205: i18n.t("codeStatus.2205"), // 'registry unavailable'
  2206: i18n.t("codeStatus.2206"), // 'There is no registry available'
  2250: i18n.t("codeStatus.2250"), // 'wrong region id'
  2251: i18n.t("codeStatus.2251"), // 'create registry region fail'
  2252: i18n.t("codeStatus.2252"), // 'incorrect password'
  2253: i18n.t("codeStatus.2253"), // 'failed to modify registry password'
  2254: i18n.t("codeStatus.2254"), // 'registry user already exists'
  2255: i18n.t("codeStatus.2255"), // 'registry user does not exist'
  2256: i18n.t("codeStatus.2256"), // 'add registry user fail'
  2257: i18n.t("codeStatus.2257"), // 'registry project already exists'
  2258: i18n.t("codeStatus.2258"), // 'failed to associate project and user'
  2259: i18n.t("codeStatus.2259"), // 'Each user can only create one registry user, and the current user has already created a registry user'
  2260: i18n.t("codeStatus.2260"), // 'failed to create registry project'
  2261: i18n.t("codeStatus.2261"), // 'The project already exists in the registry'
  2262: i18n.t("codeStatus.2262"), // 'registry project does not exist'
  2263: i18n.t("codeStatus.2263"), // 'registry user has been dropped'
  2264: i18n.t("codeStatus.2264"), // 'registry project has been dropped'
  2265: i18n.t("codeStatus.2265"), // 'registry repo does not exist'
  2266: i18n.t("codeStatus.2266"), // 'failed to modify registry repo'
  2267: i18n.t("codeStatus.2267"), // 'registry request http client error'
  2268: i18n.t("codeStatus.2268"), // 'registry repo tag does not exist'
  2269: i18n.t("codeStatus.2269"), // 'third party registry duplicated'
  2270: i18n.t("codeStatus.2270"), // 'third party registry  not exist'
  2271: i18n.t("codeStatus.2271"), // 'same name registry exist'
  2272: i18n.t("codeStatus.2272"), // 'third party registry has been dropped'
  2273: i18n.t("codeStatus.2273"), // 'failed to update pre download record'
  2274: i18n.t("codeStatus.2274"), // 'region registry not found'
  2275: i18n.t("codeStatus.2275"), // 'get repo list error'
  2276: i18n.t("codeStatus.2276"), // 'A user can only have one registry user'
  2277: i18n.t("codeStatus.2277"), // 'update registry user password fail'
  2278: i18n.t("codeStatus.2278"), // 'bp visible project need to have bpid'
  2280: i18n.t("codeStatus.2280"), // 'msg processing failed'
  2281: i18n.t("codeStatus.2281"), // 'registry command type error'
  2282: i18n.t("codeStatus.2282"), // 'There are associated resources in the registry'
  2283: i18n.t("codeStatus.2283"), // 'There are associated resources in the project'
  2290: i18n.t("codeStatus.2290"), // 'task execute exception'
  2291: i18n.t("codeStatus.2291"), // 'registry http request exception'
  2292: i18n.t("codeStatus.2292"), // 'incorrect user id'
  2293: i18n.t("codeStatus.2293"), // 'user registry password has not been set'

  /** *******
   * plugin*
   *********/
  2600: i18n.t("codeStatus.2600"), // 'no plugin for the software'
  2601: i18n.t("codeStatus.2601"), // 'plugin param error'

  /** **content ****/
  3300: i18n.t("codeStatus.3300"), // 'content not exist'
  3301: i18n.t("codeStatus.3301"), // 'get content list error'
  3302: i18n.t("codeStatus.3302"), // 'unsupport content type'

  /** ****count error*************/
  4800: i18n.t("codeStatus.4800"), // 'statistic error'

  /** ****cluster agent error*************/
  5001: i18n.t("codeStatus.5001"), // 'worker not exist'
  5002: i18n.t("codeStatus.5002"), // 'cluster not exist'
  5003: i18n.t("codeStatus.5003"), // 'cluster dropped'
  5004: i18n.t("codeStatus.5004"), // 'worker have not been check passed'
  5053: i18n.t("codeStatus.5053"), // 'worker reg token is invalid'

  /** ****service manager error*************/
  6001: i18n.t("codeStatus.6001"), // 'service gateway not exist'
  6002: i18n.t("codeStatus.6002"), // 'service port not exist'
  6003: i18n.t("codeStatus.6003"), // 'port ranges error'
  /** ****service gateway error*************/
  6101: i18n.t("codeStatus.6101"), // 'service gateway port conflict'

  8001: i18n.t("codeStatus.8001"), // 'submit msg error'

  /** *******
   * sys*
   *********/
  8800: i18n.t("codeStatus.8800"), // 'publish config error'
  8801: i18n.t("codeStatus.8801"), // 'get config error'
  8802: i18n.t("codeStatus.8802"), // 'config operation error'
  8803: i18n.t("codeStatus.8803"), // 'operation not allow'
  8804: i18n.t("codeStatus.8804"), // 'delete config error'
  8805: i18n.t("codeStatus.8805"), // 'over license define node num'
  8806: i18n.t("codeStatus.8806"), // 'license abnormal'
  8807: i18n.t("codeStatus.8807"), // 'license params error'

  8808: i18n.t("codeStatus.8808"), // 'license params error'

  8809: i18n.t("codeStatus.8809"), // 'over license define core num'

  /** *******
   * OTHER *
   *********/
  9000: i18n.t("codeStatus.9000"), // 'param error.'
  /** No user logged in for action that requires authorization */
  9999: i18n.t("codeStatus.9999"), // 'System error.'
  /** Error with no explanation */
  9998: i18n.t("codeStatus.9998"), // 'Unknown error.'
  9997: i18n.t("codeStatus.9997"), // 'redirect'
  9996: i18n.t("codeStatus.9996"), // 'Unknown service.'
  9100: i18n.t("codeStatus.9100"), // 'data exception'
  9200: i18n.t("codeStatus.9200"), // 'illegal argument'
  9201: i18n.t("codeStatus.9201"), // 'Parameter is not valid for operation'
  9300: i18n.t("codeStatus.9300"), // 'encode failed'
  9301: i18n.t("codeStatus.9301"), // 'decode failed'
  9302: i18n.t("codeStatus.9302"), // 'dump yaml file failed'
  9303: i18n.t("codeStatus.9303"), // 'load yaml file failed'
  9304: i18n.t("codeStatus.9304"), // 'data  duplicated'

  /** *******
   * cmp*
   *********/
  // compute
  12001: i18n.t("codeStatus.12001"), // 'cloud not exists'
  12002: i18n.t("codeStatus.12002"), // 'cloud agent dropped'
  12003: i18n.t("codeStatus.12003"), // 'prohibit update cloud'
  12004: i18n.t("codeStatus.12004"), // 'forbidden action'
  12005: i18n.t("codeStatus.12005"), // 'cloud is maintaining'
  12006: i18n.t("codeStatus.12006"), // 'cloud is off shelf'
  // compute
  12200: i18n.t("codeStatus.12200"), // 'baremetal device not exists'
  12208: i18n.t("codeStatus.12208"), // "pci device group does not exists"
  12209: i18n.t("codeStatus.12209"), // "pci device does not exists"
  12210: i18n.t("codeStatus.12210"), // "falvor is using"
  12211: i18n.t("codeStatus.12211"), // "vms are running on the node"
  12220: i18n.t("codeStatus.12220"), // 'baremetal instance existed'
  12221: i18n.t("codeStatus.12221"), // 'baremetal instance not exists'
  12222: i18n.t("codeStatus.12222"), // 'baremetal instance get network info error'
  12223: i18n.t("codeStatus.12223"), // 'pubkey not exists'
  12224: i18n.t("codeStatus.12224"), // 'baremtal instance is running with the baremetal_device_id'
  12225: i18n.t("codeStatus.12225"), // 'flavor not exists'
  12226: i18n.t("codeStatus.12226"), // 'vm instance not exists'
  12227: i18n.t("codeStatus.12227"), // 'vm snap not exists'
  12228: i18n.t("codeStatus.12228"), // 'hypervisor node not exists'
  12229: i18n.t("codeStatus.12229"), // 'remove the snaps first'
  12230: i18n.t("codeStatus.12230"), // "vm snap is switching,please wait"
  12231: i18n.t("codeStatus.12231"), // 'instance group is not empty'
  12232: i18n.t("codeStatus.12232"), // 'the groupId of the vm instance is not null'
  12233: i18n.t("codeStatus.12233"), // 'the groupId of the vm instance is not correct'
  12234: i18n.t("codeStatus.12234"), // 'instance group does not exist'
  12235: i18n.t("codeStatus.12235"), // 'instance group create failed'
  12241: i18n.t("codeStatus.12241"), // "vm instance is migrating,please wait"
  12242: i18n.t("codeStatus.12242"), // "vm snap is updating,please wait"
  12243: i18n.t("codeStatus.12243"), // "volume is updating,please wait"
  // network
  12300: i18n.t("codeStatus.12300"), // 'vpc not exist'
  12301: i18n.t("codeStatus.12301"), // 'subnet not exist'
  12302: i18n.t("codeStatus.12302"), // 'backend service error'
  12303: i18n.t("codeStatus.12303"), // 'remove the subnets first'
  12304: i18n.t("codeStatus.12304"), // 'remove the instances first'
  12305: i18n.t("codeStatus.12305"), // 'update database error'
  12306: i18n.t("codeStatus.12306"), // 'eip not exists'
  12307: i18n.t("codeStatus.12307"), // 'eipMap not exists'
  12308: i18n.t("codeStatus.12308"), // 'portMap not exists'
  12309: i18n.t("codeStatus.12309"), // 'eip not enough'
  12310: i18n.t("codeStatus.12310"), // 'eipMap already exists'
  12311: i18n.t("codeStatus.12311"), // 'cidr already exists'
  12312: i18n.t("codeStatus.12312"), // 'security group not exists'
  12313: i18n.t("codeStatus.12313"), // 'remove the rules first'
  12314: i18n.t("codeStatus.12314"), // 'security group rule not exists'
  12315: i18n.t("codeStatus.12315"), // "default security group can't remove"
  12316: i18n.t("codeStatus.12316"), // "default security group can't update"
  12317: i18n.t("codeStatus.12317"), // 'remove the nat gateway first'
  12318: i18n.t("codeStatus.12318"), // 'security group is used by other rules'
  12319: i18n.t("codeStatus.12319"), // 'eip pool not exists'
  12320: i18n.t("codeStatus.12320"), // "cidr overlap"
  12321: i18n.t("codeStatus.12321"), // "network address error"
  12322: i18n.t("codeStatus.12322"), // "detach the eip first"
  12323: i18n.t("codeStatus.12323"), // "eip is not attached"
  12324: i18n.t("codeStatus.12324"), // "port is not created"
  12400: i18n.t("codeStatus.12400"), // 'the relationship between the eip pool and the vpc not exists'
  12401: i18n.t("codeStatus.12401"), // 'the relationship between the eip pool and the vpc already exists'
  12402: i18n.t("codeStatus.12402"), // 'remove the eips first'
  12403: i18n.t("codeStatus.12403"), // "object is deleting,can't update it"
  12404: i18n.t("codeStatus.12404"), // "static ip is invalid"
  12405: i18n.t("codeStatus.12405"), // "ip address is occupied"
  12406: i18n.t("codeStatus.12406"), // 'detach the eip first'
  12407: i18n.t("codeStatus.12407"), // "nat gateway check error"
  12408: i18n.t("codeStatus.12408"), // "nat gateway is removing"
  12409: i18n.t("codeStatus.12409"), // "eip already attached"
  12410: i18n.t("codeStatus.12410"), // "security group is bound"
  12411: i18n.t("codeStatus.12411"), // "the instance has been bound to the security group"
  // image
  12500: i18n.t("codeStatus.12500"), // 'image does not exists'
  12501: i18n.t("codeStatus.12501"), // 'image is using'
  12502: i18n.t("codeStatus.12502"), // 'share does not exists'
  12503: i18n.t("codeStatus.12503"), // 'default image,cannot be deleted'
  12504: i18n.t("codeStatus.12504"), // 'image name already exists'
  /**
   * volume
   */
  12700: i18n.t("codeStatus.12700"), // 'volume does not exists'
  12701: i18n.t("codeStatus.12701"), // 'volume is using'
  12702: i18n.t("codeStatus.12702"), // 'volume snap does not exists'
  12703: i18n.t("codeStatus.12703"), // 'volume is not detached'
  12704: i18n.t("codeStatus.12704"), // 'volume is not attached'
  12721: i18n.t("codeStatus.12721"), // 'volume has never been used before'
  12705: i18n.t("codeStatus.12705"), // 'vm instance should be shut down'
  12907: i18n.t("codeStatus.12907"), // 'vm instance is not powered on'
  12908: i18n.t("codeStatus.12908"), // 'vm instance is not powered off'
  12706: i18n.t("codeStatus.12706"), // "vm instance has vmSnaps,can't attach or detach more volumes"
  12720: i18n.t("codeStatus.12720"), // 'storage pool does not exists'
  12707: i18n.t("codeStatus.12707"), // 'remove the volumeSnaps first'
  12708: i18n.t("codeStatus.12708"), // 'volume is already attached'
  12709: i18n.t("codeStatus.12709"), // 'volume count exceed'
  12710: i18n.t("codeStatus.12710"), // 'volume size too large'
  12711: i18n.t("codeStatus.12711"), // 'volume attached too many'
  /**
   * openstack
   */
  12801: i18n.t("codeStatus.12801"), // 'volume backup does not exists'
  12802: i18n.t("codeStatus.12802"), // 'floating ip not exists'
  12803: i18n.t("codeStatus.12803"), // 'port forwarding not exists'
  12804: i18n.t("codeStatus.12804"), // 'key pair not exists'
  12805: i18n.t("codeStatus.12805"), // 'network not exist'
  12806: i18n.t("codeStatus.12806"), // 'port not exist'
  12807: i18n.t("codeStatus.12807"), // 'router not exist'
  12808: i18n.t("codeStatus.12808"), // 'instance not exist'
  /**
   * easystack
   */
  12811: i18n.t("codeStatus.12811"), // 'firewall does not exists'
  12812: i18n.t("codeStatus.12812"), // 'firewall policy does not exists'
  12813: i18n.t("codeStatus.12813"), // 'firewall rule does not exists'

  /**
   *
   */
  12903: i18n.t("codeStatus.12903"), // "vm status error"
  12904: i18n.t("codeStatus.12904"), // "vm instance cannot be modified"
  12911: i18n.t("codeStatus.12911"), // "vm with pci device should be power off"
  12900: i18n.t("codeStatus.12900"), // "pci device is not detached"
  12901: i18n.t("codeStatus.12901"), // "pci device is not attached"
  12902: i18n.t("codeStatus.12902"), // "pci device group already attached by the other vm"
  12906: i18n.t("codeStatus.12906"), // "node resource not enough"
  12909: i18n.t("codeStatus.12909"), // "the size of root disk is not same with the flavor"
  12910: i18n.t("codeStatus.12910"), // "the name of gpu is not same with the flavor"
  12950: i18n.t("codeStatus.12950"), // "backend not exist"
  12951: i18n.t("codeStatus.12951"), // "frontend not exist"
  12952: i18n.t("codeStatus.12952"), // "loadbalancer not exist"
  12905: i18n.t("codeStatus.12905"), // "please retry"
  12712: i18n.t("codeStatus.12712"), // "volume is exporting,please wait"
  /**
   * cmp
   */
  13001: i18n.t("codeStatus.13001"), // 'no available eip'
  13002: i18n.t("codeStatus.13002"), // 'expire time conflict'
  13003: i18n.t("codeStatus.13003"), // 'resource is creating,please wait'

  /** *******
   * bill*
   *********/
  24001: i18n.t("codeStatus.24001"), // 'bill parent category not exist'
  24002: i18n.t("codeStatus.24002"), // 'category not exist'
  24003: i18n.t("codeStatus.24003"), // 'product not exist'
  24004: i18n.t("codeStatus.24004"), // 'discount not exist'
  24005: i18n.t("codeStatus.24005"), // 'bp account not exist'
  24006: i18n.t("codeStatus.24006"), // 'category that does not belong to the specified cloud'
  24007: i18n.t("codeStatus.24007"), // 'product not yet priced'
  24008: i18n.t("codeStatus.24008"), // 'Discount value needs to be between 0 and 1 when Discount Type is Percentage'
  24009: i18n.t("codeStatus.24009"), // 'the correct cloud id and specification id are required'
  24010: i18n.t("codeStatus.24010"), // 'cloud does not exist'
  24011: i18n.t("codeStatus.24011"), // 'bp account overdue'
  24012: i18n.t("codeStatus.24012"), // 'category  exist'
  24013: i18n.t("codeStatus.24013"), // 'Period and period Unit parameters are required for prepaid'
  24014: i18n.t("codeStatus.24014"), // 'recharge amount cannot be less than 0'
  24015: i18n.t("codeStatus.24015"), // 'delete product first'
  24016: i18n.t("codeStatus.24016"), // 'the product is still under billing and cannot be deleted.'
  24017: i18n.t("codeStatus.24017"), // 'the product is still under billing and cannot be update.'
  24018: i18n.t("codeStatus.24018"), // 'User account balance is insufficient, please charge'
  24019: i18n.t("codeStatus.24019"), // 'Unknown charge type,optional values are: prepaid, postpaid'
  24020: i18n.t("codeStatus.24020"), // 'the billing instance does not exist'
  24021: i18n.t("codeStatus.24021"), // 'category name already exists'
  24022: i18n.t("codeStatus.24022"), // 'product disk params error'
  24023: i18n.t("codeStatus.24023"), // 'order not exist'
  24024: i18n.t("codeStatus.24024"), // 'order item not exist'
  24025: i18n.t("codeStatus.24025"), // 'instance parameter cannot be empty'
  24026: i18n.t("codeStatus.24026"), // 'bill order action type error'
  24027: i18n.t("codeStatus.24027"), // 'spec parameter cannot be empty'
  24028: i18n.t("codeStatus.24028"), // 'product info incorrect'
  24044: i18n.t("codeStatus.24044"), // 'order expired'
  24045: i18n.t("codeStatus.24045"), // 'public remit code gen error'
  24047: i18n.t("codeStatus.24047"), // 'public remit code not exist'
  24048: i18n.t("codeStatus.24048"), // 'public remit code already charged'

  /** *******
   * omc *
   *********/
  25010: i18n.t("codeStatus.25010"), // 'alarm add failed'
  25011: i18n.t("codeStatus.25011"), // 'alarm delete failed'
  25020: i18n.t("codeStatus.25020"), // 'receiver not exist'
  25021: i18n.t("codeStatus.25021"), // 'rule cannot not empty'
  25022: i18n.t("codeStatus.25022"), // 'alarm not exist'
  25050: i18n.t("codeStatus.25050"), // 'prometheus reload error'
  25051: i18n.t("codeStatus.25051"), // 'prometheus reload failed'
  25052: i18n.t("codeStatus.25052"), // 'rule add failed'
  25053: i18n.t("codeStatus.25053"), // 'prometheus config update failed'

  // 运营中心
  26001: i18n.t("codeStatus.26001"), // oss settle rule not exist
  26003: i18n.t("codeStatus.26003"), // provider not exist
  26004: i18n.t("codeStatus.26004"), // iam user already used
  26005: i18n.t("codeStatus.26005"), // as resource not exist
  26006: i18n.t("codeStatus.26006"), // provider account not exist
  26012: i18n.t("codeStatus.26012"), // a certain accounting period already settled
  26013: i18n.t("codeStatus.26013"), // oss settle not exist
  26014: i18n.t("codeStatus.26014"), // already settled, cannot update
  26015: i18n.t("codeStatus.26015"), // settle rule status error
  26016: i18n.t("codeStatus.26016"), // a settle rule has been set for the resource pool
  26017: i18n.t("codeStatus.26017"), // the resource pool already settled not allowed update and delete
  26018: i18n.t("codeStatus.26018"), // provider disabled settle not allowed
  26019: i18n.t("codeStatus.26019"), // a settle rule has been set for the provider account
  // custom
  99999: i18n.t("codeStatus.99999"), // 'error'
};
