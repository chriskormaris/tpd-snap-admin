package local.tpd.oracle.model.dtpd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.ailef.snapadmin.external.annotations.DisplayName;
import tech.ailef.snapadmin.external.annotations.Filterable;

import java.time.LocalDate;

import static local.tpd.oracle.validation.GreekValidationMessages.NOT_NULL_MESSAGE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPLOYEE_CHILD_INFO", schema = "DTPD")
public class EmployeeChildInfo {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_child_info_generator")
    @SequenceGenerator(name = "employee_child_info_generator", sequenceName = "EMPLOYEE_CHILD_INFO_SEQ", allocationSize = 1)
    private long ID;

    @ManyToOne
    @Filterable
    @NotNull(message = NOT_NULL_MESSAGE)
    @JoinColumn(name = "EMPLOYEE_INFO_ID", referencedColumnName = "ID", nullable = false)
    private EmployeeInfo employeeInfo;

    @Filterable
    @Size(max = 100)
    @Column(name = "ONOMA", nullable = false)
    private String onoma;

    @Filterable
    @Size(max = 100)
    @Column(name = "EPONYMO", nullable = false)
    private String eponymo;

    @Filterable
    @Column(name = "HMNIA_GENHSHS")
    private LocalDate hmniaGenhshs;

    @DisplayName
    public String getDisplayName() {
        if (onoma == null || eponymo == null || onoma.isEmpty() || eponymo.isEmpty()) {
            return String.valueOf(ID);
        }
        return onoma + " " + eponymo;
    }

}
