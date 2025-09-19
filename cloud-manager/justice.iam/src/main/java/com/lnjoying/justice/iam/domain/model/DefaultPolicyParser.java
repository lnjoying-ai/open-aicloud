package com.lnjoying.justice.iam.domain.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lnjoying.justice.commonweb.util.Base64Utils;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.db.model.TblIamPolicy.DocumentType.REGO;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/21 19:59
 */

@Slf4j
public class DefaultPolicyParser implements PolicyParser
{
    public static final PolicyParser DEFAULT_INSTANCE = new DefaultPolicyParser();

    public static final String RULE_SYSTEM = "system";

    public static final String BP_PREFIX = "bp_";

    public static final String CONDITION_RULE_PREFIX = "rule__";

    public static final String CONDITION_BOUNDARY_RULE_PREFIX = "boundary__rule__";

    public  List<String> extractConditionRuleNames(String documentText)
    {
        TblIamPolicy.Document<TblIamPolicy.RegoDocument> document = null;
        try
        {
            document = JacksonUtils.strToObjType(documentText, new TypeReference<TblIamPolicy.Document<TblIamPolicy.RegoDocument>>(){});
            String type = document.getType();
            if (REGO.name().equalsIgnoreCase(type))
            {
                List<String> allRuleNames = document.getStatement().stream().map(rule ->
                {
                    TblIamPolicy.RegoDocument rego = (TblIamPolicy.RegoDocument) rule;
                    String regoRule = Base64Utils.decodeToString(rego.getRule());
                    List<String> ruleNames = toExtractConditionRuleNamesV2(regoRule);
                    return ruleNames;
                }).flatMap(Collection::stream).collect(Collectors.toList());
                return allRuleNames;
            }
        }
        catch (IOException e)
        {
            log.error("parse document failed, error:{}", e);
        }

        return Collections.emptyList();
    }

    @Override
    public String rewriteDoc(String ruleDoc, boolean system, String bpId)
    {
      try
      {
            String rewriteDoc = toRewriteDoc(ruleDoc, system, bpId);
            return rewriteDoc;
        }
        catch (Exception e)
        {
            log.error("rewrite document failed, error:{}", e);
        }

        return ruleDoc;
    }

