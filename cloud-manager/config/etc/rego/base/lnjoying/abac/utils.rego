package lnjoying.generated.abac.utils

import future.keywords.in

# not undefined if object 'x' has a key 'k'
has_key(x, k) {
    _ := x[k]
}

# If a field 'k' relies in both 'a' and 'b' objects, pick its value from 'a'.
pick_first(k, a, b) = a[k] {
    has_key(a, k)
}

else = b[k] {
    true
}

# haystack, needle
_matches_actions(haystack, needles)
{
	some needle in needles
	_matches_exemption(haystack, needle)
}


_matches_exemption(haystack, exemption) {
    not endswith(exemption, "*")
    exemption == haystack
}

_matches_exemption(haystack, exemption) {
    endswith(exemption, "*")
    prefix := trim_suffix(exemption, "*")
    startswith(haystack, prefix)
}


# Merging objects a & b. If a field relies in both of them, pick it's value from a.
merge_objects(a, b) = c {
    ks := {k | some k; _ = a[k]} | {k | some k; _ = b[k]}
    c := {k: v | some k; ks[k]; v := pick_first(k, b, a)}
}

merge_arrays(a, b) = c {
 c := {k | some k; k = a[_]} | {k | some k; k = b[_]}
}

roles[roleKey] {
    some roleKey in data.users[input.user.key].roleAssignments[input.resource.project]
}

cus_concat(a, b) = c {
    c := sprintf("%s%s", [a, b])
}

prefix_bp := "bp_"

prefix_boundary_rule = "boundary_"
prefix_common_rule = "rule_"

__generated_user_attributes = {"roles": roles}

__generated_resource_attributes = {"type": input.resource.type}

default __stored_user_attributes = {}

__stored_user_attributes = data.users[input.user.key].attributes

default __input_user_attributes = {}

default __input_resource_attributes = {}

default __input_context_attributes = {}

__input_user_attributes = input.user.attributes

__input_resource_attributes = input.resource.attributes

__input_context_attributes = input.context

__user_attributes = merge_objects(__input_user_attributes, __stored_user_attributes)

attributes = {
    "user": merge_objects(__user_attributes, __generated_user_attributes),
    "resource": merge_objects(__input_resource_attributes, __generated_resource_attributes),
    "context": __input_context_attributes,
}

input_query := {
    "action": input.action,
    "user": {
        "key": input.user.key,
        "kind": input.user.kind,
        "bp": input.user.bp,
    },
    "resource": {
        "type": input.resource.type,
        "project":input.resource.project,
    }
}

project := project_key {
    input_query.resource.project != null
    project_key := input_query.resource.project
}

user_roles[role_key] {
    some role_key in data.users[input_query.user.key].roleAssignments[project]
}

all_role_rules[rule] {
    some role_key in user_roles
    some rule in data.condition_set_role_rules[role_key][_].rules
}

# userset resourceset
condition_set_permissions := data.condition_set_permissions

all_user_rules[rule] {
    some rule in data.condition_set_user_rules[input_query.user.key][project][_].rules
}

boundary_role_rules[rule] {
    some role_key in user_roles
    all_rules := data.condition_set_role_rules[role_key][_].rules
    boundary_rules := [item | item = all_rules[_]; startswith(item, prefix_boundary_rule) ]
    some rule in boundary_rules
}

boundary_user_rules[rule] {
    all_rules := data.condition_set_user_rules[input_query.user.key][project][_].rules
    boundary_rules := [item | item = all_rules[_]; startswith(item, prefix_boundary_rule) ]
    some rule in boundary_rules
}

common_role_rules[rule] {
    some role_key in user_roles
    all_rules := data.condition_set_role_rules[role_key][_].rules
    common_rules := [item | item = all_rules[_]; startswith(item, prefix_common_rule) ]
    some rule in common_rules
}

common_user_rules[rule] {
    all_rules :=data.condition_set_user_rules[input_query.user.key][project][_].rules
    common_rules := [item | item = all_rules[_]; startswith(item, prefix_common_rule) ]
    some rule in common_rules
}


# action user
condition_set_all_rules := all_user_rules | all_role_rules
condition_set_common_rules := common_user_rules | common_role_rules
condition_set_boundary_rules := boundary_user_rules | boundary_role_rules