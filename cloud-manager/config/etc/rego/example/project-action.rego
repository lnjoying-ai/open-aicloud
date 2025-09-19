package lnjoying.generated.conditionset


import future.keywords.in
import data.lnjoying.generated.abac.utils.attributes
import data.lnjoying.generated.abac.utils._matches_actions

#default actionset_projectlist = false




actionset__projectlist [actions]{

	attributes.resource.type == "iam:project"
    # exact match
	#input.action in ["iam:getProject", "iam:getProjectList"]
    # fuzzy match
     actions := ["iam:*", "*", "iam:getProjectList"]
    _matches_actions(input.action, actions)
}