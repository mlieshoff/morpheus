package morpheus;

import static morpheus.Preconditions.notEmpty;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeneratorProperties {

  private final File file;

  @Getter
  private final VelocityContext velocityContext = new VelocityContext();

  private final Properties properties = new Properties();

  public GeneratorProperties(File file) {
    this.file = file;
  }

  public void init() {
    try {
      properties.load(new FileReader(file));
    } catch (IOException e) {
      log.error("cannot load generator properties: {}", file.getPath());
    }
    for (Entry<Object, Object> entry : properties.entrySet()) {
      velocityContext.put(entry.getKey().toString(), entry.getValue().toString());
    }
    notEmpty("forStereotype", getForStereotype());
    notEmpty("outputDirectory", getOutputDirectory());
    notEmpty("sourceDirectory", getSourceDirectory());
    notEmpty("filenamePattern", getFilenamePattern());
    notEmpty("extension", getExtension());
    notEmpty("template", getTemplateName());
    notEmpty("language", getLanguage());
    velocityContext.put("StringUtils", StringUtils.class);
    velocityContext.put("WordUtils", WordUtils.class);
  }

  public String getName() {
    return file.getName();
  }

  public String getTemplateName() {
    return properties.getProperty("templateName");
  }

  public String getForStereotype() {
    return properties.getProperty("forStereotype");
  }

  public String getOutputDirectory() {
    return properties.getProperty("outputDirectory");
  }

  public String getSourceDirectory() {
    return properties.getProperty("sourceDirectory");
  }

  public String getFilenamePattern() {
    return properties.getProperty("filenamePattern");
  }

  public String getExtension() {
    return properties.getProperty("extension");
  }

  public String getLanguage() {
    return properties.getProperty("language");
  }

}
