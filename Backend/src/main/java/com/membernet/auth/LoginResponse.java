package com.membernet.auth;

import com.membernet.user.Role;
import java.util.Set;

public record LoginResponse(String message, String username, String displayName, String memberId,
                            Set<Role> roles, String homePage) { }
