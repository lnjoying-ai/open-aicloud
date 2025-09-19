package com.lnjoying.justice.iam.authz.opa.pep.pathMatcher;

import com.micro.core.common.Pair;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.common.rest.definition.RestOperationMeta;
import org.apache.servicecomb.common.rest.definition.path.PathRegExp;
import org.springframework.http.MediaType;
import org.springframework.http.server.RequestPath;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.authz.opa.pep.pathMatcher.RequestMappingInfoUtils.EMPTY_PATH_PATTERN;
import static com.lnjoying.justice.iam.authz.opa.pep.pathMatcher.RequestMappingInfoUtils.PATH_ATTRIBUTE;
import static org.springframework.web.servlet.HandlerMapping.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/27 20:19
 */

@Slf4j
public class SwaggerRequestMapping
{
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    private PathMatcher pathMatcher = new AntPathMatcher();

    private PathPatternParser patternParser = new PathPatternParser();

    private final MappingRegistry mappingRegistry = new MappingRegistry();

    private SwaggerLoader swaggerLoader = new DefaultSwaggerLoader();

    public SwaggerLoader getSwaggerLoader()
    {
        return swaggerLoader;
    }

    protected void registerMapping(RequestMappingInfo mapping)
    {
        this.mappingRegistry.register(mapping);
    }

    /**
     * Load the api document in the system and convert it to Request Mapping Info for saving.
     */
    public void detectMappingInfo()
    {
        List<Swagger> swaggers = swaggerLoader.getSwaggers();
        List<RequestMappingInfo> mappings = new ArrayList<>();

        if (!CollectionUtils.isEmpty(swaggers))
        {
            List<Map<String, Path>> swaggerMaps = swaggers.stream().map(swagger -> fixPaths(swagger)).collect(Collectors.toList());
            swaggerMaps.stream().forEach(swaggerPath -> {
                swaggerPath.forEach((path, pathItem) -> {
                    mappings.addAll(getMappings(path, pathItem));
                });
            });
        }

        if (!CollectionUtils.isEmpty(mappings))
        {
            mappings.stream().forEach(mappingInfo -> registerMapping(mappingInfo));
        }

    }


    /**
     * Obtain the most matching Request Mapping by request
     * @param request
     * @return
     */
    public RequestMappingInfo lookUp(MockHttpServletRequest request)
    {

        List<Match> matches = new ArrayList<>();
        Pair<String, List<RequestMappingInfo>> mappingInfoPair = this.mappingRegistry.getMappingsByDirectPath(request);
        String lookupPath = mappingInfoPair.getLeft();
        List<RequestMappingInfo> directPathMatches = mappingInfoPair.getRight();

        if (directPathMatches != null) {
            addMatchingMappings(directPathMatches, matches, request);
        }
        if (matches.isEmpty()) {
            addMatchingMappings(this.mappingRegistry.getRegistrations().keySet(), matches, request);
        }

        if (!matches.isEmpty()) {
            Match bestMatch = matches.get(0);
            if (matches.size() > 1) {
                Comparator<Match> comparator = new MatchComparator(getMappingComparator(request));
                matches.sort(comparator);
                bestMatch = matches.get(0);
                if (log.isTraceEnabled()) {
                    log.trace(matches.size() + " matching mappings: " + matches);
                }

                Match secondBestMatch = matches.get(1);
                if (comparator.compare(bestMatch, secondBestMatch) == 0) {
//                    Method m1 = bestMatch.getHandlerMethod().getMethod();
//                    Method m2 = secondBestMatch.getHandlerMethod().getMethod();
                    String uri = request.getRequestURI();
                    throw new IllegalStateException(
                            "Ambiguous handler methods mapped for '" + uri + "': ");
                }

            }
            //request.setAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE, bestMatch.getHandlerMethod());
            match(bestMatch.mapping, lookupPath, request);
            return bestMatch.getMapping();
        }
        else {
            // servicecomb RegExp
            if (matches.isEmpty())
            {
                addMatchingMappingsSb(this.mappingRegistry.getRegistrations().keySet(), matches, request, lookupPath);
                if (!matches.isEmpty())
                {
                    return matches.get(0).getMapping();
                }

            }
        }

        return null;
    }

