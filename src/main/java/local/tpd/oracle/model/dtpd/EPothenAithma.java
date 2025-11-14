package local.tpd.oracle.model.dtpd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import local.tpd.oracle.validation.AfmConstraint;
import lombok.Data;
import lombok.Getter;
import tech.ailef.snapadmin.external.annotations.Filterable;

import java.time.LocalDate;

import static local.tpd.oracle.validation.GreekValidationMessages.NINE_DIGITS_LENGTH_MESSAGE;

@Data
@Entity
@Getter
@Table(name = "E_POTHEN_AITHMA", schema = "DTPD")
public class EPothenAithma {

    @Id
    @Column(name = "REQUEST_ID", nullable = false)
    private String REQUEST_ID;

    @Filterable
    @AfmConstraint
    @Column(name = "AFM")
    @Size(min = 9, max = 9, message = NINE_DIGITS_LENGTH_MESSAGE)
    private String afm;

    @Filterable
    @Column(name = "NAME")
    private String name;

    @Filterable
    @Column(name = "SURNAME")
    private String surname;

    @Filterable
    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Filterable
    @Column(name = "REFERENCE_DATE")
    private LocalDate referenceDate;

    @Filterable
    @Column(name = "REQUEST_TYPE")
    private String requestType;

    @Filterable
    @Column(name = "INSERT_DATE")
    private LocalDate insertDate;

    @Filterable
    @Column(name = "FATHER_NAME")
    private String fatherName;

    @Filterable
    @Column(name = "MOTHER_NAME")
    private String motherName;

    @Filterable
    @Column(name = "PROCESSED_FLAG")
    private Long processedFlag;

    @Filterable
    @Column(name = "PROCESSED_DATE")
    private LocalDate processedDate;

    @Filterable
    @Column(name = "RESULT_CODE")
    private Long resultCode;

    @Filterable
    @Column(name = "RESULT_MESSAGE")
    private String resultMessage;

}
