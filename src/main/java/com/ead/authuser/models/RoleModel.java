package com.ead.authuser.models;

import com.ead.authuser.emuns.RoleType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ROLES")
public class RoleModel  implements GrantedAuthority, Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private RoleType roleName;

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public RoleType getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleType roleName) {
        this.roleName = roleName;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return this.roleName.toString();
    }
}
