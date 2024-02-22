package morpheus;

import static java.util.stream.Collectors.toList;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import morpheus.gen.model.AttributeType;
import morpheus.gen.model.EntityType;
import morpheus.gen.model.MethodType;
import morpheus.gen.model.ModelType;
import morpheus.gen.model.StereotypeType;

@Slf4j
public class Helper {

  @Getter
  private final String version;

  private final Map<Key, EntityType> entityTypeMap = new HashMap<>();

  public Helper(ModelType modelType) {
    this.version = modelType.getVersion();
    for (EntityType entityType : modelType.getEntity()) {
      entityTypeMap.put(new Key(entityType.getScope(), entityType.getName()), entityType);
    }
  }

  public EntityType getEntity(String name) {
    EntityType entityType = entityTypeMap.get(new Key("default", name));
    if (entityType == null) {
      log.warn("entity for '{}' not found!", name);
    }
    return entityType;
  }

  public EntityType getEntity(String scope, String name) {
    EntityType entityType = entityTypeMap.get(new Key(scope, name));
    if (entityType == null) {
      log.warn("entity for '{}/{}' not found!", scope, name);
    }
    return entityType;
  }

  public boolean isStereotype(String stereotypeName, String scope, String typeName) {
    return isStereotype(stereotypeName, getEntity(scope, typeName));
  }

  public boolean isStereotype(String stereotypeName, EntityType entityType) {
    if (entityType != null) {
      for (StereotypeType stereotypeType : entityType.getStereotype()) {
        if (stereotypeType.getName().equals(stereotypeName)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isStereotype(String stereotypeName, AttributeType attributeType) {
    if (attributeType != null) {
      for (StereotypeType stereotypeType : attributeType.getStereotype()) {
        if (stereotypeType.getName().equals(stereotypeName)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isStereotype(String stereotypeName, MethodType methodType) {
    if (methodType != null) {
      for (StereotypeType stereotypeType : methodType.getStereotype()) {
        if (stereotypeType.getName().equals(stereotypeName)) {
          return true;
        }
      }
    }
    return false;
  }

  public Map<String, String> getStereotypes(String stereotypeName, String scope, String typeName) {
    return getStereotypes(stereotypeName, getEntity(scope, typeName));
  }

  public Map<String, String> getStereotypes(String stereotypeName, EntityType entityType) {
    Map<String, String> map = new LinkedHashMap<>();
    if (entityType != null) {
      for (StereotypeType stereotypeType : entityType.getStereotype()) {
        if (stereotypeType.getName().equals(stereotypeName)) {
          map.put(stereotypeType.getKey(), stereotypeType.getValue());
        }
      }
    }
    return map;
  }

  public Map<String, String> getStereotypes(String stereotypeName, AttributeType attributeType) {
    Map<String, String> map = new LinkedHashMap<>();
    if (attributeType != null) {
      for (StereotypeType stereotypeType : attributeType.getStereotype()) {
        if (stereotypeType.getName().equals(stereotypeName)) {
          map.put(stereotypeType.getKey(), stereotypeType.getValue());
        }
      }
    }
    return map;
  }

  public Map<String, String> getStereotypes(String stereotypeName, MethodType methodType) {
    Map<String, String> map = new LinkedHashMap<>();
    if (methodType != null) {
      for (StereotypeType stereotypeType : methodType.getStereotype()) {
        if (stereotypeType.getName().equals(stereotypeName)) {
          map.put(stereotypeType.getKey(), stereotypeType.getValue());
        }
      }
    }
    return map;
  }

  public List<AttributeType> getMandatoryAttributes(EntityType entityType) {
    return entityType.getAttribute().stream().filter(attributeType -> !attributeType.isOptional()).collect(toList());
  }

  public List<AttributeType> getOptionalAttributes(EntityType entityType) {
    return entityType.getAttribute().stream().filter(AttributeType::isOptional).collect(toList());
  }

  public List<AttributeType> getAttributesWithStereotype(
      String stereotypeTypeName, EntityType entityType) {
    return entityType.getAttribute().stream()
        .filter(attributeType -> isStereotype(stereotypeTypeName, attributeType))
        .collect(toList());
  }

  public String mapKeyValueCommaSeparated(Map<String, String> map) {
    List<String> list = new ArrayList<>();
    map.forEach(
        (key, value) -> {
          list.add("\"" + key + "\"");
          list.add("\"" + value + "\"");
        });
    return String.join(",", list);
  }

  record Key (String scope, String name) {}
}
