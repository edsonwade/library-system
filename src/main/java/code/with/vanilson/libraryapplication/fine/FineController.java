package code.with.vanilson.libraryapplication.fine;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * FineController
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-10
 */
@RestController
@RequestMapping("/api/fines")
@Slf4j
public class FineController {

    public static final String FINES = "fines";
    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    // Retrieve all fines
    @GetMapping
    public ResponseEntity<List<FineResponse>> getAllFines() {
        log.info("Fetching all fines");
        List<FineResponse> fines = fineService.getAllFines();
        // Add HATEOAS links to each member response
        fines.forEach(fine -> {
            fine.add(linkTo(methodOn(FineController.class).getFineById(fine.getId())).withSelfRel());
            fine.add(linkTo(methodOn(FineController.class).getAllFines()).withRel(FINES));
        });
        return ResponseEntity.ok(fines);
    }

    // Retrieve fine by ID
    @GetMapping("/{id}")
    public ResponseEntity<FineResponse> getFineById(@PathVariable Long id) {
        log.info("Fetching fine with ID: {}", id);

        // Fetch the fine from the service
        FineResponse fineResponse = fineService.getFineById(id);

        // Add HATEOAS links to the FineResponse
        fineResponse.add(linkTo(methodOn(FineController.class).getFineById(id)).withSelfRel());
        fineResponse.add(linkTo(methodOn(FineController.class).getAllFines()).withRel(FINES));

        return ResponseEntity.ok(fineResponse);
    }

    // Apply a fine
    @PostMapping(value = "/create-fine")
    public ResponseEntity<FineResponse> applyFine(@Valid @RequestBody FineRequest fineRequest) {
        log.info("Applying fine for member ID: {}", fineRequest.getMemberId());
        FineResponse fineResponse = fineService.applyFine(fineRequest);

        // Add HATEOAS links
        fineResponse.add(linkTo(methodOn(FineController.class).getFineById(fineResponse.getId())).withSelfRel());
        fineResponse.add(linkTo(methodOn(FineController.class).getAllFines()).withRel(FINES));

        return ResponseEntity.status(HttpStatus.CREATED).body(fineResponse);
    }

    // Update an existing fine
    @PutMapping("/update-fine/{id}")
    public ResponseEntity<FineResponse> updateFine(
            @PathVariable Long id,
            @Valid @RequestBody FineRequest fineRequest) {
        log.info("Updating fine with ID: {}", id);
        FineResponse updatedFine = fineService.updateFine(id, fineRequest);

        // Add HATEOAS links
        updatedFine.add(linkTo(methodOn(FineController.class).getFineById(id)).withSelfRel());
        updatedFine.add(linkTo(methodOn(FineController.class).getAllFines()).withRel(FINES));

        return ResponseEntity.ok(updatedFine);
    }

    // Delete fine by ID
    @DeleteMapping("/delete-fine/{id}")
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        log.warn("Deleting fine with ID: {}", id);
        fineService.deleteFine(id);

        // Add HATEOAS link to the list of all fines
        // Create a base URL for listing fines
        String allFinesUrl = linkTo(methodOn(FineController.class).getAllFines()).toUri().toString();
        // You can include a location header or return the link in the response body if necessary
        return ResponseEntity.noContent().header(HttpHeaders.LINK, allFinesUrl).build();
    }

}
