package lnjoying.generated.conditionset

import future.keywords.in

import data.lnjoying.generated.abac.utils.attributes

default resourceset__autogen__${resource_name} = false

resourceset__autogen__${resource_name} {
    attributes.resource.type == "${resource}"
}