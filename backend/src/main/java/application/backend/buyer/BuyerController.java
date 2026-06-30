package application.backend.buyer;

import application.backend.buyer.dto.BuyerListItem;
import application.backend.buyer.dto.BuyerResponse;
import application.backend.buyer.dto.CreateBuyerRequest;
import application.backend.entity.BuyerStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/buyers")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping
    public ResponseEntity<BuyerResponse> createBuyer(@Valid @RequestBody CreateBuyerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(buyerService.createBuyer(request));
    }

    @GetMapping
    public ResponseEntity<List<BuyerListItem>> listBuyers(
            @RequestParam(required = false) BuyerStatus status) {
        return ResponseEntity.ok(buyerService.listBuyers(Optional.ofNullable(status)));
    }
}
