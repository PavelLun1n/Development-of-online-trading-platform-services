package com.example.restController;

import com.example.model.Bid;
import com.example.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService service;

    @GetMapping
    public List<Bid> getAll() {
        return service.getAllBids();
    }

    @GetMapping("/{id}")
    public Bid getById(@PathVariable Long id) {
        return service.getBidById(id).orElseThrow();
    }

    @PostMapping
    public Bid create(@RequestBody Bid bid) {
        return service.createBid(bid);
    }

    @PutMapping("/{id}")
    public Bid update(@PathVariable Long id, @RequestBody Bid bid) {
        return service.updateBid(id, bid);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteBid(id);
    }
}
