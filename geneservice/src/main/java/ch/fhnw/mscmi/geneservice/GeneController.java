package ch.fhnw.mscmi.geneservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@RestController
@RequestMapping("geneservice")
public class GeneController {

    @Autowired
    private GeneService geneService;

    private static final Logger logger = LoggerFactory.getLogger(GeneserviceApplication.class);

    @GetMapping("/health-check")
    @CrossOrigin
    public String health() {
        return "OK";
    }

    @GetMapping("/byid")
    @CrossOrigin
    public SearchResponse<List<Gene>> getById(@RequestParam(value = "id") Integer id) {
        logger.debug("GENEius: Endpoint getById called with parameter: " + id);
        Gene gene = geneService.findById(id);
        if (gene != null) {
            ArrayList<Gene> singleItemList = new ArrayList<Gene>();
            singleItemList.add(0, gene);
            return new SearchResponse<List<Gene>>(1, 1, singleItemList);
        } else {
            return new SearchResponse<List<Gene>>();
        }
    }

    @GetMapping("/bysymbol")
    @CrossOrigin
    public SearchResponse<List<Gene>> getBySymbol(@RequestParam(value = "symbol") String symbol, @RequestParam(value = "offset") int offset, @RequestParam(value = "pageSize") int pageSize) {
        logger.debug("GENEius: Endpoint getBySymbol called with parameter: " + symbol);
        Page<Gene> genes = geneService.findBySymbol(symbol, offset, pageSize);
        if (genes != null && genes.hasContent()) {
            return new SearchResponse<>(genes.getSize(), genes.getTotalElements(), genes.getContent());
        } else {
            return new SearchResponse<List<Gene>>();
        }
    }

    @GetMapping("/bydescription")
    @CrossOrigin
    public SearchResponse<List<Gene>> getByDescription(@RequestParam(value = "description") String description, @RequestParam(value = "offset") int offset, @RequestParam(value = "pageSize") int pageSize) {
        logger.debug("GENEius: Endpoint getByDescription called with parameter: " + description);
        Page<Gene> genes = geneService.findByDescription(description, offset, pageSize);
        if (genes != null && genes.hasContent()) {
            return new SearchResponse<>(genes.getSize(), genes.getTotalElements(), genes.getContent());
        } else {
            return new SearchResponse<List<Gene>>();
        }
    }

    @GetMapping("/search")
    public SearchResponse<List<Gene>> searchGenesPaginated(@RequestParam(value = "q") String searchQuery, @RequestParam(value = "offset") int offset, @RequestParam(value = "pageSize") int pageSize) {
        logger.debug("GENEius: Endpoint searchGenesPaginated called with search query: " + searchQuery); 
        
        int intSearchQuery;
        try {
            intSearchQuery =  Integer.parseInt(searchQuery);
        } catch (NumberFormatException e) {
            intSearchQuery = 0;
        }

        Page<Gene> genes = geneService.search(searchQuery, intSearchQuery, offset, pageSize);
        if (genes != null && genes.hasContent()) {
            return new SearchResponse<>(genes.getSize(), genes.getTotalElements(), genes.getContent());
        } else {
            return new SearchResponse<List<Gene>>();
        }
    }
}
