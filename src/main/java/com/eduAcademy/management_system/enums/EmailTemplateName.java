package com.eduAcademy.management_system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account");

    private final String TemplateName;
}
