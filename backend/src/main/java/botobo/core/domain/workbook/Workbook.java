package botobo.core.domain.workbook;

import botobo.core.domain.BaseEntity;
import botobo.core.domain.card.Card;
import botobo.core.domain.card.Cards;
import botobo.core.domain.heart.Heart;
import botobo.core.domain.heart.Hearts;
import botobo.core.domain.tag.Tags;
import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.workbooktag.WorkbookTag;
import botobo.core.exception.workbook.WorkbookNameBlankException;
import botobo.core.exception.workbook.WorkbookNameLengthException;
import botobo.core.exception.workbook.WorkbookTagLimitException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
@Where(clause = "deleted=false")
public class Workbook extends BaseEntity {

    private static final int MAX_NAME_LENGTH = 30;
    private static final int MAX_TAG_SIZE = 5;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean opened;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean deleted;

    @Embedded
    private Cards cards = new Cards();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_workbook_to_user"))
    private User user;

    @OneToMany(mappedBy = "workbook", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<WorkbookTag> workbookTags = new ArrayList<>();

    @Embedded
    private final Hearts hearts = new Hearts();

    @Builder
    public Workbook(Long id, String name, boolean opened, boolean deleted, Cards cards, User user, Tags tags) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.opened = opened;
        this.deleted = deleted;
        if (Objects.nonNull(user)) {
            setUser(user);
        }
        if (Objects.nonNull(cards)) {
            this.cards = cards;
        }
        if (Objects.nonNull(tags)) {
            setTags(tags);
        }
    }

    private void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new WorkbookNameBlankException();
        }
        if (name.isBlank()) {
            throw new WorkbookNameBlankException();
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new WorkbookNameLengthException();
        }
    }

    public void setUser(User user) {
        if (Objects.nonNull(this.user)) {
            this.user.getWorkbooks().remove(this);
        }
        this.user = user;
        user.getWorkbooks().add(this);
    }

    public void setTags(Tags tags) {
        validateTagSize(tags);
        this.workbookTags.addAll(
                castWorkbookTags(tags)
        );
    }

    private void validateTagSize(Tags tags) {
        Tags currentTags = tags();
        final int alreadyHasTagsSize = currentTags.countSameTagName(tags);
        final int notHasTagsCount = tags.size() - alreadyHasTagsSize;

        if (currentTags.size() + notHasTagsCount > MAX_TAG_SIZE) {
            throw new WorkbookTagLimitException();
        }
    }

    private List<WorkbookTag> castWorkbookTags(Tags tags) {
        return tags.stream()
                .map(tag -> WorkbookTag.of(this, tag))
                .collect(Collectors.toList());
    }

    public Tags tags() {
        return Tags.of(workbookTags.stream()
                .map(WorkbookTag::getTag)
                .collect(Collectors.toList()));
    }

    public String author() {
        if (Objects.isNull(user)) {
            return "존재하지 않는 유저";
        }
        return user.getUserName();
    }

    public boolean isAuthorOf(User user) {
        return this.user.equals(user);
    }

    public boolean containsWord(String word) {
        return name.toLowerCase()
                .contains(word.toLowerCase());
    }

    public boolean isPrivate() {
        return !isOpened();
    }

    public boolean authorIsUser() {
        return user.isUser();
    }

    public int cardCount() {
        return cards.counts();
    }

    public void update(Workbook other) {
        this.name = other.name;
        this.opened = other.opened;
        clearWorkbookTags();
        setTags(other.tags());
    }

    public void clearWorkbookTags() {
        this.workbookTags.clear();
    }

    public void delete() {
        this.user.getWorkbooks().remove(this);
        this.deleted = true;
        cards.delete();
        workbookTags.forEach(WorkbookTag::delete);
    }

    public void addCards(Cards cards) {
        cards.getCards().forEach(this::addCard);
    }

    public void addCard(Card card) {
        card.addWorkbook(this);
        cards.addCard(card);
    }

    public boolean toggleHeart(Heart heart) {
        return hearts.toggleHeart(heart);
    }

    public boolean existsHeartByAppUser(AppUser appUser) {
        if (appUser.isAnonymous()) {
            return false;
        }
        return hearts.contains(appUser.getId());
    }

    public int heartCount() {
        return hearts.size();
    }

    // TODO

    public WorkbookDocument toDocument(Tags tags) {
        return WorkbookDocument.of(this.id, this.name, tags);
    }

    public WorkbookDocument toDocument() {
        return WorkbookDocument.of(this.id, this.name, this.tags());
    }
}