     public static List<String> toExtractConditionRuleNames(String regoRuleText)
    {
        // todo package contained in conditionset.rules
        if (regoRuleText.contains("conditionset.rules"))
        {
            String[] lines = StringUtils.split(regoRuleText, "\r\n");
            List<String> ruleNames = Arrays.stream(lines).filter(x -> x.contains("default") && !x.startsWith("#")).map(x ->
            {
                String y = StringUtils.strip(x);
                String z = StringUtils.substringAfter(y, "default");
                String[] split = StringUtils.split(z, ":=");
                if (split.length == 2)
                {
                    String ruleName = StringUtils.strip(split[0]);
                    if (StringUtils.startsWith(ruleName, "rule__"))
                    {
                        return ruleName;
                    }
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());

            return ruleNames;
        }

        return Collections.emptyList();
    }

    private String toRewriteDoc(String ruleDoc, boolean system, String bpId)
    {

        if (ruleDoc.contains("conditionset.rules"))
        {
            String[] lines = StringUtils.split(ruleDoc, "\r\n");
            List<String> rewriteLines = Arrays.stream(lines).map(x -> StringUtils.strip(x)).map(x ->
                    {
                        // handle rules package
                        if (x.contains("package") && !x.startsWith("#") && x.contains("conditionset.rules"))
                        {
                            return doProcessRulePackage(system, bpId, x);
                        }

                        if (!system && StringUtils.isNotBlank(bpId))
                        {
                            if (x.contains("import") && !x.startsWith("#") && x.contains("conditionset"))
                            {
                                return doProcessRuleImport(bpId, x);
                            }
                        }

                        return x;
                    }).filter(Objects::nonNull).collect(Collectors.toList());

            return rewriteLines.stream().collect(Collectors.joining(System.lineSeparator()));
        }


        if (ruleDoc.contains("lnjoying.generated.conditionset"))
        {
            String[] lines = StringUtils.split(ruleDoc, "\r\n");
            List<String> rewriteLines = Arrays.stream(lines).map(x -> StringUtils.strip(x)).map(x ->
            {
                if (!system && StringUtils.isNotBlank(bpId))
                {
                    if (x.contains("package") && !x.startsWith("#") && x.contains("conditionset"))
                    {
                        return doProcessConditionSetPackage(bpId, x);
                    }
                }
                return x;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            return rewriteLines.stream().collect(Collectors.joining(System.lineSeparator()));
        }
        return ruleDoc;
    }


    private String doProcessRuleImport(String bpId, String line)
    {
        String wordToFind = "conditionset";
        String wordToAppend = wordToFind;
        // rewrite bp rule import
        // like this, import data.lnjoying.generated.conditionset.bpxx as conditionset
        StringBuilder sb = new StringBuilder();
        wordToAppend = sb.append(wordToFind).append(".").append(BP_PREFIX).append(bpId).append("  as conditionset").toString();

        String replace = Strings.replace(line, wordToFind, wordToAppend);
        return replace;
    }


    private String doProcessConditionSetPackage(String bpId, String line)
    {
        String wordToFind = "conditionset";
        String wordToAppend = wordToFind;

        // like this, package lnjoying.generated.conditionset.bpxx
        StringBuilder sb = new StringBuilder();
        wordToAppend = sb.append(wordToFind).append(".").append(BP_PREFIX).append(bpId).toString();
        String replace = Strings.replace(line, wordToFind, wordToAppend);
        return replace;
    }

    private static String doProcessRulePackage(boolean system, String bpId, String line)
    {
        String wordToFind = "conditionset.rules";
        String wordToAppend = wordToFind;
        // rewrite rule package
        if (system)
        {
            StringBuilder sb = new StringBuilder();
            wordToAppend = sb.append(wordToFind).append(".").append(RULE_SYSTEM).toString();
        }
        else
        {
            if (StringUtils.isNotBlank(bpId))
            {
                // like this, package lnjoying.generated.conditionset.rules.bpxx
                StringBuilder sb = new StringBuilder();
                wordToAppend = sb.append(wordToFind).append(".").append(BP_PREFIX).append(bpId).toString();
            }

        }

        String replace = Strings.replace(line, wordToFind, wordToAppend);
        return replace;
    }


    public static List<String> toExtractConditionRuleNamesV2(String regoRuleText)
    {
        if (regoRuleText.contains("conditionset.rules"))
        {
            String[] lines = StringUtils.split(regoRuleText, "\r\n");
            List<String> ruleNames = Arrays.stream(lines).map(x -> StringUtils.strip(x))
                    .filter(x -> (x.contains(CONDITION_RULE_PREFIX) || x.contains(CONDITION_BOUNDARY_RULE_PREFIX)) && !x.startsWith("#")).map(x ->
            {
                // rule__xx[action]
                String[] split = StringUtils.split(x, "[");
                if (split.length == 2)
                {
                    String ruleName = StringUtils.strip(split[0]);
                    if (StringUtils.startsWith(ruleName, CONDITION_RULE_PREFIX) || StringUtils.startsWith(ruleName, CONDITION_BOUNDARY_RULE_PREFIX))
                    {
                        return ruleName;
                    }
                }

                // rule_xx {
               split = StringUtils.split(x, "{");
                if (split.length > 0)
                {
                    String ruleName = StringUtils.strip(split[0]);
                    if (StringUtils.startsWith(ruleName, CONDITION_RULE_PREFIX) || StringUtils.startsWith(ruleName, CONDITION_BOUNDARY_RULE_PREFIX))
                    {
                        return ruleName;
                    }
                }

                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());

            return ruleNames;
        }

        return Collections.emptyList();
    }

}
