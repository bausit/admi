package org.bausit.admin.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.models.Participant;
import org.bausit.admin.services.ParticipantService;
import org.springframework.web.bind.annotation.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
@Log4j2
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping
    public Iterable<Participant> search(@RequestParam(required = false) String query,
                                        @RequestParam(required = false, defaultValue = "0") int startPage,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("search keywords: {}", query);

        return participantService.query(query, startPage, size);
    }

    @GetMapping("/export")
    public void export(@RequestParam(required = false) String query,
                       HttpServletResponse response) throws IOException {
        log.info("search keywords: {}", query);

        response.setContentType("text/csv;charset=UTF-8");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        Iterable<Participant> participants = participantService.query(query, 0, 10000);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"English Name", "Chinese Name", "Email", "Phone Number", "Birth Year",
            "Type", "Gender", "City", "State", "Zipcode"};
        String[] nameMapping = {"englishName", "chineseName", "email", "phoneNumber", "birthYear",
            "type", "gender", "city", "state", "zipcode"};

        csvWriter.writeHeader(csvHeader);

        for (Participant participant : participants) {
            csvWriter.write(participant, nameMapping);
        }

        csvWriter.close();
    }

    //note is a sensitive field, only super admins are allowed to view/edie
    @GetMapping("/{participantId}/note")
    public String updateNote(@PathVariable long participantId) {
        Participant participant = participantService.findById(participantId);

        return participant.getNote();
    }

    @PatchMapping("/{participantId}/note")
    public void updateNote(@PathVariable long participantId, @RequestBody String note) {
        Participant participant = participantService.findById(participantId);

        participant.setNote(note);

        participantService.save(participant);
    }

}
