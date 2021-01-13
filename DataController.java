package com.adt.navik.signals.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adt.navik.signals.commons.SignalConstants;
import com.adt.navik.signals.dto.LexisDataDTO;
import com.adt.navik.signals.dto.LexisNexisDataDTO;
import com.adt.navik.signals.dto.LexisNexisDataRequest;
import com.adt.navik.signals.service.DataService;
import com.adt.navik.signals.service.SummaryService;

@RestController

@RequestMapping(value = {"/data"})
public class DataController {
  @Autowired
  DataService dataService;

  @Autowired
  SummaryService summaryService;


  @PostMapping(value = "/dataDownload", consumes = "application/json")
  public ResponseEntity<String> dataDownload(@RequestBody LexisNexisDataRequest request) {
    dataService.dataDownload(request);
    String path = StringUtils.isEmpty(request.getFolder())
        ? SignalConstants.OUTPUT_FILE_PATH + SignalConstants.LEXIS_NEXIS_DATA_PATH
        : SignalConstants.OUTPUT_FILE_PATH + SignalConstants.LEXIS_NEXIS_DATA_PATH + "/" + request.getFolder();
    return new ResponseEntity<>("Data Download has begun on path :" + path
        + ". Please coordinate with tech team for more details", HttpStatus.OK);
  }

  @GetMapping(value = "/pillarSearch")
  public ResponseEntity<List<LexisNexisDataDTO>> esSignalsPillarSearch(
      @RequestParam(value = "index") String index, @RequestParam(value = "pillar") String pillar)
      throws IOException {
    Collection<String> keywords = dataService.searchList(pillar);
    // List<LexisNexisDataDTO> list = dataService.esSignalsPillarSearch(index, keywords);
    MultiValueMap<String, String> headers = getHeaders();
    return new ResponseEntity<>(dataService.esSignalsPillarSearch(index, keywords), headers,
        HttpStatus.OK);
  }

  @GetMapping(value = "/keywordSearch")
  public ResponseEntity<List<LexisDataDTO>> esSignalsKeywordSearch(
      @RequestParam(value = "index") String index, @RequestParam(value = "keyword") String keyword,
      @RequestParam(value = "attribute") String attribute) {
    MultiValueMap<String, String> headers = getHeaders();
    return new ResponseEntity<>(dataService.esSignalsKeywordSearch(index, keyword, attribute),
        headers, HttpStatus.OK);
  }


  private <E> MultiValueMap<String, String> getHeaders() {
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("Message", "Successful");
    headers.add("Code", "1");
    return headers;
  }

}
