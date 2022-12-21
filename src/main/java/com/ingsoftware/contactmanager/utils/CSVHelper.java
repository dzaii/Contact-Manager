package com.ingsoftware.contactmanager.utils;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.el.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<ContactRequestDto> csvToContactRequestDto(InputStream is) throws ParseException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser =
                     new CSVParser(fileReader, CSVFormat.Builder.create(CSVFormat.DEFAULT)
                       .setHeader()
                       .setSkipHeaderRecord(true)
                       .setIgnoreHeaderCase(true)
                       .setTrim(true)
                       .setNullString("")
                       .build());) {

            List<ContactRequestDto> contactRequestDtos = new ArrayList<ContactRequestDto>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                ContactRequestDto contactRequestDto = new ContactRequestDto(
                        csvRecord.get("firstName"),
                        csvRecord.get("lastName"),
                        csvRecord.get("email"),
                        csvRecord.get("phoneNumber"),
                        csvRecord.get("address"),
                        csvRecord.get("type"),
                        csvRecord.get("info")
                );
                    contactRequestDtos.add(contactRequestDto);
            }

            return contactRequestDtos;
        } catch (Exception e) {
            throw new ParseException("Failed to parse CSV file: " + e.getMessage());
        }
    }
}
