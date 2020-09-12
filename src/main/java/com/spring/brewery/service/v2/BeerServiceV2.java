package com.spring.brewery.service.v2;

import com.spring.brewery.model.v2.BeerDtoV2;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface BeerServiceV2 {
    BeerDtoV2 getBeerById(UUID beerId);

    BeerDtoV2 saveNewBeer(BeerDtoV2 beerDtoV2);

    void updateBeer(UUID beerId, BeerDtoV2 beerDto);

    void deleteBeerById(UUID beerId);
}
