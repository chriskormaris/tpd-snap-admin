package local.tpd.oracle.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tech.ailef.snapadmin.external.annotations.DisplayFormat;
import tech.ailef.snapadmin.external.annotations.DisplayName;
import tech.ailef.snapadmin.external.annotations.Filterable;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "MHTRWO")
public class Mhtrwo {

    @Id
    @NotNull
    @Column(name = "ID")
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mhtrwo_generator")
    @SequenceGenerator(name = "mhtrwo_generator", sequenceName = "mhtrwo_seq", allocationSize = 1)
    private long id;

    @Filterable
    @Size(max = 128)
    @Column(name = "ONOMA")
    private String onoma;

    @Filterable
    @Size(max = 128)
    @Column(name = "EPONYMO")
    private String eponymo;

    @Filterable
    @Size(max = 128)
    @Column(name = "PATRONYMO")
    private String patronymo;

    @Filterable
    @Column(name = "AFM")
    private Long afm;

    @Filterable
    @Column(name = "HMNIA")
    private LocalDate hmnia;

    @Filterable
    @Column(name = "SALARY")
    @DisplayFormat(format = "€%.2f")
    private Double salary;

    @DisplayName
    public String getDisplayName() {
        StringBuilder output = new StringBuilder();
        if (onoma != null && !onoma.isEmpty()) {
            output.append(onoma).append(" ");
        }
        if (eponymo != null && !eponymo.isEmpty()) {
            output.append(eponymo).append(" ");
        }
        if (afm != null) {
            output.append(afm);
        }
        return output.toString();
    }

    @Override
    public String toString() {
        return id +
                ",\"" + onoma + "\"" +
                ",\"" + eponymo + "\"" +
                "," + afm + "\"" +
                ",\"" + hmnia + "\"" +
                "," + salary;
    }

}
