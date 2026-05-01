package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.CreateTeamDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/team")
public class TeamController {

    @PostMapping
    public void addTeam(@RequestBody CreateTeamDto createTeamDto) {
        //TODO: process POST request
    }
    

}
