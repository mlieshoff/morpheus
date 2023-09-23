
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
@XmlType(name = "methodType", propOrder = {
    "stereotype",
    "attribute",
    "context"
})
public class MethodType {

    protected List<StereotypeType> stereotype;
    protected List<AttributeType> attribute;
    protected List<ContextType> context;
    @XmlAttribute(name = "return")
    protected String _return;

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


    public List<ContextType> getContext() {
        if (context == null) {
            context = new ArrayList<>();
        }
        return this.context;
    }

    public String getReturn() {
        return _return;
    }

    public void setReturn(String value) {
        this._return = value;
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
