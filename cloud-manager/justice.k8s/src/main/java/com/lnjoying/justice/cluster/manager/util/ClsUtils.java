package com.lnjoying.justice.cluster.manager.util;

//import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.UserBasicInfo;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.commonweb.util.UserUtil;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import sun.net.util.IPAddressUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Author: Regulus
 * @Date: 11/17/21 2:37 PM
 * @Description: cluster utils
 */
public class ClsUtils
{
    private static Logger LOGGER = LogManager.getLogger();
    
    public static Date rollYear(Date d, int year)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }
    
    public static boolean isIpAdress(String ipAddress)
    {
        if (IPAddressUtil.isIPv4LiteralAddress(ipAddress)
                || IPAddressUtil.isIPv6LiteralAddress(ipAddress))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static String getMD5(String need2Encode) throws NoSuchAlgorithmException
    {
        byte[] buf = need2Encode.getBytes();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(buf);
        byte[] tmp = md5.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : tmp)
        {
            if (b >= 0 && b < 16)
            {
                sb.append("0");
            }
            sb.append(Integer.toHexString(b & 0xff));
        }
        return sb.toString();
    }
    
    public static String getStringChecksum(String config) throws NoSuchAlgorithmException
    {
        return getMD5(config);
    }
    
    static String HealthzAddress   = "localhost";
    static String HealthzEndpoint  = "/healthz";
    static String HTTPProtoPrefix  = "http://";
    static String HTTPSProtoPrefix = "https://";
    
    public static String getHealthCheckURL(Boolean useTls, int port)
    {
        if (useTls)
        {
            return String.format("%s%s:%d%s", HTTPSProtoPrefix, HealthzAddress, port, HealthzEndpoint);
        }
        
        return String.format("%s%s:%d%s", HTTPProtoPrefix, HealthzAddress, port, HealthzEndpoint);
    }
    
    public static int versionCompare(String version1, String version2)
    {
        String[] versionArr1 = version1.split("\\.");
        String[] versionArr2 = version2.split("\\.");
    
        int minLen = Math.min(versionArr1.length, versionArr2.length);
        int diff = 0;
    
        for (int i = 0; i < minLen; i++)
        {
            String v1 = versionArr1[i];
            String v2 = versionArr2[i];
            diff = v1.length() - v2.length();
            if (diff == 0)
            {
                diff = v1.compareTo(v2);
            }
            if (diff != 0)
            {
                break;
            }
        }
        
        diff = (diff != 0) ? diff : (versionArr1.length - versionArr2.length);
        return diff;
    }
    
    public static String getBase64(String str) throws UnsupportedEncodingException
    {
        return Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
    }
    
    public static String getBase64(byte [] data) throws UnsupportedEncodingException
    {
        return Base64.getEncoder().encodeToString(data);
    }
    
    public static String getTagMajorVersion(String tag)
    {
        String [] splitTag = tag.split(".");
        if (splitTag.length < 2)
        {
            return "";
        }
        
        return StringUtils.join(Arrays.copyOfRange(splitTag,0,2),".");
    }
    
    /**
     * @description if ${log} contain ${judgePattern} ${matchnum}, then return true
     * @author Regulus
     * @date 11/26/21 9:23 PM
     * @param log
     * @param judgePattern
     * @param matchNum 
     * @return boolean
     */
    public static boolean checkLogResult(String log, String judgePattern, int matchNum)
    {
        int ind = 0;
        int cnt = 0;
        while (true)
        {
            int pos = log.indexOf(judgePattern, ind);
            if (pos < 0)
            {
                break;
            }
            cnt++;
            ind = pos + 1;
        }
        
        return cnt >= matchNum;
    }
    
    public static Map<String, Object> objectToMap(Object obj)
    {
        if (obj == null)
        {
            return null;
        }
        
        Map<String, Object> map = new HashMap<>();
        try
        {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields)
            {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        }
        catch (Exception e) {
        
        }
        
        return map;
    }
    
    public static List<Object> listObjToMap(List<?> listObj)
    {
        if (CollectionUtils.isEmpty(listObj))
        {
            return null;
        }
        
        List<Object> listMapObj = new ArrayList<>();
        
        for (Object object : listObj)
        {
            Map<String, Object> map = objectToMap(object);
            listMapObj.add(map);
        }
        
        
        return listMapObj;
    }
    
    public static void setDefaultIfEmpty(String varName, String defaultValue)
    {
        if (varName == null)
        {
            varName = new String();
        }
        
        if (varName.isEmpty())
        {
            varName = defaultValue;
        }
    }

    static Yaml yaml = new Yaml();
    public static String toJson(Object object)
    {
        if (object == null)
        {
            return null;
        }
        
        return JsonUtils.toJson(object);
    }
    
    public static String toJson(List<?> object)
    {
        if (object == null || object.isEmpty())
        {
            return null;
        }
        
        return JsonUtils.toJson(object);
    }
    
    public static String toJson(Map<?,?> object)
    {
        if (object == null || object.isEmpty())
        {
            return null;
        }
        
        return JsonUtils.toJson(object);
    }
    
    public static boolean isClusterUser(String authorities)
    {
        if (StringUtils.isEmpty(authorities))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        
        return true;
    }

    public static Pair<String, String> getOperUserInfo(String userId, String bpId, String userKind) throws WebSystemException
    {
        if (UserUtil.isAdmin(userKind))
        {
            return new Pair<>(null, null);
        }
        else if (UserUtil.isBpAdmin(userKind))
        {
            return new Pair<>(null, bpId);
        }
        else if (UserUtil.isPersonal(userKind))
        {
            return new Pair<>(userId, null);
        }
        else if (UserUtil.isBpSubUser(userKind))
        {
            return new Pair<>(userId, bpId);
        }
        else
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_NOT_GRANTED, ErrorLevel.INFO);
        }
    }
    
    public static String toYamlStr(Object obj) throws JsonProcessingException
    {
        String jsonStr = JsonUtils.toJson(obj);
        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonStr);
        String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);
        Map<String,Object> map = (Map<String, Object>) yaml.load(jsonAsYaml);
        return yaml.dump(map);
    }
    
    public static boolean checkUserGrantForCluster(UserBasicInfo userBasicInfo, TblClusterInfo tblClusterInfo)
    {
        if (CollectionUtils.hasContent(userBasicInfo.getUserId()) && ! UserUtil.isAdmin(userBasicInfo.getUserKind()))
        {
//            if (! userBasicInfo.getUserId().equals(tblClusterInfo.getOwner()) && userBasicInfo.getUserId().equals(tblClusterInfo.getCreator()))
           //cretator admin privileges may be revoked
            if (! userBasicInfo.getUserId().equals(tblClusterInfo.getOwner()))
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_NOT_GRANTED, ErrorLevel.INFO);
            }
        }
    
        if (CollectionUtils.hasContent(userBasicInfo.getBpId()))
        {
            if (! userBasicInfo.getBpId().equals(tblClusterInfo.getBp()))
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_NOT_GRANTED, ErrorLevel.INFO);
            
            }
        }
        
        return true;
    }
    
    public static boolean checkUserGrantForCluster(UserBasicInfo userBasicInfo, ClusterInnerInfo clusterInnerInfo)
    {
        if (CollectionUtils.hasContent(userBasicInfo.getUserId()) && ! UserUtil.isAdmin(userBasicInfo.getUserKind()))
        {
            if (! userBasicInfo.getUserId().equals(clusterInnerInfo.getOwner()))
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_NOT_GRANTED, ErrorLevel.INFO);
            }
            else
            {
                return true;
            }
        }
        
        if (CollectionUtils.hasContent(userBasicInfo.getBpId()))
        {
            if (! userBasicInfo.getBpId().equals(clusterInnerInfo.getBp()))
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_NOT_GRANTED, ErrorLevel.INFO);
                
            }
        }
        
        return true;
    }
    
    public static void writeFile(String addonCfg, String path, String fileName)
    {
        String dst = Utils.buildStr(path, "/", fileName);
        File fileDir = new File(path);
        File file =new File(dst);
        
        try
        {
            if (! fileDir.exists())
            {
                fileDir.mkdirs();
            }
            
            if (file.exists())
            {
                file.delete();
            }
            
            FileWriter fileWritter = new FileWriter(file);
            fileWritter.write(addonCfg);
            fileWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String featureToString(Map<String, Boolean> features)
    {
        if (CollectionUtils.isEmpty(features))
        {
            return null;
        }
        StringBuilder featuresStrBuilder = new StringBuilder();
        for(Map.Entry<String, Boolean> entry : features.entrySet())
        {
            if (featuresStrBuilder.length() > 0)
            {
                featuresStrBuilder.append(',');
            }
            featuresStrBuilder.append(entry.getKey());
            featuresStrBuilder.append('=');
            featuresStrBuilder.append(entry.getValue());
        }
        return featuresStrBuilder.toString();
    }
    
    public static String readKey()
    {
        String baseConfigPath = System.getProperty("lj_config");
        String filePath = Utils.buildStr(baseConfigPath, "/ssl");
        String sslkey = Utils.buildStr(filePath, "/ssl.key");
        File fileKey = new File(sslkey);
        if (! fileKey.exists() || ! fileKey.isFile())
        {
            LOGGER.info("{} is not exist", sslkey);
            return "";
        }
    
        try
        {
            FileInputStream fs = new FileInputStream(fileKey);
            String key = IOUtils.toString(fs, StandardCharsets.UTF_8);
            
            return key;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    
        LOGGER.info("server ssl files are exist");
        return "";
    }
    
    public static String readCert()
    {
        String baseConfigPath = System.getProperty("lj_config");
        String filePath = Utils.buildStr(baseConfigPath, "/ssl");
        String sslcertpem = Utils.buildStr(filePath, "/ssl.pem");
        File fileCert = new File(sslcertpem);
        if (! fileCert.exists() || ! fileCert.isFile())
        {
            LOGGER.info("{} is not exist", sslcertpem);
            return "";
        }
    
        try
        {
            FileInputStream fs = new FileInputStream(fileCert);
            return IOUtils.toString(fs, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        LOGGER.info("server ssl files are exist");
        return "";
    }
    
    public static boolean needGenCert()
    {
        String baseConfigPath = System.getProperty("lj_config");
        String filePath = Utils.buildStr(baseConfigPath, "/ssl");
        String sslcertpem = Utils.buildStr(filePath, "/ssl.pem");
        File fileCert = new File(sslcertpem);
        if (! fileCert.exists() || ! fileCert.isFile())
        {
            LOGGER.info("{} is not exist", sslcertpem);
            return true;
        }
        
        String sslkey = Utils.buildStr(filePath, "/ssl.key");
        File fileKey = new File(sslkey);
        if (! fileKey.exists() || ! fileKey.isFile())
        {
            LOGGER.info("{} is not exist", sslkey);
            return true;
        }
        
        LOGGER.info("server ssl files are exist");
        return false;
    }
}
