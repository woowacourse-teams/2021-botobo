package botobo.core.domain.heart;

import botobo.core.domain.workbook.Workbook;
import botobo.core.exception.heart.HeartAdditionFailureException;
import botobo.core.exception.heart.HeartRemovalFailureException;
import botobo.core.exception.heart.HeartsCreationFailureException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Embeddable
public class Hearts {

    @OneToMany(mappedBy = "workbook", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Heart> hearts = new ArrayList<>();

    private Hearts(List<Heart> hearts) {
        addHeartList(hearts);
    }

    public static Hearts of(List<Heart> hearts) {
        return new Hearts(hearts);
    }

    private void addHeartList(List<Heart> hearts) {
        for (Heart heart : hearts) {
            addHeart(heart);
        }
    }

    private void addHeart(Heart heart) {
        validateSameWorkbook(heart.getWorkbook());
        validateNonExistentUserId(heart.getUserId());
        if (contains(heart)) {
            throw new HeartAdditionFailureException();
        }
        hearts.add(heart);
    }

    private void validateSameWorkbook(Workbook workbook) {
        if (hearts.isEmpty()) {
            return;
        }
        Workbook workbookOfFirstHeart = hearts.get(0).getWorkbook();
        if (!workbook.equals(workbookOfFirstHeart)) {
            throw new HeartsCreationFailureException("같은 문제집의 하트만 추가할 수 있습니다");
        }
    }

    private void validateNonExistentUserId(Long userId) {
        if (contains(userId)) {
            throw new HeartsCreationFailureException("하나의 유저 아이디를 여러번 추가할 수 없습니다");
        }
    }

    private boolean contains(Heart heart) {
        Long userId = heart.getUserId();
        return contains(userId);
    }

    public boolean contains(Long userId) {
        return hearts.parallelStream()
                .anyMatch(h -> h.ownedBy(userId));
    }

    public boolean toggleHeart(Heart heart) {
        if (contains(heart)) {
            removeHeart(heart);
            return false;
        }
        hearts.add(heart);
        return true;
    }

    private void removeHeart(Heart heart) {
        Long userId = heart.getUserId();
        Heart removalTarget = hearts.parallelStream()
                .filter(h -> h.ownedBy(userId))
                .findAny()
                .orElseThrow(HeartRemovalFailureException::new);
        hearts.remove(removalTarget);
    }

    public void addHearts(Hearts hearts) {
        addHeartList(hearts.hearts);
    }

    public int size() {
        return hearts.size();
    }
}