    public UrlPathHelper getUrlPathHelper() {
        return this.urlPathHelper;
    }

    public PathPatternParser getPatternParser() {
        return this.patternParser;
    }

    public boolean usesPathPatterns() {
        return getPatternParser() != null;
    }

    protected  List<RequestMappingInfo> getMappings(String path, Path pathItem)
    {
        return createRequestMappingInfo(path, pathItem);
    }

    class MappingRegistry
    {
        private final MultiValueMap<String, RequestMappingInfo> pathLookup = new LinkedMultiValueMap<>();

        private final Map<RequestMappingInfo, MappingRegistration> registry = new HashMap<>();

        public void register(RequestMappingInfo mapping)
        {
            Set<String> directPaths = getDirectPaths(mapping);
            for (String path : directPaths)
            {
                this.pathLookup.add(path, mapping);
            }

            String name = getName(mapping);

            this.registry.put(mapping, new MappingRegistration(mapping, directPaths, name));
        }

        private String getName(RequestMappingInfo mapping)
        {
            // todo
            return null;
        }

        public Pair<String, List<RequestMappingInfo>> getMappingsByDirectPath(MockHttpServletRequest request) {
            String urlPath = initLookupPath(request);
            String requestURI = request.getRequestURI();
            String requestURISuffix = StringUtils.substringAfter(requestURI, urlPath);
            urlPath = fixUrlPath(urlPath);
            List<RequestMappingInfo> requestMappingInfoList = this.pathLookup.get(urlPath);
            if (CollectionUtils.isEmpty(requestMappingInfoList))
            {
                if (urlPath.endsWith("/"))
                {
                    urlPath = org.apache.commons.lang.StringUtils.substringBeforeLast(urlPath, "/");
                    requestMappingInfoList = this.pathLookup.get(urlPath);
                }
                else
                {
                    urlPath = urlPath + "/";
                    requestMappingInfoList = this.pathLookup.get(urlPath);
                }
            }

            request.setRequestURI(urlPath);
            return new Pair<>(urlPath, requestMappingInfoList);
        }

        public Map<RequestMappingInfo, MappingRegistration> getRegistrations() {
            return this.registry;
        }

        private String fixUrlPath(String path)
        {

            if (!path.startsWith("/api/iam") && path.startsWith("/api"))
            {
                path = StringUtils.substringAfter(path, "/api");
            }

            return path;
        }
    }

    static class MappingRegistration
    {
        private final RequestMappingInfo mapping;

        private final Set<String> directPaths;

        private final String mappingName;


        MappingRegistration(RequestMappingInfo mapping, Set<String> directPaths, String mappingName)
        {
            this.mapping = mapping;
            this.directPaths = directPaths;
            this.mappingName = mappingName;
        }

        public RequestMappingInfo getMapping()
        {
            return mapping;
        }

        public Set<String> getDirectPaths()
        {
            return directPaths;
        }

        public String getMappingName()
        {
            return mappingName;
        }
    }

    private class Match
    {
        private final RequestMappingInfo mapping;

        private final MappingRegistration registration;


        private Match(RequestMappingInfo mapping, MappingRegistration registration)
        {
            this.mapping = mapping;
            this.registration = registration;
        }

        public RequestMappingInfo getMapping()
        {
            return mapping;
        }
    }

    private class MatchComparator implements Comparator<Match> {

        private final Comparator<RequestMappingInfo> comparator;

        public MatchComparator(Comparator<RequestMappingInfo> comparator) {
            this.comparator = comparator;
        }

        @Override
        public int compare(Match match1, Match match2) {
            return this.comparator.compare(match1.mapping, match2.mapping);
        }
    }

    private void addMatchingMappings(Collection<RequestMappingInfo> mappings, List<Match> matches, HttpServletRequest request) {
        for (RequestMappingInfo mapping : mappings) {
            RequestMappingInfo match = getMatchingMapping(mapping, request);
            if (match != null) {
                matches.add(new Match(match, this.mappingRegistry.getRegistrations().get(mapping)));
                MappingRegistration mappingRegistration = this.mappingRegistry.getRegistrations().get(mapping);
            }

        }
    }

