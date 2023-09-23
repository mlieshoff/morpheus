
package morpheus.gen.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "extendType", propOrder = {
    "stereotype",
    "context"
})
public class ExtendType {

    protected List<StereotypeType> stereotype;
    protected List<ContextType> context;

    @Getter
    @XmlAttribute(name = "name")
    protected String name;

    @Getter
    @XmlAttribute(name = "type")
    protected String type;


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

    public void setType(String value) {
        this.type = value;
    }

}
