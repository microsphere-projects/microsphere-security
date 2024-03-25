package io.microsphere.security.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityConstantsTest {

    @Test
    public void testPropertyNamePrefix() {
        assertEquals("microsphere.security.", SecurityConstants.PROPERTY_NAME_PREFIX);
    }

    @Test
    public void testWebSecurityPropertyNamePrefix() {
        assertEquals("microsphere.security.web", SecurityConstants.WEB_SECURITY_PROPERTY_NAME_PREFIX);
    }

    @Test
    public void testWebSecurityFilterPropertyNamePrefix() {
        assertEquals("microsphere.security.web.filter.", SecurityConstants.WEB_SECURITY_FILTER_PROPERTY_NAME_PREFIX);
    }

    @Test
    public void testWebSecurityCspPropertyNamePrefix() {
        assertEquals("microsphere.security.web.csp.", SecurityConstants.WEB_SECURITY_CSP_PROPERTY_NAME_PREFIX);
    }

    @Test
    public void testWebSecurityCorsPropertyNamePrefix() {
        assertEquals("microsphere.security.web.cors.", SecurityConstants.WEB_SECURITY_CORS_PROPERTY_NAME_PREFIX);
    }
}
