package morpheus;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXB;
import lombok.extern.slf4j.Slf4j;
import morpheus.gen.model.EntityType;
import morpheus.gen.model.ModelType;

@Slf4j
public class Morpheus {

  private ModelType modelType;

  private Set<GeneratorProperties> properties = new HashSet<>();

  private VelocityEngine velocityEngine;

  private Map<String, Replacer> replacers = new HashMap<>();

  public static void main(String[] args) {
    new Morpheus().start(args);
  }

  private void start(String[] args) {
    replacers.put("java", new JavaReplacer());

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
  }

  private void loadGeneratorProperties(File generatorPropertiesDir) {
    log.info("load generator properties: {}", generatorPropertiesDir.getPath());
    Collection<File> files = FileUtils.listFiles(generatorPropertiesDir, new String[]{"properties"}, true);
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
    log.info("start generation...");
    for (GeneratorProperties entry : properties) {
      generate(entry);
    }
  }

  private void generate(GeneratorProperties generatorProperties) {
    log.info(" generate {}...", generatorProperties.getName());
    VelocityContext velocityContext = generatorProperties.getVelocityContext();
    Template template = velocityEngine.getTemplate(generatorProperties.getTemplateName());
    for (EntityType entityType : modelType.getEntity()) {
      if (isStereotypeApplyingFor(entityType, generatorProperties.getForStereotype())) {
        Replacer replacer = findReplacer(generatorProperties.getLanguage());
        StringWriter stringWriter = new StringWriter();
        String filename = replacer.replaceFilename(generatorProperties.getFilenamePattern(), entityType);
        velocityContext.internalPut("filename", filename);
        velocityContext.internalPut("entity", entityType);
        velocityContext.internalPut("properties", generatorProperties);
        velocityContext.internalPut("helper", replacer);
        template.merge(velocityContext, stringWriter);
        String outputDirectoryName = generatorProperties.getOutputDirectory();
        String sourceDirectoryName = generatorProperties.getSourceDirectory();
        File baseDir = new File(".");
        File outputDirectory = new File(baseDir, outputDirectoryName);
        File sourceDirectory = new File(outputDirectory, sourceDirectoryName);
        File file = new File(sourceDirectory, filename + "." + generatorProperties.getExtension());
        try {
          FileUtils.forceMkdir(sourceDirectory);
          FileUtils.write(file, stringWriter.toString(), StandardCharsets.UTF_8);
          log.info("write file {}", file.getAbsolutePath());
        } catch (IOException e) {
          log.error("error writing file {}", file.getAbsolutePath(), e);
        }
      }
    }
  }

  private Replacer findReplacer(String language) {
    return replacers.get(language);
  }

  private boolean isStereotypeApplyingFor(EntityType entityType, String forStereotype) {
    return entityType.getStereotype().stream().anyMatch(stereotype -> stereotype.getName().equals(forStereotype));
  }

}
