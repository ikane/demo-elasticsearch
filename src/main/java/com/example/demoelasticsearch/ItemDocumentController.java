package com.example.demoelasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
public class ItemDocumentController {

	@Autowired
	ItemDocumentRepository itemDocumentRepository;

	@GetMapping("/items")
	public List<ItemDocument> getItems() {
		List<ItemDocument> items = new ArrayList<>();
		itemDocumentRepository.findAll().forEach(itemDocument -> items.add(itemDocument));
		return items;
	}


}
