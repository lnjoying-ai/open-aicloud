package com.lnjoying.justice.iam.authz.opa.pep;

import com.lnjoying.justice.iam.authz.opa.client.query.OpaQueryApi;
import com.lnjoying.justice.iam.authz.opa.client.query.OpaQueryRequest;
import com.lnjoying.justice.iam.authz.opa.client.query.OpaQueryResponse;
import com.lnjoying.justice.iam.authz.opa.pep.request.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/24 15:36
 */

@Slf4j
public class OpaEnforcer implements Enforcer
{
    private final OpaQueryApi opaQueryApi;

    private final String queryPath;

    private final String adHocQueryPath;

    private final OpaRequestSupplier<?> requestSupplier;

    public OpaEnforcer(@NonNull OpaQueryApi opaQueryApi,@NonNull String queryPath, @NonNull String adHocQueryPath)
    {
        this(opaQueryApi, queryPath, adHocQueryPath, new DefaultOpaContextSupplier());
    }


    public OpaEnforcer(@NonNull OpaQueryApi opaQueryApi,@NonNull String queryPath, @NonNull String adHocQueryPath, @NonNull OpaRequestSupplier<?> requestSupplier)
    {
        this.opaQueryApi = opaQueryApi;
        this.queryPath = queryPath;
        this.adHocQueryPath = adHocQueryPath;
        this.requestSupplier = requestSupplier;
    }

    @Override
    public AccessDecision check(User user, String action, Resource resource, Context context) throws PepException
    {
        try
        {
            OpaInput opaInput = OpaInput.builder().user(user).action(action).resource(resource).build();
            AccessDecision accessDecision = doCheck(opaInput);
            if (!accessDecision.isAllow())
            {
                denyAccess(user, action, accessDecision);
            }

            return accessDecision;
        }
        catch (Exception e)
        {
            logAndDeny(user, action, e);
        }

        return null;
    }

    @Override
    public AccessDecision check(User user, String action, Resource resource) throws PepException
    {
        return check(user, action, resource, new Context());
    }


    @Override
    public AdHocAccessDecision query(User user, Resource resource)
    {
        try
        {
            OpaInput opaInput = OpaInput.builder().user(user).resource(resource).build();
            OpaAdHocInput opaAdHocInput = OpaAdHocInput.builder().query(this.adHocQueryPath).opaInput(opaInput).build();
            AdHocAccessDecision accessDecision = doQuery(opaAdHocInput);
            return accessDecision;
        }
        catch (Exception e)
        {
           log.error("ad hoc request error:{}", e);
        }

        return null;
    }

    private AccessDecision doCheck(OpaInput opaInput)
    {
        OpaQueryRequest opaQueryRequest = new OpaQueryRequest(opaInput, queryPath);
        if (log.isDebugEnabled())
        {
            log.debug("user:{} opa request action:{}", opaInput.getUser().getKey(), opaInput.getAction());
        }
        OpaQueryResponse opaQueryResponse = opaQueryApi.query(opaQueryRequest, OpaQueryResponse.class);
        AccessDecision result = Optional.ofNullable(opaQueryResponse).map(response ->
        {
            boolean allow = response.getResult().isAllow();
            if (log.isDebugEnabled())
            {
                String debug = response.getResult().getDebug().toString();
                log.debug("opa input:{}, response :{}", debug);
            }
            AccessDecision accessDecision = new AccessDecision();
            accessDecision.setAllow(allow);
            // todo set reason
            return accessDecision;
        }).orElse(new AccessDecision());

        return result;
    }

    private AdHocAccessDecision doQuery(OpaAdHocInput opaAdHocInput)
    {
        OpaQueryRequest opaQueryRequest = new OpaQueryRequest(opaAdHocInput, null);
        if (log.isDebugEnabled())
        {
            log.debug("user:{} opa ad hoc request resource type:{}", opaAdHocInput.getOpaInput().getUser().getKey(), opaAdHocInput.getOpaInput().getResource().getType());
        }
        return opaQueryApi.adHocQuery(opaQueryRequest, AdHocAccessDecision.class);
    }

    private void denyAccess(User user, String action, AccessDecision accessDecision)
    {
        String rejectMsg = String.format("%s reject access %s by opa, because: %s", user.getKey(), action, accessDecision.getReason());
        log.warn(rejectMsg);

        // todo more like sending failure events etc
    }


    private void logAndDeny(User user, String action, Exception e)
    {
        String rejectMsg = String.format("exception caught when requesting user:%s access to path:%s, error:{}", user.getKey(), action);
        log.error(rejectMsg, e);


    }
}
