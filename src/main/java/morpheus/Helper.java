package morpheus;

import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import morpheus.gen.model.EntityType;
import morpheus.gen.model.ModelType;
import morpheus.gen.model.StereotypeType;

public class Helper {

  private final Map<Key, EntityType> entityTypeMap = new HashMap<>();

  public Helper(ModelType modelType) {
    for (EntityType entityType : modelType.getEntity()) {
      entityTypeMap.put(new Key(entityType.getScope(), entityType.getName()), entityType);
    }
  }

  public EntityType getEntity(String name) {
    return entityTypeMap.get(new Key("default", name));
  }

  public EntityType getEntity(String scope, String name) {
    return entityTypeMap.get(new Key(scope, name));
  }

  public boolean isStereotype(String stereotypeName, String scope, String typeName) {
    EntityType entityType = getEntity(scope, typeName);
    if (entityType != null) {
      for (StereotypeType stereotypeType : entityType.getStereotype()) {
        if (stereotypeType.getName().equals(stereotypeName)) {
          return true;
        }
      }
    }
    return false;
  }

  @EqualsAndHashCode
  @RequiredArgsConstructor
  static class Key {
    final String scope;
    final String name;
  }
}
