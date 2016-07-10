package com.accounts.rb.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String ORG_ADMIN = "ROLE_ORG_ADMIN";
    
    public static final String EDIT_INVOICE = "ROLE_EDIT_INVOICE";

    private AuthoritiesConstants() {
    }
}
