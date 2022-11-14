package com.rm.toolkit.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class TokensResponse {

    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJybXRfbW9iaWxlX2F1dGgiLCJzdWIiOiJlNDViZmZmMC0xYjA0LTRiOGEtYWM5" +
            "NS05NjI5ZGZmODhhM2UiLCJpYXQiOjE2MzM3ODE4MjUsImV4cCI6MTYzMzc4NTQyNSwicm9sZVByaW9yaXR5IjoxMCwiYXV0aG9yaXR" +
            "pZXMiOlsiQVVUSE9SSVpBVElPTiIsIkVNUExPWUVFX0xJU1QiLCJFTVBMT1lFRV9DQVJEIiwiVVNFUl9TVEFUVVNfU0VUVElOR1MiLC" +
            "JBRERfRU1QTE9ZRUVfVE9fREVQQVJUTUVOVCIsIlZJRVdfT05FX1RPX09ORSIsIlZJRVdfRkVFREJBQ0tTIiwiVklFV19BU1NFU1NNR" +
            "U5UX0dPQUxTIiwiVklFV19DT01NRU5UUyIsIlZJRVdfUk9MRVMiLCJWSUVXX0RFUEFSVE1FTlRTIiwiRURJVF9ERVBBUlRNRU5UUyIs" +
            "IkVESVRfUk9MRVMiLCJFRElUX0lOVEVSVkFMUyJdfQ.cbx3J4uiwQ7hahMTCOJV5jbSyRckBDpallJgzqB3jSmcGAJ1J_EV_whnOO-M" +
            "lFk9ZLFl0sU15osCC1HNAwx8cA")
    private String accessToken;

    @Schema(example = "Bearer", description = "Всегда равен 'Bearer'")
    private String tokenType;

    @Schema(description = "Время жизни access токена в секундах. BTW, refresh токен живёт 9 часов.", example = "3600")
    private Integer expiresIn;

    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJybXRfbW9iaWxlX2F1dGgiLCJzdWIiOiJlNDViZmZmMC0xYjA0LTRiOGEtYWM5" +
            "NS05NjI5ZGZmODhhM2UiLCJpYXQiOjE2MzM3ODE4MjUsImV4cCI6MTYzMzgxNDIyNX0.Sf7oZFi_aNYbzYdCGZVAm5eb1rq9sfQ1WdJ" +
            "2Kl95vHXzCCmuck5Rzp7Fn1c0L40dMpvBtQ56uf3nbk853HYhjg")
    private String refreshToken;
}
