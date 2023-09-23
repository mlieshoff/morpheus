
package morpheus.gen.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contextType", propOrder = {
    "stereotype"
})
public class ContextType {

    protected List<StereotypeType> stereotype;

    @Getter
    @XmlAttribute(name = "key")
    protected String key;

    @Getter
    @XmlAttribute(name = "value")
    protected String value;

    public List<StereotypeType> getStereotype() {
        if (stereotype == null) {
            stereotype = new ArrayList<>();
        }
        return this.stereotype;
    }

    public void setKey(String value) {
        this.key = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
