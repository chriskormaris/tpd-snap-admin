package local.tpd.oracle.model.thyr;

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
import tech.ailef.snapadmin.external.annotations.DisableCreate;
import tech.ailef.snapadmin.external.annotations.DisableDelete;
import tech.ailef.snapadmin.external.annotations.DisableEdit;
import tech.ailef.snapadmin.external.annotations.DisplayName;
import tech.ailef.snapadmin.external.annotations.Filterable;

import java.time.LocalDate;

import static local.tpd.oracle.validation.GreekValidationMessages.FIVE_DIGITS_LENGTH_MESSAGE;
import static local.tpd.oracle.validation.GreekValidationMessages.MAX_SIZE_MESSAGE;
import static local.tpd.oracle.validation.GreekValidationMessages.NINE_DIGITS_LENGTH_MESSAGE;

@Data
@Entity
@Getter
@DisableEdit
@DisableCreate
@DisableDelete
@Table(name = "PHYSIKA_PROSOPA", schema = "THYR")
public class PhysikaProsopa {

    @Id
    @Column(name = "PHYSIKA_PROSOPA_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "physika_prosopa_generator")
    @SequenceGenerator(
            name = "physika_prosopa_generator",
            sequenceName = "SEQ_PHYSIKA_PROSOPA",
            allocationSize = 1,
            schema = "THYR"
    )
    private long PHYSIKA_PROSOPA_ID;

    @Filterable
    @Column(name = "PELATIS_THYRIDAS_ID")
    private Long pelatisThyridasId;

    @Filterable
    @Column(name = "THYRIDA_ID")
    private Integer thyridaId;

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
    @Column(name = "TOPOS_GENHSHS")
    @Size(max = 50, message = MAX_SIZE_MESSAGE)
    private String toposGenhshs;

    @Filterable
    @Column(name = "HMNIA_GENHSHS")
    private LocalDate hmniaGenhshs;

    @Filterable
    @Column(name = "EPAGELMA")
    @Size(max = 100, message = MAX_SIZE_MESSAGE)
    private String epagelma;

    @Filterable
    @Column(name = "DIEFTHINSH_ARITHMOS")
    @Size(max = 100, message = MAX_SIZE_MESSAGE)
    private String diefthinshArithmos;

    @Filterable
    @Column(name = "TK")
    @Size(min = 5, max = 5, message = FIVE_DIGITS_LENGTH_MESSAGE)
    private String tk;

    @Filterable
    @Column(name = "POLH")
    @Size(max = 30, message = MAX_SIZE_MESSAGE)
    private String polh;

    @Filterable
    @Column(name = "XORA")
    @Size(max = 30, message = MAX_SIZE_MESSAGE)
    private String xora;

    @Filterable
    @Column(name = "THLEFONA")
    @Size(max = 50, message = MAX_SIZE_MESSAGE)
    private String thlefona;

    @Filterable
    @Column(name = "AST_TAFTOTHTA")
    @Size(max = 20, message = MAX_SIZE_MESSAGE)
    private String astTaftothta;

    @Filterable
    @Column(name = "EKD_ARXH")
    @Size(max = 50, message = MAX_SIZE_MESSAGE)
    private String ekdArxh;

    @Filterable
    @AfmConstraint
    @Column(name = "AFM", unique = true)
    @Size(min = 9, max = 9, message = NINE_DIGITS_LENGTH_MESSAGE)
    private String afm;

    @Filterable
    @Column(name = "DOY")
    @Size(max = 50, message = MAX_SIZE_MESSAGE)
    private String doy;

    @Filterable
    @Column(name = "STATUS_PELATH")
    private Character statusPelath;

    @Filterable
    @Column(name = "V_TABLE_NAME")
    @Size(max = 35, message = MAX_SIZE_MESSAGE)
    private String vTableName;

    @Filterable
    @Column(name = "V_SYSTEM")
    @Size(max = 5, message = MAX_SIZE_MESSAGE)
    private String vSystem;

    @Filterable
    @Column(name = "V_KEY_NUM")
    private Long vKeyNum;

    @Filterable
    @Column(name = "V_KEY_CHAR")
    @Size(max = 30, message = MAX_SIZE_MESSAGE)
    private String vKeyChar;

    @Filterable
    @Column(name = "CRM_ID")
    private Long crmId;

    @DisplayName
    public String getDisplayName() {
        if (onoma == null & eponymo == null && afm == null) {
            return String.valueOf(PHYSIKA_PROSOPA_ID);
        }
        StringBuilder output = new StringBuilder();
        if (onoma != null && !onoma.isEmpty()) {
            output.append(" ").append(onoma);
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
