package morpheus;

import morpheus.gen.model.AttributeType;
import morpheus.gen.model.EntityType;

public interface Replacer {

  String replaceFilename(String s, EntityType entityType);

  String getPackageName(GeneratorProperties generatorProperties);

  String getMethodName(String s);

  String getGetterMethodName(String s);

  String getTypeName(String s);

  String getAttributeName(String s);

  String getAliasName(AttributeType attributeType);

}
