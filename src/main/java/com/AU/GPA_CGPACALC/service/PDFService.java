package com.AU.GPA_CGPACALC.service;

import com.AU.GPA_CGPACALC.dto.ReportRequest;
import com.AU.GPA_CGPACALC.entity.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PDFService {

    public byte[] generateMarksheet(User user, List<GradeEntry> gradeEntries, GpaResult gpaResult, Integer semester) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("ANNA UNIVERSITY", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Subtitle
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Paragraph subtitle = new Paragraph("STATEMENT OF GRADES - SEMESTER " + semester, subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(30);
            document.add(subtitle);

            // Student Information
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);

            addInfoRow(infoTable, "Student Name:", user.getName());
            addInfoRow(infoTable, "Register Number:", user.getEmail()); // Using email as register number for demo
            addInfoRow(infoTable, "Department:", user.getDepartment().getName());
            addInfoRow(infoTable, "Regulation:", user.getRegulation().getName());
            addInfoRow(infoTable, "Semester:", semester.toString());

            document.add(infoTable);

            // Grades Table
            PdfPTable gradesTable = new PdfPTable(5);
            gradesTable.setWidthPercentage(100);
            gradesTable.setSpacingAfter(20);

            // Table headers
            addTableHeader(gradesTable, "Subject Code");
            addTableHeader(gradesTable, "Subject Name");
            addTableHeader(gradesTable, "Credits");
            addTableHeader(gradesTable, "Grade");
            addTableHeader(gradesTable, "Points");

            // Table rows
            for (GradeEntry gradeEntry : gradeEntries) {
                addTableRow(gradesTable, gradeEntry.getSubject().getCode());
                addTableRow(gradesTable, gradeEntry.getSubject().getName());
                addTableRow(gradesTable, gradeEntry.getSubject().getCredits().toString());
                addTableRow(gradesTable, gradeEntry.getGrade());
                addTableRow(gradesTable, gradeEntry.getPoints().toString());
            }

            document.add(gradesTable);

            // GPA Result
            if (gpaResult != null) {
                PdfPTable resultTable = new PdfPTable(2);
                resultTable.setWidthPercentage(50);
                resultTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

                addResultRow(resultTable, "Total Credits:", gpaResult.getTotalCredits().toString());
                addResultRow(resultTable, "Total Points:", gpaResult.getTotalPoints().toString());
                addResultRow(resultTable, "GPA:", String.format("%.2f", gpaResult.getGpa()));

                document.add(resultTable);
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating marksheet PDF", e);
        }
    }

    public byte[] generateCGPASummary(User user, List<GpaResult> gpaResults) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("ANNA UNIVERSITY", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Subtitle
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Paragraph subtitle = new Paragraph("CGPA SUMMARY", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(30);
            document.add(subtitle);

            // Student Information
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);

            addInfoRow(infoTable, "Student Name:", user.getName());
            addInfoRow(infoTable, "Register Number:", user.getEmail());
            addInfoRow(infoTable, "Department:", user.getDepartment().getName());
            addInfoRow(infoTable, "Regulation:", user.getRegulation().getName());

            document.add(infoTable);

            // GPA Results Table
            PdfPTable gpaTable = new PdfPTable(4);
            gpaTable.setWidthPercentage(100);
            gpaTable.setSpacingAfter(20);

            addTableHeader(gpaTable, "Semester");
            addTableHeader(gpaTable, "Total Credits");
            addTableHeader(gpaTable, "Total Points");
            addTableHeader(gpaTable, "GPA");

            double totalCredits = 0;
            double totalPoints = 0;

            for (GpaResult gpaResult : gpaResults) {
                addTableRow(gpaTable, gpaResult.getSemester().toString());
                addTableRow(gpaTable, gpaResult.getTotalCredits().toString());
                addTableRow(gpaTable, gpaResult.getTotalPoints().toString());
                addTableRow(gpaTable, String.format("%.2f", gpaResult.getGpa()));

                totalCredits += gpaResult.getTotalCredits();
                totalPoints += gpaResult.getTotalPoints();
            }

            document.add(gpaTable);

            // CGPA Calculation
            double cgpa = totalPoints / totalCredits;

            PdfPTable cgpaTable = new PdfPTable(2);
            cgpaTable.setWidthPercentage(50);
            cgpaTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            addResultRow(cgpaTable, "Overall Credits:", String.format("%.2f", totalCredits));
            addResultRow(cgpaTable, "Overall Points:", String.format("%.2f", totalPoints));
            addResultRow(cgpaTable, "CGPA:", String.format("%.2f", cgpa));

            document.add(cgpaTable);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating CGPA summary PDF", e);
        }
    }

    public byte[] generateStudentReport(List<User> students) {
        // Implementation for student report
        return new byte[0];
    }

    public byte[] generateSubjectReport(List<Subject> subjects, ReportRequest reportRequest) {
        // Implementation for subject report
        return new byte[0];
    }

    public byte[] generateResultReport(List<GpaResult> results) {
        // Implementation for result report
        return new byte[0];
    }

    private void addInfoRow(PdfPTable table, String label, String value) {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addTableHeader(PdfPTable table, String header) {
        PdfPCell cell = new PdfPCell(new Phrase(header));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addTableRow(PdfPTable table, String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value));
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addResultRow(PdfPTable table, String label, String value) {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}