package local.tpd.oracle.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "MHTRWO_AUDIT_LOG", catalog = "", schema = "DTPD")
public class MhtrwoAuditLog {

    @Id
    @NotNull
    @Column(name = "ID")
    @Basic(optional = false)
    private long id;

    @Filterable
    @Size(max = 100)
    @Column(name = "MESSAGE")
    private String message;

    @Filterable
    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    @Filterable
    @Size(max = 100)
    @Column(name = "TABLE_NAME")
    private String tableName;

    @Override
    public String toString() {
        return id +
                ",\"" + message + "\"" +
                ",\"" + timestamp + "\"" +
                ",\"" + tableName + "\"";
    }

}
