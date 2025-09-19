package lnjoying.abac

import future.keywords.in
import future.keywords.every
import data.lnjoying.generated.conditionset
import data.lnjoying.generated.abac.utils
import data.lnjoying.generated.resource.user.utils as user_utils

default allow = false


allow {
    boundary_allow
    common_allow
}

boundary_allow {
    matching_boundary_rules
}

boundary_allow {
    matching_boundary_bp_rules
}

common_allow {
    #count(allowing_rules) > 0
    count(matching_rules) > 0
}

common_allow {
    count(matching_bp_rules) > 0
}

#allowing_rules[rule] {
#    some rule, value in conditionset.rules
#    value == true
#}



matching_boundary_rules  {

    all_boundary_rules =  utils.condition_set_boundary_rules
    count(all_boundary_rules) > 0

    rules := {
        rule:value |
        some rule, value in conditionset.rules.system
        rule in all_boundary_rules
    }

    every _, value in rules {
        value == true
    }
}

matching_boundary_rules {
    all_boundary_rules =  utils.condition_set_boundary_rules
    count(all_boundary_rules) = 0

}

matching_boundary_bp_rules {
    user_utils.bp_kind
    all_boundary_rules =  utils.condition_set_boundary_rules
    count(all_boundary_rules) > 0
    newBp := utils.cus_concat(utils.prefix_bp, input.user.bp)
    rules := {
        rule:value |
        some rule, value in conditionset.rules[newBp]
        rule in all_boundary_rules
    }

    every _, value in rules {
        value == true
    }
}

matching_boundary_bp_rules  {
    user_utils.bp_kind
    all_boundary_rules =  utils.condition_set_boundary_rules
    count(all_boundary_rules) = 0
}

merge_rules := utils.merge_arrays(matching_rules, matching_bp_rules)



matching_rules[{"rule":rule, "actions":value}] {
    some rule, value in conditionset.rules.system
    rule in utils.condition_set_common_rules
    count(value)>0
    #value == true
}

matching_bp_rules[{"rule":rule, "actions":value}] {
    user_utils.bp_kind
    newBp := utils.cus_concat(utils.prefix_bp, input.user.bp)
    some rule, value in conditionset.rules[newBp]
    rule in utils.condition_set_common_rules
    count(value)>0
}

matching_usersets[userset] {
    some set, value in conditionset
    startswith(set, "userset_")
    value == true
    userset := trim_prefix(set, "userset_")
}

matching_resourcesets[resourceset] {
    some set, value in conditionset
    startswith(set, "resourceset_")
    value == true
    resourceset := trim_prefix(set, "resourceset_")
}

matching_actionsets[actionset] {
    some set, value in conditionset
    startswith(set, "actionset_")
    value == true
    actionset := trim_prefix(set, "actionset_")
}


userset_permissions[userset] = resourceset_permissions {
    some userset in matching_usersets
    resourceset_permissions := {
        resourceset: permissions |
            some resourceset in matching_resourcesets
            permissions := utils.condition_set_permissions[userset][resourceset]
    }
}

default debug = null
debug = {
    #"allowing_rules": allowing_rules,
    "merge_rules":merge_rules,
    "matching_actionsets":matching_actionsets,
    "matching_usersets": matching_usersets,
    "matching_resourcesets": matching_resourcesets,
    "userset_permissions": userset_permissions,
    "attributes": {
        "context": {
            "generated": {},
            "input": utils.__input_context_attributes,
            "result": utils.attributes.context,
        },
        "user": {
            "generated": utils.__generated_user_attributes,
            "input": utils.__input_user_attributes,
            "result": utils.attributes.user,
        },
        "resource": {
            "generated": utils.__generated_resource_attributes,
            "input": utils.__input_resource_attributes,
            "result": utils.attributes.resource,
        },
    }
}