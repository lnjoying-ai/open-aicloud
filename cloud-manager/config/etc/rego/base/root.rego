package lnjoying.root

import data.lnjoying.policies
import data.lnjoying.custom

 # The policy will allow if both policies.allow and my_custom_rule are true

default allow := false

allow {
    policies.allow
}

allow {
    custom.allow
}

debug = policies.debug