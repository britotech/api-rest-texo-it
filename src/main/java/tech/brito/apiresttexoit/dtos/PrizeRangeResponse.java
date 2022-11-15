package tech.brito.apiresttexoit.dtos;

import java.util.List;

public record PrizeRangeResponse(List<PrizeRangeDTO> min, List<PrizeRangeDTO> max) {

}
