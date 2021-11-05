package morpheus;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.WordUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import morpheus.gen.model.AttributeType;
import morpheus.gen.model.ContextType;
import morpheus.gen.model.EntityType;

@Slf4j
public class JavaReplacer implements Replacer {

  @Override
  public String replaceFilename(String s, EntityType entityType) {
    return String.format(s, entityType.getName());
  }

  @Override
  public String getPackageName(GeneratorProperties generatorProperties) {
    return generatorProperties.getSourceDirectory().replace("/", ".");
  }

  @Override
  public String getTypeName(String s) {
    if (isBlank(s)) {
      return "void";
    }
    s = s.replace("(", "<").replace(")", ">");
    return WordUtils.capitalize(s);
  }

  @Override
  public String getAttributeName(String s) {
    return WordUtils.uncapitalize(s);
  }

  @Override
  public String getAliasName(AttributeType attributeType) {
    return isBlank(attributeType.getAlias()) ? attributeType.getName() : attributeType.getAlias();
  }

  private Map<Object, Map<String, String>> contextCache = new HashMap<>();

  @Override
  public String findContextValue(List<ContextType> contextTypes, String key) {
    if (CollectionUtils.isNotEmpty(contextTypes)) {
      for (ContextType contextType : contextTypes) {
        if (key.equals(contextType.getKey())) {
          return contextType.getValue();
        }
      }
    }
    return null;
  }

  @Override
  public String getMethodName(String s) {
    return WordUtils.uncapitalize(s);
  }

  @Override
  public String concatAttributes(List<AttributeType> attributeTypes, String delimiter, String format) {
    if (CollectionUtils.isNotEmpty(attributeTypes)) {
      StringBuilder s = new StringBuilder();
      for (int i = 0, n = attributeTypes.size(); i < n; i++) {
        AttributeType attributeType = attributeTypes.get(i);
        s.append(format
            .replace("{type}", getTypeName(attributeType.getType()))
            .replace("{name}", getAttributeName(attributeType.getName()))
        );
        if (i < n - 1) {
          s.append(delimiter);
        }
      }
      return s.toString();
    }
    return null;
  }

}
