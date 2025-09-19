package lnjoying.generated.rbac.utils

import future.keywords.in



roles[roleKey] {
    some roleKey in data.users[input.user.key].roleAssignments[input.resource.project]
}
