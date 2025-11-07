package local.tpd.oracle.model.tpd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import tech.ailef.snapadmin.external.annotations.DisplayName;
import tech.ailef.snapadmin.external.annotations.Filterable;

import static local.tpd.oracle.validation.GreekValidationMessages.FIVE_DIGITS_LENGTH_MESSAGE;
import static local.tpd.oracle.validation.GreekValidationMessages.MAX_SIZE_MESSAGE;
import static local.tpd.oracle.validation.GreekValidationMessages.TEN_DIGITS_LENGTH_MESSAGE;

@Data
@Entity
@Getter
@Table(name = "EMPLOYEE_INFO")
public class EmployeeInfo {

    @Id
    @Column(name = "ID", nullable = false)
    private long ID;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Filterable
    @Column(name = "ONOMA")
    @Size(max = 100, message = MAX_SIZE_MESSAGE)
    private String onoma;

    @Filterable
    @Column(name = "EPONYMO")
    @Size(max = 100, message = MAX_SIZE_MESSAGE)
    private String eponymo;

    @Filterable
    @Column(name = "EMAIL")
    @Size(max = 100, message = MAX_SIZE_MESSAGE)
    private String email;

    @Filterable
    @Column(name = "ADT", unique = true)
    private String adt;

    @Filterable
    @Column(name = "ADDRESS")
    @Size(max = 100, message = MAX_SIZE_MESSAGE)
    private String address;

    @Filterable
    @Column(name = "CITY")
    @Size(max = 100, message = MAX_SIZE_MESSAGE)
    private String city;

    @Filterable
    @Column(name = "POSTAL_CODE")
    @Size(min = 9, max = 9, message = FIVE_DIGITS_LENGTH_MESSAGE)
    private Integer postalCode;

    @Filterable
    @Column(name = "HOME_PHONE")
    @Size(min = 10, max = 10, message = TEN_DIGITS_LENGTH_MESSAGE)
    private Long homePhone;

    @Filterable
    @Column(name = "MOBILE_PHONE")
    @Size(min = 10, max = 10, message = TEN_DIGITS_LENGTH_MESSAGE)
    private Long mobilePhone;

    @DisplayName
    public String getDisplayName() {
        if (onoma == null || eponymo == null || onoma.isEmpty() || eponymo.isEmpty()) {
            return String.valueOf(username);
        }
        return onoma + " " + eponymo;
    }

}
