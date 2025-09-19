package com.lnjoying.justice.aos.helm.model;

import com.lnjoying.justice.commonweb.util.YamlParserUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/6 20:54
 */
@SuppressWarnings("unchecked")
@Data
public class IndexFile
{
    private String apiVersion;

    private String generated;

    private Map<String, String> annotations;

    private List<String> publicKeys;

    private  SortedMap<String, SortedSet<ChartVersion>> entries;

    public IndexFile()
    {
        this(null);
    }

    public IndexFile(final SortedMap<String, SortedSet<ChartVersion>> entries)
    {
        if (entries == null || entries.isEmpty()) {
            this.entries = Collections.emptySortedMap();
        } else {
            this.entries = Collections.unmodifiableSortedMap(entries);
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartVersion implements Comparable<ChartVersion>
    {
        /**
         * The name of the chart. Required.
         */
        private String name;

        /**
         * The URL to a relevant project page, git repo, or contact person
         */
        private String home;

        /**
         * Source is the URL to the source code of this chart
         */
        private List<String> sources;

        /**
         * A SemVer 2 conformant version string of the chart. Required.
         */
        private String version;

        /**
         * A one-sentence description of the chart
         */
        private String description;

        /**
         * A list of string keywords
         */
        private List<String> keywords = new ArrayList<>();

        /**
         * The URL to an icon file
         */
        private String icon;

        /**
         * The API Version of this chart. Required.
         */
        private String apiVersion;

        /**
         * The condition to check to enable chart
         */
        private String condition;

        /**
         * The API Version of this chart. Required.
         */
        private String appVersion;

        /**
         * Whether or not this chart is deprecated
         */
        private boolean deprecated;

        /**
         * Annotations are additional mappings uninterpreted by Helm,
         * made available for inspection by other applications.
         */
        private Map<String, String> annotations;

        private List<String> urls;

        private String created;

        private String removed;

        private String digest;

        private String engine;

        private List<Maintainer> maintainers;

        private List<Dependency> dependencies;

        private String type;

        private String kubeVersion;

        @Override
        public int compareTo(ChartVersion that)
        {

            if (this.getName() == null && that.getName() == null)
            {
                return 0;
            }
            else if (this.getName() == null)
            {
                return -1;
            }
            else if (that.getName() == null)
            {
                return 1;
            }
            else
            {
                int nameComparison = this.getName().compareTo(that.getName());
                if (nameComparison != 0)
                {
                    return nameComparison < 0 ? -1 : 1;
                }
            }

            if (this.getVersion() == null && that.getVersion() == null)
            {
                return 0;
            }
            else if (this.getVersion() == null)
            {
                return -1;
            }
            else if (that.getVersion() == null)
            {
                return 1;
            }
            else
            {
                int versionComparison = this.getVersion().compareTo(that.getVersion());
                if (versionComparison != 0)
                {
                    return versionComparison < 0 ? -1 : 1;
                }
            }

            return 0;
        }

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Maintainer
    {

        private String name;

        private String email;

        private String url;

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Dependency
    {

        private String name;

        private String version;

        private String repository;

        private String condition;

        private List<String> tags;

        private boolean enabled;

        private Object importValues;

        private String alias;

    }

    public static final IndexFile loadFrom(String path)
    {
        final IndexFile res;
        Map<?, ?> yamlMap = YamlParserUtil.readYamlConfig(path, Map.class);

        if (CollectionUtils.isEmpty(yamlMap))
        {
            res = new IndexFile(null);
        }
        else {
            final SortedMap<String, SortedSet<ChartVersion>> sortedEntryMap = new TreeMap<>();
            final Map<String, ? extends Collection<? extends Map<String, ?>>> entriesMap = (Map<String, ? extends Collection<? extends Map<String, ?>>>) yamlMap.get("entries");
            if (!CollectionUtils.isEmpty(entriesMap))
            {
                entriesMap.entrySet().forEach(entry->{
                    String entryName = entry.getKey();
                    if (StringUtils.isNotBlank(entryName))
                    {
                        Collection<? extends Map<?, ?>> entryContents = entry.getValue();
                        if (!CollectionUtils.isEmpty(entryContents))
                        {
                            List<ChartVersion> entryObjects = entryContents.stream().map(contents ->
                            {
                                return assembleChartVersion(contents);
                            }).collect(Collectors.toList());

                            sortedEntryMap.computeIfAbsent(entryName, value->{
                                return new TreeSet<ChartVersion>();
                            }).addAll(entryObjects);
                        }
                    }
                });
            }
            res = new IndexFile(sortedEntryMap);
            res.setApiVersion((String)yamlMap.get("apiVersion"));
            Object generated = yamlMap.get("generated");
            res.setGenerated(Objects.nonNull(generated) ? generated.toString() : "");
        }

        return res;
    }

    private static ChartVersion assembleChartVersion( Map<?, ?> contents)
    {
        ChartVersion.ChartVersionBuilder builder = ChartVersion.builder();

        String apiVersion = (String) contents.get("apiVersion");
        if (StringUtils.isNotBlank(apiVersion))
        {
            builder.apiVersion(apiVersion);
        }

        Object appVersion = contents.get("appVersion");
        if (appVersion instanceof Date)
        {
            appVersion = (Date) appVersion;
            builder.appVersion(appVersion.toString());
        }
        else if (appVersion instanceof String)
        {
            builder.appVersion((String) appVersion);
        }

        Object created = contents.get("created");
        if (created instanceof String)
        {
            builder.created((String)created);
        }
        else if (created instanceof Date)
        {
            builder.created(created.toString());
        }


        String description = (String) contents.get("description");
        builder.description(description);

        String digest = (String) contents.get("digest");
        builder.digest(digest);

        String engine = (String) contents.get("engine");
        builder.engine(engine);

        String icon = (String) contents.get("icon");
        builder.icon(icon);

        Object keywords = contents.get("keywords");
        if (keywords instanceof List)
        {
            builder.keywords((List) keywords);
        }

        String name = (String) contents.get("name");
        builder.name(name);

        Object sources = contents.get("sources");
        if (sources instanceof List)
        {
            builder.sources((List) sources);
        }

        Object urls = contents.get("urls");
        if (urls instanceof List)
        {
            builder.urls((List) urls);
        }

        String version = (String) contents.get("version");
        builder.version(version);

        String kubeVersion = (String) contents.get("kubeVersion");
        builder.kubeVersion(kubeVersion);

        String type = (String) contents.get("type");
        builder.type(type);

        Object maintainers = contents.get("maintainers");

        List<Maintainer> outMaintainers = new ArrayList<>();
        if (maintainers instanceof List)
        {
            maintainers = (List) maintainers;
            ((List<?>) maintainers).stream().forEach(maintainer ->
            {
                if (maintainer instanceof LinkedHashMap)
                {
                    LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) maintainer;
                    if (!CollectionUtils.isEmpty(map))
                    {
                        Maintainer build = Maintainer.builder().email(map.get("email"))
                                .url(map.get("url")).name(map.get("name")).build();
                        outMaintainers.add(build);
                    }
                }
            });
        }
        builder.maintainers(outMaintainers);

        Object dependencies = contents.get("dependencies");

        List<Dependency> outdependencies = new ArrayList<>();
        if (dependencies instanceof List)
        {
            dependencies = (List) dependencies;
            ((List<?>) dependencies).stream().forEach(dependency ->
            {
                LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) dependency;

                if (!CollectionUtils.isEmpty(map))
                {
                    List<String> innerTags = new ArrayList<>();
                    Object tags = map.get("tags");
                    if (urls instanceof List)
                    {
                        innerTags = (List) tags;
                    }

                    Dependency build = Dependency.builder()
                            .name(map.get("name"))
                            .version(map.get("version"))
                            .repository(map.get("repository"))
                            .condition(map.get("condition"))
                            .tags(innerTags)
                            .enabled(Boolean.valueOf(map.get("enabled")))
                            .alias(map.get("alias"))
                            .importValues(map.get("importValues"))
                            .build();
                    outdependencies.add(build);
                }
            });
        }

        builder.dependencies(outdependencies);
        return builder.build();
    }

}
