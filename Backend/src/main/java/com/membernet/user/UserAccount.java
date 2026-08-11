package com.membernet.user;

import java.util.Set;

public record UserAccount(String username, String displayName, String memberId, Set<Role> roles) { }
