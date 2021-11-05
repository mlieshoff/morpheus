package morpheus;

import java.util.List;
import morpheus.gen.model.AttributeType;
import morpheus.gen.model.ContextType;
import morpheus.gen.model.EntityType;

public interface Replacer {

  String replaceFilename(String s, EntityType entityType);

  String getPackageName(GeneratorProperties generatorProperties);

  String getMethodName(String s);

  String getTypeName(String s);

  String getAttributeName(String s);

  String getAliasName(AttributeType attributeType);

  String findContextValue(List<ContextType> contextTypes, String key);

  String concatAttributes(List<AttributeType> attributeTypes, String delimiter, String format);

}
