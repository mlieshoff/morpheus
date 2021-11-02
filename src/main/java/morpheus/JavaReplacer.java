package morpheus;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.apache.commons.lang.WordUtils;

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

  @Override
  public String getGetterMethodName(String s) {
    return "get" + WordUtils.capitalize(s);
  }

  @Override
  public String getMethodName(String s) {
    return "";
  }

  @Override
  public String getReturnTypeName(String s) {
    return isBlank(s) ? "void" : s;
  }

  @Override
  public String getMethodParameterTypeName(String s) {
    return WordUtils.capitalize(s);
  }

  @Override
  public String getMethodParameterName(String s) {
    return WordUtils.uncapitalize(s);
  }

}
