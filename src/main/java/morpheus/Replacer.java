package morpheus;

import morpheus.gen.model.EntityType;

public interface Replacer {

  String replaceFilename(String s, EntityType entityType);

  String getPackageName(GeneratorProperties generatorProperties);

}
