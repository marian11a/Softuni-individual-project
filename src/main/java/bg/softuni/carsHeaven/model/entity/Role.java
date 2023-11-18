package bg.softuni.carsHeaven.model.entity;

import bg.softuni.carsHeaven.model.enums.RoleEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public Role() {
    }

    public Role(RoleEnum role) {
        setRole(role);
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
