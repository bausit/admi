package org.bausit.admin.services;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import org.bausit.admin.models.Event;
import org.bausit.admin.models.TeamMember;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;

@Service
public class PdfService {
    public static final DateFormat dateFormat = DateFormat.getDateTimeInstance();

    @SneakyThrows
    public void export(Event event, OutputStream os) {
        Document document = new Document();
        PdfWriter.getInstance(document, os);
        document.open();
        PdfPTable table = new PdfPTable(2);

        //event name as table header
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setPhrase(new Phrase(event.getName()));
        header.setColspan(3);
        table.addCell(header);

        addRows(table, "Location", event.getLocation());
        addRows(table, "Date", dateFormat.format(new Date(event.getDate().toEpochMilli())));

        event.getTeams().stream()
            .filter(team -> !team.isDefault())
            .forEach(team -> {
                StringBuffer members = new StringBuffer();
                for (TeamMember member : team.getMembers()) {
                    members.append(member.getParticipant().getEnglishName() + " ");
                    members.append(member.getParticipant().getChineseName() + "\n");
                }
                addRows(table, team.getName(), members.toString());
            });

        document.add(table);
        document.close();
    }

    private void addRows(PdfPTable table, String cell1, String cell2) {
        table.addCell(cell1);
        table.addCell(cell2);
    }
}
