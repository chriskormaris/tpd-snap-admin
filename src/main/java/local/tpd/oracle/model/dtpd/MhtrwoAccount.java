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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import local.tpd.oracle.validation.IbanConstraint;
import lombok.Data;
import tech.ailef.snapadmin.external.annotations.DisplayFormat;
import tech.ailef.snapadmin.external.annotations.Filterable;

import static local.tpd.oracle.validation.GreekValidationMessages.NOT_NULL_MESSAGE;
import static local.tpd.oracle.validation.GreekValidationMessages.TWENTY_SEVEN_DIGITS_LENGTH_MESSAGE;

@Data
@Entity
@Table(name = "MHTRWO_ACCOUNT", schema = "DTPD")
public class MhtrwoAccount {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mhtrwo_account_generator")
    @SequenceGenerator(
            name = "mhtrwo_account_generator",
            sequenceName = "MHTRWO_ACCOUNT_SEQ",
            allocationSize = 1,
            schema = "DTPD"
    )
    private long ID;

    @ManyToOne
    @Filterable
    @NotNull(message = NOT_NULL_MESSAGE)
    @JoinColumn(name = "MHTRWO_ID", referencedColumnName = "ID", nullable = false)
    private Mhtrwo mhtrwo;

    @Filterable
    @IbanConstraint
    @Column(name = "IBAN", unique = true)
    @Size(min = 27, max = 27, message = TWENTY_SEVEN_DIGITS_LENGTH_MESSAGE)
    private String iban;

    @Filterable
    @Column(name = "BALANCE")
    @DisplayFormat(format = "€%.2f")
    private Double balance;

}
