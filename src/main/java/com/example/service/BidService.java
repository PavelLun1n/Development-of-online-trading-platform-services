package com.example.service;

import com.example.model.Auction;
import com.example.model.Bid;
import com.example.model.User;
import com.example.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BidService {
    private final BidRepository bidRepository;

    @Autowired
    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    public Optional<Bid> getBidById(Long id) {
        return bidRepository.findById(id);
    }

    public Bid createBid(Bid bid) {
        return bidRepository.save(bid);
    }

    public Bid updateBid(Long id, Bid bidDetails) {
        return bidRepository.findById(id).map(bid -> {
            bid.setBidAmount(bidDetails.getBidAmount());
            return bidRepository.save(bid);
        }).orElseThrow(() -> new RuntimeException("Bid not found"));
    }
    public void importBidsFromCSV(InputStream csvInputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                Bid bid = new Bid();
                bid.setBidAmount(new BigDecimal(parts[1].trim()));
                bid.setBidTime(LocalDateTime.parse(parts[2].trim()));

                User user = new User();
                user.setId(Long.parseLong(parts[3].trim()));
                bid.setUser(user);

                Auction auction = new Auction();
                auction.setId(Long.parseLong(parts[4].trim()));
                bid.setAuction(auction);

                bidRepository.save(bid);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при импорте ставок: " + e.getMessage(), e);
        }
    }


    public void deleteBid(Long id) {
        bidRepository.deleteById(id);
    }
}
