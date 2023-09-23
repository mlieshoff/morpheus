
package morpheus.gen.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "entityType", propOrder = {
    "stereotype",
    "attribute",
    "method",
    "extend",
    "context"
})
public class EntityType {

    protected List<StereotypeType> stereotype;
    protected List<AttributeType> attribute;
    protected List<MethodType> method;
    protected List<ExtendType> extend;
    protected List<ContextType> context;

    @Getter
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "scope")
    protected String scope;

    public List<StereotypeType> getStereotype() {
        if (stereotype == null) {
            stereotype = new ArrayList<>();
        }
        return this.stereotype;
    }

    public List<AttributeType> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<>();
        }
        return this.attribute;
    }

    public List<MethodType> getMethod() {
        if (method == null) {
            method = new ArrayList<>();
        }
        return this.method;
    }

    public List<ExtendType> getExtend() {
        if (extend == null) {
            extend = new ArrayList<>();
        }
        return this.extend;
    }

    public List<ContextType> getContext() {
        if (context == null) {
            context = new ArrayList<>();
        }
        return this.context;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getScope() {
      return Objects.requireNonNullElse(scope, "default");
    }

    public void setScope(String value) {
        this.scope = value;
    }

}
