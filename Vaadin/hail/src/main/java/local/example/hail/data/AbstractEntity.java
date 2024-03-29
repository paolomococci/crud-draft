package local.example.hail.data;

import com.vaadin.fusion.Nonnull;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue
    @Nonnull
    private Long id;

    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractEntity other)) {
            return false;
        }

        if (id != null) {
            return id.equals(other.id);
        }
        return super.equals(other);
    }
}
