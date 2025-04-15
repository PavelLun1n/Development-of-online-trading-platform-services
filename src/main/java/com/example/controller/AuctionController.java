package com.example.controller;

import com.example.model.Auction;
import com.example.model.User;
import com.example.service.AuctionService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final UserService userService;

    // Получение всех аукционов
    @GetMapping
    public ResponseEntity<List<Auction>> getAllAuctions() {
        return ResponseEntity.ok(auctionService.getAllAuctions());
    }

    // Получение только активных аукционов
    @GetMapping("/active")
    public ResponseEntity<List<Auction>> getActiveAuctions() {
        return ResponseEntity.ok(auctionService.getActiveAuctions());
    }

    // Получение конкретного аукциона по ID
    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable Long id) {
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }

    // Создание аукциона
    @PostMapping
    public ResponseEntity<Auction> createAuction(@RequestBody Auction auction) {
        return ResponseEntity.ok(auctionService.createAuction(auction));
    }

    // Обновление аукциона
    @PutMapping("/{id}")
    public ResponseEntity<Auction> updateAuction(@PathVariable Long id, @RequestBody Auction auctionDetails) {
        return ResponseEntity.ok(auctionService.updateAuction(id, auctionDetails));
    }

    // Удаление аукциона
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable Long id) {
        auctionService.deleteAuction(id);
        return ResponseEntity.noContent().build();
    }

    // Сделать ставку
    @PostMapping("/{id}/bid")
    public ResponseEntity<Auction> placeBid(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            Principal principal
    ) {
        // Получаем пользователя по логину из Principal (если у тебя JWT — подскажи, изменим)
        User user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok(auctionService.placeBid(id, user, amount));
    }

    // Завершить аукцион вручную (если требуется)
    @PostMapping("/{id}/close")
    public ResponseEntity<Auction> closeAuction(@PathVariable Long id) {
        return ResponseEntity.ok(auctionService.closeAuction(id));
    }
}
