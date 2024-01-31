package cg.tcarespb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "employee_skills")
public class EmployeeSkill {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private Boolean deleted = false;

    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Skill skill;

    public EmployeeSkill(Employee employee, Skill skill) {
        this.employee = employee;
        this.skill = skill;
    }
}
