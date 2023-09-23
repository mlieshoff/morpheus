
package morpheus.gen.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "modelType", propOrder = {
    "stereotype",
    "entity",
    "context"
})
public class ModelType {

    protected List<StereotypeType> stereotype;
    protected List<EntityType> entity;
    protected List<ContextType> context;
    @Getter
    @XmlAttribute(name = "version")
    protected String version;

    public List<StereotypeType> getStereotype() {
        if (stereotype == null) {
            stereotype = new ArrayList<>();
        }
        return this.stereotype;
    }

    public List<EntityType> getEntity() {
        if (entity == null) {
            entity = new ArrayList<>();
        }
        return this.entity;
    }

    public List<ContextType> getContext() {
        if (context == null) {
            context = new ArrayList<>();
        }
        return this.context;
    }

}
