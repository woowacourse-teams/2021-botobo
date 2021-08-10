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

    @NotEmpty(message = "W015")
    private List<Long> cardIds;

    public List<Long> distinctCardIds() {
        return cardIds.stream().distinct().collect(Collectors.toList());
    }
}
