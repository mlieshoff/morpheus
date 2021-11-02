package morpheus;

import morpheus.gen.model.EntityType;

public class JavaReplacer implements Replacer {

  @Override
  public String replaceFilename(String s, EntityType entityType) {
    return String.format(s, entityType.getName());
  }

  @Override
  public String getPackageName(GeneratorProperties generatorProperties) {
    return generatorProperties.getSourceDirectory().replace("/", ".");
  }

}
