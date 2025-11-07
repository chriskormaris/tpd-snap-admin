package local.tpd.oracle.model.thyr;

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
import tech.ailef.snapadmin.external.annotations.DisableCreate;
import tech.ailef.snapadmin.external.annotations.DisableDelete;
import tech.ailef.snapadmin.external.annotations.DisableEdit;
import tech.ailef.snapadmin.external.annotations.DisplayName;
import tech.ailef.snapadmin.external.annotations.Filterable;

import static local.tpd.oracle.validation.GreekValidationMessages.MAX_SIZE_MESSAGE;

@Data
@Entity
@Getter
@DisableEdit
@DisableCreate
@DisableDelete
@Table(name = "MISTHOMA_KATHGORIA", schema = "THYR")
public class MisthwmaKathgoria {

    @Id
    @Column(name = "KATHG_MIST_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "misthoma_kathgoria_generator")
    @SequenceGenerator(
            name = "misthoma_kathgoria_generator",
            sequenceName = "SEQ_MISTHOMA_KATHGORIA",
            allocationSize = 1,
            schema = "THYR"
    )
    private long KATHG_MIST_ID;

    @Filterable
    @Column(name = "KATHG_TIMHS")
    @Size(max = 50, message = MAX_SIZE_MESSAGE)
    private String kathgTimhs;

    @DisplayName
    public String getDisplayName() {
        if (kathgTimhs == null || kathgTimhs.isEmpty()) {
            return String.valueOf(KATHG_MIST_ID);
        }
        return kathgTimhs;
    }

}
