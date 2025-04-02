package com.example.service;

import com.example.model.Auction;
import com.example.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    public Optional<Auction> getAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    public Auction createAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    public Auction updateAuction(Long id, Auction auctionDetails) {
        return auctionRepository.findById(id).map(auction -> {
            auction.setStartPrice(auctionDetails.getStartPrice());
            auction.setCurrentPrice(auctionDetails.getCurrentPrice());
            auction.setEndTime(auctionDetails.getEndTime());
            return auctionRepository.save(auction);
        }).orElseThrow(() -> new RuntimeException("Auction not found"));
    }

    public void deleteAuction(Long id) {
        auctionRepository.deleteById(id);
    }
}
