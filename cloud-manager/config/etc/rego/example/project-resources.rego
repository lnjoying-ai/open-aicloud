package lnjoying.generated.conditionset

import future.keywords.in

import data.lnjoying.generated.abac.utils.attributes

default resourceset__project := false

resourceset__project {
	attributes.resource.type == "iam:project"
	resourceset_project_any_of_0
	resourceset_project_any_of_1
}

default resourceset_project_any_of_0 = false

resourceset_project_any_of_0 {
	attributes.resource.limit >= 20
}

resourceset_project_any_of_0 {
	attributes.resource.limit in [8, 18, 28]
}

default resourceset_project_any_of_1 = false

resourceset_project_any_of_1 {
	attributes.resource.limit >= 60
}

resourceset_project_any_of_1 {
	attributes.resource.name == "abc"
}
