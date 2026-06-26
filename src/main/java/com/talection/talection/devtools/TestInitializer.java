package com.talection.talection.devtools;

import com.talection.talection.enums.TestOptionType;
import com.talection.talection.enums.TestType;
import com.talection.talection.model.tests.TestOption;
import com.talection.talection.model.tests.TestQuestion;
import com.talection.talection.model.tests.TestTemplate;
import com.talection.talection.repository.tests.TestOptionRepository;
import com.talection.talection.repository.tests.TestQuestionRepository;
import com.talection.talection.repository.tests.TestTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class TestInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(TestInitializer.class);

    private final TestTemplateRepository testTemplateRepository;
    private final TestQuestionRepository testQuestionRepository;
    private final TestOptionRepository testOptionRepository;

    public TestInitializer(TestTemplateRepository testTemplateRepository,
                           TestQuestionRepository testQuestionRepository,
                           TestOptionRepository testOptionRepository) {
        this.testTemplateRepository = testTemplateRepository;
        this.testQuestionRepository = testQuestionRepository;
        this.testOptionRepository = testOptionRepository;
    }

    /**
     * Holds the definition for a single Big Five question:
     * the question text, which trait it measures, and whether scoring is reversed.
     */
    private record QuestionDef(String text, String trait, boolean reversed) {}

    /**
     * All 50 IPIP Big Five items, mapped to their trait and scoring direction.
     * Trait mapping follows the standard IPIP 50-item inventory.
     *
     * Extraversion:      Q1, Q2, Q13, Q21, Q30, Q33, Q34, Q39, Q45, Q49
     * Agreeableness:     Q5, Q8, Q9, Q15, Q16, Q18, Q24, Q28, Q38, Q42
     * Conscientiousness: Q3, Q4, Q6, Q7, Q14, Q19, Q22, Q26, Q29, Q48
     * Neuroticism:       Q10, Q11, Q12, Q17, Q25, Q27, Q35, Q43, Q46, Q47
     * Openness:          Q20, Q23, Q31, Q32, Q36, Q37, Q40, Q41, Q44, Q50
     */
    private static final List<QuestionDef> BIG_FIVE_QUESTIONS = List.of(
        new QuestionDef("I don't talk a lot",                                      "Extraversion",      true),
        new QuestionDef("I have little to say",                                    "Extraversion",      true),
        new QuestionDef("I make a mess of things",                                 "Conscientiousness", true),
        new QuestionDef("I pay attention to details",                              "Conscientiousness", false),
        new QuestionDef("I insult people",                                         "Agreeableness",     true),
        new QuestionDef("I neglect my duties",                                     "Conscientiousness", true),
        new QuestionDef("I get chores done right away",                            "Conscientiousness", false),
        new QuestionDef("I am not interested in other people's problems",          "Agreeableness",     true),
        new QuestionDef("I am interested in people",                               "Agreeableness",     false),
        new QuestionDef("I often feel melancholic",                                "Neuroticism",       false),
        new QuestionDef("I am relaxed most of the time",                           "Neuroticism",       true),
        new QuestionDef("I change my mood a lot",                                  "Neuroticism",       false),
        new QuestionDef("I don't mind being the center of attention",              "Extraversion",      false),
        new QuestionDef("I often forget to put things back in their proper place", "Conscientiousness", true),
        new QuestionDef("I take time out for others",                              "Agreeableness",     false),
        new QuestionDef("I am not really interested in others",                    "Agreeableness",     true),
        new QuestionDef("I have frequent mood swings",                             "Neuroticism",       false),
        new QuestionDef("I make people feel at ease",                              "Agreeableness",     false),
        new QuestionDef("I am always prepared",                                    "Conscientiousness", false),
        new QuestionDef("I have excellent ideas",                                  "Openness",          false),
        new QuestionDef("I feel comfortable around people",                        "Extraversion",      false),
        new QuestionDef("I leave my belongings around",                            "Conscientiousness", true),
        new QuestionDef("I am not interested in abstract ideas",                   "Openness",          true),
        new QuestionDef("I feel others' emotions",                                 "Agreeableness",     false),
        new QuestionDef("I get irritated easily",                                  "Neuroticism",       false),
        new QuestionDef("I am demanding in my work",                               "Conscientiousness", false),
        new QuestionDef("I seldom feel melancholic",                               "Neuroticism",       true),
        new QuestionDef("I feel little concern for others",                        "Agreeableness",     true),
        new QuestionDef("I like order",                                            "Conscientiousness", false),
        new QuestionDef("I don't like to draw attention to myself",                "Extraversion",      true),
        new QuestionDef("I have a rich vocabulary",                                "Openness",          false),
        new QuestionDef("I am quick to understand things",                         "Openness",          false),
        new QuestionDef("I talk to a lot of different people at parties",          "Extraversion",      false),
        new QuestionDef("I am the life of the party",                              "Extraversion",      false),
        new QuestionDef("I get upset easily",                                      "Neuroticism",       false),
        new QuestionDef("I have difficulty understanding abstract ideas",          "Openness",          true),
        new QuestionDef("I am full of ideas",                                      "Openness",          false),
        new QuestionDef("I have a soft heart",                                     "Agreeableness",     false),
        new QuestionDef("I keep in the background",                                "Extraversion",      true),
        new QuestionDef("I have a vivid imagination",                              "Openness",          false),
        new QuestionDef("I use difficult words",                                   "Openness",          false),
        new QuestionDef("I sympathize with others' feelings",                      "Agreeableness",     false),
        new QuestionDef("I worry about things",                                    "Neuroticism",       false),
        new QuestionDef("I do not have a good imagination",                        "Openness",          true),
        new QuestionDef("I am quiet around strangers",                             "Extraversion",      true),
        new QuestionDef("I get stressed out easily",                               "Neuroticism",       false),
        new QuestionDef("I am easily disturbed",                                   "Neuroticism",       false),
        new QuestionDef("I follow a schedule",                                     "Conscientiousness", false),
        new QuestionDef("I start conversations",                                   "Extraversion",      false),
        new QuestionDef("I spend time reflecting on things",                       "Openness",          false)
    );

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        initiateBig5();
        initiateFluidIntelligence();
    }

    private void initiateBig5() {
        if (testTemplateRepository.findByTestType(TestType.BIG_5).isPresent()) {
            logger.info("Big Five Personality Test already exists, skipping initialization.");
            return;
        }
        logger.info("Initializing Big Five Personality Test...");

        // Create template
        TestTemplate big5 = new TestTemplate();
        big5.setName("BIG-5");
        big5.setDescription("Big Five Personality Test");
        big5.setTestType(TestType.BIG_5);
        big5.setOptionType(TestOptionType.LIKERT_SCALE);

        // Create the five Likert scale options (shared across all questions)
        List<TestOption> options = new ArrayList<>();
        for (int level = 1; level <= 5; level++) {
            TestOption option = new TestOption();
            option.setAgreementLevel(level);
            option.setType(TestOptionType.LIKERT_SCALE);
            options.add(testOptionRepository.save(option));
        }

        // Create questions from the definitions
        List<TestQuestion> questions = new ArrayList<>();
        for (QuestionDef def : BIG_FIVE_QUESTIONS) {
            TestQuestion question = new TestQuestion();
            question.setQuestionText(def.text());
            question.setTrait(def.trait());
            question.setReversed(def.reversed());
            question.setOptions(options);
            questions.add(question);
        }
        testQuestionRepository.saveAll(questions);

        big5.setQuestions(questions);
        testTemplateRepository.save(big5);
        logger.info("Big Five Personality Test initialized successfully with {} questions.", questions.size());
    }

    private void initiateFluidIntelligence() {
        if (testTemplateRepository.findByTestType(TestType.INTELLIGENCE_FLUID).isPresent()) {
            logger.info("Fluid Intelligence Test already exists, skipping initialization.");
            return;
        }
        logger.info("Initializing Fluid Intelligence Test...");

        TestTemplate fluidIntelligence = new TestTemplate();
        fluidIntelligence.setName("Intelligence Fluid");
        fluidIntelligence.setDescription("Fluid Intelligence Test");
        fluidIntelligence.setTestType(TestType.INTELLIGENCE_FLUID);
        fluidIntelligence.setOptionType(TestOptionType.MULTIPLE_CHOICE);

        List<TestQuestion> questions = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            String[] optionLabels = {"OptionA", "OptionB", "OptionC", "OptionD", "OptionE", "OptionF"};
            List<TestOption> options = new ArrayList<>();
            for (String label : optionLabels) {
                TestOption option = new TestOption();
                option.setOptionText("Q" + i + "_" + label);
                option.setType(TestOptionType.MULTIPLE_CHOICE);
                options.add(testOptionRepository.save(option));
            }

            TestQuestion question = new TestQuestion();
            question.setQuestionText("Q" + i + "_Main");
            question.setOptions(options);
            question.setCorrectOptions(List.of(options.get(0)));
            questions.add(question);
        }
        testQuestionRepository.saveAll(questions);

        fluidIntelligence.setQuestions(questions);
        testTemplateRepository.save(fluidIntelligence);
        logger.info("Fluid Intelligence Test initialized successfully.");
    }
}