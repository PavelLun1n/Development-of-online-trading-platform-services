package com.example.service;

import com.example.model.Auction;
import com.example.model.User;
import com.example.repository.AuctionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    public List<Auction> getActiveAuctions() {
        return auctionRepository.findByEndTimeAfter(LocalDateTime.now());
    }

    public Auction getAuctionById(Long id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Аукцион не найден"));
    }

    public Auction createAuction(Auction auction) {
        auction.setStartTime(LocalDateTime.now());
        auction.setCurrentPrice(auction.getStartPrice());
        return auctionRepository.save(auction);
    }

    @Transactional
    public Auction placeBid(Long auctionId, User bidder, BigDecimal bidAmount) {
        Auction auction = getAuctionById(auctionId);

        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
            throw new RuntimeException("Аукцион завершён");
        }

        if (bidAmount.compareTo(auction.getCurrentPrice()) <= 0) {
            throw new RuntimeException("Ставка должна быть выше текущей цены");
        }

        auction.setCurrentPrice(bidAmount);
        auction.setWinner(bidder);
        return auctionRepository.save(auction);
    }

    public Auction closeAuction(Long auctionId) {
        Auction auction = getAuctionById(auctionId);

        if (LocalDateTime.now().isBefore(auction.getEndTime())) {
            throw new RuntimeException("Аукцион ещё не завершён");
        }

        return auction;
    }

    public Auction updateAuction(Long id, Auction auctionDetails) {
        return auctionRepository.findById(id).map(auction -> {
            auction.setStartPrice(auctionDetails.getStartPrice());
            auction.setCurrentPrice(auctionDetails.getCurrentPrice());
            auction.setEndTime(auctionDetails.getEndTime());
            return auctionRepository.save(auction);
        }).orElseThrow(() -> new RuntimeException("Auction not found"));
    }
    public void importAuctionsFromCSV(InputStream csvInputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                Auction auction = new Auction();
                auction.setStartPrice(new BigDecimal(parts[1].trim()));
                auction.setCurrentPrice(new BigDecimal(parts[2].trim()));
                auction.setStartTime(LocalDateTime.parse(parts[3].trim()));
                auction.setEndTime(LocalDateTime.parse(parts[4].trim()));

                User winner = new User();
                winner.setId(Long.parseLong(parts[5].trim()));
                auction.setWinner(winner);

                auctionRepository.save(auction);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при импорте аукционов: " + e.getMessage(), e);
        }
    }


    public void deleteAuction(Long id) {
        auctionRepository.deleteById(id);
    }
}
