package com.example.demoelasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@GetMapping("/customers")
	public List<Customer> getCustomers() {
		return customerService.findCustomers();
	}

	@GetMapping("/aggregates/gender")
	public Object getGenderAggregates() {
		return customerService.getCustomerAggregateByGenderActive();
	}

	@GetMapping("/customers-by-tags")
	public Object findCustomersWithTags(@RequestParam List<String> tags) {
		return customerService.findCustomersWithTags(tags);
	}

	@GetMapping("/customers-by-tags-scroll")
	public Object findCustomersWithTagsAll(@RequestParam List<String> tags) {
		return customerService.findCustomersWithTagsAll(tags);
	}

	@GetMapping("/customers-having-tag")
	public Object getCustomerAggregateHavingTagAndEyeColor(@RequestParam String tag, @RequestParam String eyeColor) {
		return customerService.getCustomerAggregateHavingTagAndEyeColor(tag, eyeColor);
	}

	@GetMapping("/customers-export")
	public ResponseEntity<StreamingResponseBody> exportCustomers(@RequestParam List<String> tags) {

		List<Customer> customers = customerService.findCustomersWithTagsAll(tags);
		StreamingResponseBody t = out -> {
			//out.write('\ufeff');
			ZipOutputStream zout = new ZipOutputStream(out);
			File csvFile = File.createTempFile("test", ".csv");

			//out.write("id;name;email\n".getBytes());
			FileWriter fileWriter  = new FileWriter(csvFile);
			fileWriter.write("id;name;email\n");

			for(Customer customer : customers) {
				StringJoiner stringJoiner = new StringJoiner(";")
								.add(customer.getId())
								.add(customer.getName())
								.add(customer.getEmail())
								.add("\n");
				//out.write(stringJoiner.toString().getBytes());
				fileWriter.write(stringJoiner.toString());
			}
			fileWriter.close();
			zout.putNextEntry(new ZipEntry("test.csv"));
			zout.write(Files.readAllBytes(Paths.get(csvFile.getAbsolutePath())));
			zout.closeEntry();
			zout.close();
			out.close();
		};
		return ResponseEntity.ok()
		                     .header(CONTENT_TYPE, "application/zip")
		                     .header(CONTENT_DISPOSITION, "attachment; filename=test.zip")
		                     .body(t);
	}
}
