package com.spring.brewery.controller.v2;

import com.spring.brewery.model.v2.BeerDtoV2;
import com.spring.brewery.service.v2.BeerServiceV2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v2/beer")
@RestController
public class BeerControllerV2 {
    private final BeerServiceV2 beerServiceV2;

    public BeerControllerV2(BeerServiceV2 beerServiceV2) {
        this.beerServiceV2 = beerServiceV2;
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDtoV2> getBeer(@PathVariable("beerId") UUID beerId){
        return new ResponseEntity<>(beerServiceV2.getBeerById(beerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BeerDtoV2> handlePost(@RequestBody BeerDtoV2 beerDtoV2){
        BeerDtoV2 saveDto = beerServiceV2.saveNewBeer(beerDtoV2);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer" + saveDto.getId().toString());

        return new ResponseEntity<>(headers,HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDtoV2> handleUpdate(@PathVariable("beerId") UUID beerId, @RequestBody BeerDtoV2 beerDtoV2){
        beerServiceV2.updateBeer(beerId, beerDtoV2);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{beerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeer(@PathVariable("beerId") UUID beerId){
        beerServiceV2.deleteBeerById(beerId);
    }
}
