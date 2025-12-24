package local.tpd.oracle.model.dtpd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import tech.ailef.snapadmin.external.annotations.DisplayName;
import tech.ailef.snapadmin.external.annotations.Filterable;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static local.tpd.oracle.validation.GreekValidationMessages.ELEVEN_DIGITS_LENGTH_MESSAGE;
import static local.tpd.oracle.validation.GreekValidationMessages.FIVE_DIGITS_LENGTH_MESSAGE;
import static local.tpd.oracle.validation.GreekValidationMessages.MAX_SIZE_MESSAGE;
import static local.tpd.oracle.validation.GreekValidationMessages.NINE_DIGITS_LENGTH_MESSAGE;
import static local.tpd.oracle.validation.GreekValidationMessages.TEN_DIGITS_LENGTH_MESSAGE;

@Data
@Entity
@Getter
@Table(name = "EMPLOYEE_INFO", schema = "DTPD")
public class EmployeeInfo {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_info_generator")
    @SequenceGenerator(name = "employee_info_generator", sequenceName = "EMPLOYEE_INFO_SEQ", allocationSize = 1)
    private long ID;

    @Filterable
    @Column(name = "ONOMA")
    @Size(max = 100, message = MAX_SIZE_MESSAGE)
    private String onoma;

    @Filterable
    @Column(name = "EPONYMO")
    @Size(max = 100, message = MAX_SIZE_MESSAGE)
    private String eponymo;

    @Size(max = 100)
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Filterable
    @Column(name = "EMAIL", nullable = false, unique = true)
    @Size(max = 100, message = MAX_SIZE_MESSAGE)
    private String email;

    @Filterable
    @Size(max = 100)
    @Column(name = "ONOMA_PATERA")
    private String onomaPatera;

    @Filterable
    @Size(max = 100)
    @Column(name = "EPONYMO_PATERA")
    private String eponymoPatera;

    @Filterable
    @Size(max = 100)
    @Column(name = "ONOMA_MHTERAS")
    private String onomaMhteras;

    @Filterable
    @Size(max = 100)
    @Column(name = "EPONYMO_MHTERAS")
    private String eponymoMhteras;

    @Filterable
    @Column(name = "HMNIA_GENHSHS")
    private LocalDate hmniaGenhshs;

    @Filterable
    @Size(min = 9, max = 9, message = NINE_DIGITS_LENGTH_MESSAGE)
    @Column(name = "AFM", unique = true)
    private String afm;

    @Filterable
    @Column(name = "DOY")
    private String doy;

    @Filterable
    @Column(name = "ADT", unique = true)
    private String adt;

    @Filterable
    @Column(name = "HMNIA_EKDOSHS")
    private LocalDate hmniaEkdoshs;

    @Filterable
    @Size(min = 11, max = 11, message = ELEVEN_DIGITS_LENGTH_MESSAGE)
    @Column(name = "AMKA", unique = true)
    private String amka;

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
    private String postalCode;

    @Filterable
    @Column(name = "HOME_PHONE")
    @Size(min = 10, max = 10, message = TEN_DIGITS_LENGTH_MESSAGE)
    private String homePhone;

    @Filterable
    @Column(name = "MOBILE_PHONE", unique = true)
    @Size(min = 10, max = 10, message = TEN_DIGITS_LENGTH_MESSAGE)
    private String mobilePhone;

    @Filterable
    @Size(max = 100)
    @Column(name = "PERSONAL_EMAIL", unique = true)
    private String personalEmail;

    @Filterable
    @Column(name = "INSERT_TIMESTAMP")
    private LocalDateTime insertTimestamp;

    @Filterable
    @Column(name = "UPDATE_TIMESTAMP")
    private LocalDateTime updateTimestamp;

    @Filterable
    @Column(name = "TEMPORARY_FLAG")
    private Integer temporaryFlag;

    @DisplayName
    public String getDisplayName() {
        if (onoma == null || eponymo == null || onoma.isEmpty() || eponymo.isEmpty()) {
            return String.valueOf(username);
        }
        return onoma + " " + eponymo;
    }

}
