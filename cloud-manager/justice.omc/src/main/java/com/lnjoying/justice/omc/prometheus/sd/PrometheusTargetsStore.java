package com.lnjoying.justice.omc.prometheus.sd;

import com.lnjoying.justice.commonweb.util.JacksonUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/3 19:47
 */

public class PrometheusTargetsStore
{
    /**
     * label name  is a regular expression matching valid label names "^[a-zA-Z_][a-zA-Z0-9_]*$"
     */
    public static String TARGET_LABEL_DEFAULT_UID = "user_id";
    
    public static String TARGET_LABEL_DEFAULT_BP_ID = "bp_id";

    public static String TARGET_LABEL_DEFAULT_SITE_ID = "site_id";

    public static String TARGET_LABEL_DEFAULT_NODE_ID = "node_id";


    /**
     * bpId --> userId ---> targets
     */
    private Map<String, Map<String, List<TargetsData>>> targetsCache = new ConcurrentHashMap<>();

    private Map<String, TargetsData>  uniqueKeyMap = new ConcurrentHashMap<>();


    public void addTargetsData(String bpId, String userId, TargetsData targetsData) {
        if (!StringUtils.hasText(bpId))
        {
            bpId = "default";
        }
        targetsCache.computeIfAbsent(bpId, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>())
                .add(targetsData);
    }


    public boolean addTarget(String bpId, String userId, String siteId, String nodeId, String target, String uniqueKey) {
        if (!StringUtils.hasText(bpId))
        {
            bpId = "default";
        }
        List<TargetsData> targetsDataList = targetsCache.computeIfAbsent(bpId, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>());

        Map<String, String> newLabels = new HashMap<>();
        newLabels.put(TARGET_LABEL_DEFAULT_UID, userId);
        newLabels.put(TARGET_LABEL_DEFAULT_BP_ID, bpId);
        newLabels.put(TARGET_LABEL_DEFAULT_SITE_ID, siteId);
        newLabels.put(TARGET_LABEL_DEFAULT_NODE_ID, nodeId);

        TargetsData existingTargetsData = findTargetsDataWithLabels(targetsDataList, newLabels);
        TargetsData targetsData = new TargetsData();
        if (Objects.nonNull(existingTargetsData))
        {
            existingTargetsData.addTarget(target);
            targetsData.getLabels().putAll(existingTargetsData.getLabels());
            targetsData.addTarget(target);
        }
        else
        {
            targetsData.addTarget(target);
            targetsData.addLabels(newLabels);
            targetsDataList.add(targetsData);
        }

        uniqueKeyMap.put(uniqueKey, targetsData);
        return true;
    }

    public boolean deleteTarget(String uniqueKey) {
        TargetsData targetsData = uniqueKeyMap.get(uniqueKey);
        String bpId = targetsData.getLabels().get(TARGET_LABEL_DEFAULT_BP_ID);
        String userId = targetsData.getLabels().get(TARGET_LABEL_DEFAULT_UID);
        String target = targetsData.getTargets().get(0);
        if (!StringUtils.hasText(bpId))
        {
            bpId = "default";
        }
        List<TargetsData> targetsDataList = targetsCache.computeIfAbsent(bpId, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>());

        if (!CollectionUtils.isEmpty(targetsDataList))
        {
            targetsDataList.stream().forEach(td ->
            {
                List<String> targets = td.getTargets();
                if (!CollectionUtils.isEmpty(targets))
                {
                    if (targets.size() == 1)
                    {
                        targetsDataList.removeIf(x -> x.equals(td));
                    }
                    else
                    {
                        targets.removeIf(t -> t.equals(target));
                    }

                }
            });
        }
        return true;
    }

    public boolean deleteTarget(String bpId, String userId, String target) {
        if (!StringUtils.hasText(bpId))
        {
            bpId = "default";
        }
        List<TargetsData> targetsDataList = targetsCache.computeIfAbsent(bpId, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>());

        if (!CollectionUtils.isEmpty(targetsDataList))
        {
            targetsDataList.stream().forEach(targetsData ->
            {
                List<String> targets = targetsData.getTargets();
                if (!CollectionUtils.isEmpty(targets))
                {
                    targets.removeIf(t -> t.equals(target));
                }
            });
        }
        return true;
    }


    public List<TargetsData> getTargetsData(String bpId, String userId) {
        return targetsCache.getOrDefault(bpId, new ConcurrentHashMap<>())
                .getOrDefault(userId, new ArrayList<>());
    }

    public List<TargetsData> getAllTargetsData() {
        return targetsCache.values().stream()
                .flatMap(targets -> targets.values().stream().flatMap(List::stream))
                .collect(Collectors.toList());
    }

    public static  List<TargetsData> newTargetsDate(String bpId, String userId, String siteId, String nodeId, String target, String uniqueKey)
    {
        List<TargetsData> targetsDataList = new ArrayList<>();
        TargetsData targetsData = new TargetsData();
        Map<String, String> newLabels = new HashMap<>();
        newLabels.put(TARGET_LABEL_DEFAULT_UID, userId);
        newLabels.put(TARGET_LABEL_DEFAULT_BP_ID, bpId);
        newLabels.put(TARGET_LABEL_DEFAULT_SITE_ID, siteId);
        newLabels.put(TARGET_LABEL_DEFAULT_NODE_ID, nodeId);
        targetsData.addTarget(target);
        targetsData.addLabels(newLabels);
        targetsDataList.add(targetsData);
        return targetsDataList;
    }

    public static String getTargetsDataContent(String bpId, String userId, String siteId, String nodeId, String target, String uniqueKey)
    {
        List<TargetsData> targetsData = newTargetsDate(bpId, userId, siteId, nodeId, target, uniqueKey);
        return JacksonUtils.objToStrDefault(targetsData);
    }

    private TargetsData findTargetsDataWithLabels(List<TargetsData> targetsDataList, Map<String, String> labels) {
        return targetsDataList.stream()
                .filter(targetsData -> Objects.equals(targetsData.getLabels(), labels))
                .findFirst()
                .orElse(null);
    }


}
