package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.web.exceptions.SequenceValidationException;
import lombok.NonNull;

import java.util.List;

public class FileUploadServiceUtils {

    public static void validateSequenceList(@NonNull List sequences) throws SequenceValidationException {
        switch (sequences.size()) {
            case 0:
                throw new SequenceValidationException("File has invalid format.");
            case 1:
                break;
            default:
                throw new SequenceValidationException("Upload " +
                        sequences.size() + " sequences at once. " +
                        "Please, upload only one sequence per file."
                );
        }
    }
}
