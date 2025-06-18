package local.tpd.oracle.model.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import local.tpd.oracle.validation.AfmConstraint;
import lombok.Data;
import lombok.Getter;
import tech.ailef.snapadmin.external.annotations.DisplayFormat;
import tech.ailef.snapadmin.external.annotations.DisplayName;
import tech.ailef.snapadmin.external.annotations.Filterable;

import java.time.LocalDate;

import static local.tpd.oracle.validation.GreekValidationMessages.MAX_SIZE_MESSAGE;
import static local.tpd.oracle.validation.GreekValidationMessages.NINE_DIGITS_LENGTH_MESSAGE;

@Data
@Entity
@Getter
@Table(name = "MHTRWO")
public class Mhtrwo {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mhtrwo_generator")
    @SequenceGenerator(name = "mhtrwo_generator", sequenceName = "MHTRWO_SEQ", allocationSize = 1)
    private long ID;

    @Filterable
    @Column(name = "ONOMA")
    @Size(max = 50, message = MAX_SIZE_MESSAGE)
    private String onoma;

    @Filterable
    @Column(name = "EPONYMO")
    @Size(max = 80, message = MAX_SIZE_MESSAGE)
    private String eponymo;

    @Filterable
    @Column(name = "PATRONYMO")
    @Size(max = 50, message = MAX_SIZE_MESSAGE)
    private String patronymo;

    @Filterable
    @AfmConstraint
    @Column(name = "AFM", unique = true)
    @Size(min = 9, max = 9, message = NINE_DIGITS_LENGTH_MESSAGE)
    private String afm;

    @Filterable
    @Column(name = "HMNIA_GENHSHS")
    private LocalDate hmniaGenhshs;

    @Filterable
    @Column(name = "SALARY")
    @DisplayFormat(format = "€%.2f")
    private Double salary;

    @DisplayName
    public String getDisplayName() {
        if (onoma == null & eponymo == null && afm == null) {
            return String.valueOf(ID);
        }
        StringBuilder output = new StringBuilder();
        if (onoma != null && !onoma.isEmpty()) {
            output.append(onoma);
        }
        if (eponymo != null && !eponymo.isEmpty()) {
            output.append(" ").append(eponymo);
        }
        if (afm != null) {
            output.append(" ").append(afm);
        }
        return output.toString();
    }

}
