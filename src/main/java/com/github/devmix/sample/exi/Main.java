package com.github.devmix.sample.exi;

import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.main.api.sax.EXIResult;
import com.siemens.ct.exi.main.api.sax.EXISource;
import jakarta.xml.bind.JAXBContext;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Sergey Grachev
 */
public class Main {

    public static final String EXI_FILE = "output/xml-sample.exi";
    public static final String XML_FILE = "src/main/resources/xml-sample.xml";
    public static final String XML_FILE_DECODE = "output/xml-sample-decoded.xml";
    public static final String EXI_FILE_JAXB = "output/xml-sample-jaxb.exi";

    public static void main(String[] args) throws Exception {
        encode();
        decode();
        marshal();
        unmarshal();
    }

    private static void encode() throws Exception {
        try (var osEXI = new FileOutputStream(EXI_FILE)) {
            final var exiResult = new EXIResult(DefaultEXIFactory.newInstance());
            exiResult.setOutputStream(osEXI);

            // java.xml

            final var xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(exiResult.getHandler());
            xmlReader.parse(XML_FILE); // parse XML input
        }

        final var xmlSize = Files.size(Paths.get(XML_FILE));
        final var exiSize = Files.size(Paths.get(EXI_FILE));

        System.out.println("Encode:");
        System.out.printf("\t XML size: %d bytes%n", xmlSize);
        System.out.printf("\t EXI size: %d bytes%n", exiSize);
        System.out.printf("\t Ratio: %.2f%%%n", (double) exiSize / xmlSize * 100);
    }

    private static void decode() throws Exception {
        final var exiSource = new EXISource(DefaultEXIFactory.newInstance());
        exiSource.setInputSource(new InputSource(EXI_FILE));

        // java.xml

        final var transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(exiSource, new StreamResult(XML_FILE_DECODE));

        final var xmlSize = Files.size(Paths.get(XML_FILE_DECODE));
        final var exiSize = Files.size(Paths.get(EXI_FILE));

        System.out.println("Decode:");
        System.out.printf("\t XML size: %d bytes%n", xmlSize);
        System.out.printf("\t EXI size: %d bytes%n", exiSize);
        System.out.printf("\t Ratio: %.2f%%%n", (double) xmlSize / exiSize * 100);
    }

    private static void marshal() throws Exception {
        final var sensor = new Sensor();
        sensor.setId("device-001");
        sensor.setTemperature(24.5);
        sensor.setTimestamp("2026-03-26T10:00:00Z");

        try (var osEXI = new FileOutputStream(EXI_FILE_JAXB)) {
            final var exiResult = new EXIResult(DefaultEXIFactory.newInstance());
            exiResult.setOutputStream(osEXI);

            final var factory = JAXBContext.newInstance(sensor.getClass());
            final var marshaller = factory.createMarshaller();
            marshaller.marshal(sensor, exiResult);
        }

        final var exiSize = Files.size(Paths.get(EXI_FILE_JAXB));

        System.out.printf("JAXB (serialize): EXI size: %d bytes%n", exiSize);
    }

    private static void unmarshal() throws Exception {
        final var exiSource = new EXISource(DefaultEXIFactory.newInstance());
        exiSource.setInputSource(new InputSource(EXI_FILE_JAXB));

        final var factory = JAXBContext.newInstance(Sensor.class);
        final var unmarshaller = factory.createUnmarshaller();
        final var sensor = (Sensor) unmarshaller.unmarshal(exiSource);

        System.out.printf("JAXB (deserialize):  %s%n", sensor.toString());
    }
}