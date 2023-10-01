package com.helmes.cities.domain.city.api.v1;

import com.helmes.cities.domain.city.api.v1.model.ChangeCityRequest;
import com.helmes.cities.domain.city.entity.City;
import com.helmes.cities.domain.city.repository.CityRepository;
import com.helmes.cities.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/cities")
@RequiredArgsConstructor
public class CitiesController {

    private final CityRepository cityRepository;

    @GetMapping()
    public ResponseEntity<Page<City>> getCities(@RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(cityRepository.findAll(PageRequest.of(page, size)), OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<City>> getCitiesByName(@RequestParam("name") String name, @RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(cityRepository.findAllByNameContainingIgnoreCase(name, PageRequest.of(page, size)), OK);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void changeCity(@PathVariable("id") Long id, @Validated @RequestBody ChangeCityRequest request) {
        cityRepository.findById(id).map(it -> {
            it.setName(request.name());
            it.setPhoto(request.photo());

            return cityRepository.save(it);
        }).orElseThrow(() -> new NotFoundException("City", String.format("No city was found by id: %d", id)));
    }
}
