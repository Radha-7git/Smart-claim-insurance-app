package com.radha.smartclaim.documentservice.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;
import com.radha.smartclaim.documentservice.dto.ParsedDocumentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AIParsingService {
    private final ChatModel chatModel;
    private static final Logger log = LoggerFactory.getLogger(AIParsingService.class);

    public AIParsingService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String parseText(String ocrText) {
        log.info("Starting AI parsing for OCR text");
        if (ocrText == null || ocrText.isBlank()) {
            throw new RuntimeException("OCR text is null or empty");
        }
        String prompt = """
                Extract the following fields as STRICT JSON:
                hospital_name, bill_date (YYYY-MM-DD), patient_name, diagnosis, total_amount.

                Rules:
                - Output ONLY valid JSON
                - No explanation
                - No markdown
                - Use null if field not found

                Text:
                """ + ocrText;
        String raw = chatModel.call(new Prompt(prompt))
                .getResult()
                .getOutput()
                .getText();
        log.info("AI raw response");
        String cleanedResponse = cleanResponse(raw);
        log.info("Cleaned JSON");
        return cleanedResponse;
    }

    public String cleanResponse(String result) {
        if (result == null || result.isBlank()) {
            throw new RuntimeException("AI returned empty response");
        }
        int start = result.indexOf("{");
        int end = result.lastIndexOf("}");

        if (start != -1 && end != -1 && end > start) {
            return result.substring(start, end + 1);
        }
        throw new RuntimeException("No valid JSON object found in AI response: " + result);
    }

    public ParsedDocumentDto convertJsonToDto(String json) {
        try {
            JsonMapper mapper = JsonMapper.builder().build();
            return mapper.readValue(json, ParsedDocumentDto.class);
        } catch (Exception e) {
            log.error("Failed to parse JSON", e);
            throw new RuntimeException("Failed to parse JSON: " + e.getMessage());
        }
    }
}