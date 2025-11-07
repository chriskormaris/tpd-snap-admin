package local.tpd.oracle.model.thyr;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import tech.ailef.snapadmin.external.annotations.DisplayName;
import tech.ailef.snapadmin.external.annotations.Filterable;

import static local.tpd.oracle.validation.GreekValidationMessages.NOT_NULL_MESSAGE;

@Data
@Entity
@Getter
@Table(name = "THYRIDA", schema = "THYR")
public class Thyrida {

    @Id
    @Column(name = "THYRIDA_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "thyrida_generator")
    @SequenceGenerator(
            name = "thyrida_generator",
            sequenceName = "SEQ_THYRIDA",
            allocationSize = 1,
            schema = "THYR"
    )
    private long THYRIDA_ID;

    @Filterable
    @NotNull(message = NOT_NULL_MESSAGE)
    @Column(name = "THYRIDA_TYPOS", nullable = false)
    private Character thyridaTypos;

    @Filterable
    @Column(name = "SYMBOLAIO_ACTIVE_ID")
    private Long symbolaioActiveId;

    @Filterable
    @Column(name = "THYRIDA_STATUS")
    private Character thyridaStatus;

    @Filterable
    @Column(name = "YPOKATASTHMA")
    private Integer ypokatasthma;

    @Filterable
    @Column(name = "THYRIDA_NUM")
    private Integer thyridaNum;

    @DisplayName
    public String getDisplayName() {
        StringBuilder output = new StringBuilder();
        output.append(thyridaTypos);
        if (thyridaNum != null) {
            output.append(thyridaNum);
        }
        return output.toString();
    }

}
