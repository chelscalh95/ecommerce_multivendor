package com.zosh.ai.controllers;

import com.zosh.ai.services.AiChatBotService;
import com.zosh.model.User;
import com.zosh.request.Prompt;
import com.zosh.response.ApiResponse;
import com.zosh.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/chat")
@Tag(name = "AI Chat Bot", description = "AI-powered chatbot APIs for intelligent customer support and product assistance")
public class AiChatBotController {

    private final AiChatBotService aiChatBotService;
    private final UserService userService;

    @Operation(summary = "Generate AI chat response", description = "Generate AI-powered response for user queries with optional product context")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "AI response generated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid prompt data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required (when JWT provided)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error or AI service failure")
    })
    @PostMapping()
    public ResponseEntity<ApiResponse> generate(
            @Parameter(description = "User prompt for AI chatbot", required = true) @RequestBody Prompt prompt,
            @Parameter(description = "User ID (optional)", required = false) @RequestParam(required = false) Long userId,
            @Parameter(description = "Product ID for context (optional)", required = false) @RequestParam(required = false) Long productId,
            @Parameter(description = "JWT Authorization token (optional)", required = false) @RequestHeader(required = false,name = "Authorization")String jwt) throws Exception {

        String message = prompt.getPrompt();
        if (productId != null) {
            message = "the product id is " + productId +", " + message ;
        }

        User user=new User();
        if(jwt!=null)
            user=userService.findUserProfileByJwt(jwt);

//        Long userId;
//        if(user!=null){
//            userId=user.getId();
//        }

        ApiResponse apiResponse = aiChatBotService.aiChatBot(message,productId,user.getId());

        return ResponseEntity.ok(apiResponse);

    }

}
