package botobo.core;

//@Component
//@Slf4j
//@Profile("!test")
public class DataLoader {
//
//    @Value("${dummy.file-path}")
//    private String filePath;
//    @Value("${dummy.categories}")
//    private List<String> categories;
//    private WorkbookRepository workbookRepository;
//    private CardRepository cardRepository;

//    public DataLoader(WorkbookRepository workbookRepository, CardRepository cardRepository) {
//        this.workbookRepository = workbookRepository;
//        this.cardRepository = cardRepository;
//    }
//
//    @Override
//    public void run(String... args) {
//        if (!workbookRepository.findAll().isEmpty()) {
//            return;
//        }
//        for (String category : categories) {
//            readFile(filePath + category);
//        }
//    }

//    public void readFile(String filePath) {
//        try {
//            File file = new File(filePath);
//            FileReader fileReader = new FileReader(file);
//
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            setData(bufferedReader);
//            bufferedReader.close();
//        } catch (FileNotFoundException e) {
//            log.info("Dummy Data 파일 경로를 찾지 못했습니다.");
//        } catch (IOException e) {
//            log.info("Dummy Data를 불러오는 중 자바 입출력 에러가 발생했습니다.");
//        }
//    }

//    private void setData(BufferedReader bufferedReader) throws IOException {
//        String line = "";
//        while ((line = bufferedReader.readLine()) != null) {
//            Workbook workbook = makeWorkbook(line);
//            Card card = Card.builder()
//                    .workbook(workbook)
//                    .question("")
//                    .isDeleted(false)
//                    .build();
//            setQuestionAndAnswer(bufferedReader, workbook);
//        }
//    }

//    private void setQuestionAndAnswer(BufferedReader bufferedReader, Workbook workbook) throws IOException {
//        String line = "";
//        while ((line = bufferedReader.readLine()) != null) {
//            if (count % 2 == 0) {
//                card = setQuestion(workbook, line);
//            } else {
//                setAnswer(card, line);
//            }
//
//        }
//    }
//
//    private Workbook makeWorkbook(String workbookName) {
//        Workbook workbook = Workbook.builder()
//                .name(workbookName)
//                .isDeleted(false).build();
//        return workbookRepository.save(workbook);
//    }

//    private Card setQuestion(Workbook workbook, String question) {
//        Card card = Card.builder()
//                .question(question)
//                .answer(answer)
//                .workbook(workbook)
//                .isDeleted(false)
//                .build();
//        return cardRepository.save(card);
//    }

//    private void setAnswer(Card card, String answer) {
//        answerRepository.save(Answer.builder()
//                .content(answer)
//                .card(card)
//                .isDeleted(false)
//                .build());
//    }
}
