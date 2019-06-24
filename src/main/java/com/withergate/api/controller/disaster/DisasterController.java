package com.withergate.api.controller.disaster;

import java.security.Principal;

import com.withergate.api.model.disaster.Disaster;
import com.withergate.api.service.disaster.DisasterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Disaster controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class DisasterController {

    private final DisasterService disasterService;

    /**
     * Retrieves the current disaster for clan.
     *
     * @param principal the principal
     * @return the current disaster if visible
     */
    @GetMapping("/disaster")
    public ResponseEntity<Disaster> getClan(Principal principal) {
        Disaster disaster = disasterService.getDisasterForClan(Integer.parseInt(principal.getName()));

        return new ResponseEntity<>(disaster, HttpStatus.OK);
    }

}
