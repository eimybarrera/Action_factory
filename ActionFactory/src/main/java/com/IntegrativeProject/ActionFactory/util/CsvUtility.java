package com.IntegrativeProject.ActionFactory.util;

import com.IntegrativeProject.ActionFactory.model.Device;
import com.IntegrativeProject.ActionFactory.model.Employee;
import com.IntegrativeProject.ActionFactory.model.Supplier;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvUtility {

//  Definición del tipo MIME(Multipurpose Internet Mail Extensions) para archivos CSV
    public static String type = "text/csv";

//  Encabezados de los atributos del archivo
    static String[] headers = {"imei", "status", "score", "validation_status", "validation_date", "supplier_id", "employee_id"};

//  Método para verificar el formato CSV
    public static boolean hasCsvFormat(MultipartFile file) {
        return type.equals(file.getContentType());
    }

//  Método para convertir un archivo CSV en una lista de objetos Device
    public static List<Device> csvToDeviceList(InputStream is) {
        try (
//              Lectura del InputStream de archivo CSV mediante BufferedReader
                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                                                                                      wFRAH()                 wIHC()            wT()
                CSVParser csvParser = new CSVParser(bReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());      //Parsea el archivo CSV usando CSVParser
        ) {                                                                                                                                       //wFRAH() -> indica que la primera fila del archivo CSV son los encabezados de las columnas
            List<Device> deviceList = new ArrayList<>();                                                                                          //wIHC() ->  ignora las diferencias de mayúsculas/minúsculas al comparar los nombres de las columanas con los encabezados predeterminados
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();                                                                              //wT() -> indica que se deben eliminar los espacios en blanco alrededor de los valores de las celdas del CSV durante el proceso de parsing
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

            for (CSVRecord csvRecord : csvRecords) {
                Device device = new Device();
                device.setImei(Long.parseLong(csvRecord.get("imei")));
                device.setStatus(csvRecord.get("status"));
                device.setScore(Integer.parseInt(csvRecord.get("score")));
                device.setValidationStatus(csvRecord.get("validation_status"));
                device.setValidationDate(LocalDateTime.parse(csvRecord.get("validation_date")));
                device.setSupplier(new Supplier(Long.parseLong(csvRecord.get("supplier_id"))));
                device.setEmployee(new Employee(Long.parseLong(csvRecord.get("employee_id"))));
                deviceList.add(device);
            }
            return deviceList;
        } catch (IOException e) {
            throw new RuntimeException("CSV data is failed to parse: " + e.getMessage());
        }
    }
}