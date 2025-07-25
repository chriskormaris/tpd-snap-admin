package local.tpd.oracle.model.dtpd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import tech.ailef.snapadmin.external.annotations.DisableCreate;
import tech.ailef.snapadmin.external.annotations.DisableDelete;
import tech.ailef.snapadmin.external.annotations.DisableEdit;
import tech.ailef.snapadmin.external.annotations.Filterable;

import java.time.LocalDateTime;

@Data
@Entity
@DisableEdit
@DisableCreate
@DisableDelete
@Table(name = "MHTRWO_AUDIT_LOG", schema = "DTPD")
public class MhtrwoAuditLog {

    @Id
    @Column(name = "ID", nullable = false)
    private long ID;

    @Filterable
    @Column(name = "MESSAGE")
    private String message;

    @Filterable
    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    @Filterable
    @Column(name = "TABLE_NAME")
    private String tableName;

}
