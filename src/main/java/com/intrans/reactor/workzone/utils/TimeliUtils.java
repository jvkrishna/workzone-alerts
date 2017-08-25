package com.intrans.reactor.workzone.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.intrans.reactor.workzone.entities.CameraInventory;
import com.intrans.reactor.workzone.entities.SensorCameraMapping;

/**
 * Utility Functions.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Mar 30, 2017
 *
 */
public class TimeliUtils {

	private static ObjectMapper jsonMapper = new ObjectMapper();
	private static ObjectMapper xmlMapper = new XmlMapper();
	static {
		configureObjectMapper(jsonMapper);
		configureObjectMapper(xmlMapper);
	}

	/**
	 * Converts XML content into an Object.
	 * 
	 * @param xmlContent
	 * @param valueType
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public static <T> T readValue(String xmlContent, Class<T> valueType) throws IOException {
		return readXMLValue(xmlContent, valueType);
	}

	/**
	 * Converts an Object to XML String.
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public static <T> String writeValueAsString(T obj) throws IOException {
		return writeValueAsXML(obj);
	}

	public static <T> T readXMLValue(String xmlContent, Class<T> valueType) throws IOException {
		return readValue(xmlMapper, xmlContent, valueType);
	}

	public static <T> String writeValueAsXML(T obj) throws IOException {
		return writeValueAsString(xmlMapper, obj);
	}

	public static <T> T readJSONValue(String jsonContent, Class<T> valueType) throws IOException {
		return readValue(jsonMapper, jsonContent, valueType);
	}

	public static <T> String writeValueAsJSON(T obj) throws IOException {
		return writeValueAsString(jsonMapper, obj);
	}

	public static <T> T readValue(ObjectMapper om, String content, Class<T> valueType) throws IOException {
		return om.readValue(content, valueType);
	}

	public static <T> String writeValueAsString(ObjectMapper om, T obj) throws IOException {
		return om.writeValueAsString(obj);
	}

	/**
	 * Converts an Object to XML string with custom parameters.
	 * 
	 * @param obj
	 * @param inclusions
	 * @return
	 * @throws IOException
	 */
	public static <T> String writeValueAsString(T obj, List<JsonInclude.Include> inclusions) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		for (JsonInclude.Include incl : inclusions) {
			mapper.setSerializationInclusion(incl);
		}
		return mapper.writeValueAsString(obj);
	}

	public static void updateCSVFileWithHeaders(File f, String... headers) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < headers.length - 1; i++) {
			sb.append(headers[i]);
		}
		sb.append(headers[headers.length - 1]).append("\n");
		FileUtils.writeStringToFile(f, sb.toString());
	}

	public static void gUnZip(File gzFile, File output) throws IOException {
		byte[] buffer = new byte[1024];
		GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(gzFile));

		FileOutputStream out = new FileOutputStream(output);

		int len;
		while ((len = gzis.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}

		gzis.close();
		out.close();

	}

	/**
	 * Parse a given XML string to {@link Document} object
	 * 
	 * @param xmlString
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document parseXML(String xmlString) throws ParserConfigurationException, SAXException, IOException {
		return parseXML(new ByteArrayInputStream(xmlString.getBytes()));
	}

	/**
	 * Parse a given input stream to {@link Document} object
	 * 
	 * @param xmlInputStream
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document parseXML(InputStream xmlInputStream)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(xmlInputStream);

	}

	/**
	 * Creates basic authentication interceptor.
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static ClientHttpRequestInterceptor getBasicAuthInterceptor(String username, String password) {
		return new BasicAuthorizationInterceptor(username, password);
	}

	public static List<CameraInventory> unmarshallCameraInventory(String xmlString)
			throws ParserConfigurationException, SAXException, IOException, ParseException {
		List<CameraInventory> cameraInventoryList = new ArrayList<CameraInventory>();
		Document document = parseXML(xmlString);
		NodeList nodeList = document.getElementsByTagName("cctv-inventory-item");
		CameraInventory cameraInventory = null;
		for (int i = 0, len = nodeList.getLength(); i < len; i++) {
			cameraInventory = new CameraInventory();
			Element cctv_inventory = (Element) nodeList.item(i);
			cameraInventory.setDeviceId(
					Long.valueOf(cctv_inventory.getElementsByTagName("device-id").item(0).getTextContent()));
			cameraInventory.setDeviceName(StringUtils.deleteWhitespace(
					cctv_inventory.getElementsByTagName("device-name").item(0).getTextContent().trim()));
			cameraInventory.setLatitude(
					Double.valueOf(cctv_inventory.getElementsByTagName("latitude").item(0).getTextContent()));
			cameraInventory.setLongitude(
					Double.valueOf(cctv_inventory.getElementsByTagName("longitude").item(0).getTextContent()));
			cameraInventory.setRoute(cctv_inventory.getElementsByTagName("route-designator").item(0).getTextContent());
			NodeList imageUrlElemnt = cctv_inventory.getElementsByTagName("tcore:static-image-url");
			if ((imageUrlElemnt.getLength()) > 0) {
				cameraInventory.setImageUrl(imageUrlElemnt.item(0).getTextContent());
			}
			NodeList videoUrlElement = cctv_inventory.getElementsByTagName("tcore:live-video-url");
			if (videoUrlElement.getLength() > 0) {
				cameraInventory.setVideoUrl(videoUrlElement.item(0).getTextContent());

			}
			StringBuilder lastUpdateDateStr = new StringBuilder()
					.append(cctv_inventory.getElementsByTagName("date").item(0).getTextContent())
					.append(cctv_inventory.getElementsByTagName("time").item(0).getTextContent())
					.append(cctv_inventory.getElementsByTagName("offset").item(0).getTextContent());
			Date lastUpdateDate = DateUtils.parseDate(lastUpdateDateStr.toString(), DateUtils.PATTERN_YYYYMMDDHHMMSSZ);
			cameraInventory.setLastUpdatedTime(lastUpdateDate.getTime());
			cameraInventoryList.add(cameraInventory);
		}
		return cameraInventoryList;
	}

	public static List<SensorCameraMapping> createSensorCameraMappingFromCSV(String csvFile) throws IOException {
		List<SensorCameraMapping> sensorCameraMappingList = new ArrayList<>();
		Scanner scanner = new Scanner(new ClassPathResource(csvFile).getInputStream());
		SensorCameraMapping sensorCameraMapping = null;
		while (scanner.hasNext()) {
			List<String> line = CSVUtils.parseLine(scanner.nextLine());
			sensorCameraMapping = new SensorCameraMapping();
			sensorCameraMapping.setSensorName(StringUtils.deleteWhitespace(line.get(0)));
			sensorCameraMapping
					.setCameraName(StringUtils.replaceAll(StringUtils.deleteWhitespace(line.get(1)), "_", "/"));
			sensorCameraMappingList.add(sensorCameraMapping);
		}
		scanner.close();
		return sensorCameraMappingList;
	}


	public static String formatStringToDecimal(String decimalString, int decimalLocation) {
		return new BigDecimal(new BigInteger(decimalString), decimalLocation).toString();
	}

	public static String formatLocationString(String str) {
		return formatStringToDecimal(str, 6);
	}

	private static void configureObjectMapper(ObjectMapper om) {
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		om.setVisibility(PropertyAccessor.FIELD, Visibility.ANY).setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
				.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);
	}

}
