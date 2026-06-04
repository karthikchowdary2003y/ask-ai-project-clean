package Example.springai.controller;
import org.springframework.web.bind.annotation.*;
import Example.springai.service.GroqService;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ai")
public class AIController {
    private final GroqService groqService;

    public AIController(GroqService groqService) {
        this.groqService = groqService;
    }

    private static final List<String> AGRI_KEYWORDS = List.of(
        "crop", "soil", "farm", "plant", "seed", "fertilizer", "irrigation",
        "harvest", "pest", "disease", "weather", "livestock", "farmer",
        "agriculture", "paddy", "wheat", "rice", "vegetable", "fruit",
        "drought", "rain", "manure", "organic", "yield", "sowing", "agri",
        "poultry", "dairy", "cow", "goat", "field", "tractor", "borewell",
        "kharif", "rabi", "millet", "sugarcane", "cotton", "groundnut",
        "tomato", "onion", "potato", "compost", "greenhouse", "nursery",
        "weed", "insecticide", "fungicide", "herbicide", "drip", "sprinkler",
        "pm kisan", "subsidy", "mandi", "market price", "loan", "fasal"
    );

    @GetMapping("/health")
    public String health() {
        return "🌾 AskAI Agricultural Assistant is running!";
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String prompt) {
        String lower = prompt.toLowerCase();

        boolean isAgriRelated = AGRI_KEYWORDS.stream().anyMatch(lower::contains);

        if (!isAgriRelated) {
            return "🌾 I'm AskAI, your Agricultural Assistant. I can only help with farming and agriculture-related topics. Please ask me about crops, soil, pests, irrigation, or any farming question!";
        }

        return groqService.askAI(prompt);
    }
}
