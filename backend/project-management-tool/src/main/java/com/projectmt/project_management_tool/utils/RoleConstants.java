package com.projectmt.project_management_tool.utils;

import java.util.List;

public class RoleConstants {
    public static final String ADMIN = "Administrateur";
    public  static  final String MEMBRE = "Membre";
    public static final String OBS = "Observateur";

    public static final List<String> ROLES_UPDATE = List.of(ADMIN, MEMBRE);
    public static final List<String> ALL_ROLES = List.of(ADMIN, MEMBRE, OBS);

}
