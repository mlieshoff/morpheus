package morpheus;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.JavaFormatterOptions;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXB;
import lombok.extern.slf4j.Slf4j;
import morpheus.gen.model.EntityType;
import morpheus.gen.model.ModelType;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

@Slf4j
public class Morpheus {

  private ModelType modelType;

  private Set<GeneratorProperties> properties = new HashSet<>();

  private VelocityEngine velocityEngine;

  private Map<String, Replacer> replacers = new HashMap<>();

  private Helper helper;

  private Formatter formatter;

  public static void main(String[] args) {
    new Morpheus().start(args);
  }

  private void start(String[] args) {
    File modelFile = new File(args[0]);
    File generatorPropertiesDir = new File(args[1]);
    File templatesDir = new File(args[2]);

    loadModel(modelFile);
    loadGeneratorProperties(generatorPropertiesDir);
    loadTemplates(templatesDir);
    generate();
  }

  private void loadModel(File modelFile) {
    log.info("load model: {}", modelFile.getPath());
    modelType = JAXB.unmarshal(modelFile, ModelType.class);
    helper = new Helper(modelType);
    replacers.put("java", new JavaReplacer(helper));
  }

  private void loadGeneratorProperties(File generatorPropertiesDir) {
    log.info("load generator properties: {}", generatorPropertiesDir.getPath());
    Collection<File> files =
        FileUtils.listFiles(generatorPropertiesDir, new String[] {"properties"}, true);
    for (File file : files) {
      GeneratorProperties generatorProperties = new GeneratorProperties(file);
      generatorProperties.init();
      properties.add(generatorProperties);
    }
  }

  private void loadTemplates(File templatesDir) {
    log.info("load templates: {}", templatesDir.getPath());
    velocityEngine = new VelocityEngine();
    velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
    velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, templatesDir.getPath());
  }

  private void generate() {
    formatter =
        new Formatter(
            JavaFormatterOptions.builder()
                .style(JavaFormatterOptions.Style.AOSP)
                .build());
    log.info("start generation...");
    for (GeneratorProperties entry : properties) {
      generate(entry);
    }
  }

  private void generate(GeneratorProperties generatorProperties) {
    log.info(" generate {}...", generatorProperties.getName());
    VelocityContext velocityContext = generatorProperties.getVelocityContext();
    Template template = velocityEngine.getTemplate(generatorProperties.getTemplateName());
    Replacer replacer = findReplacer(generatorProperties.getLanguage());
    if (generatorProperties.getForStereotype().startsWith("all-")) {
      String subStereoType = generatorProperties.getForStereotype().substring("all-".length());
      List<EntityType> entities = new ArrayList<>();
      for (EntityType entityType : modelType.getEntity()) {
        if (isStereotypeApplyingFor(entityType, subStereoType)) {
          entities.add(entityType);
        }
      }
      generateInternally(
          generatorProperties,
          velocityContext,
          template,
          replacer,
          generatorProperties.getFilenamePattern(),
          entities);
    } else {
      for (EntityType entityType : modelType.getEntity()) {
        if (isStereotypeApplyingFor(entityType, generatorProperties.getForStereotype())) {
          String filename =
              replacer.replaceFilename(generatorProperties.getFilenamePattern(), entityType);
          generateInternally(
              generatorProperties, velocityContext, template, replacer, filename, entityType);
        }
      }
    }
  }

  private Replacer findReplacer(String language) {
    return replacers.get(language);
  }

  private boolean isStereotypeApplyingFor(EntityType entityType, String forStereotype) {
    return entityType.getStereotype().stream()
        .anyMatch(stereotype -> stereotype.getName().equals(forStereotype));
  }

  private void generateInternally(
      GeneratorProperties generatorProperties,
      VelocityContext velocityContext,
      Template template,
      Replacer replacer,
      String filenamePattern,
      Object object) {
    String outputDirectoryName = generatorProperties.getOutputDirectory();
    String sourceDirectoryName = generatorProperties.getSourceDirectory();
    String filename = filenamePattern;
    if (object instanceof EntityType) {
      EntityType entityType = (EntityType) object;
      filename = replacer.replaceFilename(filenamePattern, entityType);
      sourceDirectoryName = replacer.replaceFilename(sourceDirectoryName, entityType);
      outputDirectoryName = replacer.replaceFilename(outputDirectoryName, entityType);
    }
    StringWriter stringWriter = new StringWriter();
    velocityContext.internalPut("filename", filename);
    velocityContext.internalPut("object", object);
    velocityContext.internalPut("properties", generatorProperties);
    velocityContext.internalPut("replacer", replacer);
    velocityContext.internalPut("helper", helper);
    template.merge(velocityContext, stringWriter);
    File outputDirectory = createFile(replacer, null, outputDirectoryName, object);
    File sourceDirectory = createFile(replacer, outputDirectory, sourceDirectoryName, object);
    File file =
        createFile(
            replacer, sourceDirectory, filename + "." + generatorProperties.getExtension(), object);
    try {
      FileUtils.forceMkdir(sourceDirectory);
      String source = stringWriter.toString();
      if (file.getName().endsWith(".java")) {
        source = formatter.formatSourceAndFixImports(source);
      }
      FileUtils.write(file, source, StandardCharsets.UTF_8);
      log.info("write file {}", file.getAbsolutePath());
    } catch (IOException | FormatterException e) {
      log.error("error writing file {}", file.getAbsolutePath(), e);
    }
  }

  private File createFile(Replacer replacer, File dir, String filename, Object object) {
    if (object instanceof EntityType) {
      EntityType entityType = (EntityType) object;
      filename = replacer.replaceFilename(filename, entityType);
    }
    return dir == null ? new File(filename) : new File(dir, filename);
  }
}
