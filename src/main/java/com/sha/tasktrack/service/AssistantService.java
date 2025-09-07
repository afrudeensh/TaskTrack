package com.sha.tasktrack.service;

import com.sha.tasktrack.dto.Prompt;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

@Service
public class AssistantService {

    // ✅ Nova Lite model
    private static final String MODEL_ID = "amazon.nova-lite-v1:0";

    @Autowired
    private BedrockRuntimeClient bedrockClient;

    public String askAssistant(Prompt prompt) {
        return syncResponse(prompt.getQuestion());
    }

    private String syncResponse(String question) {
        try {
            // ✅ Prepend "system-like" instructions to user content
            String enrichedPrompt =
                    "You are a helpful assistant. If you are unsure, say 'I don't know' instead of guessing. " +
                            "Answer clearly and factually.\n\n" +
                            "Question: " + question;

            // ✅ Build request payload
            JSONObject payload = new JSONObject()
                    .put("messages", new JSONArray()
                            .put(new JSONObject()
                                    .put("role", "user")
                                    .put("content", new JSONArray()
                                            .put(new JSONObject().put("text", enrichedPrompt))
                                    )
                            )
                    )
                    .put("inferenceConfig", new JSONObject()
                            .put("maxTokens", 200)
                            .put("temperature", 0.5)
                    );

            // ✅ Create Bedrock request
            InvokeModelRequest request = InvokeModelRequest.builder()
                    .modelId(MODEL_ID)
                    .body(SdkBytes.fromUtf8String(payload.toString()))
                    .contentType("application/json")
                    .accept("application/json")
                    .build();

            // ✅ Call Bedrock
            InvokeModelResponse response = bedrockClient.invokeModel(request);

            String rawJson = response.body().asUtf8String();
            System.out.println("📩 Raw Bedrock response: " + rawJson);

            // ✅ Parse JSON safely
            JSONObject responseBody = new JSONObject(rawJson);

            if (!responseBody.has("output")) {
                throw new RuntimeException("❌ Unexpected response: 'output' not found!");
            }

            JSONObject output = responseBody.getJSONObject("output");
            JSONObject message = output.getJSONObject("message");
            JSONArray content = message.getJSONArray("content");

            String generatedText = content.getJSONObject(0).getString("text");

            // ✅ Log both question + reply
            System.out.println("🙋 User asked: " + question);
            System.out.println("🤖 Assistant reply: " + generatedText);

            return generatedText;

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error communicating with Bedrock: " + e.getMessage();
        }
    }
}
