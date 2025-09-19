# 'lnjoying/generated/conditionset_rules/rule__project.rego'
package lnjoying.generated.conditionset.rules

import future.keywords.in

import data.lnjoying.generated.abac.utils.attributes
#import data.lnjoying.generated.abac.utils.condition_set_permissions
import data.lnjoying.generated.conditionset

#default rule__project = false

rule__project[action] {

	some action in conditionset.actionset__projectlist
    conditionset.resourceset_project
}