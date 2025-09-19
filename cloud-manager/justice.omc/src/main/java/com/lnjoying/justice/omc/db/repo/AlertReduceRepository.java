package com.lnjoying.justice.omc.db.repo;

import com.lnjoying.justice.omc.db.mapper.TblOmcAlertInhibitionRuleMapper;
import com.lnjoying.justice.omc.db.mapper.TblOmcAlertSilenceRuleMapper;
import com.lnjoying.justice.omc.db.model.TblOmcAlertInhibitionRule;
import com.lnjoying.justice.omc.db.model.TblOmcAlertSilenceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/24 15:12
 */

@Repository
public class AlertReduceRepository
{
    @Autowired
    private TblOmcAlertInhibitionRuleMapper inhibitionRuleMapper;

    @Autowired
    private TblOmcAlertSilenceRuleMapper silenceRuleMapper;

    public List<TblOmcAlertInhibitionRule> selectAllAlertInhibitionRules()
    {
        return inhibitionRuleMapper.selectAll(null);
    }

    public List<TblOmcAlertSilenceRule> selectAllAlertSilenceRules()
    {
        return silenceRuleMapper.selectAll(null);
    }
}
