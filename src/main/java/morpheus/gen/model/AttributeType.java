
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
@XmlType(name = "attributeType", propOrder = {
    "stereotype",
    "context"
})
public class AttributeType {

    protected List<StereotypeType> stereotype;
    protected List<ContextType> context;
    @Getter
    @XmlAttribute(name = "name")
    protected String name;
    @Getter
    @XmlAttribute(name = "alias")
    protected String alias;
    @Getter
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "scope")
    protected String scope;
    @XmlAttribute(name = "optional")
    protected Boolean optional;

    public List<StereotypeType> getStereotype() {
        if (stereotype == null) {
            stereotype = new ArrayList<>();
        }
        return this.stereotype;
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

    public void setAlias(String value) {
        this.alias = value;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getScope() {
      return Objects.requireNonNullElse(scope, "default");
    }

    public void setScope(String value) {
        this.scope = value;
    }

    public boolean isOptional() {
      return Objects.requireNonNullElse(optional, false);
    }

    public void setOptional(Boolean value) {
        this.optional = value;
    }

}
