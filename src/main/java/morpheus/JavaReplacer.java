package morpheus;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import morpheus.gen.model.AttributeType;
import morpheus.gen.model.ContextType;
import morpheus.gen.model.EntityType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.text.WordUtils;

@Slf4j
@RequiredArgsConstructor
public class JavaReplacer implements Replacer {

  public static final Set<String> atomics =
      new HashSet<>(
          asList("byte", "boolean", "short", "char", "int", "float", "long", "double", "String"));

  private final Helper helper;

  @Override
  public String replaceFilename(String s, EntityType entityType) {
    return replaceEntityTypeProps(String.format(s, entityType.getName()), entityType);
  }

  private String replaceEntityTypeProps(String s, EntityType entityType) {
    if (entityType != null) {
      s = s.replace("{scope}", entityType.getScope().replace(".", "/"));
    }
    return s;
  }

  @Override
  public String getPackageName(GeneratorProperties generatorProperties, EntityType entityType) {
    return replaceEntityTypeProps(generatorProperties.getSourceDirectory(), entityType)
        .replace("/", ".");
  }

  @Override
  public String getTypeName(String s) {
    if (isBlank(s)) {
      return "void";
    }
    s = s.replace("(", "<").replace(")", ">");
    if (!atomics.contains(s)) {
      return WordUtils.capitalize(s);
    }
    return s;
  }

  @Override
  public String getAttributeName(String s) {
    return WordUtils.uncapitalize(s);
  }

  @Override
  public String getAliasName(AttributeType attributeType) {
    return isBlank(attributeType.getAlias()) ? attributeType.getName() : attributeType.getAlias();
  }

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
  public String concatAttributes(
      List<AttributeType> attributeTypes, String delimiter, String format) {
    return concatAttributes(attributeTypes, delimiter, format, EMPTY);
  }

  @Override
  public String concatAttributes(
      List<AttributeType> attributeTypes, String delimiter, String format, String innerDelimiter) {
    delimiter = delimiter.replace("$#LF#$", "\n");
    if (CollectionUtils.isNotEmpty(attributeTypes)) {
      StringBuilder s = new StringBuilder();
      for (int i = 0, n = attributeTypes.size(); i < n; i++) {
        AttributeType attributeType = attributeTypes.get(i);
        String inner =
            format
                .replace("{type}", getTypeName(attributeType.getType()))
                .replace("{name}", getAttributeName(attributeType.getName()));
        String stringifyReplacePrefix = "String.valueOf(";
        String stringifyReplaceSuffix = ")";
        if (getTypeName(attributeType.getType()).equals("String")) {
          stringifyReplacePrefix = "";
          stringifyReplaceSuffix = "";
        }
        if (i < n - 1) {
          inner = inner + innerDelimiter;
        }
        inner =
            inner
                .replace("$#stringify-begin#$", stringifyReplacePrefix)
                .replace("$#stringify-end#$", stringifyReplaceSuffix);
        s.append(inner);
        if (i < n - 1) {
          s.append(delimiter);
        }
      }
      return s.toString();
    }
    return EMPTY;
  }

  @Override
  public String getPackageNamePrefix(
      GeneratorProperties generatorProperties, String scope, String typeName) {
    String basePackage = generatorProperties.getSourceDirectory().replace("/", ".");
    EntityType entityType = helper.getEntity(scope, typeName);
    if (entityType == null) {
      return EMPTY;
    }
    return basePackage.replace("{scope}", scope) + ".";
  }

  @Override
  public String getBasePackageName(GeneratorProperties generatorProperties) {
    String basePackage = generatorProperties.getSourceDirectory().replace("/", ".");
    int index = basePackage.indexOf(".");
    if (index <= 0) {
      index = basePackage.length();
    }
    return basePackage.substring(0, index);
  }

  @Override
  public String getFixtureValue(AttributeType attributeType) {
    String type = attributeType.getType();
    if ("long".equals(type) || "Long".equals(type)) {
      return "4711L";
    } else if ("int".equals(type) || "Integer".equals(type)) {
      return "815";
    } else if ("String".equals(type)) {
      return "\"" + attributeType.getName() + "\"";
    }
    return "null";
  }

  @Override
  public String getVersion() {
    return helper.getVersion();
  }

  @Override
  public String getCurrentDate() {
    return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
  }
}
