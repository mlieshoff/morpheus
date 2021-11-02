package morpheus;

import morpheus.gen.model.EntityType;

public interface Replacer {

  String replaceFilename(String s, EntityType entityType);

  String getPackageName(GeneratorProperties generatorProperties);

  String getMethodName(String s);

  String getGetterMethodName(String s);

  String getReturnTypeName(String s);

  String getMethodParameterTypeName(String s);

  String getMethodParameterName(String s);

}
