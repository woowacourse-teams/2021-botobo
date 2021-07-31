package botobo.core.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScrapCardRequest {

    @NotEmpty(message = "카드를 내 문제집으로 옮기려면 카드 아이디가 필요합니다.")
    private List<Long> cardIds;

    public List<Long> distinctCardIds() {
        return cardIds.stream().distinct().collect(Collectors.toList());
    }
}
