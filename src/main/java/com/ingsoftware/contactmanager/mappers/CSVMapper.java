package com.ingsoftware.contactmanager.mappers;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import com.ingsoftware.contactmanager.utils.CSVHelper;
import org.apache.el.parser.ParseException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;


@Component
public class CSVMapper {

    public List<ContactRequestDto> CVSToContactRequestDto(MultipartFile file)
            throws ParseException, FileUploadException, DataFormatException {

       if(CSVHelper.hasCSVFormat(file)){
            try{
                 return CSVHelper.csvToContactRequestDto(file.getInputStream());
            }
            catch (IOException e){
                throw new FileUploadException("Could not read file.");
              }
       }
       throw new DataFormatException("Please upload CSV file");
    }
}