    private void addMatchingMappingsSb(Collection<RequestMappingInfo> mappings, List<Match> matches, HttpServletRequest request, String lookupPath) {
        for (RequestMappingInfo mapping : mappings) {
            try
            {
                PatternsRequestCondition patternsCondition = mapping.getPatternsCondition();
                if (Objects.nonNull(patternsCondition))
                {
                    //String lookupPath = urlPathHelper.getLookupPathForRequest(request, HandlerMapping.LOOKUP_PATH);
                    Set<String> patterns = patternsCondition.getPatterns();
                    if (!CollectionUtils.isEmpty(patterns))
                    {
                        for (String pattern : patterns)
                        {
                            if (pattern.equals(lookupPath))
                            {
                                doPathRegMatch((List<Match>) matches, request, mapping);

                            }
                            else
                            {
                                PathRegExp oPathRegExp = new PathRegExp(pattern);
                                String match = oPathRegExp.match(lookupPath, new HashMap<>());
                                if ("".equals(match)) {
                                    doPathRegMatch((List<Match>) matches, request, mapping);
                                }
                            }

                        }


                    }
                }
            }
            catch (Exception e)
            {
                log.error("failed to use servicecomb reg match ,{}", e);
            }

        }
    }

    private void doPathRegMatch(List<Match> matches, HttpServletRequest request, RequestMappingInfo mapping)
    {
        RequestMethodsRequestCondition methodsCondition = mapping.getMethodsCondition();
        String requestMethod = request.getMethod();
        if (Objects.nonNull(methodsCondition) && StringUtils.isNotBlank(requestMethod))
        {
            Set<RequestMethod> methods = methodsCondition.getMethods();
            if (!CollectionUtils.isEmpty(methods))
            {
                Optional<String> appropriateMethod =
                        methods.stream().map(method -> method.name()).filter(method -> method.equalsIgnoreCase(requestMethod)).findFirst();
                if (appropriateMethod.isPresent())
                {
                    matches.add(new Match(mapping, this.mappingRegistry.getRegistrations().get(mapping)));
                }
            }
        }
    }

    protected void match(RequestMappingInfo mapping, String lookupPath, HttpServletRequest request) {
        request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, lookupPath);
        RequestCondition<?> condition = mapping.getPatternsCondition();

        extractMatchDetails((PatternsRequestCondition) condition, lookupPath, request);

        ProducesRequestCondition producesCondition = mapping.getProducesCondition();
        if (!producesCondition.isEmpty()) {
            Set<MediaType> mediaTypes = producesCondition.getProducibleMediaTypes();
            if (!mediaTypes.isEmpty()) {
                request.setAttribute(PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
            }
        }
    }

    protected Comparator<RequestMappingInfo> getMappingComparator(final HttpServletRequest request) {
        return (info1, info2) -> info1.compareTo(info2, request);
    }

    protected RequestMappingInfo getMatchingMapping(RequestMappingInfo info, HttpServletRequest request) {
        return info.getMatchingCondition(request);
    }

    protected String initLookupPath(HttpServletRequest request) {
        if (getPatternParser() != null) {
            RequestPath requestPath = ServletRequestPathUtils.parseAndCache(request);
            return requestPath.pathWithinApplication().value();
        }
        if (usesPathPatterns()) {
            request.removeAttribute(PATH_ATTRIBUTE);
            RequestPath requestPath = getRequestPath(request);
            String lookupPath = requestPath.pathWithinApplication().value();
            return UrlPathHelper.defaultInstance.removeSemicolonContent(lookupPath);
        }
        else {
            return resolveAndCacheLookupPath(request);
        }
    }

    protected Set<String> getDirectPaths(RequestMappingInfo mapping) {
        Set<String> urls = Collections.emptySet();
        for (String path : getMappingPathPatterns(mapping)) {
            if (!getPathMatcher().isPattern(path)) {
                urls = (urls.isEmpty() ? new HashSet<>(1) : urls);
                urls.add(path);
            }
        }
        return urls;
    }

    protected Set<String> getMappingPathPatterns(RequestMappingInfo info) {
        return getConditionDirectPaths(info);
    }

    public PathMatcher getPathMatcher() {
        return this.pathMatcher;
    }

    private  List<RequestMappingInfo> createRequestMappingInfo(String path, Path pathItem)
    {
        List<RequestMappingInfo> requestMappingInfoList = RequestMappingInfoUtils.doMappingBuilder(path, pathItem);
        return requestMappingInfoList;
    }

