package com.smarttech.agribot.nlp;

import com.smarttech.agribot.dto.InfoDto;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.namefind.*;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class IntentTrainer {

  private static DocumentCategorizerME categorizer;

  NameFinderME[] nameFinderMEs;

  private static final Logger LOGGER = LoggerFactory.getLogger(IntentTrainer.class);

  @PostConstruct
  public void loadModels() {
    try {
      trainModels();
    } catch (Exception e) {
      LOGGER.error("Exception occurred while training models", e);
    }
  }

  public void trainModels() throws Exception {

    try {
      //Path path = Path.of(this.getClass().getResource("/tmp/models/train").toURI());
      //URL resource = getClass().getClassLoader().getResource("/models/train");
      //URL resource2 = getClass().getResource("todays-egg-rate.txt");

      File trainingDirectory = ResourceUtils.getFile("/tmp/models/train").getAbsoluteFile();
      //File trainingDirectory = Path.of(this.getClass().getResource("/tmp/models/train").toURI()).toFile();
      //File trainingDirectory = new File("/Users/jayachandranputtalin/Desktop/agribotAssets/models/train");
      //File trainingDirectory = path.toFile();
      // String [] entityTags =
      // coroverConfig.getPartnerConfig().getEntityTags().split(",");
      String[] entityTags = new String[]{"item"};

      /* defining intent with tokenizing */
      List<ObjectStream<DocumentSample>> categoryStreams = new ArrayList<>();
      for (File trainingFile : trainingDirectory.listFiles()) {
        LOGGER.info("training file " + trainingFile);
        String intent = trainingFile.getName().replaceFirst("[.][^.]+$", "");
        ObjectStream<String> lineStream = new PlainTextByLineStream(
          new MarkableFileInputStreamFactory(trainingFile), "UTF-8");
        ObjectStream<DocumentSample> documentSampleStream = new IntentDocumentSampleStream(intent, lineStream);
        categoryStreams.add(documentSampleStream);
      }

      ObjectStream<DocumentSample> combinedDocumentSampleStream = ObjectStreamUtils
        .concatenateObjectStream(categoryStreams);
      TrainingParameters trainingParams = new TrainingParameters();
      trainingParams.put(TrainingParameters.ITERATIONS_PARAM, 10);
      trainingParams.put(TrainingParameters.CUTOFF_PARAM, 0);

      DoccatModel doccatModel = DocumentCategorizerME.train("en", combinedDocumentSampleStream, trainingParams,
        new DoccatFactory());
      combinedDocumentSampleStream.close();

      List<TokenNameFinderModel> tokenNameFinderModels = new ArrayList<>();
      for (String slot : entityTags) {
        List<ObjectStream<NameSample>> nameStreams = new ArrayList<>();
        for (File trainingFile : trainingDirectory.listFiles()) {
          ObjectStream<String> lineStream = new PlainTextByLineStream(
            new MarkableFileInputStreamFactory(trainingFile), "UTF-8");
          ObjectStream<NameSample> nameSampleStream = new NameSampleDataStream(lineStream);
          nameStreams.add(nameSampleStream);
        }
        ObjectStream<NameSample> combinedNameSampleStream = ObjectStreamUtils
          .concatenateObjectStream(nameStreams);

        TokenNameFinderModel tokenNameFinderModel = NameFinderME.train("en", slot, combinedNameSampleStream,
          trainingParams, new TokenNameFinderFactory());
        combinedNameSampleStream.close();
        tokenNameFinderModels.add(tokenNameFinderModel);
      }

      categorizer = new DocumentCategorizerME(doccatModel);
      nameFinderMEs = new NameFinderME[tokenNameFinderModels.size()];
      for (int i = 0; i < tokenNameFinderModels.size(); i++) {
        nameFinderMEs[i] = new NameFinderME(tokenNameFinderModels.get(i));
      }
      LOGGER.info("Training complete. Ready.!!");
    } catch (Exception e) {
      LOGGER.error("Exception occurred while training models", e);
      throw new Exception("Exception occurred while training models", e);
    }
  }

  public InfoDto getIntentAndEntities(String input) {
    InfoDto result = new InfoDto();

    try {
     // File trainingDirectory = Path.of(this.getClass().getResource("/tmp/models/en-token.bin").toURI()).toFile();
      //InputStream modelIn = new FileInputStream(trainingDirectory);
     // Path path = Path.of(this.getClass().getResource("/models/train").toURI());
      //InputStream modelIn = getClass().getClassLoader().getResourceAsStream("/tmp/models/en-token.bin");
      File file = ResourceUtils.getFile("/tmp/models/en-token.bin");
      InputStream modelIn = new FileInputStream(file);
      TokenizerModel model = new TokenizerModel(modelIn);
      Tokenizer tokenizer = new TokenizerME(model);
      String[] tokens = tokenizer.tokenize(input.toLowerCase());

      double[] outcome = categorizer.categorize(tokens);

      String intent = categorizer.getBestCategory(outcome);

      // Map<String,List<String>> entities = new HashMap<>();
      List<String> entities = new ArrayList<>();

      for (NameFinderME nameFinderME : nameFinderMEs) {
        Span[] spans = nameFinderME.find(tokens);
        String[] names = Span.spansToStrings(spans, tokens);
        for (int i = 0; i < spans.length; i++) {
          entities.add(names[i].toLowerCase());
        }
      }
      result.setIntent(intent);
      result.setEntities(entities);
    } catch (Exception e) {
      LOGGER.error("Exception occurred while fetching intent and entities", e);
      result = null;
    }

    return result;
  }

  public static void main(String[] args) throws Exception {
    IntentTrainer trainer = new IntentTrainer();
    trainer.trainModels();
    List<String> asList = Arrays.asList("my country is india nation");
    for (String input : asList) {
      System.out.println("===============================");
      System.out.println(input);
      InfoDto info = trainer.getIntentAndEntities(input);
      System.out.println(info.getIntent());
      System.out.println(info.getEntities());
    }
  }
}
