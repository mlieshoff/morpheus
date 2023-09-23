
package morpheus.gen.model;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


@XmlRegistry
public class ObjectFactory {

    private final static QName _Model_QNAME = new QName("", "model");

    public ObjectFactory() {
    }

    public ModelType createModelType() {
        return new ModelType();
    }

    public MethodType createMethodType() {
        return new MethodType();
    }

    public EntityType createEntityType() {
        return new EntityType();
    }

    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    public ContextType createContextType() {
        return new ContextType();
    }

    public StereotypeType createStereotypeType() {
        return new StereotypeType();
    }

    public ExtendType createExtendType() {
        return new ExtendType();
    }

    @XmlElementDecl(namespace = "", name = "model")
    public JAXBElement<ModelType> createModel(ModelType value) {
        return new JAXBElement<>(_Model_QNAME, ModelType.class, null, value);
    }

}
