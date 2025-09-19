package lnjoying.generated.resource.user.utils

is_admin(kind) {
    kind == "ADMIN"
}

is_admin(kind) {
    kind == "SYSTEM"
}

is_bp_admin(kind) {
    kind == "BP_ADMIN"
}

is_bp_user(kind) {
    kind == "BP_SUB_USER"
}

is_personal(kind) {
    kind == "PERSONAL"
}

admin_kind{
 is_admin(user_kind)
}

bp_admin_kind{
 is_bp_admin(user_kind)
}

bp_user_kind{
 is_bp_user(user_kind)
}

bp_kind{
 is_bp_admin(user_kind)
}

bp_kind{
 is_bp_user(user_kind)
}


personal_kind{
 is_personal(user_kind)
}

user_kind := kind {
    # kind := data.users[input_query.user.key].attributes.kind
    kind := input.user.kind
}