package local.tpd.oracle.model.thyr;

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
import lombok.Data;
import lombok.Getter;
import tech.ailef.snapadmin.external.annotations.DisableCreate;
import tech.ailef.snapadmin.external.annotations.DisableDelete;
import tech.ailef.snapadmin.external.annotations.DisableEdit;
import tech.ailef.snapadmin.external.annotations.Filterable;

import java.time.LocalDate;

import static local.tpd.oracle.validation.GreekValidationMessages.MAX_SIZE_MESSAGE;
import static local.tpd.oracle.validation.GreekValidationMessages.NOT_NULL_MESSAGE;

@Data
@Entity
@Getter
@DisableEdit
@DisableCreate
@DisableDelete
@Table(name = "SYMBOLAIO", schema = "THYR")
public class Symbolaio {

    @Id
    @Column(name = "SYMBOLAIO_ACTIVE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "symbolaio_generator")
    @SequenceGenerator(
            name = "symbolaio_generator",
            sequenceName = "SEQ_SYMBOLAIO",
            allocationSize = 1,
            schema = "THYR"
    )
    private long SYMBOLAIO_ACTIVE_ID;

    @ManyToOne
    @Filterable
    @NotNull(message = NOT_NULL_MESSAGE)
    @JoinColumn(name = "THYRIDA_ID", referencedColumnName = "THYRIDA_ID", nullable = false)
    private Thyrida thyridaId;

    @Filterable
    @Column(name = "SYMBOLAIO_STATUS")
    private Character symbolaioStatus;

    @Filterable
    @Column(name = "DIARKEIA_SYMBOL_MHNES")
    private Integer diarkeiaSymbolMhnes;

    @Filterable
    @Column(name = "AR_FAKELOU")
    private Integer arFakelou;

    @Filterable
    @Column(name = "PROHG_AR_SYMBOLAIOU")
    private Integer prohgArSymbolaiou;

    @ManyToOne
    @Filterable
    @JoinColumn(name = "KATHG_MIST_ID", referencedColumnName = "KATHG_MIST_ID")
    private MisthwmaKathgoria kathgMistId;

    @Filterable
    @Column(name = "HMNIA_ANAN_MIST")
    private LocalDate hmniaAnanMist;

    @Filterable
    @NotNull(message = NOT_NULL_MESSAGE)
    @Column(name = "HMNIA_MISTHOSHS", nullable = false)
    private LocalDate hmniaMisthoshs;

    @Filterable
    @Column(name = "KATASTASI")
    private Integer katastasi;

    @ManyToOne
    @Filterable
    @NotNull(message = NOT_NULL_MESSAGE)
    @JoinColumn(name = "PHYSIKA_PROSOPA_ID", referencedColumnName = "PHYSIKA_PROSOPA_ID", nullable = false)
    private PhysikaProsopa physikaProsopaId;

    @ManyToOne
    @Filterable
    @JoinColumn(name = "PHYSIKA_PROSOPA_ID_A1", referencedColumnName = "PHYSIKA_PROSOPA_ID")
    private PhysikaProsopa physikaProsopaIdA1;

    @ManyToOne
    @Filterable
    @JoinColumn(name = "PHYSIKA_PROSOPA_ID_A2", referencedColumnName = "PHYSIKA_PROSOPA_ID")
    private PhysikaProsopa physikaProsopaIdA2;

    @ManyToOne
    @Filterable
    @JoinColumn(name = "PHYSIKA_PROSOPA_ID_A3", referencedColumnName = "PHYSIKA_PROSOPA_ID")
    private PhysikaProsopa physikaProsopaIdA3;

    @Filterable
    @Column(name = "USER_COMMENT")
    @Size(max = 300, message = MAX_SIZE_MESSAGE)
    private String userComment;

    @Filterable
    @Column(name = "ADMIN_COMMENT")
    @Size(max = 300, message = MAX_SIZE_MESSAGE)
    private String adminComment;

    @NotNull(message = NOT_NULL_MESSAGE)
    @Filterable
    @Column(name = "HMNIA_END", nullable = false)
    private LocalDate hmniaEnd;

}
