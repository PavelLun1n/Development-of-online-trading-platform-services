package com.example.service;

import com.example.model.Bid;
import com.example.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void deleteBid(Long id) {
        bidRepository.deleteById(id);
    }
}
