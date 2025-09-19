package lnjoying.rbac

import future.keywords
import data.lnjoying.generated.rbac.utils
import data.lnjoying.generated.resource.user.utils as user_utils

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

# By default, deny requests.
default allow = false

# Allow the action if the user is granted permission to perform the action.
allow {
    count(matching_grants) > 0
}

allow {
  user_utils.admin_kind
}

matching_grants[grant] {
    # Find grants for the user.
    some grant in grants

    # Check if the grant permits the action.
    input_query.action == grant
}

project := project_key {
    input_query.resource.project != null
    project_key := input_query.resource.project
}

user_roles[role_key] {
    some role_key in data.users[input_query.user.key].roleAssignments[project]
}

grants[grant] {
    some role_key in user_roles
    some grant in data.roles[role_key].grants[input_query.resource.type]
}


default debug = null
debug := {
    "input_query": input_query,
    "projet": project,
    "user_roles": user_roles,
    "grants": grants,
    "matching_grants": matching_grants,
}

