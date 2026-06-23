package com.talection.talection.devtools;
import com.talection.talection.enums.TestOptionType;
import com.talection.talection.enums.TestType;
import com.talection.talection.model.tests.TestOption;
import com.talection.talection.model.tests.TestQuestion;
import com.talection.talection.model.tests.TestTemplate;
import com.talection.talection.repository.tests.TestOptionRepository;
import com.talection.talection.repository.tests.TestQuestionRepository;
import com.talection.talection.repository.tests.TestTemplateRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class TestInitializer implements ApplicationListener<ApplicationReadyEvent> {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private final Logger logger = LoggerFactory.getLogger(TestInitializer.class);

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
        TestTemplate big5 = new TestTemplate();
        big5.setName("BIG-5");
        big5.setDescription("Big Five Personality Test");
        big5.setTestType(TestType.BIG_5);
        big5.setOptionType(TestOptionType.LIKERT_SCALE);

        TestOption stronglyDisagree = new TestOption();
        stronglyDisagree.setAgreementLevel(1);
        stronglyDisagree.setType(TestOptionType.LIKERT_SCALE);
        stronglyDisagree = testOptionRepository.save(stronglyDisagree);

        TestOption disagree = new TestOption();
        disagree.setAgreementLevel(2);
        disagree.setType(TestOptionType.LIKERT_SCALE);
        disagree = testOptionRepository.save(disagree);

        TestOption neutral = new TestOption();
        neutral.setAgreementLevel(3);
        neutral.setType(TestOptionType.LIKERT_SCALE);
        neutral = testOptionRepository.save(neutral);

        TestOption agree = new TestOption();
        agree.setAgreementLevel(4);
        agree.setType(TestOptionType.LIKERT_SCALE);
        agree = testOptionRepository.save(agree);

        TestOption stronglyAgree = new TestOption();
        stronglyAgree.setAgreementLevel(5);
        stronglyAgree.setType(TestOptionType.LIKERT_SCALE);
        stronglyAgree = testOptionRepository.save(stronglyAgree);

        List<TestOption> options = List.of(stronglyDisagree, disagree, neutral, agree, stronglyAgree);
        testOptionRepository.saveAll(options);


        TestQuestion question1 = new TestQuestion();
        question1.setQuestionText("I don't talk a lot");
        question1.setOptions(options);

        TestQuestion question2 = new TestQuestion();
        question2.setQuestionText("I have little to say");
        question2.setOptions(options);

        TestQuestion question3 = new TestQuestion();
        question3.setQuestionText("I make a mess of things");
        question3.setOptions(options);

        TestQuestion question4 = new TestQuestion();
        question4.setQuestionText("I pay attention to details");
        question4.setOptions(options);

        TestQuestion question5 = new TestQuestion();
        question5.setQuestionText("I insult people");
        question5.setOptions(options);

        TestQuestion question6 = new TestQuestion();
        question6.setQuestionText("I neglect my duties");
        question6.setOptions(options);

        TestQuestion question7 = new TestQuestion();
        question7.setQuestionText("I get chores done right away");
        question7.setOptions(options);

        TestQuestion question8 = new TestQuestion();
        question8.setQuestionText("I am not interested in other people's problems");
        question8.setOptions(options);

        TestQuestion question9 = new TestQuestion();
        question9.setQuestionText("I am interested in people");
        question9.setOptions(options);

        TestQuestion question10 = new TestQuestion();
        question10.setQuestionText("I often feel melancholic");
        question10.setOptions(options);

        TestQuestion question11 = new TestQuestion();
        question11.setQuestionText("I am relaxed most of the time");
        question11.setOptions(options);

        TestQuestion question12 = new TestQuestion();
        question12.setQuestionText("I change my mood a lot");
        question12.setOptions(options);

        TestQuestion question13 = new TestQuestion();
        question13.setQuestionText("I don't mind being the center of attention");
        question13.setOptions(options);

        TestQuestion question14 = new TestQuestion();
        question14.setQuestionText("I often forget to put things back in their proper place");
        question14.setOptions(options);

        TestQuestion question15 = new TestQuestion();
        question15.setQuestionText("I take time out for others");
        question15.setOptions(options);

        TestQuestion question16 = new TestQuestion();
        question16.setQuestionText("I am not really interested in others");
        question16.setOptions(options);

        TestQuestion question17 = new TestQuestion();
        question17.setQuestionText("I have frequent mood swings");
        question17.setOptions(options);

        TestQuestion question18 = new TestQuestion();
        question18.setQuestionText("I make people feel at ease");
        question18.setOptions(options);

        TestQuestion question19 = new TestQuestion();
        question19.setQuestionText("I am always prepared");
        question19.setOptions(options);

        TestQuestion question20 = new TestQuestion();
        question20.setQuestionText("I have excellent ideas");
        question20.setOptions(options);

        TestQuestion question21 = new TestQuestion();
        question21.setQuestionText("I feel comfortable around people");
        question21.setOptions(options);

        TestQuestion question22 = new TestQuestion();
        question22.setQuestionText("I leave my belongings around");
        question22.setOptions(options);

        TestQuestion question23 = new TestQuestion();
        question23.setQuestionText("I am not interested in abstract ideas");
        question23.setOptions(options);

        TestQuestion question24 = new TestQuestion();
        question24.setQuestionText("I feel others' emotions");
        question24.setOptions(options);

        TestQuestion question25 = new TestQuestion();
        question25.setQuestionText("I get irritated easily");
        question25.setOptions(options);

        TestQuestion question26 = new TestQuestion();
        question26.setQuestionText("I am demanding in my work");
        question26.setOptions(options);

        TestQuestion question27 = new TestQuestion();
        question27.setQuestionText("I seldom feel melancholic");
        question27.setOptions(options);

        TestQuestion question28 = new TestQuestion();
        question28.setQuestionText("I feel little concern for others");
        question28.setOptions(options);

        TestQuestion question29 = new TestQuestion();
        question29.setQuestionText("I like order");
        question29.setOptions(options);

        TestQuestion question30 = new TestQuestion();
        question30.setQuestionText("I don't like to draw attention to myself");
        question30.setOptions(options);

        TestQuestion question31 = new TestQuestion();
        question31.setQuestionText("I have a rich vocabulary");
        question31.setOptions(options);

        TestQuestion question32 = new TestQuestion();
        question32.setQuestionText("I am quick to understand things");
        question32.setOptions(options);

        TestQuestion question33 = new TestQuestion();
        question33.setQuestionText("I talk to a lot of different people at parties");
        question33.setOptions(options);

        TestQuestion question34 = new TestQuestion();
        question34.setQuestionText("I am the life of the party");
        question34.setOptions(options);

        TestQuestion question35 = new TestQuestion();
        question35.setQuestionText("I get upset easily");
        question35.setOptions(options);

        TestQuestion question36 = new TestQuestion();
        question36.setQuestionText("I have difficulty understanding abstract ideas");
        question36.setOptions(options);

        TestQuestion question37 = new TestQuestion();
        question37.setQuestionText("I am full of ideas");
        question37.setOptions(options);

        TestQuestion question38 = new TestQuestion();
        question38.setQuestionText("I have a soft heart");
        question38.setOptions(options);

        TestQuestion question39 = new TestQuestion();
        question39.setQuestionText("I keep in the background");
        question39.setOptions(options);

        TestQuestion question40 = new TestQuestion();
        question40.setQuestionText("I have a vivid imagination");
        question40.setOptions(options);

        TestQuestion question41 = new TestQuestion();
        question41.setQuestionText("I use difficult words");
        question41.setOptions(options);

        TestQuestion question42 = new TestQuestion();
        question42.setQuestionText("I sympathize with others' feelings");
        question42.setOptions(options);

        TestQuestion question43 = new TestQuestion();
        question43.setQuestionText("I worry about things");
        question43.setOptions(options);

        TestQuestion question44 = new TestQuestion();
        question44.setQuestionText("I do not have a good imagination");
        question44.setOptions(options);

        TestQuestion question45 = new TestQuestion();
        question45.setQuestionText("I am quiet around strangers");
        question45.setOptions(options);

        TestQuestion question46 = new TestQuestion();
        question46.setQuestionText("I get stressed out easily");
        question46.setOptions(options);

        TestQuestion question47 = new TestQuestion();
        question47.setQuestionText("I am easily disturbed");
        question47.setOptions(options);

        TestQuestion question48 = new TestQuestion();
        question48.setQuestionText("I follow a schedule");
        question48.setOptions(options);

        TestQuestion question49 = new TestQuestion();
        question49.setQuestionText("I start conversations");
        question49.setOptions(options);

        TestQuestion question50 = new TestQuestion();
        question50.setQuestionText("I spend time reflecting on things");
        question50.setOptions(options);

        List<TestQuestion> questions = List.of(
            question1, question2, question3, question4, question5,
            question6, question7, question8, question9, question10,
            question11, question12, question13, question14, question15,
            question16, question17, question18, question19, question20,
            question21, question22, question23, question24, question25,
            question26, question27, question28, question29, question30,
            question31, question32, question33, question34, question35,
            question36, question37, question38, question39, question40,
            question41, question42, question43, question44, question45,
            question46, question47, question48, question49, question50
        );

        testQuestionRepository.saveAll(questions);
        big5.setQuestions(questions);

        testTemplateRepository.save(big5);
        logger.info("Big Five Personality Test initialized successfully.");
    }

    public void initiateFluidIntelligence() {
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
            String[] optionTexts = {"Q"+i+"_OptionA", "Q"+i+"_OptionB", "Q"+i+"_OptionC", "Q"+i+"_OptionD", "Q"+i+"_OptionE", "Q"+i+"_OptionF"};
            List<TestOption> options = new ArrayList<>();
            for (String optionText : optionTexts) {
                TestOption option = new TestOption();
                option.setOptionText(optionText);
                option.setType(TestOptionType.MULTIPLE_CHOICE);
                option = testOptionRepository.save(option);
                options.add(option);
            }
            TestQuestion question = new TestQuestion();
            question.setQuestionText("Q" + i + "_Main");
            question.setOptions(options);
            question.setCorrectOptions(List.of(options.get(0)));
            questions.add(question);
            testQuestionRepository.save(question);
        }

        testQuestionRepository.saveAll(questions);

        fluidIntelligence.setQuestions(questions);
        testTemplateRepository.save(fluidIntelligence);
        logger.info("Fluid Intelligence Test initialized successfully.");
    }
}
