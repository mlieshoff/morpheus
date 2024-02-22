
package morpheus.gen.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stereotypeType", propOrder = {
    "context"
})
public class StereotypeType {

    @XmlElement(required = true)
    protected ContextType context;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "key")
    protected String key;

    @XmlAttribute(name = "value")
    protected String value;

    public void setContext(ContextType value) {
        this.context = value;
    }

    public void setName(String value) {
        this.name = value;
    }

}
