package botobo.core.dto.heart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HeartResponse {
    private boolean heart;

    public static HeartResponse of(boolean heart) {
        return new HeartResponse(heart);
    }
}