    private void extractMatchDetails(
            PatternsRequestCondition condition, String lookupPath, HttpServletRequest request) {

        String bestPattern;
        Map<String, String> uriVariables;
        if (isEmptyPathMapping(condition)) {
            bestPattern = lookupPath;
            uriVariables = Collections.emptyMap();
        }
        else {
            bestPattern = condition.getPatterns().iterator().next();
            uriVariables = getPathMatcher().extractUriTemplateVariables(bestPattern, lookupPath);
            if (!getUrlPathHelper().shouldRemoveSemicolonContent()) {
                request.setAttribute(MATRIX_VARIABLES_ATTRIBUTE, extractMatrixVariables(request, uriVariables));
            }
            uriVariables = getUrlPathHelper().decodePathVariables(request, uriVariables);
        }
        request.setAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE, bestPattern);
//        ServerHttpObservationFilter.findObservationContext(request)
//                .ifPresent(context -> context.setPathPattern(bestPattern));
        request.setAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriVariables);
    }


    private Map<String, MultiValueMap<String, String>> extractMatrixVariables(
            HttpServletRequest request, Map<String, String> uriVariables) {

        Map<String, MultiValueMap<String, String>> result = new LinkedHashMap<>();
        uriVariables.forEach((uriVarKey, uriVarValue) -> {

            int equalsIndex = uriVarValue.indexOf('=');
            if (equalsIndex == -1) {
                return;
            }

            int semicolonIndex = uriVarValue.indexOf(';');
            if (semicolonIndex != -1 && semicolonIndex != 0) {
                uriVariables.put(uriVarKey, uriVarValue.substring(0, semicolonIndex));
            }

            String matrixVariables;
            if (semicolonIndex == -1 || semicolonIndex == 0 || equalsIndex < semicolonIndex) {
                matrixVariables = uriVarValue;
            }
            else {
                matrixVariables = uriVarValue.substring(semicolonIndex + 1);
            }

            MultiValueMap<String, String> vars = WebUtils.parseMatrixVariables(matrixVariables);
            result.put(uriVarKey, getUrlPathHelper().decodeMatrixVariables(request, vars));
        });
        return result;
    }

    private RequestPath getRequestPath(HttpServletRequest request) {
        return request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null ?
                ServletRequestPathUtils.getParsedRequestPath(request) :
                ServletRequestPathUtils.parseAndCache(request);
    }

    private Set<String> getConditionDirectPaths(RequestMappingInfo mapping) {
        RequestCondition<?> condition = mapping.getPatternsCondition();
        PatternsRequestCondition patternsRequestCondition = (PatternsRequestCondition) condition;
        if (isEmptyPathMapping(patternsRequestCondition)) {
            return  Collections.singleton("");
        }
        Set<String> result = Collections.emptySet();
        for (String pattern : patternsRequestCondition.getPatterns()) {
            if (!this.pathMatcher.isPattern(pattern)) {
                result = (result.isEmpty() ? new HashSet<>(1) : result);
                result.add(pattern);
            }
        }
        return result;
    }

    private String resolveAndCacheLookupPath(HttpServletRequest request) {
        String lookupPath = this.getUrlPathHelper().getLookupPathForRequest(request);
        request.setAttribute(PATH_ATTRIBUTE, lookupPath);
        return lookupPath;
    }

    private boolean isEmptyPathMapping(PatternsRequestCondition condition) {
        return condition.getPatterns() == EMPTY_PATH_PATTERN;
    }


    private static Map<String, Path> fixPaths(Swagger swagger)
    {
        Map<String, Path> paths = swagger.getPaths();
        Map<String, Path> fixPaths = new HashMap<>(paths.size());

        if (!CollectionUtils.isEmpty(paths))
        {
            paths.forEach((path, pathItem) -> {
                String fullPath = swagger.getBasePath() + path;
                fullPath = fullPath.replaceAll("/+", "/");
                fixPaths.put(fullPath, pathItem);
            });
        }
        return fixPaths;
    }

    private boolean checkHttpMethod(RestOperationMeta operation, String httpMethod) {
        return operation.getHttpMethod().equals(httpMethod);
    }

}
