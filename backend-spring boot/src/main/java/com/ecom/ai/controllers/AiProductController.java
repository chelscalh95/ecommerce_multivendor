package com.ecom.ai.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import com.ecom.ai.services.AiProductService;
import com.ecom.response.ApiResponse;

@RestController
@RequestMapping("/ai")
@Tag(name = "AI Product", description = "AI-powered product recommendation and chat demo APIs")
public class AiProductController {
    
    private final AiProductService productService;

    public AiProductController(AiProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "AI chat demo", description = "Demonstrate AI chat capabilities with simple message processing")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "AI demo response generated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid message parameter"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error or AI service failure")
    })
    @PostMapping("/chat/demo")
    public ApiResponse generate(
            @Parameter(description = "Message for AI demo chat", required = false) @RequestParam(
            value = "message",
            defaultValue = "Tell me a joke") String message) throws Exception {

        String ans = productService.simpleChat(message);

        JSONObject messageJson = new JSONObject(ans);

        // Extract the text from the JSON
        JSONArray candidates = messageJson.getJSONArray("candidates");
        JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        String text = parts.getJSONObject(0).getString("text");

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(text);

        return apiResponse;

    }
}
