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
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import tech.ailef.snapadmin.external.annotations.DisplayName;
import tech.ailef.snapadmin.external.annotations.Filterable;

import java.time.LocalDate;

@Data
@Entity
@Getter
@Table(name = "EMPLOYEE_SPOUSE_INFO", schema = "DTPD")
public class EmployeeSpouseInfo {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_spouse_info_generator")
    @SequenceGenerator(name = "employee_spouse_info_generator", sequenceName = "EMPLOYEE_SPOUSE_INFO_SEQ", allocationSize = 1)
    private long ID;

	@ManyToOne
    @Filterable
    @JoinColumn(name = "EMPLOYEE_INFO_ID", referencedColumnName = "ID", nullable = false)
	private EmployeeInfo employeeInfoEntity;

    @Filterable
    @Size(max = 100)
    @Column(name = "ONOMA")
    private String onoma;

    @Filterable
    @Size(max = 100)
    @Column(name = "EPONYMO")
    private String eponymo;

    @Filterable
    @Size(max = 100)
    @Column(name = "ONOMA_PATERA")
    private String onomaPatera;

    @Filterable
    @Column(name = "HMNIA_GAMOU")
    private LocalDate hmniaGamou;

    @Filterable
    @Column(name = "HMNIA_LYSHS_GAMOU")
    private LocalDate hmniaLyshsGamou;

    @Filterable
    @Column(name = "HMNIA_THANATOU")
    private LocalDate hmniaThanatou;

    @DisplayName
    public String getDisplayName() {
        if (onoma == null || eponymo == null || onoma.isEmpty() || eponymo.isEmpty()) {
            return String.valueOf(ID);
        }
        return onoma + " " + eponymo;
    }

}
