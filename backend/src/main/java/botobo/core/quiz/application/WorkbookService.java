package botobo.core.quiz.application;

import botobo.core.quiz.domain.workbook.Workbook;
import botobo.core.quiz.domain.workbook.WorkbookCriteria;
import botobo.core.quiz.domain.workbook.WorkbookRepository;
import botobo.core.quiz.dto.workbook.WorkbookCardResponse;
import botobo.core.quiz.dto.workbook.WorkbookResponse;
import botobo.core.quiz.exception.WorkbookNotFoundException;
import botobo.core.quiz.exception.WorkbookSearchFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class WorkbookService {

    private final WorkbookRepository workbookRepository;

    public WorkbookService(WorkbookRepository workbookRepository) {
        this.workbookRepository = workbookRepository;
    }

    public List<WorkbookResponse> findAll() {
        List<Workbook> workbooks = workbookRepository.findAll();
        return WorkbookResponse.listOf(workbooks);
    }

    // TODO: 추후 책임을 분리할 방법이 생각나면 구조 개선
    public List<WorkbookResponse> searchByCriteria(WorkbookCriteria workbookCriteria, Long userId) {
        // 중복되지 않으려고 사용
        Set<Workbook> workbooks = new LinkedHashSet<>();

        // 내 문제집만 검색하는 경우
        if (workbookCriteria.isMineType()) {
            workbooks.addAll(workbookRepository.findAllByUserId(userId));
        }
        // 전체 문제집을 검색하는 경우
        else {
            // 전체 문제집을 검색하는 private 또는 all의 문제집을 요청했으면 예외
            if (workbookCriteria.isPrivateAccess() || workbookCriteria.isAllAccess()) {
                throw new WorkbookSearchFailureException("권한이 없는 문제집이 포함되어 가져올 수 없습니다.");
            }
            workbooks.addAll(workbookRepository.findAll());
        }

        return workbooks.stream()
                .filter(filterAccessType(workbookCriteria))
                .filter(filterSearchKeyword(workbookCriteria))
                .map(WorkbookResponse::of)
                .collect(Collectors.toList());
    }

    private Predicate<Workbook> filterAccessType(WorkbookCriteria workbookCriteria) {
        // AllAccess 인 경우 모두 통과
        // 이외는 private 이나 public 검색 조건에 맞는 결과만 필터링
        return workbook -> (workbookCriteria.isAllAccess() ||
                (workbook.isPublic() && workbookCriteria.isPublicAccess()) ||
                (workbook.isPrivate() && workbookCriteria.isPrivateAccess())
        );
    }

    private Predicate<Workbook> filterSearchKeyword(WorkbookCriteria workbookCriteria) {
        // 검색어가 없으면 모두 통과
        // 이외에는 포함하면 통과
        return workbook -> (workbookCriteria.isNoSearchKeyword() ||
                (workbook.containsWord(workbookCriteria.getSearchKeywordValue()))
        );
    }

    public WorkbookCardResponse findWorkbookCardsById(Long id) {
        Workbook workbook = workbookRepository.findById(id)
                .orElseThrow(WorkbookNotFoundException::new);
        return WorkbookCardResponse.of(workbook);
    }
}
