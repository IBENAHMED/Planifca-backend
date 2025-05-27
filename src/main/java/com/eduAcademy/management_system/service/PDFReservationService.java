package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ReservationResponseDto;
import com.eduAcademy.management_system.entity.Reservation;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.mapper.ReservationMapper;
import com.eduAcademy.management_system.repository.ReservationRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PDFReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;

    public byte[] generateReservationReceipt(String reservationId) throws IOException, DocumentException {
        Reservation reservation = reservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + reservationId));

        ReservationResponseDto responseDto = reservationMapper.toReservationResponseDto(reservation);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        try {
            Image logo = Image.getInstance("src/main/resources/static/uploads/PlanifcaLogo.png");
            logo.scaleToFit(200, 100);
            logo.setAbsolutePosition(50, 750);
            document.add(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Reçu de Réservation", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);


        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(2);
        table.setWidths(new float[]{2, 5});
        table.setWidthPercentage(100);

        table.addCell(new PdfPCell(new Phrase("ID Réservation")));
        table.addCell(new PdfPCell(new Phrase(responseDto.getReservationId())));
        table.addCell(new PdfPCell(new Phrase("Prenom")));
        table.addCell(new PdfPCell(new Phrase(responseDto.getClientFirstName())));
        table.addCell(new PdfPCell(new Phrase("Nom")));
        table.addCell(new PdfPCell(new Phrase(responseDto.getClientLastName())));
        table.addCell(new PdfPCell(new Phrase("Stade")));
        table.addCell(new PdfPCell(new Phrase(responseDto.getStadium().getName())));
        table.addCell(new PdfPCell(new Phrase("Date Réservation")));
        table.addCell(new PdfPCell(new Phrase(responseDto.getReservationDate().toString())));
        table.addCell(new PdfPCell(new Phrase("Horaire Début")));
        table.addCell(new PdfPCell(new Phrase(responseDto.getStartTime().toString())));
        table.addCell(new PdfPCell(new Phrase("Horaire Fin")));
        table.addCell(new PdfPCell(new Phrase(responseDto.getEndTime().toString())));

        document.add(table);

        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}
