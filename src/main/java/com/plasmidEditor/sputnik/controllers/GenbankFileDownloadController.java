package com.plasmidEditor.sputnik.controllers;

import com.plasmidEditor.sputnik.download.*;
import org.biojava.nbio.core.sequence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.plasmidEditor.sputnik.utils.Constants.*;
import static com.plasmidEditor.sputnik.utils.RequestPath.*;

@RestController
@RequestMapping(path = GENBANK)
public class GenbankFileDownloadController {
    private final GenbankFileDownloadService<DNASequence> dnaFileDownloadService;
    private final GenbankFileDownloadService<ProteinSequence> proteinFileDownloadService;

    @Autowired
    public GenbankFileDownloadController(DNAFileDownloadService dnaFileDownloadService,
                                         ProteinFileDownloadService proteinFileDownloadService) {
        this.dnaFileDownloadService = dnaFileDownloadService;
        this.proteinFileDownloadService = proteinFileDownloadService;
    }

    @GetMapping(path = DNA,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    byte[] downloadDNA(@RequestParam String accession,
                       @RequestParam(defaultValue = DEFAULT_VERSION_VALUE) String version) {
        DNASequence sequence = dnaFileDownloadService.downloadGenbankSequence(accession, version);
        return dnaFileDownloadService.covertSequenceToByteArray(sequence);
    }

    @GetMapping(path = PROTEIN,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    byte[] downloadProtein(@RequestParam String accession,
                           @RequestParam(defaultValue = DEFAULT_VERSION_VALUE) String version) {
        ProteinSequence sequence = proteinFileDownloadService.downloadGenbankSequence(accession, version);
        return proteinFileDownloadService.covertSequenceToByteArray(sequence);
    }
}
