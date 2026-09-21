package com.membernet.authorization;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class MembershipRoleId implements Serializable {

    private UUID membershipId;
    private UUID roleId;

    public MembershipRoleId() {
    }

    public MembershipRoleId(UUID membershipId, UUID roleId) {
        this.membershipId = membershipId;
        this.roleId = roleId;
    }

    public UUID getMembershipId() {
        return membershipId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof MembershipRoleId other)) {
            return false;
        }

        return Objects.equals(membershipId, other.membershipId)
                && Objects.equals(roleId, other.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(membershipId, roleId);
    }
}