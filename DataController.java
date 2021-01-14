package com.adt.navik.signals.controller;


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
