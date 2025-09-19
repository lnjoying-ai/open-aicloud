package com.lnjoying.justice.api.config;

import lombok.Data;

import java.util.List;

@Data
public class ContactAdmin
{
    List<String> phones;
    List<String> emails;
}
