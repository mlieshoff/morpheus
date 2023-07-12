package morpheus;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import morpheus.gen.model.AttributeType;
import morpheus.gen.model.EntityType;
import morpheus.gen.model.ModelType;
import morpheus.gen.model.StereotypeType;

@Slf4j
public class Helper {

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

  public List<AttributeType> getMandatoryAttributes(EntityType entityType) {
    return entityType.getAttribute().stream().filter(attributeType -> !attributeType.isOptional()).collect(toList());
  }

  public List<AttributeType> getOptionalAttributes(EntityType entityType) {
    return entityType.getAttribute().stream().filter(attributeType -> attributeType.isOptional()).collect(toList());
  }

  public String getVersion() {
    return version;
  }

  @EqualsAndHashCode
  @RequiredArgsConstructor
  static class Key {
    final String scope;
    final String name;
  }
}
